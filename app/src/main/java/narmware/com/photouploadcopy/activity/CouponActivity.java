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
import narmware.com.photouploadcopy.adapter.CouponAdapter;
import narmware.com.photouploadcopy.models.Coupon;
import narmware.com.photouploadcopy.models.CouponResponse;
import narmware.com.photouploadcopy.support.JSONParser;


public class CouponActivity extends AppCompatActivity {

    JSONParser mJsonParser;
    List<Coupon> mCouponItems;
    protected RecyclerView mRecyclerView;
    CouponAdapter couponAdapter;
    protected Dialog mNoConnectionDialog;
    LinearLayout mLinearEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        getSupportActionBar().hide();
        init();

        setInvoiceAdapter();
        new GetCoupons().execute();

    }

    private void init() {

        mJsonParser=new JSONParser();

        mLinearEmpty= (LinearLayout) findViewById(R.id.empty_coupon);
        mLinearEmpty.setVisibility(View.INVISIBLE);
    }

    public void setInvoiceAdapter(){
        mCouponItems=new ArrayList<>();

            mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        couponAdapter= new CouponAdapter(CouponActivity.this, mCouponItems);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CouponActivity.this);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setLayoutManager(new VegaLayoutManager());
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(couponAdapter);
            mRecyclerView.setNestedScrollingEnabled(false);
            mRecyclerView.setFocusable(false);

    }

    class GetCoupons extends AsyncTask<String, String, String> {
        protected ProgressDialog mProgress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Toast.makeText(InvoiceActivity.this,"Pre execute",Toast.LENGTH_SHORT).show();
            mProgress = new ProgressDialog(CouponActivity.this);
            mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgress.setIndeterminate(true);
            mProgress.setMessage("Getting your coupons...");
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
               // params.put(Constants.USER_ID, SharedPreferencesHelper.getUserId(CouponActivity.this));

                String url = MyApplication.URL_SERVER + MyApplication.URL_COUPONS;
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
                    Log.e("coupon data", s);

                else
                    Log.e("data", "coupon is null");
                CouponResponse response = gson.fromJson(s, CouponResponse.class);
                Coupon[] array = response.getData();
                for (Coupon item : array) {
                    mCouponItems.add(item);
                }
                if(mCouponItems.size()==0)
                {
                    mLinearEmpty.setVisibility(View.VISIBLE);
                }
                if(mCouponItems.size()!=0)
                {
                    mLinearEmpty.setVisibility(View.INVISIBLE);
                }
                couponAdapter.notifyDataSetChanged();
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

        Intent intent=new Intent(CouponActivity.this,MainActivity2.class);
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

                new GetCoupons().execute();
                mNoConnectionDialog.dismiss();
            }
        });
    }

}
