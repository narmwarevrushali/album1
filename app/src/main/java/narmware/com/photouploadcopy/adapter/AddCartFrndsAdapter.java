package narmware.com.photouploadcopy.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import narmware.com.photouploadcopy.R;
import narmware.com.photouploadcopy.models.Friends;
import narmware.com.photouploadcopy.support.DatabaseAccess;


/**
 * Created by Lincoln on 31/03/16.
 */

public class AddCartFrndsAdapter extends RecyclerView.Adapter<AddCartFrndsAdapter.MyViewHolder> {

     List<Friends> images;
    Context mContext;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    DatabaseAccess databaseAccess;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mthumb_title;
        CheckBox mChkFrnd;
        Friends mItem;


        public MyViewHolder(View view) {
            super(view);
            mthumb_title= (TextView) view.findViewById(R.id.txt_frnd_name);
            mChkFrnd=view.findViewById(R.id.chk_frnd_name);

            databaseAccess= DatabaseAccess.getInstance(mContext);
            databaseAccess.open();

            mChkFrnd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    //Toast.makeText(mContext,mItem.getCart_flag()+"  "+mItem.getFr_name()+b, Toast.LENGTH_SHORT).show();

                    if(b==true)
                    {
                        //Toast.makeText(mContext,b+"", Toast.LENGTH_SHORT).show();
                        databaseAccess.UpdateCartFlag(1, Integer.parseInt(mItem.getF_id()));
                    }
                   else if(b==false) {
                        //Toast.makeText(mContext,b+"", Toast.LENGTH_SHORT).show();
                        databaseAccess.UpdateCartFlag(0,Integer.parseInt(mItem.getF_id()));
                        databaseAccess.UpdateQty("1",Integer.parseInt(mItem.getF_id()));
                    }
                }
            });
        /*    view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //1=true  0=false

                    databaseAccess= DatabaseAccess.getInstance(mContext);
                    databaseAccess.open();
                    mChkFrnd.setChecked(true);

                    if(mItem.getCart_flag()==1)
                    {
                        databaseAccess.UpdateCartFlag(0,Integer.parseInt(mItem.getF_id()));
                        databaseAccess.UpdateQty("1",Integer.parseInt(mItem.getF_id()));
                        notifyDataSetChanged();
                    }
                    else if(mItem.getCart_flag()==0 ){
                        databaseAccess.UpdateCartFlag(1, Integer.parseInt(mItem.getF_id()));
                        notifyDataSetChanged();
                    }

                }
            });*/
        }
    }

    public AddCartFrndsAdapter(Context context, List<Friends> images) {
        this.mContext = context;
        this.images = images;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_select_frnd_for_cart, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Friends image = images.get(position);


            holder.mChkFrnd.setText(image.getFr_name());
            holder.mItem = image;

            if ( holder.mItem.getCart_flag() == 1) {
                holder.mChkFrnd.setChecked(true);

        }
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
        private AddCartFrndsAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final AddCartFrndsAdapter.ClickListener clickListener) {
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
}