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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.labo.kaji.fragmentanimations.MoveAnimation;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import narmware.com.photouploadcopy.MyApplication;
import narmware.com.photouploadcopy.R;
import narmware.com.photouploadcopy.helpers.Constants;
import narmware.com.photouploadcopy.models.Address;
import narmware.com.photouploadcopy.models.Friends;
import narmware.com.photouploadcopy.support.DatabaseAccess;
import narmware.com.photouploadcopy.support.JSONParser;
import narmware.com.photouploadcopy.support.SharedPreferencesHelper;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddressFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddressFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String IMAGE = "img_path";

    // TODO: Rename and change types of parameters
    private String name,email,image;
    private JSONParser mJsonParser;
    TextView mProfName,mProfEmail;
    ImageView mProfImg;
    EditText mEdtAddr,mEdtState,mEdtCity,mEdtPin,mEdtDist,mEdtMobile;
    EditText mEdtArea,mEdtBuildName,mEdtFlatNo,mEdtLandmark;
    String mAddr,mState,mCity,mDist,mPin,mMobile;
    String mArea,mBuildName,mFlatNo,mLandmark,mAddrLine1;
    String mUserId;
    Button mBtnSubmit,mBtnCancel;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private OnFragmentInteractionListener mListener;
    int validationFlag=0;

    public AddressFragment() {
        // Required empty public constructor
    }

    public static AddressFragment newInstance(String name, String email,String image) {
        AddressFragment fragment = new AddressFragment();
        Bundle args = new Bundle();
        args.putString(NAME, name);
        args.putString(EMAIL,email);
        args.putString(IMAGE,image);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(NAME);
            email = getArguments().getString(EMAIL);
            image = getArguments().getString(IMAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_address, container, false);
        init(view);
        mListener.onFragmentInteraction("Address");
        return view;
    }

    private void init(View view) {
        mJsonParser=new JSONParser();
        String address=null;
        mBtnSubmit=view.findViewById(R.id.btn_submit);
        mBtnCancel=view.findViewById(R.id.btn_cancel);

        mBtnSubmit.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);

        mEdtArea=view.findViewById(R.id.edt_area);
        mEdtBuildName=view.findViewById(R.id.edt_bld_no);
        mEdtFlatNo=view.findViewById(R.id.edt_flat_no);
        mEdtLandmark=view.findViewById(R.id.edt_land);

        mEdtAddr=view.findViewById(R.id.edt_addr);
        mEdtState=view.findViewById(R.id.edt_state);
        mEdtCity=view.findViewById(R.id.edt_city);
        mEdtDist=view.findViewById(R.id.edt_dist);
        mEdtPin=view.findViewById(R.id.edt_pin);
        mEdtMobile=view.findViewById(R.id.edt_mobile);

        mEdtState.setEnabled(false);
        mEdtDist.setEnabled(false);
        mEdtCity.setEnabled(false);
        mEdtPin.setEnabled(false);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getContext());
        databaseAccess.open();
        ArrayList<Address> userProfile=databaseAccess.getSingleUser();

            mAddr=userProfile.get(0).getAddress();
            mState=userProfile.get(0).getState();
            mCity=userProfile.get(0).getCity();
            mDist=userProfile.get(0).getDist();
            mPin=userProfile.get(0).getPin();
            mMobile=userProfile.get(0).getMobile();

        String CurrentString =mAddr;

        try {
            String[] separated = CurrentString.split("-");
            address= separated[0].substring(0, separated[0].indexOf(","));
            mArea = separated[3].substring(0, separated[3].indexOf(","));
            mBuildName = separated[1].substring(0, separated[1].indexOf(","));
            mFlatNo = separated[2].substring(0, separated[2].indexOf(","));
            mLandmark = separated[4];
        }catch (Exception e)
        {

        }
        if(mArea!=null)
         mEdtArea.setText(mArea);
        if(mBuildName!=null)
            mEdtBuildName.setText(mBuildName);
        if(mFlatNo!=null)
            mEdtFlatNo.setText(mFlatNo);
        if(mLandmark!=null)
            mEdtLandmark.setText(mLandmark);
        mEdtAddr.setText(address);
        mEdtState.setText(mState);
        mEdtDist.setText(mDist);
        mEdtCity.setText(mCity);
        mEdtPin.setText(mPin);
        mEdtMobile.setText(mMobile);

        mProfImg=view.findViewById(R.id.prof_img);
        mProfEmail=view.findViewById(R.id.prof_mail);
        mProfName=view.findViewById(R.id.prof_name);

        mProfName.setText(name);
        YoYo.with(Techniques.BounceIn)
                .duration(1500)
                .playOn(mProfName);

        mProfEmail.setText(email);

     /*   Glide.with(getContext()).load(image)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mProfImg);
        */
        Picasso.with(getContext())
                .load(image)
                .fit()
                .into(mProfImg);

        YoYo.with(Techniques.BounceIn)
                .duration(1500)
                .playOn(mProfImg);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.btn_submit:
                validationFlag=0;
                mArea=mEdtArea.getText().toString().trim();
                mBuildName=mEdtBuildName.getText().toString().trim();
                mFlatNo=mEdtFlatNo.getText().toString().trim();
                mLandmark=mEdtLandmark.getText().toString().trim();
                mAddrLine1=mEdtAddr.getText().toString().trim();

                mAddr=mAddrLine1+",Building name-"+mBuildName+",Flat no-"+mFlatNo+",Area-"+mArea+",Landmark-"+mLandmark;
                mState=mEdtState.getText().toString().trim();
                mCity=mEdtCity.getText().toString().trim();
                mDist=mEdtDist.getText().toString().trim();
                mPin=mEdtPin.getText().toString().trim();
                mUserId= SharedPreferencesHelper.getUserId(getContext()).trim();
                mMobile=mEdtMobile.getText().toString().trim();

                if(mArea.equals("") || mArea==null)
                {
                    validationFlag=1;
                    Toast.makeText(getContext(), "Please enter Area", Toast.LENGTH_SHORT).show();
                    //mEdtArea.setError("Please enter Area");
                }

                if(mLandmark.equals("") || mLandmark==null)
                {
                    validationFlag=1;
                    // mEdtArea.setError("Please enter Landmark");
                    Toast.makeText(getContext(), "Please enter Landmark", Toast.LENGTH_SHORT).show();
                }
                if(mAddrLine1.equals("") || mAddrLine1==null)
                {
                    validationFlag=1;
                    Toast.makeText(getContext(), "Please enter Address", Toast.LENGTH_SHORT).show();
                    //mEdtArea.setError("Please enter Address");
                }
                if(mMobile.length()<10)
                {
                    validationFlag=1;
                    mEdtMobile.setError("Please enter valid mobile number");
                }
                if(mAddr.equals(""))
                {
                    validationFlag=1;
                    mEdtAddr.setError("Please enter address");
                }
                if(validationFlag==0)
                {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                    new SendAddress().execute();
                    new UpdateFrndProfile().execute();
                }
                break;

            case R.id.btn_cancel:
                fragmentManager=getFragmentManager();

                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,new ProfileFragment());
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

    class SendAddress extends AsyncTask<String, String, String> {
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
                Address address=new Address(null,null,null,null,null,null,null);

                address.setAddress(mAddr);
                address.setCity(mCity);
                address.setState(mState);
                address.setPin(mPin);
                address.setDist(mDist);
                address.setUser_id(mUserId);
                address.setMobile(mMobile);

                SharedPreferencesHelper.setUserAddr(mAddr,getContext());
                SharedPreferencesHelper.setUserCity(mCity,getContext());
                SharedPreferencesHelper.setUserState(mState,getContext());
                SharedPreferencesHelper.setUserPin(mPin,getContext());
                SharedPreferencesHelper.setUserDist(mDist,getContext());
                SharedPreferencesHelper.setUserMobile(mMobile,getContext());

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
                    Log.e("login data", s);

                else
                    Log.e("data", "login is null");

                Address addressResponse=gson.fromJson(s,Address.class);
                Log.e("login data","Response"+addressResponse.getResponse());

                int response= Integer.parseInt(addressResponse.getResponse());
                if(response==Constants.PROFILE_RESPONSE)
                {
                    Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getContext());
                    databaseAccess.open();
                    databaseAccess.UpdateUserProfile(mAddr,mState,mDist,mCity,mPin,mMobile,mUserId);
                }
                mProgress.dismiss();
            }catch (Exception e)
            {
                Toast.makeText(getContext(),"Internet not available,can not update profile",Toast.LENGTH_LONG).show();
                mProgress.dismiss();
            }
        }
    }

    class UpdateFrndProfile extends AsyncTask<String, String, String> {

        String frndId=SharedPreferencesHelper.getSelfFrndId(getContext());
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
                friends.setAddress(mAddr);
                friends.setCity(mCity);
                friends.setState(mState);
                friends.setPin(mPin);
                friends.setDist(mDist);
                friends.setUser_id(mUserId);
                friends.setMobile(mMobile);
                friends.setF_id(frndId);
                friends.setFr_email(SharedPreferencesHelper.getUserEmail(getContext()));
                friends.setFr_name(Constants.SEND_TO_ME);

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
                    int f_id= Integer.parseInt(frndId);
                    databaseAccess.UpdateFrndsProfile(mAddr,mState,mDist,mCity,mPin,mMobile,"Self",SharedPreferencesHelper.getUserEmail(getContext()),f_id);

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
                Toast.makeText(getContext(),"Internet not available,can not update profile",Toast.LENGTH_LONG).show();
                //   mProgress.dismiss();
            }
        }
    }

}
