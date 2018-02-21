package narmware.com.photouploadcopy.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;
import narmware.com.photouploadcopy.MyApplication;
import narmware.com.photouploadcopy.R;
import narmware.com.photouploadcopy.fragment.FriendsProfileFragment;
import narmware.com.photouploadcopy.helpers.Constants;
import narmware.com.photouploadcopy.models.Friends;
import narmware.com.photouploadcopy.support.DatabaseAccess;
import narmware.com.photouploadcopy.support.JSONParser;


/**
 * Created by Lincoln on 31/03/16.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.MyViewHolder> {

     List<Friends> images;
    Context mContext;
    JSONParser mJsonParser;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    String f_id;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mthumb_title,mTxtInitial,mTxtFrndInfo;
        ImageButton mImgRemove;
        Friends mItem;

        public MyViewHolder(View view) {
            super(view);
            mthumb_title= (TextView) view.findViewById(R.id.txt_frnd_name);
            mImgRemove=view.findViewById(R.id.btn_remove);
            mTxtInitial=view.findViewById(R.id.txt_initial);
            mTxtFrndInfo=view.findViewById(R.id.txt_frnd_info);

            mImgRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = (int) mImgRemove.getTag();
                   // Toast.makeText(mContext,mItem.getFr_name()+"  "+position, Toast.LENGTH_SHORT).show();
                    f_id=mItem.getF_id();
                    new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("You want to delete your friend!")
                            .setConfirmText("Yes,delete it!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.setTitleText("Deleted!")
                                            .setContentText("Your friend has been deleted!")
                                            .setConfirmText("OK")
                                            .setConfirmClickListener(null)
                                            .showCancelButton(false)
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);


                                    new DeleteFriend().execute();
                                    images.remove(position);
                                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(mContext);
                                    databaseAccess.open();
                                    databaseAccess.deleteSingleFriend(mItem.getF_id());
                                    notifyDataSetChanged();
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
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final AppCompatActivity act = (android.support.v7.app.AppCompatActivity) mContext;
                    fragmentManager= act.getSupportFragmentManager();

                    fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.addToBackStack("null");
                    fragmentTransaction.replace(R.id.fragment_container,new FriendsProfileFragment().newInstance("selected", Integer.parseInt(mItem.getF_id())));
                    fragmentTransaction.commit();

                }
            });


        }
    }

    public FriendsAdapter(Context context, List<Friends> images) {
        this.mContext = context;
        this.images = images;
        mJsonParser=new JSONParser();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_friend, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Friends image = images.get(position);


        /*Glide.with(mContext).load(image.getV_img())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.thumbnail);
*/
        holder.mthumb_title.setText(image.getFr_name());
        holder.mImgRemove.setTag(position);

        int[] randomColors = mContext.getResources().getIntArray(R.array.randomcolors);
        int randomAndroidColor = randomColors[new Random().nextInt(randomColors.length)];
        String initial= String.valueOf(image.getFr_name().charAt(0));
        holder.mTxtInitial.setText(initial);
        //holder.mTxtInitial.setBackgroundColor(randomAndroidColor);
        GradientDrawable drawable = (GradientDrawable)  holder.mTxtInitial.getBackground();
        drawable.setColor(randomAndroidColor);

        String city=image.getCity();
        String state=image.getState();
        String mobile=image.getMobile();

        holder.mTxtFrndInfo.setText(city+" , "+state+" / "+mobile);
        holder.mItem=image;
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private FriendsAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final FriendsAdapter.ClickListener clickListener) {
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


    class DeleteFriend extends AsyncTask<String, String, String> {
        protected ProgressDialog mProgress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Toast.makeText(mContext,"Pre execute",Toast.LENGTH_SHORT).show();
            mProgress = new ProgressDialog(mContext);
            mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgress.setIndeterminate(true);
            mProgress.setMessage("Deleting friend ...");
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
                params.put("f_id",f_id);

                String url = MyApplication.URL_SERVER + MyApplication.URL_DELETE_FRIEND;
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
                    Log.e("Delete friend data", s);

                else
                    Log.e("data", "friend is null");

                Friends friendResponse=gson.fromJson(s,Friends.class);
                Log.e("Delete response",friendResponse.getResponse());
                int response= Integer.parseInt(friendResponse.getResponse());
                if(response==Constants.NEW_ENTRY)
                {

                }
                else {
                    Toast.makeText(mContext,"Can't delete friend",Toast.LENGTH_LONG).show();
                }
                mProgress.dismiss();
            }catch (Exception e)
            {
                Toast.makeText(mContext,"Internet not available",Toast.LENGTH_LONG).show();
                mProgress.dismiss();
            }
        }
    }

}