package narmware.com.photouploadcopy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import narmware.com.photouploadcopy.R;
import narmware.com.photouploadcopy.models.ViewOrder;


/**
 * Created by Lincoln on 31/03/16.
 */

public class ViewOrderAdapter extends RecyclerView.Adapter<ViewOrderAdapter.MyViewHolder>{

     List<ViewOrder> orders;
    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder{
         ImageView mImgOrder;
        ViewOrder mItem;

        public MyViewHolder(View view) {
            super(view);

            mImgOrder=view.findViewById(R.id.img_order);

           /* view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final AppCompatActivity act = (AppCompatActivity) mContext;

                }
            });*/

        }

    }

    public ViewOrderAdapter(Context context, List<ViewOrder> orders) {
        this.mContext = context;
        this.orders = orders;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_view_order, parent, false);


        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ViewOrder order = orders.get(position);

        Picasso.with(mContext)
                .load(order.getOrder_img_path())
                .fit()
                .into(holder.mImgOrder);

        holder.mItem=order;
    }


    @Override
    public int getItemCount() {
        return orders.size();
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

}