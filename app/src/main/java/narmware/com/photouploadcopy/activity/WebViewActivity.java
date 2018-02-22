package narmware.com.photouploadcopy.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import narmware.com.photouploadcopy.MyApplication;
import narmware.com.photouploadcopy.R;
import narmware.com.photouploadcopy.helpers.Constants;
import narmware.com.photouploadcopy.models.Friends;
import narmware.com.photouploadcopy.models.SingleInvoiceStatus;
import narmware.com.photouploadcopy.support.DatabaseAccess;
import narmware.com.photouploadcopy.support.JSONParser;
import narmware.com.photouploadcopy.support.SharedPreferencesHelper;


public class WebViewActivity extends AppCompatActivity implements View.OnClickListener{

    String mUrl;
    String firstUrl;
    String invoice_id;
    String user_id;
    WebView mWebView;
    TextView mTxtTitle;
    ImageButton mImgBtnBack;
    protected Dialog mNoConnectionDialog;
    protected Dialog mBlankDialog;
    protected ProgressDialog mProgress;
    JSONParser mJsonParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Intent intent=getIntent();
        invoice_id=intent.getStringExtra(Constants.INVOICE_ID);
        user_id=intent.getStringExtra(Constants.USER_ID);
        init();
    }

    private void init() {
        mJsonParser=new JSONParser();

   /*     SharedPreferencesHelper.setPaymentStatus(Constants.PAID,WebViewActivity.this);
        SharedPreferencesHelper.setAlbumId(null,WebViewActivity.this);*/

   mTxtTitle= (TextView) findViewById(R.id.web_title);

        mWebView= (WebView) findViewById(R.id.webview);
        setWebView();

        if(user_id.equals(""))
        {
            String pdf_url="http://docs.google.com/gview?embedded=true&url=";
            mUrl = pdf_url+MyApplication.URL_SERVER + MyApplication.URL_PAID_BILL_DETAILS + Constants.INVOICE_ID + "=" + invoice_id;
            mTxtTitle.setText("Invoice");
        }
        else {
            mUrl = MyApplication.URL_SERVER + MyApplication.URL_PAYMENT_GATEWAY + Constants.INVOICE_ID + "=" + invoice_id + "&" + Constants.USER_ID + "=" + user_id;
        }
        Log.e("Payment url",mUrl);
            mWebView.loadUrl(mUrl);

        mImgBtnBack= (ImageButton) findViewById(R.id.btn_back);
        mImgBtnBack.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.btn_back:

                new GetInvoiceStatus().execute();

                Intent intent=new Intent(WebViewActivity.this,InvoiceActivity.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        new GetInvoiceStatus().execute();

        Intent intent=new Intent(WebViewActivity.this, MainActivity2.class);
        intent.putExtra("isDone",true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    public void setWebView() {

        mProgress = new ProgressDialog(WebViewActivity.this);
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.setIndeterminate(true);
        mProgress.setMessage("Loading...");
        mProgress.setCancelable(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);

        //   webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowUniversalAccessFromFileURLs(true);
            webSettings.setAllowFileAccessFromFileURLs(true);
        }
    }


    public class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            try {
                if (mProgress.isShowing() == false) {
                    mProgress.show();
                }
                else {
                    Log.d("Page Error : ","");
                    mProgress.dismiss();
                }
            }
            catch (Exception e) {
                Log.d("Page Exception : ","");
                mProgress.dismiss();
            }

           /* if(MyApplication.LOGOUT_URL.equals(url)){
                mWebView.loadUrl(mUrl);
                mProgress.dismiss();
            }*/
        }

        @Override
        public void onPageCommitVisible(WebView view, String url) {
            super.onPageCommitVisible(view, url);
            if(mProgress.isShowing()) {
                mProgress.dismiss();
            }

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            // TODO Auto-generated method stub

            view.loadUrl(url);
            mProgress.dismiss();
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.d("Page loaded : ", url);
            mProgress.dismiss();

            if( mBlankDialog != null) {
                if (mBlankDialog.isShowing()) {
                    mBlankDialog.dismiss();
                }

            }

           /* if(MyApplication.LOGOUT_URL.equals(url)){
                //    Toast.makeText(NavigationMenuActivity.this,"onPagefinished",Toast.LENGTH_SHORT).show();
                SharedPreferncesHelper.setLoginSharedPref(0,NavigationMenuActivity.this);
                Intent intent=new Intent(NavigationMenuActivity.this,LoginScreenActivity.class);
                startActivity(intent);
                finish();

            }*/

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                 Log.e("errorCode" ,description);
            showNoConnectionDialog();
        }

        @Override
        public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
            mProgress.dismiss();
            return super.onRenderProcessGone(view, detail);
        }
    }

    public class MyWebChromeClient extends WebChromeClient {
        public void onProgressChanged(WebView view, int progress) {
            //mHorizontalProgress.setProgress(progress);
        }


    }
    private void showBlankDialog() {
        mBlankDialog = new Dialog(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        mBlankDialog.setContentView(R.layout.dialog_blank);
        mBlankDialog.setCancelable(false);
        if(mBlankDialog.isShowing() == false) {
            mBlankDialog.show();
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

                Intent intent=new Intent(WebViewActivity.this,SplashScreen.class);
                startActivity(intent);
                finish();

                //   mWebView.loadUrl(mUrl);
                mNoConnectionDialog.dismiss();
                showBlankDialog();
            }
        });
    }


    class GetInvoiceStatus extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected String doInBackground(String... strings) {
            String json = null;
            try
            {
                Gson gson = new Gson();

                HashMap<String, String> params = new HashMap<>();
                params.put(Constants.INVOICE_ID,SharedPreferencesHelper.getInvoiceId(WebViewActivity.this));

                String url = MyApplication.URL_SERVER + MyApplication.URL_GET_INVOICE_STATUS;
                Log.e("JSON data updated url",url);

                JSONObject ob=mJsonParser.makeHttpRequest(url, "GET",params );

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

                SingleInvoiceStatus invoice=gson.fromJson(s,SingleInvoiceStatus.class);
                Log.e("Invoice status",invoice.getStatus());

              /* int response= Integer.parseInt(invoice.getResponse());
                if(response==Constants.ALREADY_PRESENT)
                {

                }
                else {
                    //Toast.makeText(getContext(),"Error while making payment",Toast.LENGTH_LONG).show();
                }*/

              if(invoice.getStatus().equals("UNPAID"))
              {
              }
              if(invoice.getStatus().equals("PAID"))
              {
                  DatabaseAccess databaseAccess = DatabaseAccess.getInstance(WebViewActivity.this);
                  databaseAccess.open();
                  List<Friends> temp =databaseAccess.getCartSelectedFriends();
                  for(int i=0;i<temp.size();i++)
                  {
                      if(temp.get(i).getFr_name().equals(Constants.SEND_TO_ME))
                      {
                        databaseAccess.UpdateQty("0", Integer.parseInt(temp.get(i).getF_id()));
                          SharedPreferencesHelper.setIsSendMe(false,WebViewActivity.this);

                      }
                      else{
                          databaseAccess.UpdateQty("0", Integer.parseInt(temp.get(i).getF_id()));
                          databaseAccess.UpdateCartFlag(0,Integer.parseInt(temp.get(i).getF_id()));
                      }
                  }
                  databaseAccess.deleteAll();
                  SharedPreferencesHelper.setPaymentStatus(Constants.PAID,WebViewActivity.this);
                  SharedPreferencesHelper.setInvoiceId(null,WebViewActivity.this);
                  SharedPreferencesHelper.setAlbumId(null,WebViewActivity.this);

                  SharedPreferencesHelper.setCouponMinPrice(null, WebViewActivity.this);
                  SharedPreferencesHelper.setCouponPrice(null, WebViewActivity.this);
                  SharedPreferencesHelper.setCouponName(null, WebViewActivity.this);
              }
            }catch (Exception e)
            {

            }
        }
    }

}
