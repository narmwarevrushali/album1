package narmware.com.photouploadcopy.activity;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
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
    protected Dialog mEmptyProfDialog;

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

       /* DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        databaseAccess.deleteAll();*/
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

        //numberProgressBar= (NumberProgressBar) findViewById(R.id.number_progress_bar);
        mJsonParser=new JSONParser();
        mBtnSelect = (Button) findViewById(R.id.btn_upload);
        mBtnSelect.setOnClickListener(this);

        mGridView= (GridView) findViewById(R.id.PhoneImageGrid);
        mGridView.setVisibility(View.INVISIBLE);

        mImgEmpty= (ImageView) findViewById(R.id.img_empty);
        mImgEmpty.setVisibility(View.VISIBLE);

        mImgBack= (ImageButton) findViewById(R.id.btn_back);
        mImgBack.setOnClickListener(this);

        /*mBtnSelectFrame= (Button) findViewById(R.id.btn_select_frame);
        mBtnSelectFrame.setOnClickListener(this);*/
        temp=new ArrayList<>();
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

           /* case R.id.btn_select_frame:
                Intent intentFrame=new Intent(SelectImagesActivity.this,SelectFrameActivity.class);
                startActivity(intentFrame);
               // Toast.makeText(this, "Frame", Toast.LENGTH_SHORT).show();
                break;*/

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

        NumberProgressBar mHorizontalProgress =mProgressDialog.findViewById(R.id.number_progress);
        mHorizontalProgress.setMax(100);
        mHorizontalProgress.setProgress(progress);
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
        /*TextView mTxtProgress=mProgressDialog.findViewById(R.id.txt_upload_progress);
        mTxtProgress.setText(+progress+" %");*/
        //Toast.makeText(this, "Ooops! Can't upload images,Server not reachable", Toast.LENGTH_LONG).show();
        showNoConnectionDialog();
    }

    @Override
    public void onCompleted(int serverResponseCode, byte[] serverResponseBody) {
       // mProgressDialog.dismiss();

        Log.e("ServerResponse", new String(serverResponseBody)+"   "+serverResponseCode);

        /*TextView mTxtProgress=mProgressDialog.findViewById(R.id.txt_upload_progress);
        mTxtProgress.setText("Upload Completed");
        NumberProgressBar mHorizontalProgress =mProgressDialog.findViewById(R.id.number_progress);
        mHorizontalProgress.setProgress(100);*/

        Intent intentFrame=new Intent(SelectImagesActivity.this,SelectFrameActivity.class);
        startActivity(intentFrame);
        finish();
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


        //NewtonCradleLoading loading=  mProgressDialog.findViewById(R.id.newton);
        //loading.start();
        mProgressDialog.show();
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


}
