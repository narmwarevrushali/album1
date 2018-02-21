package narmware.com.photouploadcopy.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import narmware.com.photouploadcopy.MyApplication;
import narmware.com.photouploadcopy.R;
import narmware.com.photouploadcopy.adapter.ViewOrderAdapter;
import narmware.com.photouploadcopy.helpers.Constants;
import narmware.com.photouploadcopy.models.ViewOrder;
import narmware.com.photouploadcopy.models.ViewOrderResponse;
import narmware.com.photouploadcopy.support.JSONParser;
import narmware.com.photouploadcopy.support.SharedPreferencesHelper;


public class ViewOrderActivity extends AppCompatActivity {

    JSONParser mJsonParser;
    List<ViewOrder> mOrderItems;
    protected RecyclerView mRecyclerView;
    ViewOrderAdapter orderAdapter;
    protected Dialog mNoConnectionDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);
        getSupportActionBar().hide();
        init();

        setInvoiceAdapter();
        new GetOrderDetails().execute();
    }

    private void init() {

        mJsonParser=new JSONParser();
    }

    public void setInvoiceAdapter(){
        mOrderItems=new ArrayList<>();

        ViewOrder viewOrder=new ViewOrder("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRiAonM_gYLKtbpOxHdszPwqgbul3Ouv4mjK-jijwSk28Uo8-hYWw");
        ViewOrder viewOrder1=new ViewOrder("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRpp_Qxl8pditcfJ8AXGgmWi3qXPgwbXnI6FbW_6HxJR4U0HzssGA");
        ViewOrder viewOrder2=new ViewOrder("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRE7WvAKbOhW30aX7rfsh33lYpv9dxfJ4UmsvA13rB088jhw8t8");

        mOrderItems.add(viewOrder);
        mOrderItems.add(viewOrder1);
        mOrderItems.add(viewOrder2);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
            orderAdapter= new ViewOrderAdapter(ViewOrderActivity.this, mOrderItems);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(ViewOrderActivity.this,3);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(orderAdapter);
            mRecyclerView.setNestedScrollingEnabled(false);
            mRecyclerView.setFocusable(false);

         //   invoiceAdapter.notifyDataSetChanged();

    }

    class GetOrderDetails extends AsyncTask<String, String, String> {
        protected ProgressDialog mProgress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Toast.makeText(InvoiceActivity.this,"Pre execute",Toast.LENGTH_SHORT).show();
            mProgress = new ProgressDialog(ViewOrderActivity.this);
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

                HashMap<String, String> params = new HashMap<>();
                params.put(Constants.INVOICE_ID, SharedPreferencesHelper.getInvoiceId(ViewOrderActivity.this));

                String url = MyApplication.URL_SERVER + MyApplication.URL_UNPAID_ORDER;
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
                ViewOrderResponse response = gson.fromJson(s, ViewOrderResponse.class);
                ViewOrder viewOrder=response.getData();

               /* Invoice[] array = response.getData();
                for (Invoice item : array) {

                    mOrderItems.add(item);
                }*/
                orderAdapter.notifyDataSetChanged();
                mProgress.dismiss();
            }catch (Exception e)
            {
                //Toast.makeText(InvoiceActivity.this,"Internet not available,can not login",Toast.LENGTH_LONG).show();
                showNoConnectionDialog();
                mProgress.dismiss();
            }
        }
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

                new GetOrderDetails().execute();
                mNoConnectionDialog.dismiss();
            }
        });
    }

}
