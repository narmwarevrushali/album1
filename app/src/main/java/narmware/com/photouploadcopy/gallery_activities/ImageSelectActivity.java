package narmware.com.photouploadcopy.gallery_activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import narmware.com.photouploadcopy.R;
import narmware.com.photouploadcopy.adapter.CustomImageSelectAdapter;
import narmware.com.photouploadcopy.helpers.Constants;
import narmware.com.photouploadcopy.models.Image;
import narmware.com.photouploadcopy.support.DatabaseAccess;
import narmware.com.photouploadcopy.support.SharedPreferencesHelper;
/* error : java.lang.OutOfMemoryError: Failed to allocate a 8294412 byte allocation with 6443432 free bytes and 6MB until OOM
*/
/**
 * Created by Darshan on 4/18/2015.
 */
public class ImageSelectActivity extends HelperActivity {
    private ArrayList<Image> images;
    private List<Image> selectedImages;
    private List<Image> selectedImagesOfOtherAlbums;
    private List<Integer> toggleImages;
    protected Dialog mSelectBlurImgDialog;
    private List<Image> temp;

    private String album;

    private TextView errorDisplay,mTxtCount;
    private Button mBtnAdd;

    private ProgressBar progressBar;
    private GridView gridView;
    private CustomImageSelectAdapter adapter;

    private ActionBar actionBar;

   // private ActionMode actionMode;
    private int countSelected;
    int countDefault=0;
    byte[] byteArray ;
    Bitmap bitmap;
    ByteArrayOutputStream byteArrayOutputStream ;
    private ContentObserver observer;
    private Handler handler;
    private Thread thread;
    int count=0;
    DatabaseAccess databaseAccess;
    private final String[] projection = new String[]{ MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA,"width", "height"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);
        setView(findViewById(R.id.layout_image_select));

        toggleImages=new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTxtCount=toolbar.findViewById(R.id.txt_count);
        mBtnAdd=toolbar.findViewById(R.id.btn_add);
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIntent();
            }
        });

      /*  actionBar = getSupportActionBar();
        if (actionBar != null) {
            //final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
            final VectorDrawableCompat upArrow  = VectorDrawableCompat.create(getResources(),R.drawable.back, null);
           // upArrow.setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(upArrow);
           // actionBar.setDisplayShowTitleEnabled(true);
            //actionBar.setTitle(R.string.image_view);
        }*/

        Intent intent = getIntent();
        if (intent == null) {
            finish();
        }
        databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        temp=new ArrayList<>();
        album = intent.getStringExtra(Constants.INTENT_EXTRA_ALBUM);
        //countDefault=intent.getIntExtra("count",0);
        errorDisplay = (TextView) findViewById(R.id.text_view_error);
        errorDisplay.setVisibility(View.INVISIBLE);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar_image_select);
        gridView = (GridView) findViewById(R.id.grid_view_image_select);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*if (actionMode == null) {
                    actionMode = ImageSelectActivity.this.startActionMode(callback);
                }
                toggleSelection(position);
                actionMode.setTitle(countSelected + " " + getString(R.string.selected));

                if (countSelected == 0) {
                    actionMode.finish();
                }*/
                try {

                    if(images.get(position).height == null || images.get(position).width == null)
                    {
                        toggleSelection(position);
                    }else {
                        int height = Integer.parseInt(images.get(position).height);
                        int width = Integer.parseInt(images.get(position).width);
                       // Toast.makeText(ImageSelectActivity.this, height + " " + width, Toast.LENGTH_SHORT).show();

                        if (height < Constants.DEFAULT_HEIGHT && width < Constants.DEFAULT_WIDTH && images.get(position).isSelected == false) {
                            Toast.makeText(ImageSelectActivity.this, R.string.blur_img_msg, Toast.LENGTH_SHORT).show();
                            //showAllowDialog(position);
                        } else {
                            toggleSelection(position);
                        }
                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                   // toggleSelection(position);
                }

            }
        });

      /*  images=new ArrayList<>();
        for(int i=0;i<images.size();i++) {
            if (images.get(i).isSelected==true)
            {
                toggleSelection(i);
            }
        }*/

    }

    @Override
    protected void onStart() {
        super.onStart();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Constants.PERMISSION_GRANTED: {
                        loadImages();
                        break;
                    }

                    case Constants.FETCH_STARTED: {
                        progressBar.setVisibility(View.VISIBLE);
                        gridView.setVisibility(View.INVISIBLE);
                        break;
                    }

                    case Constants.FETCH_COMPLETED: {
                        /*
                        If adapter is null, this implies that the loaded images will be shown
                        for the first time, hence send FETCH_COMPLETED message.
                        However, if adapter has been initialised, this thread was run either
                        due to the activity being restarted or content being changed.
                         */
                        if (adapter == null) {
                            adapter = new CustomImageSelectAdapter(getApplicationContext(), images);
                            gridView.setAdapter(adapter);

                            selectedImages=new ArrayList<>();
                            selectedImagesOfOtherAlbums=new ArrayList<>();

                            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(ImageSelectActivity.this);
                            databaseAccess.open();
                            selectedImages=databaseAccess.getSelectedImages(album);
                            selectedImagesOfOtherAlbums=databaseAccess.getSelectedImagesOfOtherAlbums(album);
                            countDefault=0;
                            countDefault = selectedImagesOfOtherAlbums.size();
                            mTxtCount.setText(countDefault+"");
                            Toast.makeText(ImageSelectActivity.this,album,Toast.LENGTH_SHORT).show();


                          try {

                              if(selectedImages.size() != 0) {
                                  long imgId = 0;

                                  for (int i = 0; i < selectedImages.size(); i++) {
                                      imgId = selectedImages.get(i).id;

                                      for (int j = 0; j < images.size(); j++) {

                                          if (imgId == images.get((int) gridView.getItemIdAtPosition(j)).id) {
                                              toggleImages.add((int) gridView.getItemIdAtPosition(j));
                                              Log.e("Selected Image pos", gridView.getItemIdAtPosition(j) + "  " + images.get((int) gridView.getItemIdAtPosition(j)).path);
                                          }
                                      }
                                  }

                                  for (int toggle = 0; toggle <= toggleImages.size(); toggle++) {

                                      Log.e("toggle image size", "Size  " + toggleImages.size());
                                      Log.e("toggle images pos", "pos  " + toggleImages.get(toggle));

                                      toggleSelection(toggleImages.get(toggle));
                                  }
                              }

                          }catch (Exception e)
                          {

                          }
                            progressBar.setVisibility(View.INVISIBLE);
                            gridView.setVisibility(View.VISIBLE);
                            orientationBasedUI(getResources().getConfiguration().orientation);

                        } else {
                            adapter.notifyDataSetChanged();
                            /*
                            Some selected images may have been deleted
                            hence update action mode title
                             */
                           /* if (actionMode != null) {
                                countSelected = msg.arg1;
                                actionMode.setTitle(countSelected + " " +R.string.selected);
                            }*/
                        }
                        break;
                    }

                    case Constants.ERROR: {
                        progressBar.setVisibility(View.INVISIBLE);
                        errorDisplay.setVisibility(View.VISIBLE);
                        break;
                    }

                    default: {
                        super.handleMessage(msg);
                    }
                }
            }
        };
        observer = new ContentObserver(handler) {
            @Override
            public void onChange(boolean selfChange) {
                loadImages();
            }
        };
        getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, false, observer);

        checkPermission();
    }

    @Override
    protected void onStop() {
        super.onStop();

        stopThread();

        getContentResolver().unregisterContentObserver(observer);
        observer = null;

        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(null);
        }
        images = null;
        if (adapter != null) {
            adapter.releaseResources();
        }
        gridView.setOnItemClickListener(null);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        orientationBasedUI(newConfig.orientation);
    }

    private void orientationBasedUI(int orientation) {
        final WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        final DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);

        if (adapter != null) {
            int size = orientation == Configuration.ORIENTATION_PORTRAIT ? metrics.widthPixels / 3 : metrics.widthPixels / 5;
            adapter.setLayoutParams(size);
        }
        gridView.setNumColumns(orientation == Configuration.ORIENTATION_PORTRAIT ? 3 : 5);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                //onBackPressed();
                return true;
            }

            default: {
                return false;
            }
        }
    }


    private ActionMode.Callback callback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater menuInflater = mode.getMenuInflater();
            menuInflater.inflate(R.menu.menu_contextual_action_bar, menu);

           // actionMode = mode;
            countSelected = 0;

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {int i = item.getItemId();
            if (i == R.id.menu_item_add_image) {
                sendIntent();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (countSelected > 0) {
                //deselectAll();
            }
           // actionMode = null;
        }
    };

    private void toggleSelection(int position) {

        final DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        if(countDefault != 0) {
            countSelected=0;
            countSelected = countDefault;
            countDefault=0;
        }
        if (!images.get(position).isSelected && countSelected >= Constants.limit) {
            Toast.makeText(
                    getApplicationContext(),
                    String.format(getString(R.string.limit_exceeded), Constants.limit),
                    Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        images.get(position).isSelected = !images.get(position).isSelected;
        if (images.get(position).isSelected) {
            countSelected++;
            mTxtCount.setText(countSelected + "");

            String id= Long.toString(images.get(position).id);
            String name=images.get(position).name;
            String path=images.get(position).path;
            String isSelected= String.valueOf(images.get(position).isSelected);
            String height=images.get(position).height;
            String width=images.get(position).width;

            Log.e("Image isSelected ",isSelected+"     "+images.get(position).isSelected+"    "+id);
            databaseAccess.setImages(id,name,path,isSelected,album,height,width);
            //Toast.makeText(ImageSelectActivity.this,"Height: "+images.get(position).height+"width: "+images.get(position).width,Toast.LENGTH_LONG).show();
        } else {
            countSelected--;

            databaseAccess.deleteSingle(images.get(position).id,images.get(position).album);
            mTxtCount.setText(countSelected+"");
        }
        adapter.notifyDataSetChanged();
    }

    private void deselectAll() {
        for (int i = 0, l = images.size(); i < l; i++) {
            images.get(i).isSelected = false;
        }
        countSelected = 0;
        adapter.notifyDataSetChanged();
    }
    public void showAllowDialog(final int position)
    {
        mSelectBlurImgDialog = new Dialog(ImageSelectActivity.this);
        mSelectBlurImgDialog.setContentView(R.layout.dialog_high_img);
        mSelectBlurImgDialog.setCancelable(false);
        mSelectBlurImgDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        mSelectBlurImgDialog.show();

        Button mBtnYes= (Button) mSelectBlurImgDialog.findViewById(R.id.btn_ok);

        mBtnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // toggleSelection(position);
                mSelectBlurImgDialog.dismiss();
            }
        });

    }
    private ArrayList<Image> getSelected() {
        temp=new ArrayList<>();
        ArrayList<Image> selectedImages = new ArrayList<>();

       // databaseAccess.deleteAll();

        for (int i = 0, l = images.size(); i < l; i++) {
            if (images.get(i).isSelected) {
                selectedImages.add(images.get(i));

             /*   String id= Long.toString(images.get(i).id);
                String name=images.get(i).name;
                String path=images.get(i).path;
                String isSelected= String.valueOf(images.get(i).isSelected);
                String height=images.get(i).height;
                String width=images.get(i).width;

                Log.e("Image isSelected ",isSelected+"     "+images.get(i).isSelected);
                databaseAccess.setImages(id,name,path,isSelected,album,height,width);*/

                //Toast.makeText(ImageSelectActivity.this,selectedImages.get(i).name,Toast.LENGTH_SHORT).show();
            }

        }

        temp=databaseAccess.getAllDetails();
        for(int c=0;c < temp.size();c++)
        {
            Log.e("Image Data","\n id  "+temp.get(c).id+"\n name  "+temp.get(c).name +"\n path  "+temp.get(c).path+"\n selected  "+temp.get(c).isSelected+"\n album  "+temp.get(c).album+"\n height  "+temp.get(c).height+"\n width  "+temp.get(c).width);

            if(temp.get(c).isSelected){
                Log.e("HEllo","USer");
            }
        }
        return selectedImages;
    }

    private void sendIntent() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES, getSelected());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void loadImages() {
        startThread(new ImageLoaderRunnable());
    }

    private class ImageLoaderRunnable implements Runnable {
        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            /*
            If the adapter is null, this is first time this activity's view is
            being shown, hence send FETCH_STARTED message to show progress bar
            while images are loaded from phone
             */
            if (adapter == null) {
                sendMessage(Constants.FETCH_STARTED);
            }

            File file;
            HashSet<Long> selectedImages = new HashSet<>();
            if (images != null) {
                Image image;
                for (int i = 0, l = images.size(); i < l; i++) {
                    image = images.get(i);
                    file = new File(image.path);
                    if (file.exists() && image.isSelected) {
                        selectedImages.add(image.id);
                    }
                }
            }

            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " =?", new String[]{ album }, MediaStore.Images.Media.DATE_ADDED);
            if (cursor == null) {
                sendMessage(Constants.ERROR);
                return;
            }

            /*
            In case this runnable is executed to onChange calling loadImages,
            using countSelected variable can result in a race condition. To avoid that,
            tempCountSelected keeps track of number of selected images. On handling
            FETCH_COMPLETED message, countSelected is assigned value of tempCountSelected.
             */
            int tempCountSelected = 0;
            ArrayList<Image> temp = new ArrayList<>(cursor.getCount());
            temp.clear();
            if (cursor.moveToLast()) {
                do {
                    if (Thread.interrupted()) {
                        return;
                    }

                    long id = cursor.getLong(cursor.getColumnIndex(projection[0]));
                   String name = cursor.getString(cursor.getColumnIndex(projection[1]));
                    String path = cursor.getString(cursor.getColumnIndex(projection[2]));
                    String height = cursor.getString(cursor.getColumnIndex(projection[3]));
                    String width = cursor.getString(cursor.getColumnIndex(projection[4]));
                    boolean isSelected = selectedImages.contains(id);

                    if (isSelected) {
                        tempCountSelected++;
                    }

                    file = new File(path);
                    if (file.exists()) {
                        temp.add(new Image(id, name, path, isSelected,album,height,width));
                    }

                } while (cursor.moveToPrevious());
            }
            cursor.close();

            if (images == null) {
                images = new ArrayList<>();
            }
            images.clear();
            images.addAll(temp);
            temp.clear();

            sendMessage(Constants.FETCH_COMPLETED, tempCountSelected);

        }
    }

    private void startThread(Runnable runnable) {
        stopThread();
        thread = new Thread(runnable);
        thread.start();
    }

    private void stopThread() {
        if (thread == null || !thread.isAlive()) {
            return;
        }

        thread.interrupt();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(int what) {
        sendMessage(what, 0);
    }

    private void sendMessage(int what, int arg1) {
        if (handler == null) {
            return;
        }

        Message message = handler.obtainMessage();
        message.what = what;
        message.arg1 = arg1;
        message.sendToTarget();
    }

    @Override
    protected void permissionGranted() {
        sendMessage(Constants.PERMISSION_GRANTED);
    }

    @Override
    protected void hideViews() {
        progressBar.setVisibility(View.INVISIBLE);
        gridView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
      //  super.onBackPressed();

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Selected images will be discard!")
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
                        SharedPreferencesHelper.setAlbumId(null,ImageSelectActivity.this);
                        SharedPreferencesHelper.setInvoiceId(null,ImageSelectActivity.this);
                        Intent intent = new Intent();
                        intent.putParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES, getSelected());
                        setResult(RESULT_OK, intent);
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
    }
}
