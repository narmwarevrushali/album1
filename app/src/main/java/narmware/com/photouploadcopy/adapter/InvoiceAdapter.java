package narmware.com.photouploadcopy.adapter;

import android.content.Context;
import android.content.Intent;
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

import java.util.List;

import narmware.com.photouploadcopy.R;
import narmware.com.photouploadcopy.activity.WebViewActivity;
import narmware.com.photouploadcopy.helpers.Constants;
import narmware.com.photouploadcopy.models.Invoice;


/**
 * Created by Lincoln on 31/03/16.
 */

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.MyViewHolder>{

     List<Invoice> invoices;
    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextInvNo,mTxtInvDate,mTxtInvTotal,mTxtInvStatus,mTxtInvASize,mTxtInvQty;
        Invoice mItem;
        Button mBtnView;

        public MyViewHolder(View view) {
            super(view);
            mTextInvNo= (TextView) view.findViewById(R.id.txt_inv_no);
            mTxtInvDate=view.findViewById(R.id.txt_inv_date);
            mTxtInvStatus=view.findViewById(R.id.txt_inv_status);
            mTxtInvTotal=view.findViewById(R.id.txt_inv_total);
            mTxtInvASize=view.findViewById(R.id.txt_inv_album_name);
            mTxtInvQty=view.findViewById(R.id.txt_inv_qty);
            mBtnView=view.findViewById(R.id.btn_view);
            mBtnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final AppCompatActivity act = (AppCompatActivity) mContext;

                    Log.e("Invoice clicked item",mItem.getInv_id()+"  "+mItem.getInv_no()+"  "+mItem.getInv_status());
                    String status=mItem.getInv_status();
                   /* if(status.equals("UNPAID"))
                    {
                      *//*  Intent intent=new Intent(mContext, WebViewActivity.class);
                        intent.putExtra(Constants.INVOICE_ID, mItem.getInv_id());
                        intent.putExtra(Constants.USER_ID,SharedPreferencesHelper.getUserId(mContext));
                        act.finish();
                        act.startActivity(intent);*//*

                        Intent intent=new Intent(mContext, ViewOrderActivity.class);
                        act.finish();
                        act.startActivity(intent);
                    }*/
                    //  else {
                    Intent intent=new Intent(mContext, WebViewActivity.class);
                    intent.putExtra(Constants.INVOICE_ID,mItem.getInv_id());
                    intent.putExtra(Constants.USER_ID,"");
                    act.finish();
                    act.startActivity(intent);
                    // }
                }
            });

          /*  view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                }
            });*/

        }

    }

    public InvoiceAdapter(Context context, List<Invoice> invoices) {
        this.mContext = context;
        this.invoices = invoices;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_invoice_details, parent, false);


        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Invoice invoice = invoices.get(position);

        String CurrentString =invoice.getInv_date();
        String[] separated = CurrentString.split(" ");

        holder.mTextInvNo.setText(invoice.getInv_no());
        holder.mTxtInvDate.setText(separated[0]);
        holder.mTxtInvStatus.setText(invoice.getInv_status());
        holder.mTxtInvTotal.setText(" "+invoice.getInv_total_amt());
        holder.mTxtInvASize.setText(invoice.getSize()+" Album");
        holder.mTxtInvQty.setText("Total albums : "+invoice.getAlbum_cnt());
        holder.mItem=invoice;
    }


    @Override
    public int getItemCount() {
        return invoices.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private InvoiceAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final InvoiceAdapter.ClickListener clickListener) {
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