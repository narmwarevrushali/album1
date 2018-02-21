package narmware.com.photouploadcopy.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.stone.vega.library.VegaLayoutManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import narmware.com.photouploadcopy.MyApplication;
import narmware.com.photouploadcopy.R;
import narmware.com.photouploadcopy.adapter.InvoiceAdapter;
import narmware.com.photouploadcopy.helpers.Constants;
import narmware.com.photouploadcopy.models.Invoice;
import narmware.com.photouploadcopy.models.InvoiceResponse;
import narmware.com.photouploadcopy.support.JSONParser;
import narmware.com.photouploadcopy.support.SharedPreferencesHelper;


public class InvoiceActivity extends AppCompatActivity {

    JSONParser mJsonParser;
    List<Invoice> mInvoiceItems;
    protected RecyclerView mRecyclerView;
    InvoiceAdapter invoiceAdapter;
    protected Dialog mNoConnectionDialog;
    LinearLayout mLinearEmpty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        getSupportActionBar().hide();
        init();

        setInvoiceAdapter();
        new GetInvoiceDetails().execute();

    }

    private void init() {

        mJsonParser=new JSONParser();

        mLinearEmpty= (LinearLayout) findViewById(R.id.empty_invoice);
        mLinearEmpty.setVisibility(View.INVISIBLE);
    }

    public void setInvoiceAdapter(){
        mInvoiceItems=new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
            invoiceAdapter= new InvoiceAdapter(InvoiceActivity.this, mInvoiceItems);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(InvoiceActivity.this);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setLayoutManager(new VegaLayoutManager());
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(invoiceAdapter);
            mRecyclerView.setNestedScrollingEnabled(false);
            mRecyclerView.setFocusable(false);

         //   invoiceAdapter.notifyDataSetChanged();

    }

    class GetInvoiceDetails extends AsyncTask<String, String, String> {
        protected ProgressDialog mProgress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Toast.makeText(InvoiceActivity.this,"Pre execute",Toast.LENGTH_SHORT).show();
            mProgress = new ProgressDialog(InvoiceActivity.this);
            mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgress.setIndeterminate(true);
            mProgress.setMessage("Getting invoice details...");
            mProgress.setCancelable(false);
            mProgress.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            String json = null;
            try
            {
                Gson gson = new Gson();

                Log.e("JSON invoice",SharedPreferencesHelper.getUserId(InvoiceActivity.this));
                HashMap<String, String> params = new HashMap<>();
                params.put(Constants.USER_ID, SharedPreferencesHelper.getUserId(InvoiceActivity.this));

                String url = MyApplication.URL_SERVER + MyApplication.URL_INVOICE_DETAILS;
                Log.e("JSON data updated url",url);

                JSONObject ob=mJsonParser.makeHttpRequest(url, "GET",params);

                if (ob == null) {
                    Log.d("RESPONSE", "ERRORRRRR");
                }
                else {
                    json = ob.toString();
                }
            }
            catch (Exception ex) {

                ex.printStackTrace();
            }

            return json;
        }

        @Override
        protected void onPostExecute(String s) {

            try{Gson gson = new Gson();
                if (s != null)
                    Log.e("invoice data", s);

                else
                    Log.e("data", "invoice is null");
                InvoiceResponse response = gson.fromJson(s, InvoiceResponse.class);
                Invoice[] array = response.getData();
                for (Invoice item : array) {
                    if(item.getInv_status().equals("PAID"))
                    mInvoiceItems.add(item);
                }
                if(mInvoiceItems.size()==0)
                {
                    mLinearEmpty.setVisibility(View.VISIBLE);
                }else{
                    mLinearEmpty.setVisibility(View.INVISIBLE);
                }
                invoiceAdapter.notifyDataSetChanged();
                mProgress.dismiss();
            }catch (Exception e)
            {
                //Toast.makeText(InvoiceActivity.this,"Internet not available,can not login",Toast.LENGTH_LONG).show();
                showNoConnectionDialog();
                mProgress.dismiss();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent=new Intent(InvoiceActivity.this,MainActivity2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    private void showNoConnectionDialog() {
        mNoConnectionDialog = new Dialog(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        mNoConnectionDialog.setContentView(R.layout.dialog_noconnectivity);
        mNoConnectionDialog.setCancelable(false);
        mNoConnectionDialog.show();

        Button exit = mNoConnectionDialog.findViewById(R.id.dialog_no_connec_exit);
        Button tryAgain = mNoConnectionDialog.findViewById(R.id.dialog_no_connec_try_again);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new GetInvoiceDetails().execute();
                mNoConnectionDialog.dismiss();
            }
        });
    }

}
