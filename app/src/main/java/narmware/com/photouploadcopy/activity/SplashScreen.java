package narmware.com.photouploadcopy.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import narmware.com.photouploadcopy.MyApplication;
import narmware.com.photouploadcopy.R;
import narmware.com.photouploadcopy.helpers.Constants;
import narmware.com.photouploadcopy.models.Friends;
import narmware.com.photouploadcopy.models.PinCode;
import narmware.com.photouploadcopy.models.PinCodeList;
import narmware.com.photouploadcopy.models.SingleInvoiceStatus;
import narmware.com.photouploadcopy.models.VersionCode;
import narmware.com.photouploadcopy.support.DatabaseAccess;
import narmware.com.photouploadcopy.support.JSONParser;
import narmware.com.photouploadcopy.support.SharedPreferencesHelper;

/**
 * Created by savvy on 12/28/2017.
 */

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2500;
    boolean isLogin;
    View mImgLogo;
    private JSONParser mJsonParser;
    int version_id;
    int version_code;
    ArrayList<PinCode> pinCodes;
    ArrayList<VersionCode> versionCodes;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        init();
        //SharedPreferencesHelper.setPinStatus(Constants.INVALID,SplashScreen.this);
        isLogin= SharedPreferencesHelper.getLogin(SplashScreen.this);
        mImgLogo= (ImageView) findViewById(R.id.img_logo);
        YoYo.with(Techniques.BounceIn)
                .duration(1500)
                .playOn(mImgLogo);

        new Handler().postDelayed(new Runnable() {

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {

               /* DatabaseAccess databaseAccess = DatabaseAccess.getInstance(SplashScreen.this);
                databaseAccess.open();
                databaseAccess.deleteAll();
*/
                // This method will be executed once the timer is over
                // Start your app main activity
                if(isLogin == true)
                {
                    Intent intent = new Intent(SplashScreen.this, MainActivity2.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

                if(SharedPreferencesHelper.getInvoiceId(SplashScreen.this)!=null)
                {
                    new GetInvoiceStatus().execute();
                }
            }
        }, SPLASH_TIME_OUT);
    }

    private void init() {
        mJsonParser=new JSONParser();

        pinCodes=new ArrayList<>();

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(SplashScreen.this);
        databaseAccess.open();

        versionCodes=databaseAccess.getVersion();
        for(int i=0;i<versionCodes.size();i++)
        {
            Log.e("Version pincode",versionCodes.get(i).getV_id()+"");
            version_id= versionCodes.get(i).getV_id();
            version_code=versionCodes.get(i).getVersion();
        }
        new CheckVersion().execute();

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
                params.put(Constants.INVOICE_ID,SharedPreferencesHelper.getInvoiceId(SplashScreen.this));

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

                //Toast.makeText(SplashScreen.this,SharedPreferencesHelper.getInvoiceId(SplashScreen.this), Toast.LENGTH_SHORT).show();

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
                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(SplashScreen.this);
                    databaseAccess.open();
                    List<Friends> temp =databaseAccess.getCartSelectedFriends();
                    for(int i=0;i<temp.size();i++)
                    {
                        if(temp.get(i).getFr_name().equals(Constants.SEND_TO_ME))
                        {
                            databaseAccess.UpdateQty("0", Integer.parseInt(temp.get(i).getF_id()));
                            SharedPreferencesHelper.setIsSendMe(false,SplashScreen.this);
                        }
                        else{
                            databaseAccess.UpdateQty("0", Integer.parseInt(temp.get(i).getF_id()));
                            databaseAccess.UpdateCartFlag(0,Integer.parseInt(temp.get(i).getF_id()));
                        }
                    }
                    databaseAccess.deleteAll();
                    SharedPreferencesHelper.setPaymentStatus(Constants.PAID,SplashScreen.this);
                    SharedPreferencesHelper.setInvoiceId(null,SplashScreen.this);
                    SharedPreferencesHelper.setAlbumId(null,SplashScreen.this);
                    SharedPreferencesHelper.setFramePrice(null,SplashScreen.this);

                    SharedPreferencesHelper.setCouponMinPrice(null, SplashScreen.this);
                    SharedPreferencesHelper.setCouponPrice(null, SplashScreen.this);
                    SharedPreferencesHelper.setCouponName(null, SplashScreen.this);

                }
            }catch (Exception e)
            {

            }
        }
    }

    class CheckVersion extends AsyncTask<String, String, String> {

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
                params.put(Constants.VERSION_ID, String.valueOf(version_id));

                String url = MyApplication.URL_SERVER + MyApplication.URL_CHECK_VERSION;
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
                    Log.e("version data", s);

                else
                    Log.e("data", "version is null");

                //Toast.makeText(SplashScreen.this,SharedPreferencesHelper.getInvoiceId(SplashScreen.this), Toast.LENGTH_SHORT).show();

                VersionCode versionCode=gson.fromJson(s,VersionCode.class);
                Log.e("Invoice status",versionCode.getV_id()+"");

               int response= Integer.parseInt(versionCode.getResponse());
                if(response==Constants.ALREADY_PRESENT)
                {
                    int server_version_code=versionCode.getVersion();
                    if(version_code<server_version_code)
                    {
                        Log.e("Version","Smaller");
                        new GetAllPinCodes().execute();

                        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(SplashScreen.this);
                        databaseAccess.open();
                        databaseAccess.UpdateVersion(server_version_code,version_id);

                    }

                }
                else {
                    //Toast.makeText(getContext(),"Error while making payment",Toast.LENGTH_LONG).show();
                }

            }catch (Exception e)
            {

            }
        }
    }

    class GetAllPinCodes extends AsyncTask<String, String, String> {

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

                String url = MyApplication.URL_SERVER + MyApplication.URL_GET_PINCODES;
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
                    Log.e("pincode data", s);

                else
                    Log.e("data", "pincode is null");

                //Toast.makeText(SplashScreen.this,SharedPreferencesHelper.getInvoiceId(SplashScreen.this), Toast.LENGTH_SHORT).show();

                PinCodeList pinCodeList=gson.fromJson(s,PinCodeList.class);
                PinCode[] array = pinCodeList.getData();
                for (PinCode item : array) {

                    //Log.e("pincode status",item.getPin_code()+" "+item.getState()+" "+item.getCity());

                    pinCodes.add(item);
                }
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(SplashScreen.this);
                databaseAccess.open();
                databaseAccess.deleteAllArea();
                for(int i=0;i<pinCodes.size();i++)
                {
                    Log.e("pincode status",pinCodes.get(i).getPin_code()+" "+pinCodes.get(i).getState()+" "+pinCodes.get(i).getCity());
                    databaseAccess.setArea(pinCodes.get(i).getPin_code(),pinCodes.get(i).getState(),pinCodes.get(i).getDist(),pinCodes.get(i).getCity());
                }

                 ArrayList<PinCode> pinList=databaseAccess.getArea();
                for(int i=0;i<pinList.size();i++)
                {
                    Log.e("pincode ",pinList.get(i).getPin_code()+" "+pinList.get(i).getState()+" "+pinList.get(i).getCity());
                }

              /* int response= Integer.parseInt(invoice.getResponse());
                if(response==Constants.ALREADY_PRESENT)
                {

                }
                else {
                    //Toast.makeText(getContext(),"Error while making payment",Toast.LENGTH_LONG).show();
                }*/


            }catch (Exception e)
            {

            }
        }
    }

}
