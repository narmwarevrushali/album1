package narmware.com.photouploadcopy.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import narmware.com.photouploadcopy.MyApplication;
import narmware.com.photouploadcopy.R;
import narmware.com.photouploadcopy.fragment.AddFrndsForCartFragment;
import narmware.com.photouploadcopy.fragment.AddressFragment;
import narmware.com.photouploadcopy.fragment.AlbumFragment;
import narmware.com.photouploadcopy.fragment.CartEmptyFragment;
import narmware.com.photouploadcopy.fragment.CartFriendsFragment;
import narmware.com.photouploadcopy.fragment.CartFullFragment;
import narmware.com.photouploadcopy.fragment.FriendsFragment;
import narmware.com.photouploadcopy.fragment.FriendsProfileFragment;
import narmware.com.photouploadcopy.fragment.HomeFragment;
import narmware.com.photouploadcopy.fragment.ProfileFragment;
import narmware.com.photouploadcopy.helpers.Constants;
import narmware.com.photouploadcopy.models.Address;
import narmware.com.photouploadcopy.models.Friends;
import narmware.com.photouploadcopy.models.PinCode;
import narmware.com.photouploadcopy.models.PinResponse;
import narmware.com.photouploadcopy.support.DatabaseAccess;
import narmware.com.photouploadcopy.support.JSONParser;
import narmware.com.photouploadcopy.support.SharedPreferencesHelper;


public class MainActivity2 extends AppCompatActivity implements AddFrndsForCartFragment.OnFragmentInteractionListener,CartFriendsFragment.OnFragmentInteractionListener,FriendsProfileFragment.OnFragmentInteractionListener,FriendsFragment.OnFragmentInteractionListener,HomeFragment.OnFragmentInteractionListener,AddressFragment.OnFragmentInteractionListener,AlbumFragment.OnFragmentInteractionListener,CartEmptyFragment.OnFragmentInteractionListener,CartFullFragment.OnFragmentInteractionListener,ProfileFragment.OnFragmentInteractionListener{

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    //TextView mTxtTitle;
    ImageView mImgLogo;
    AppBarLayout mAppBar;
    protected Dialog pinDialog;
    String selectedPinCode;
    JSONParser mJsonParser;
    String mPin,mDist,mState,mCity;
    boolean doubleBackToExitPressedOnce = false;
    BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                     setFragment(new AlbumFragment());
                    return true;
                case R.id.navigation_profile:
                    setFragment(new ProfileFragment());
                    return true;
                case R.id.navigation_cart:

                    if(SharedPreferencesHelper.getAlbumId(MainActivity2.this)==null)
                    {
                        setFragment(new CartEmptyFragment());
                        //mImgLogo.setVisibility(View.VISIBLE);
                        //mTxtTitle.setVisibility(View.INVISIBLE);
                    }

                    else {
                        setFragment(new CartFullFragment());
                    }

                    return true;
            }
            return false;
        }

    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent=getIntent();
        boolean isDone=intent.getBooleanExtra("isDone",false);

        mJsonParser=new JSONParser();
         navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if(isDone==true)
        {
            navigation.setSelectedItemId(R.id.navigation_cart);

        }
        else
        {
            navigation.setSelectedItemId(R.id.navigation_home);
        }

        if(SharedPreferencesHelper.getPinStatus(MainActivity2.this).equals(Constants.INVALID))
        {
            showPinDialog();
        }


        //setFragment(new HomeFragment());

        //mTxtTitle= (TextView) findViewById(R.id.txt_title);
        mImgLogo= (ImageView) findViewById(R.id.img_logo);
        mAppBar= (AppBarLayout) findViewById(R.id.app_bar);
        mAppBar.setVisibility(View.INVISIBLE);

        //mImgLogo.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void setFragment(Fragment fragment)
    {
        mFragmentManager=getSupportFragmentManager();
        for(int i = 0; i < mFragmentManager.getBackStackEntryCount(); ++i) {
            mFragmentManager.popBackStack();
        }
        mFragmentTransaction=mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.fragment_container,fragment);
        mFragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(String title) {


        if(title.equals("Home"))
        {
            mImgLogo.setVisibility(View.VISIBLE);
            //mTxtTitle.setVisibility(View.INVISIBLE);
            mAppBar.setVisibility(View.VISIBLE);
        }
        else
        {
            mImgLogo.setVisibility(View.INVISIBLE);
           // mTxtTitle.setVisibility(View.VISIBLE);
            mAppBar.setVisibility(View.INVISIBLE);
            mAppBar.setVisibility(View.GONE);
           // mTxtTitle.setText(title);
        }
    }

    @Override
    public void onBackPressed() {

        //super.onBackPressed();

        String selectedFrag=SharedPreferencesHelper.getSelectedFragment(MainActivity2.this);
        if(selectedFrag==null){

        }
        else if(selectedFrag.equals("CartFullFragment") || selectedFrag.equals("AlbumFragment") || selectedFrag.equals("ProfileFragment"))
        {

            SharedPreferencesHelper.setSelectedFragment("Fragment", MainActivity2.this);

            if (doubleBackToExitPressedOnce==true) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
        else{
            super.onBackPressed();

        }

    }

    public void showPinDialog() {
        pinDialog = new Dialog(MainActivity2.this);
        pinDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        pinDialog.setContentView(R.layout.dialog_pincode);
        pinDialog.setCancelable(false);
        pinDialog.show();

        Button cancel = pinDialog.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinDialog.dismiss();
                finish();
            }
        });
        final AutoCompleteTextView mAutoPin=pinDialog.findViewById(R.id.edt_pin);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(MainActivity2.this);
        databaseAccess.open();
         final ArrayList<PinCode> pinList=databaseAccess.getArea();
        ArrayList<String> mPins=new ArrayList<>();

        for(int i=0;i<pinList.size();i++)
        {
            mPins.add(pinList.get(i).getPin_code());

            Log.e("JSON",pinList.get(i).getCity()+" "+pinList.get(i).getState()+" "+pinList.get(i).getDist()+" "+mPin);
        }

        ArrayAdapter mAdapter= new ArrayAdapter(MainActivity2.this,android.R.layout.simple_list_item_1,mPins);
        mAutoPin.setAdapter(mAdapter);
        mAutoPin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity2.this,pinList.get(i).getPin_code(), Toast.LENGTH_SHORT).show();
                mPin=pinList.get(i).getPin_code();
                mCity=pinList.get(i).getCity();
                mState=pinList.get(i).getState();
                mDist=pinList.get(i).getDist();

                Log.e("JSON",mCity+" "+mState+" "+mDist+" "+mPin);

            }
        });

        Button mBtnSubmit=pinDialog.findViewById(R.id.btn_submit);
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPinCode=mAutoPin.getText().toString();
                new GetPinStatus().execute();

            }
        });

    }


    class GetPinStatus extends AsyncTask<String, String, String> {

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
                params.put(Constants.PIN_CODE, selectedPinCode);

                String url = MyApplication.URL_SERVER + MyApplication.URL_GET_PIN_STATUS;
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

            try{
                Gson gson = new Gson();
                if (s != null)
                    Log.e("pincode data", s);

                else
                    Log.e("data", "pincode is null");

                PinResponse response = gson.fromJson(s, PinResponse.class);

                if(response.getPin_status().equals(Constants.VALID))
                {
                    SharedPreferencesHelper.setPinStatus(Constants.VALID,MainActivity2.this);
                    new SendAddress().execute();
                    new SendFrndProfile().execute();
                    pinDialog.dismiss();
                }
                if(response.getPin_status().equals(Constants.INVALID))
                {
                    Toast.makeText(MainActivity2.this, "Service is not available for this area", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e)
            {
                //Toast.makeText(InvoiceActivity.this,"Internet not available,can not login",Toast.LENGTH_LONG).show();
                //showNoConnectionDialog();
            }
        }
    }


    class SendAddress extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.e("JSON",mCity+" "+mState+" "+mDist+" "+mPin);
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = null;
            try
            {
                Gson gson = new Gson();
                Address address=new Address(null,null,null,null,null,null,null);

                address.setCity(mCity);
                address.setState(mState);
                address.setPin(mPin);
                address.setDist(mDist);
                address.setUser_id(SharedPreferencesHelper.getUserId(MainActivity2.this));


                json = gson.toJson(address);
                Log.e("JSON data updated",json);

                HashMap<String, String> params = new HashMap<>();
                params.put(Constants.JSON_STRING,json);

                Log.e("JSON data updated ob",json);
                String url = MyApplication.URL_SERVER + MyApplication.URL_PROFILE_UPDATE;
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
                    Log.e("Address data", s);

                else
                    Log.e("data", "Address is null");

                Address addressResponse=gson.fromJson(s,Address.class);
                Log.e("Address data","Response"+addressResponse.getResponse());

                int response= Integer.parseInt(addressResponse.getResponse());
                if(response==Constants.PROFILE_RESPONSE)
                {
                    //Toast.makeText(MainActivity2.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(MainActivity2.this);
                    databaseAccess.open();
                    databaseAccess.UpdateUserProfile(null,mState,mDist,mCity,mPin,null,SharedPreferencesHelper.getUserId(MainActivity2.this));

                }
            }catch (Exception e)
            {
                Toast.makeText(MainActivity2.this,"Internet not available,can not login",Toast.LENGTH_LONG).show();
            }
        }
    }

    class SendFrndProfile extends AsyncTask<String, String, String> {

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
                Friends friends=new Friends(0,null,null,null,null,null,null,null,null,null,null,null,null,0);
                friends.setCity(mCity);
                friends.setState(mState);
                friends.setPin(mPin);
                friends.setDist(mDist);
                friends.setUser_id(SharedPreferencesHelper.getUserId(MainActivity2.this));
                friends.setFr_email(SharedPreferencesHelper.getUserEmail(MainActivity2.this));
                friends.setFr_name(Constants.SEND_TO_ME);

                json = gson.toJson(friends);
                Log.e("JSON data updated",json);

                HashMap<String, String> params = new HashMap<>();
                params.put(Constants.JSON_STRING,json);

                Log.e("JSON data updated ob",json);
                String url = MyApplication.URL_SERVER + MyApplication.URL_FRIEND_PROFILE;
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
                    Log.e("login data", s);

                else
                    Log.e("data", "login is null");

                Friends friendResponse=gson.fromJson(s,Friends.class);
                Log.e("login data","Response"+friendResponse.getResponse()+ "  "+friendResponse.getF_id());
                String mFrndServerId=friendResponse.getF_id();
                SharedPreferencesHelper.setSelfFrndId(friendResponse.getF_id(),MainActivity2.this);
                int response= Integer.parseInt(friendResponse.getResponse());
                if(response==Constants.NEW_ENTRY)
                {
                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(MainActivity2.this);
                    databaseAccess.open();
                    databaseAccess.setFrndsProfile(null,mState,mDist,mCity,mPin,null,"Self",SharedPreferencesHelper.getUserEmail(MainActivity2.this),mFrndServerId,"0",1);

                    List<Friends> temp =databaseAccess.getFriends();
                    for(int c=0;c < temp.size();c++)
                    {
                        Log.e("Friends data","Name  "+temp.get(c).getFr_name()+"server id  "+temp.get(c).getF_id());
                    }

                    // Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                }
                if(response==Constants.ALREADY_PRESENT)
                {
                    // Toast.makeText(getContext(), "Friend is already added", Toast.LENGTH_SHORT).show();

                }

            }catch (Exception e)
            {
                // Toast.makeText(getContext(),"Internet not available",Toast.LENGTH_LONG).show();
            }
        }
    }

}
