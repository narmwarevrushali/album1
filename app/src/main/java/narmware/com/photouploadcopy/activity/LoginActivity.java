package narmware.com.photouploadcopy.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.RotateDownTransformer;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import narmware.com.photouploadcopy.MyApplication;
import narmware.com.photouploadcopy.R;
import narmware.com.photouploadcopy.fragment.HomeFragment;
import narmware.com.photouploadcopy.fragment.IntroductionFragment;
import narmware.com.photouploadcopy.helpers.Constants;
import narmware.com.photouploadcopy.models.Address;
import narmware.com.photouploadcopy.models.AddressResponse;
import narmware.com.photouploadcopy.models.Friends;
import narmware.com.photouploadcopy.models.FriendsResponse;
import narmware.com.photouploadcopy.models.Login;
import narmware.com.photouploadcopy.support.DatabaseAccess;
import narmware.com.photouploadcopy.support.JSONParser;
import narmware.com.photouploadcopy.support.SharedPreferencesHelper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,HomeFragment.OnFragmentInteractionListener,IntroductionFragment.OnFragmentInteractionListener,GoogleApiClient.OnConnectionFailedListener{

    //for google sign in
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private JSONParser mJsonParser;
    //private SignInButton btnSignIn;

    String personName;
    String personPhotoUrl ;
    String email;

    Button mBtnSignUp;
    private ViewPager mViewPager;
    private PagerAdapter mAdapter;

    int sendToMePos=0;
    List<Friends> temp;
    List<Address> user;

    protected Dialog mNoConnectionDialog;
    RelativeLayout rltvSignin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        init();
    }

    private void init() {

        temp=new ArrayList<>();
        user=new ArrayList<>();

        mJsonParser=new JSONParser();
        mBtnSignUp= (Button) findViewById(R.id.btn_signup_fb);
        mBtnSignUp.setOnClickListener(this);

        rltvSignin= (RelativeLayout) findViewById(R.id.rltv_sign_in);
        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .playOn(rltvSignin);

        mViewPager= (ViewPager) findViewById(R.id.intro_pager);
        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .playOn(mViewPager);

        mAdapter=new PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setPageTransformer(true, new RotateDownTransformer());

        //google sign in initialization
        /*btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        btnSignIn.setOnClickListener(this);*/

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Customizing G+ button
      /*  btnSignIn.setSize(SignInButton.SIZE_STANDARD);
        btnSignIn.setScopes(gso.getScopeArray());*/
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            try {
                personName = acct.getDisplayName();
                email = acct.getEmail();
                personPhotoUrl = acct.getPhotoUrl().toString();

            }catch (Exception e)
            {
                e.printStackTrace();
            }
            Log.e(TAG, "Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl);

            if(personName==null || email==null)
            {
                Toast.makeText(this, "Unsupported format", Toast.LENGTH_SHORT).show();
            }

            else{

                if(personPhotoUrl==null)
                {
                    personPhotoUrl="http://narmware.com/kp/avatar.png";
                }

                new SendLogin().execute();

                Toast.makeText(LoginActivity.this,personName,Toast.LENGTH_SHORT).show();

                SharedPreferencesHelper.setUserName(personName,LoginActivity.this);
                SharedPreferencesHelper.setUserEmail(email,LoginActivity.this);
                SharedPreferencesHelper.setUserProfPic(personPhotoUrl,LoginActivity.this);
            }


         /*   txtName.setText(personName);
            txtEmail.setText(email);
            Glide.with(getApplicationContext()).load(personPhotoUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfilePic);*/

        } else {
            // Signed out, show unauthenticated UI.
            SharedPreferencesHelper.setLogin(false,LoginActivity.this);

        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.btn_signup_fb:
                signIn();
               /* personName="Rohit Savant";
                email="savantrohit8@gmail.com";
                personPhotoUrl="http://narmware.com/kp/avatar.png";

                new SendLogin().execute();

                Toast.makeText(LoginActivity.this,personName,Toast.LENGTH_SHORT).show();

                SharedPreferencesHelper.setUserName(personName,LoginActivity.this);
                SharedPreferencesHelper.setUserEmail(email,LoginActivity.this);
                SharedPreferencesHelper.setUserProfPic(personPhotoUrl,LoginActivity.this);*/
                break;

        }
    }

    class SendLogin extends AsyncTask<String, String, String> {
        protected ProgressDialog mProgress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

             //Toast.makeText(LoginActivity.this,"Pre execute",Toast.LENGTH_SHORT).show();
            mProgress = new ProgressDialog(LoginActivity.this);
            mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgress.setIndeterminate(true);
            mProgress.setMessage("Validating Credentials");
            mProgress.setCancelable(false);
            mProgress.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            String json = null;
            try
            {
                Gson gson = new Gson();
                Login login=new Login();

                login.setName(personName);
                login.setEmail(email);
                login.setImg_url(personPhotoUrl);

                json = gson.toJson(login);
                Log.e("JSON data updated",json);

                HashMap<String, String> params = new HashMap<>();
                params.put(Constants.JSON_STRING,json);

                Log.e("JSON data updated ob",json);
                String url = MyApplication.URL_SERVER + MyApplication.URL_LOGIN;
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

                Login loginresponse=gson.fromJson(s,Login.class);
                Log.e("login data","Response"+loginresponse.getResponse()+"\n user_id"+loginresponse.getUser_id());

                if(loginresponse.getUser_id() != null) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity2.class);
                    startActivity(intent);
                    finish();
                    SharedPreferencesHelper.setLogin(true, LoginActivity.this);
                    SharedPreferencesHelper.setUserId(loginresponse.getUser_id(), LoginActivity.this);
                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(LoginActivity.this);
                    databaseAccess.open();
                    databaseAccess.setUserProfile(null,null,null,null,null,null,SharedPreferencesHelper.getUserId(LoginActivity.this));
                    if(SharedPreferencesHelper.getUserId(LoginActivity.this)!=null)
                    {
                        new getAllFrndsFromServer().execute();
                        new GetUserProfile().execute();
                    }
                }
                mProgress.dismiss();
            }catch (Exception e)
            {
                showNoConnectionDialog();
                //Toast.makeText(LoginActivity.this,"Internet not available,can not login",Toast.LENGTH_LONG).show();
                mProgress.dismiss();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        }
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            //showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(String title) {

    }


    public class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {

            switch (index) {
                case 0:
                    return new IntroductionFragment().newInstance("Create your own personalized album. From home.",R.drawable.album1);
                case 1:
                    return new IntroductionFragment().newInstance("Create your lovely memories.",R.drawable.hardcover);
                case 2:
                    return new IntroductionFragment().newInstance("Create your lovely memories.",R.drawable.album1);
            }

            return null;
        }

        @Override
        public int getCount() {
            // get item count - equal to number of tabs
            return 3;
        }
    }


    class getAllFrndsFromServer extends AsyncTask<String, String, String> {
        protected ProgressDialog mProgress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Toast.makeText(LoginActivity.this,"Pre execute",Toast.LENGTH_SHORT).show();
            mProgress = new ProgressDialog(LoginActivity.this);
            mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgress.setIndeterminate(true);
            mProgress.setMessage("Geting Friends");
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
                params.put(Constants.USER_ID, SharedPreferencesHelper.getUserId(LoginActivity.this));

                String url = MyApplication.URL_SERVER + MyApplication.URL_GET_ALL_FRNDS;
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
                    Log.e("Friend all data", s);

                else
                    Log.e("data", "login is null");

                FriendsResponse response = gson.fromJson(s, FriendsResponse.class);
                Friends[] array = response.getData();
                for (Friends item : array) {

                    temp.add(item);
                }
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(LoginActivity.this);
                databaseAccess.open();

                for(int c=0;c < temp.size();c++)
                {
                    if(temp.get(c).getFr_name().equals(Constants.SEND_TO_ME))
                    {
                        sendToMePos=c;
                    }
                    else {
                        databaseAccess.setFrndsProfile(temp.get(c).getAddress(), temp.get(c).getState(), temp.get(c).getDist(), temp.get(c).getCity(), temp.get(c).getPin(), temp.get(c).getMobile(), temp.get(c).getFr_name(), temp.get(c).getFr_email(), temp.get(c).getF_id(), "1", 0);
                    }
                    Log.e("Friends data","Name  "+temp.get(c).getFr_name()+"server id  "+temp.get(c).getF_id());
                }
                Log.e("Sendtome","Name  "+temp.get(sendToMePos).getFr_name());
                databaseAccess.setFrndsProfile(temp.get(sendToMePos).getAddress(),temp.get(sendToMePos).getState(),temp.get(sendToMePos).getDist(),temp.get(sendToMePos).getCity(),temp.get(sendToMePos).getPin(),temp.get(sendToMePos).getMobile(),temp.get(sendToMePos).getFr_name(),temp.get(sendToMePos).getFr_email(),temp.get(sendToMePos).getF_id(),"0",1);

                mProgress.dismiss();
            }catch (Exception e)
            {
                //Toast.makeText(SplashScreen.this,"Internet not available,can not login",Toast.LENGTH_LONG).show();
                e.printStackTrace();
                mProgress.dismiss();
            }
        }
    }
    class GetUserProfile extends AsyncTask<String, String, String> {
        protected ProgressDialog mProgress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Toast.makeText(LoginActivity.this,"Pre execute",Toast.LENGTH_SHORT).show();
            mProgress = new ProgressDialog(LoginActivity.this);
            mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgress.setIndeterminate(true);
            mProgress.setMessage("Geting user");
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
                params.put(Constants.USER_ID, SharedPreferencesHelper.getUserId(LoginActivity.this));

                String url = MyApplication.URL_SERVER + MyApplication.URL_USER_PROFILE;
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
                    Log.e("User all data", s);

                else
                    Log.e("data", "login is null");

                AddressResponse response = gson.fromJson(s, AddressResponse.class);
                Address[] array = response.getData();
                for (Address Addritem : array) {

                    user.add(Addritem);
                }
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(LoginActivity.this);
                databaseAccess.open();

                for(int c=0;c < user.size();c++)
                {
                    databaseAccess.UpdateUserProfile(user.get(c).getAddress(),user.get(c).getState(),user.get(c).getDist(),user.get(c).getCity(),user.get(c).getPin(),user.get(c).getMobile(),user.get(c).getUser_id());
                    Log.e("User data","address  "+user.get(c).getAddress()+"  city  "+user.get(c).getCity());
                }
                mProgress.dismiss();
            }catch (Exception e)
            {
                //Toast.makeText(SplashScreen.this,"Internet not available,can not login",Toast.LENGTH_LONG).show();
                e.printStackTrace();
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

                new SendLogin().execute();
                mNoConnectionDialog.dismiss();
            }
        });
    }

}
