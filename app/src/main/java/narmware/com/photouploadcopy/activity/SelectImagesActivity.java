package narmware.com.photouploadcopy.activity;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.google.gson.Gson;

import net.gotev.uploadservice.MultipartUploadRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;
import narmware.com.photouploadcopy.MyApplication;
import narmware.com.photouploadcopy.R;
import narmware.com.photouploadcopy.adapter.GalleryAdapter;
import narmware.com.photouploadcopy.broadcast.SingleUploadBroadcastReceiver;
import narmware.com.photouploadcopy.gallery_activities.AlbumSelectActivity;
import narmware.com.photouploadcopy.helpers.Constants;
import narmware.com.photouploadcopy.models.Address;
import narmware.com.photouploadcopy.models.Friends;
import narmware.com.photouploadcopy.models.Image;
import narmware.com.photouploadcopy.services.AutoUploadService;
import narmware.com.photouploadcopy.support.DatabaseAccess;
import narmware.com.photouploadcopy.support.JSONParser;
import narmware.com.photouploadcopy.support.SharedPreferencesHelper;

/**
 * Created by comp16 on 12/13/2017.
 */

public class SelectImagesActivity extends AppCompatActivity implements View.OnClickListener,SingleUploadBroadcastReceiver.Delegate {
    int PICK_IMAGE_REQUEST = 1;
    int numOfImgToSelect=30;
    int endPosition=0;
    Button mBtnSelect;
    GridView mGridView;
   // Button mBtnSelectFrame;
    ImageView mImgEmpty;
    ImageButton mImgBack;
    public static int countSelected;
    DatabaseAccess databaseAccess;
    public static ArrayList<Image> images;
    public static List<Image> temp;
    public static GalleryAdapter mAdapter;
    private boolean uploadEnabled;
    PendingIntent pintent;
    AlarmManager alarm;
   protected Dialog mProgressDialog;
    private JSONParser mJsonParser;

    protected Dialog mNoConnectionDialog;
    AnimatedCircleLoadingView circleLoading;
    protected Dialog mEmptyProfDialog;
    int validationFlag=0;
    EditText mEdtAddr,mEdtMobile;
    EditText mEdtArea,mEdtBuildName,mEdtFlatNo,mEdtLandmark;
    String mAddress,mState,mCity,mDist,mPin,mMobile;
    String mArea,mBuildName,mFlatNo,mLandmark,mAddrLine1;
    Button mProgressBtnSubmit;
    //  NumberProgressBar numberProgressBar;
    private Timer timer;

    private final SingleUploadBroadcastReceiver uploadReceiver =
            new SingleUploadBroadcastReceiver();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image);

        init();

        databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();

        Intent intent = new Intent(this, AlbumSelectActivity.class);
        //set limit on number of images that can be selected, default is 10
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, numOfImgToSelect);
        intent.putExtra("count", countSelected);
        startActivityForResult(intent, Constants.REQUEST_CODE);

        setUploadEnabled(true);

    }

    private void setUploadEnabled(boolean enable) {
        uploadEnabled = enable;

        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(SelectImagesActivity.this, AutoUploadService.class);
        pintent = PendingIntent.getService(SelectImagesActivity.this, 0, intent, 0);
        alarm = (AlarmManager)SelectImagesActivity.this.getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),2, pintent);

        if (enable) {
            // start detect service

            startService(intent);

        } else {
            // stop detect service
            stopService(intent);

        }
    }
    private void init() {
        mJsonParser=new JSONParser();
        mBtnSelect = (Button) findViewById(R.id.btn_upload);
        mBtnSelect.setOnClickListener(this);

        mGridView= (GridView) findViewById(R.id.PhoneImageGrid);
        mGridView.setVisibility(View.INVISIBLE);

        mImgEmpty= (ImageView) findViewById(R.id.img_empty);
        mImgEmpty.setVisibility(View.VISIBLE);

        mImgBack= (ImageButton) findViewById(R.id.btn_back);
        mImgBack.setOnClickListener(this);

        temp=new ArrayList<>();
    }

    public void getUserData()
    {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(SelectImagesActivity.this);
        databaseAccess.open();
        ArrayList<Address> userProfile = databaseAccess.getSingleUser();

        mAddress=userProfile.get(0).getAddress();
        mState=userProfile.get(0).getState();
        mCity=userProfile.get(0).getCity();
        mDist=userProfile.get(0).getDist();
        mPin=userProfile.get(0).getPin();
        mMobile=userProfile.get(0).getMobile();

        String CurrentString =mAddress;

        try {
            String[] separated = CurrentString.split("-");
            mAddrLine1= separated[0].substring(0, separated[0].indexOf(","));
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
        mEdtAddr.setText(mAddrLine1);
        mEdtMobile.setText(mMobile);

        Log.e("Loader data",mArea+"  "+mMobile);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_back:
                Intent intent = new Intent(this, AlbumSelectActivity.class);
            //set limit on number of images that can be selected, default is 10
                intent.putExtra(Constants.INTENT_EXTRA_LIMIT, numOfImgToSelect);
                intent.putExtra("count", countSelected);
                startActivityForResult(intent, Constants.REQUEST_CODE);
                break;

            case R.id.btn_upload:

                new DeletePhotoFromAlbum().execute();

                Log.e("Album id",SharedPreferencesHelper.getAlbumId(SelectImagesActivity.this));
                showUploadProress();

                if(images.size()!=0)
                {
                    endPosition=images.size()-1;
                    for(int totalImg=0;totalImg<images.size();totalImg++)
                    {
                        uploadMultipart(totalImg,endPosition);
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            //The array list has the image paths of the selected images


            temp.clear();
            temp=databaseAccess.getAllDetails();
            if(temp.size() != 0)
            {
                mImgEmpty.setVisibility(View.INVISIBLE);
                mGridView.setVisibility(View.VISIBLE);
                mBtnSelect.setEnabled(true);

            }
            if(temp.size() == 0)
            {
                mImgEmpty.setVisibility(View.VISIBLE);
                mGridView.setVisibility(View.INVISIBLE);
                mBtnSelect.setEnabled(false);
                finish();
            }
            //temp = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            images=new ArrayList<>();
            images.clear();
            for(int i=0;i<temp.size();i++) {

                images.add(temp.get(i));

                // Toast.makeText(SelectImagesActivity.this,images.get(i).name, Toast.LENGTH_SHORT).show();
            }
            if(images.size() == 0)
            {
                mImgEmpty.setVisibility(View.VISIBLE);
                mGridView.setVisibility(View.INVISIBLE);
                mBtnSelect.setEnabled(false);
                finish();
            }
            countSelected=images.size();
            mAdapter=new GalleryAdapter(images,SelectImagesActivity.this);
            mGridView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    databaseAccess.deleteSingle(images.get(i).id,images.get(i).album);
                    countSelected--;

                    if(countSelected==0)
                    {
                       finish();
                    }
                    try{
                        Log.e("Remove items", adapterView.getItemIdAtPosition(i) + "    " + images.get(i).id);
                        images.remove(images.get((int) adapterView.getItemIdAtPosition(i)));
                    }
                    catch (Exception e)
                    {
                            e.printStackTrace();
                    }
                    mAdapter.notifyDataSetChanged();

                }
            });

        }
}

    @Override
    public void onBackPressed() {

       // Toast.makeText(this, "OnBackPress", Toast.LENGTH_SHORT).show();

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Your changes will be discard!")
                .setConfirmText("Yes,discard it!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                      /*  sDialog
                                .setTitleText("Discarded!")
                                .setContentText("Your imaginary file has been deleted!")
                                .setConfirmText("OK")
                                .setConfirmClickListener(null)
                                .showCancelButton(false)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);*/

                        databaseAccess.deleteAll();
                        SharedPreferencesHelper.setAlbumId(null,SelectImagesActivity.this);
                        SharedPreferencesHelper.setInvoiceId(null,SelectImagesActivity.this);
                        finish();

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
        //databaseAccess.deleteAll();
    }

    public void uploadMultipart(int position,int end) {

        //getting the actual path of the image
        String path =images.get(position).path;
        String img_id= String.valueOf(images.get(position).id);
        String uploadId = UUID.randomUUID().toString();

        uploadReceiver.setDelegate(this);
        uploadReceiver.setUploadID(uploadId);

        //Uploading code
        try {
             //Creating a multi part request
                new MultipartUploadRequest(this, uploadId, MyApplication.URL_PHOTO_UPLOAD)
                        .addFileToUpload(path,Constants.IMAGE_PATH) //Adding file
                        .addParameter(Constants.IMAGE_NAME, "MyImage")//Adding text parameter to the request
                        .addParameter(Constants.IMAGE_ID, img_id)//Adding text parameter to the request
                        .addParameter(Constants.ALBUM_ID,SharedPreferencesHelper.getAlbumId(SelectImagesActivity.this))//Adding text parameter to the request
                        .setMaxRetries(2)
                        //.setNotificationConfig(new UploadNotificationConfig())
                        .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        uploadReceiver.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        uploadReceiver.unregister(this);
    }

    @Override
    public void onProgress(int progress) {

        Log.e("Progress",""+progress);

        if(progress<100) {
            circleLoading.setPercent(progress);
        }
    }

    @Override
    public void onProgress(long uploadedBytes, long totalBytes) {
        Log.e("ServerProgress",uploadedBytes+" ");
        //your implementation
    }

    @Override
    public void onError(Exception exception) {
        Log.e("ServerError","Errrrrorrrr!!!!");
        mProgressDialog.dismiss();

        TextView mTxtProgress=mProgressDialog.findViewById(R.id.txt_upload_progress);
        mTxtProgress.setText("Upload Error");
        //Toast.makeText(this, "Ooops! Can't upload images,Server not reachable", Toast.LENGTH_LONG).show();
        showNoConnectionDialog();
    }

    @Override
    public void onCompleted(int serverResponseCode, byte[] serverResponseBody) {
       // mProgressDialog.dismiss();

        Log.e("ServerResponse", new String(serverResponseBody)+"   "+serverResponseCode);
        circleLoading.setPercent(100);
        circleLoading.stopOk();

        TextView mTxtProgress=mProgressDialog.findViewById(R.id.txt_upload_progress);
        mTxtProgress.setText("Upload Completed");

        mProgressBtnSubmit.setEnabled(true);
        mProgressBtnSubmit.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

       /* Intent intentFrame=new Intent(SelectImagesActivity.this,SelectFrameActivity.class);
        startActivity(intentFrame);
        finish();*/
    }

    @Override
    public void onCancelled() {
        //your implementation
    }


    class DeletePhotoFromAlbum extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Toast.makeText(LoginActivity.this,"Pre execute",Toast.LENGTH_SHORT).show();

        }

        @Override
        protected String doInBackground(String... strings) {
            String json = null;
            try
            {
                Gson gson = new Gson();

                HashMap<String, String> params = new HashMap<>();
                params.put(Constants.ALBUM_ID, SharedPreferencesHelper.getAlbumId(SelectImagesActivity.this));

                String url =MyApplication.URL_DELETE_ALBUM;
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
                    Log.e("Album data", s);

                else
                    Log.e("data", "Album is null");

            }catch (Exception e)
            {
                mProgressDialog.dismiss();
                showNoConnectionDialog();
                //Toast.makeText(SelectImagesActivity.this,"Internet not available",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showUploadProress() {
        mProgressDialog = new Dialog(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        mProgressDialog.setContentView(R.layout.dialog_loader);
        mProgressDialog.setCancelable(false);
        mProgressDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        circleLoading =mProgressDialog.findViewById(R.id.number_progress);
        circleLoading.startDeterminate();

        mEdtArea=mProgressDialog.findViewById(R.id.edt_area);
        mEdtBuildName=mProgressDialog.findViewById(R.id.edt_bld_no);
        mEdtFlatNo=mProgressDialog.findViewById(R.id.edt_flat_no);
        mEdtLandmark=mProgressDialog.findViewById(R.id.edt_land);

        mEdtAddr=mProgressDialog.findViewById(R.id.edt_addr);
        mEdtMobile=mProgressDialog.findViewById(R.id.edt_mobile);

        mProgressDialog.show();
        getUserData();

        mProgressBtnSubmit=mProgressDialog.findViewById(R.id.btn_submit);
        mProgressBtnSubmit.setEnabled(false);
        mProgressBtnSubmit.setBackgroundColor(getResources().getColor(R.color.grey_500));

        mProgressBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validationFlag=0;

                mArea=mEdtArea.getText().toString().trim();
                mBuildName=mEdtBuildName.getText().toString().trim();
                mFlatNo=mEdtFlatNo.getText().toString().trim();
                mLandmark=mEdtLandmark.getText().toString().trim();
                mAddrLine1=mEdtAddr.getText().toString().trim();
                mAddress=mAddrLine1+",Building name-"+mBuildName+",Flat no-"+mFlatNo+",Area-"+mArea+",Landmark-"+mLandmark;
                mMobile=mEdtMobile.getText().toString().trim();

                if(mArea.equals("") || mArea==null)
                {
                    validationFlag=1;
                    Toast.makeText(SelectImagesActivity.this, "Please enter Area", Toast.LENGTH_SHORT).show();
                    //mEdtArea.setError("Please enter Area");
                }

                if(mLandmark.equals("") || mLandmark==null)
                {
                    validationFlag=1;
                   // mEdtArea.setError("Please enter Landmark");
                    Toast.makeText(SelectImagesActivity.this, "Please enter Landmark", Toast.LENGTH_SHORT).show();
                }
                if(mAddrLine1.equals("") || mAddrLine1==null)
                {
                    validationFlag=1;
                    Toast.makeText(SelectImagesActivity.this, "Please enter Address", Toast.LENGTH_SHORT).show();
                    //mEdtArea.setError("Please enter Address");
                }
                if(mMobile.length()<10)
                {
                    validationFlag=1;
                    Toast.makeText(SelectImagesActivity.this, "Please enter valid mobile number", Toast.LENGTH_SHORT).show();
                    //mEdtArea.setError("Please enter Address");
                }
                if(validationFlag==0) {
                    new SendAddress().execute();
                    new UpdateFrndProfile().execute();
                }
            }
        });
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
                databaseAccess.deleteAll();
                finish();
            }
        });

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mNoConnectionDialog.dismiss();
            }
        });
    }

    class SendAddress extends AsyncTask<String, String, String> {
        protected ProgressDialog mProgress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Toast.makeText(LoginActivity.this,"Pre execute",Toast.LENGTH_SHORT).show();
            mProgress = new ProgressDialog(SelectImagesActivity.this);
            mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgress.setIndeterminate(true);
            mProgress.setMessage("Updating Address");
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
                address.setAddress(mAddress);
                address.setCity(mCity);
                address.setState(mState);
                address.setPin(mPin);
                address.setDist(mDist);
                address.setMobile(mMobile);
                address.setUser_id(SharedPreferencesHelper.getUserId(SelectImagesActivity.this));

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
                    Toast.makeText(SelectImagesActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(SelectImagesActivity.this);
                    databaseAccess.open();
                    databaseAccess.UpdateUserAddress(mAddress, SharedPreferencesHelper.getUserId(SelectImagesActivity.this));
                    databaseAccess.UpdateUserMobile(mMobile, SharedPreferencesHelper.getUserId(SelectImagesActivity.this));

                    Intent intentFrame=new Intent(SelectImagesActivity.this,SelectFrameActivity.class);
                    startActivity(intentFrame);
                    finish();
                }
                mProgress.dismiss();
            }catch (Exception e)
            {
                Toast.makeText(SelectImagesActivity.this,"Internet not available,can not update profile",Toast.LENGTH_LONG).show();
                mProgress.dismiss();
            }
        }
    }

    class UpdateFrndProfile extends AsyncTask<String, String, String> {

        String frndId=SharedPreferencesHelper.getSelfFrndId(SelectImagesActivity.this);
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
                friends.setAddress(mAddress);
                friends.setCity(mCity);
                friends.setState(mState);
                friends.setPin(mPin);
                friends.setDist(mDist);
                friends.setMobile(mMobile);
                friends.setUser_id(SharedPreferencesHelper.getUserId(SelectImagesActivity.this));
                friends.setMobile(mMobile);
                friends.setF_id(frndId);
                friends.setFr_email(SharedPreferencesHelper.getUserEmail(SelectImagesActivity.this));
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
                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(SelectImagesActivity.this);
                    databaseAccess.open();
                    int f_id= Integer.parseInt(frndId);
                    databaseAccess.UpdateFrndsProfile(mAddress,mState,mDist,mCity,mPin,mMobile,"Self",SharedPreferencesHelper.getUserEmail(SelectImagesActivity.this),f_id);

                    List<Friends> temp =databaseAccess.getFriends();
                    for(int c=0;c < temp.size();c++)
                    {
                        Log.e("Friends data","Name  "+temp.get(c).getFr_name()+"server id  "+temp.get(c).getF_id());
                    }

                    Toast.makeText(SelectImagesActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                }
                if(response==Constants.ALREADY_PRESENT)
                {
                    Toast.makeText(SelectImagesActivity.this, "Friend is already added", Toast.LENGTH_SHORT).show();
                }
                // mProgress.dismiss();
            }catch (Exception e)
            {
                Toast.makeText(SelectImagesActivity.this,"Internet not available,can not update profile",Toast.LENGTH_LONG).show();
                //   mProgress.dismiss();
            }
        }
    }

}
