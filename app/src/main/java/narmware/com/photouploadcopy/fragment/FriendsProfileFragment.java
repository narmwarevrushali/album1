package narmware.com.photouploadcopy.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.labo.kaji.fragmentanimations.MoveAnimation;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import narmware.com.photouploadcopy.MyApplication;
import narmware.com.photouploadcopy.R;
import narmware.com.photouploadcopy.helpers.Constants;
import narmware.com.photouploadcopy.models.Friends;
import narmware.com.photouploadcopy.models.PinCode;
import narmware.com.photouploadcopy.models.PinResponse;
import narmware.com.photouploadcopy.support.DatabaseAccess;
import narmware.com.photouploadcopy.support.JSONParser;
import narmware.com.photouploadcopy.support.SharedPreferencesHelper;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FriendsProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FriendsProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendsProfileFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "flag";
    private static final String ARG_PARAM2 = "f_id";

    // TODO: Rename and change types of parameters
    private String flag;
    int f_id;
    AutoCompleteTextView mAutoPin;
    private JSONParser mJsonParser;
    EditText mEdtAddr,mEdtState,mEdtCity,mEdtDist,mEdtMobile,mEdtName,mEdtMail;
    String mAddr,mState,mCity,mDist,mPin,mMobile,mName,mMail,mFrndServerId;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    String mUserId;
    Button mBtnSubmit,mBtnCancel;
    int validationFlag=0;
    private OnFragmentInteractionListener mListener;

    public FriendsProfileFragment() {
        // Required empty public constructor
    }

    public static FriendsProfileFragment newInstance(String flag,int f_id) {
        FriendsProfileFragment fragment = new FriendsProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, flag);
        args.putInt(ARG_PARAM2,f_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            flag = getArguments().getString(ARG_PARAM1);
            f_id=getArguments().getInt(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_friends_profile, container, false);
        init(view);
        if (getArguments() != null) {
            if (flag.equals("selected")) {
                //Toast.makeText(getContext(), "Selected frnd", Toast.LENGTH_SHORT).show();
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getContext());
                databaseAccess.open();
                Friends friends = databaseAccess.getSingleFrnd(f_id);

                mAddr = friends.getAddress();
                mState = friends.getState();
                mCity = friends.getCity();
                mDist = friends.getDist();
                mPin = friends.getPin();
                mMobile = friends.getMobile();
                mName = friends.getFr_name();
                mMail = friends.getFr_email();
                mFrndServerId=friends.getF_id();

                mEdtAddr.setText(mAddr);
                mEdtState.setText(mState);
                mEdtDist.setText(mDist);
                mEdtCity.setText(mCity);
                mAutoPin.setText(mPin);
                mEdtMobile.setText(mMobile);
                mEdtName.setText(mName);
                mEdtMail.setText(mMail);
            }
        }
        mListener.onFragmentInteraction("Friend's Profile");
        return view;
    }

    private void init(View view) {
       // SharedPreferencesHelper.setSelectedFragment("FriendsProfileFragment",getContext());
        mJsonParser=new JSONParser();

        mBtnSubmit=view.findViewById(R.id.btn_submit);
        mBtnCancel=view.findViewById(R.id.btn_cancel);

        mBtnSubmit.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);

        mEdtAddr=view.findViewById(R.id.edt_addr);
        mEdtState=view.findViewById(R.id.edt_state);
        mEdtCity=view.findViewById(R.id.edt_city);
        mEdtDist=view.findViewById(R.id.edt_dist);
        //mEdtPin=view.findViewById(R.id.edt_pin);
        mEdtMobile=view.findViewById(R.id.edt_mobile);
        mEdtName=view.findViewById(R.id.edt_name);
        mEdtMail=view.findViewById(R.id.edt_mail);

        mEdtState.setEnabled(false);
        mEdtCity.setEnabled(false);
        mEdtDist.setEnabled(false);

        YoYo.with(Techniques.BounceIn)
                .duration(1500)
                .playOn(mEdtName);

        mAutoPin=view.findViewById(R.id.auto_pin);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getContext());
        databaseAccess.open();
        final ArrayList<PinCode> pinList=databaseAccess.getArea();
        ArrayList<String> mPins=new ArrayList<>();

        for(int i=0;i<pinList.size();i++)
        {
            mPins.add(pinList.get(i).getPin_code());

            Log.e("JSON",pinList.get(i).getCity()+" "+pinList.get(i).getState()+" "+pinList.get(i).getDist()+" "+mPin);
        }

        ArrayAdapter mAdapter= new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,mPins);
        mAutoPin.setAdapter(mAdapter);
        mAutoPin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(),pinList.get(i).getPin_code(), Toast.LENGTH_SHORT).show();
                mPin=pinList.get(i).getPin_code();
                mCity=pinList.get(i).getCity();
                mState=pinList.get(i).getState();
                mDist=pinList.get(i).getDist();

                Log.e("JSON",mCity+" "+mState+" "+mDist+" "+mPin);

                mEdtState.setText(mState);
                mEdtCity.setText(mCity);
                mEdtDist.setText(mDist);
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.btn_submit:
                validationFlag=0;
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                mAddr=mEdtAddr.getText().toString().trim();
                mState=mEdtState.getText().toString().trim();
                mCity=mEdtCity.getText().toString().trim();
                mDist=mEdtDist.getText().toString().trim();
                mPin=mAutoPin.getText().toString().trim();
                mUserId= SharedPreferencesHelper.getUserId(getContext()).trim();
                mMobile=mEdtMobile.getText().toString().trim();
                mName=mEdtName.getText().toString().trim();
                mMail=mEdtMail.getText().toString().trim();

                if(mPin.equals("") || mPin==null)
                {
                    validationFlag=1;
                    mAutoPin.setError("Please select pincode");
                }
                if(mMobile.length()<10)
                {
                    validationFlag=1;
                    mEdtMobile.setError("Please enter valid mobile number");
                }
              if(mName.equals("") || mName==null)
                {
                    validationFlag=1;
                    mEdtName.setError("Please enter friend name");
                }
             if(mMail.equals("") || mMail==null)
                {
                    validationFlag=1;
                    mEdtMail.setError("Please enter valid email id");
                }

                if (!mMail.matches(emailPattern))
                {
                    validationFlag=1;
                    Toast.makeText(getContext(),"Invalid email address",Toast.LENGTH_SHORT).show();
                    mEdtMail.setError("Please enter valid email id");
                }

                if(validationFlag==0)
            {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                new GetPinStatus().execute();
            }
                break;

            case R.id.btn_cancel:
                fragmentManager=getFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,new FriendsFragment());
                fragmentTransaction.commit();
                break;
        }
    }
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if(enter) {
            return MoveAnimation.create(MoveAnimation.LEFT, enter, 400);
        }else {
            return MoveAnimation.create(MoveAnimation.RIGHT, enter, 400);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String title);
    }

    class SendFrndProfile extends AsyncTask<String, String, String> {
        protected ProgressDialog mProgress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Toast.makeText(LoginActivity.this,"Pre execute",Toast.LENGTH_SHORT).show();
            mProgress = new ProgressDialog(getContext());
            mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgress.setIndeterminate(true);
            mProgress.setMessage("Updating Profile");
            mProgress.setCancelable(false);
            mProgress.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = null;
            try
            {
                Gson gson = new Gson();
                Friends friends=new Friends(0,null,null,null,null,null,null,null,null,null,null,null,null,0);
                friends.setAddress(mAddr);
                friends.setCity(mCity);
                friends.setState(mState);
                friends.setPin(mPin);
                friends.setDist(mDist);
                friends.setUser_id(mUserId);
                friends.setMobile(mMobile);
                friends.setFr_name(mName);
                friends.setFr_email(mMail);

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
                mFrndServerId=friendResponse.getF_id();
                int response= Integer.parseInt(friendResponse.getResponse());
                if(response==Constants.NEW_ENTRY)
                {
                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getContext());
                    databaseAccess.open();
                    databaseAccess.setFrndsProfile(mAddr,mState,mDist,mCity,mPin,mMobile,mName,mMail,mFrndServerId,"1",0);

                    List<Friends> temp =databaseAccess.getFriends();
                    for(int c=0;c < temp.size();c++)
                    {
                        Log.e("Friends data","Name  "+temp.get(c).getFr_name()+"server id  "+temp.get(c).getF_id());
                    }

                    flag="selected";
                    Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                }
                if(response==Constants.ALREADY_PRESENT)
                {
                    Toast.makeText(getContext(), "Friend is already added", Toast.LENGTH_SHORT).show();

                }

                mProgress.dismiss();
            }catch (Exception e)
            {
                Toast.makeText(getContext(),"Internet not available",Toast.LENGTH_LONG).show();
                mProgress.dismiss();
            }
        }
    }

    class UpdateFrndProfile extends AsyncTask<String, String, String> {
        protected ProgressDialog mProgress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Toast.makeText(LoginActivity.this,"Pre execute",Toast.LENGTH_SHORT).show();
            mProgress = new ProgressDialog(getContext());
            mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgress.setIndeterminate(true);
            mProgress.setMessage("Updating Profile");
            mProgress.setCancelable(false);
           // mProgress.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = null;
            try
            {
                Gson gson = new Gson();
                Friends friends=new Friends(0,null,null,null,null,null,null,null,null,null,null,null,null,0);
                friends.setAddress(mAddr);
                friends.setCity(mCity);
                friends.setState(mState);
                friends.setPin(mPin);
                friends.setDist(mDist);
                friends.setUser_id(mUserId);
                friends.setMobile(mMobile);
                friends.setFr_name(mName);
                friends.setFr_email(mMail);
                friends.setF_id(mFrndServerId);

                json = gson.toJson(friends);
                Log.e("JSON data updated",json);

                HashMap<String, String> params = new HashMap<>();
                params.put(Constants.JSON_STRING,json);

                Log.e("JSON data updated ob",json);
                String url = MyApplication.URL_SERVER + MyApplication.URL_UPDATE_FRIEND_PROFILE;
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
                Log.e("friend data","Response"+friendResponse.getResponse());
                int response= Integer.parseInt(friendResponse.getResponse());
                if(response==Constants.NEW_ENTRY)
                {
                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getContext());
                    databaseAccess.open();
                    int f_id= Integer.parseInt(mFrndServerId);
                    databaseAccess.UpdateFrndsProfile(mAddr,mState,mDist,mCity,mPin,mMobile,mName,mMail,f_id);

                    List<Friends> temp =databaseAccess.getFriends();
                    for(int c=0;c < temp.size();c++)
                    {
                        Log.e("Friends data","Name  "+temp.get(c).getFr_name()+"server id  "+temp.get(c).getF_id());
                    }

                    Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                }
                if(response==Constants.ALREADY_PRESENT)
                {
                    Toast.makeText(getContext(), "Friend is already added", Toast.LENGTH_SHORT).show();
                }
               // mProgress.dismiss();
            }catch (Exception e)
            {
                Toast.makeText(getContext(),"Internet not available",Toast.LENGTH_LONG).show();
             //   mProgress.dismiss();
            }
        }
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
                params.put(Constants.PIN_CODE, mPin);

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
                    if (flag.equals("selected")) {
                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Are you sure?")
                                .setContentText("You want to update your friend!")
                                .setConfirmText("Yes !")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.setTitleText("Updated!")
                                                .setContentText("Your friend has been updated!")
                                                .setConfirmText("OK")
                                                .setConfirmClickListener(null)
                                                .showCancelButton(false)
                                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                                        new UpdateFrndProfile().execute();

                                    }
                                })

                                .showCancelButton(true)
                                .setCancelText("Cancel")
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                    }
                                })
                                .show();
                    }
                    else if(flag.equals("addFriend")){
                        new SendFrndProfile().execute();
                    }
                }
                if(response.getPin_status().equals(Constants.INVALID))
                {
                    Toast.makeText(getContext(), "Service is not available for this area", Toast.LENGTH_SHORT).show();

                }
            }catch (Exception e)
            {
                //Toast.makeText(InvoiceActivity.this,"Internet not available,can not login",Toast.LENGTH_LONG).show();
                //showNoConnectionDialog();
            }
        }
    }

}
