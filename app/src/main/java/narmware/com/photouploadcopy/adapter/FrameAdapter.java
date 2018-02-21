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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import narmware.com.photouploadcopy.MyApplication;
import narmware.com.photouploadcopy.R;
import narmware.com.photouploadcopy.activity.MainActivity2;
import narmware.com.photouploadcopy.helpers.Constants;
import narmware.com.photouploadcopy.models.Frame;
import narmware.com.photouploadcopy.models.FrameResponse;
import narmware.com.photouploadcopy.support.JSONParser;
import narmware.com.photouploadcopy.support.SharedPreferencesHelper;


/**
 * Created by Lincoln on 31/03/16.
 */

public class FrameAdapter extends RecyclerView.Adapter<FrameAdapter.MyViewHolder> {

     List<Frame> frames;
    Context mContext;
    JSONParser mJsonParser;
    protected Dialog mNoConnectionDialog;

    /* FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
*/
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mthumb_title;
       ImageView mImgFrame;
        Frame mItem;


        public MyViewHolder(View view) {
            super(view);
            mthumb_title= (TextView) view.findViewById(R.id.txt_frame_name);
            mImgFrame=view.findViewById(R.id.img_frame);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Toast.makeText(mContext,mItem.getFrame_title()+" "+mItem.getFrame_id(), Toast.LENGTH_SHORT).show();
                    SharedPreferencesHelper.setFrameId(mItem.getFrame_id(),mContext);
                    SharedPreferencesHelper.setFramePath(mItem.getFrame_path(),mContext);
                    SharedPreferencesHelper.setFramePrice(mItem.getFrame_price(),mContext);

                        new SendAlbumFrame().execute();

                }
            });
        }
    }

    public FrameAdapter(Context context, List<Frame> frames) {
        mJsonParser=new JSONParser();
        this.mContext = context;
        this.frames = frames;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_frame, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Frame frame = frames.get(position);


        Picasso.with(mContext)
                .load(frame.getFrame_path())
                .fit()
                .into(holder.mImgFrame);

        holder.mthumb_title.setText(frame.getFrame_title());
        holder.mItem=frame;
    }

    @Override
    public int getItemCount() {
        return frames.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private FrameAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final FrameAdapter.ClickListener clickListener) {
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

    class SendAlbumFrame extends AsyncTask<String, String, String> {
        protected ProgressDialog mProgress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Toast.makeText(LoginActivity.this,"Pre execute",Toast.LENGTH_SHORT).show();
            mProgress = new ProgressDialog(mContext);
            mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgress.setIndeterminate(true);
            mProgress.setMessage("");
            mProgress.setCancelable(false);
            //  mProgress.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = null;
            try
            {
                Gson gson = new Gson();

                HashMap<String, String> params = new HashMap<>();
                params.put(Constants.ALBUM_ID, SharedPreferencesHelper.getAlbumId(mContext));
                params.put(Constants.FRAME_ID, SharedPreferencesHelper.getFrameId(mContext));

                String url = MyApplication.URL_SERVER+MyApplication.URL_SEND_FRAME_ALBUM;
                Log.e("JSON data updated url",url);

                JSONObject ob = mJsonParser.makeHttpRequest(url, "GET", params);

                    if (ob == null) {
                        Log.d("RESPONSE", "ERRORRRRR");
                    } else {
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
            //mProgress.dismiss();

            try{Gson gson = new Gson();
                if (s != null)
                    Log.e("Frame data", s);

                else
                    Log.e("data", "Frame is null");

                FrameResponse frameResponse=gson.fromJson(s,FrameResponse.class);
                int response= Integer.parseInt(frameResponse.getResponse());
                if(response== Constants.ALREADY_PRESENT)
                {
                    final AppCompatActivity act = (android.support.v7.app.AppCompatActivity) mContext;
                    Intent intent=new Intent(mContext, MainActivity2.class);
                    intent.putExtra("isDone",true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    act.startActivity(intent);
                    act.finish();
                }
            }catch (Exception e)
            {
                showNoConnectionDialog();
                //Toast.makeText(mContext,"Internet not available,can not login",Toast.LENGTH_LONG).show();
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

                new SendAlbumFrame().execute();
                mNoConnectionDialog.dismiss();
            }
        });
    }

}