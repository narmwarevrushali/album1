package narmware.com.photouploadcopy.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import narmware.com.photouploadcopy.MyApplication;
import narmware.com.photouploadcopy.R;
import narmware.com.photouploadcopy.activity.SelectImagesActivity;
import narmware.com.photouploadcopy.helpers.Constants;
import narmware.com.photouploadcopy.models.UserAlbum;
import narmware.com.photouploadcopy.support.DatabaseAccess;
import narmware.com.photouploadcopy.support.JSONParser;
import narmware.com.photouploadcopy.support.SharedPreferencesHelper;


/**
 * Created by Lincoln on 31/03/16.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyViewHolder>{

     List<UserAlbum> albums;
    Context mContext;
    String mAlbumSize,mAlbumPrice;
    int mShippingCharges;
    JSONParser mJsonParser;
    protected Dialog mNoConnectionDialog;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView mTxtAlbumSize,mTxtAlbumPrice;
        UserAlbum mItem;

        public MyViewHolder(View view) {
            super(view);
            mTxtAlbumPrice= (TextView) view.findViewById(R.id.txt_album_price);
            mTxtAlbumSize=view.findViewById(R.id.txt_album_size);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                if(SharedPreferencesHelper.getPinStatus(mContext).equals(Constants.INVALID))
                {
                }
                else{
                    mAlbumPrice=mItem.getA_price();
                    mAlbumSize=mItem.getA_size();
                    mShippingCharges= Integer.parseInt(mItem.getA_shippingcharge());
                    SharedPreferencesHelper.setAlbumPrice(mAlbumPrice,mContext);
                    SharedPreferencesHelper.setAlbumSize(mAlbumSize,mContext);
                    SharedPreferencesHelper.setShippingCharges(mShippingCharges,mContext);

                    if(SharedPreferencesHelper.getAlbumId(mContext)==null)
                    {
                        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(mContext);
                        databaseAccess.open();
                        databaseAccess.deleteAll();

                        new GetAlbumID().execute();
                    }
                    else {
                        if (SharedPreferencesHelper.getPaymentStatus(mContext).equals(Constants.UNPAID)) {

                            new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Cancel previous order?")
                                    .setContentText("Your previous order is saved")
                                    .setConfirmText("Yes")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {

                                            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(mContext);
                                            databaseAccess.open();
                                            databaseAccess.deleteAll();

                                            new GetAlbumID().execute();
                                            sDialog.cancel();

                                        }
                                    })
                                    .showCancelButton(true)
                                    .setCancelText("No")
                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.cancel();

                                            final AppCompatActivity act = (AppCompatActivity) mContext;
                                            Intent intent = new Intent(mContext, SelectImagesActivity.class);
                                            act.startActivity(intent);
                                        }
                                    })
                                    .show();
                        } else {
                            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(mContext);
                            databaseAccess.open();
                            databaseAccess.deleteAll();

                            new GetAlbumID().execute();
                        }
                    }
                    }

                }
            });

        }
    }

    public AlbumAdapter(Context context, List<UserAlbum> albums) {
        mJsonParser=new JSONParser();
        this.mContext = context;
        this.albums = albums;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_album_next, parent, false);


        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        UserAlbum album = albums.get(position);

        holder.mTxtAlbumSize.setText(album.getA_size());
        holder.mTxtAlbumPrice.setText(album.getA_price());

        holder.mItem=album;
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private AlbumAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final AlbumAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    class GetAlbumID extends AsyncTask<String, String, String> {
        protected ProgressDialog mProgress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Toast.makeText(LoginActivity.this,"Pre execute",Toast.LENGTH_SHORT).show();
            mProgress = new ProgressDialog(mContext);
            mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgress.setIndeterminate(true);
            mProgress.setMessage("Creating Album...");
            mProgress.setCancelable(false);
            mProgress.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            String json = null;
            try
            {
                Gson gson = new Gson();
                UserAlbum userAlbum=new UserAlbum();

                userAlbum.setUser_id(SharedPreferencesHelper.getUserId(mContext));
                userAlbum.setA_price(mAlbumPrice);
                userAlbum.setA_size(mAlbumSize);

                json = gson.toJson(userAlbum);
                Log.e("JSON data updated",json);

                HashMap<String, String> params = new HashMap<>();
                params.put(Constants.JSON_STRING,json);

                Log.e("JSON data updated ob",json);
                String url = MyApplication.URL_SERVER + MyApplication.URL_GET_ALBUM;
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

                UserAlbum userAlbum=gson.fromJson(s,UserAlbum.class);
                Log.e("JSON Album data","Response"+userAlbum.getResponse()+"\n album id"+userAlbum.getA_id());

                int response= Integer.parseInt(userAlbum.getResponse());
                if(response== Constants.NEW_ENTRY)
                {
                    SharedPreferencesHelper.setAlbumId(userAlbum.getA_id(),mContext);
                    SharedPreferencesHelper.setPaymentStatus(Constants.UNPAID,mContext);

                    final AppCompatActivity act = (AppCompatActivity) mContext;
                    Intent intent = new Intent(mContext, SelectImagesActivity.class);
                    act.startActivity(intent);
                }
                mProgress.dismiss();
            }catch (Exception e)
            {
                showNoConnectionDialog();
               // Toast.makeText(mContext,"Internet not available,can not login",Toast.LENGTH_LONG).show();
                mProgress.dismiss();
            }
        }
    }


    private void showNoConnectionDialog() {
        mNoConnectionDialog = new Dialog(mContext, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        mNoConnectionDialog.setContentView(R.layout.dialog_noconnectivity);
        mNoConnectionDialog.setCancelable(false);
        mNoConnectionDialog.show();

        Button exit = mNoConnectionDialog.findViewById(R.id.dialog_no_connec_exit);
        Button tryAgain = mNoConnectionDialog.findViewById(R.id.dialog_no_connec_try_again);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AppCompatActivity act = (AppCompatActivity) mContext;
                act.finish();
            }
        });

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new GetAlbumID().execute();
                mNoConnectionDialog.dismiss();
            }
        });
    }

}