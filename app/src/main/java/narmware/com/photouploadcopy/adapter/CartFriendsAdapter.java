package narmware.com.photouploadcopy.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import narmware.com.photouploadcopy.R;
import narmware.com.photouploadcopy.fragment.CartFullFragment;
import narmware.com.photouploadcopy.models.Friends;
import narmware.com.photouploadcopy.support.DatabaseAccess;
import narmware.com.photouploadcopy.support.SharedPreferencesHelper;


/**
 * Created by Lincoln on 31/03/16.
 */

public class CartFriendsAdapter extends RecyclerView.Adapter<CartFriendsAdapter.MyViewHolder>{

     List<Friends> friends;
    Context mContext;
    int totalCount,totalAmount,finalTotal;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView mthumb_title;
        int count=1;
        Friends mItem;
        TextView mTxtSelectionCount;
        ImageButton mFabAdd,mFabDelete;
        Button mBtnRemove;
         CheckBox mCheckSelf;

        DatabaseAccess databaseAccess;
        public MyViewHolder(View view) {
            super(view);
            mthumb_title= (TextView) view.findViewById(R.id.txt_frnd_name);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final AppCompatActivity act = (AppCompatActivity) mContext;

                    //Toast.makeText(mContext,mItem.getF_id()+"", Toast.LENGTH_SHORT).show();
                }
            });

            mFabAdd=itemView.findViewById(R.id.fab_add_item);
            mFabDelete=itemView.findViewById(R.id.fab_delete_item);

            mBtnRemove=itemView.findViewById(R.id.btn_remove_frnd);
            mCheckSelf=itemView.findViewById(R.id.chk_self);
            mCheckSelf.setVisibility(View.INVISIBLE);

            databaseAccess= DatabaseAccess.getInstance(mContext);
            databaseAccess.open();
            mTxtSelectionCount=itemView.findViewById(R.id.txt_selection_count);

            mFabAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    count++;
                    mTxtSelectionCount.setText(String.valueOf(count));

                    if (mFabDelete.isClickable() == false) {
                        mFabDelete.setClickable(true);
                    }

                    if(mCheckSelf.isChecked()==false)
                    {
                        mCheckSelf.setChecked(true);
                    }
                   // Toast.makeText(mContext,mItem.getFr_name(), Toast.LENGTH_SHORT).show();
                    databaseAccess.UpdateQty(String.valueOf(count), Integer.parseInt(mItem.getF_id()));
                    CartFullFragment.setPrice();
                }
            });
            mFabDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    count--;
                    mTxtSelectionCount.setText(String.valueOf(count));

                    if(count==1)
                    {
                        mFabDelete.setClickable(false);
                    }
                   // Toast.makeText(mContext,mItem.getFr_name(), Toast.LENGTH_SHORT).show();
                    databaseAccess.UpdateQty(String.valueOf(count),Integer.parseInt(mItem.getF_id()));
                    CartFullFragment.setPrice();

                }
            });
           /* if(count==1) {
                mFabDelete.setClickable(false);
            }
            else {
                mFabDelete.setClickable(true);
            }*/

            mBtnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = (int) mBtnRemove.getTag();

                    new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("You want to remove your friend!")
                            .setConfirmText("Yes,remove it!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.setTitleText("Removed!")
                                            .setContentText("Your friend has been removed!")
                                            .setConfirmText("OK")
                                            .setConfirmClickListener(null)
                                            .showCancelButton(false)
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                                       //  Toast.makeText(mContext,friends.get(position).getFr_name(), Toast.LENGTH_SHORT).show();
                                        databaseAccess.UpdateCartFlag(0, Integer.parseInt(mItem.getF_id()));
                                        databaseAccess.UpdateQty("1", Integer.parseInt(mItem.getF_id()));
                                        CartFullFragment.setPrice();
                                        friends.remove(position);
                                        notifyDataSetChanged();
/*
                                    final AppCompatActivity act = (android.support.v7.app.AppCompatActivity) mContext;
                                    Intent intent=new Intent(mContext, MainActivity2.class);
                                    intent.putExtra("isDone",true);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    act.startActivity(intent);
                                    act.finish();*/
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


            mCheckSelf.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    //Toast.makeText(mContext,mItem.getCart_flag()+"  "+mItem.getFr_name()+b, Toast.LENGTH_SHORT).show();


                    if(b==true)
                    {
                        SharedPreferencesHelper.setIsSendMe(true,mContext);
                        databaseAccess.UpdateQty(String.valueOf(count),Integer.parseInt(mItem.getF_id()));
                        CartFullFragment.setPrice();
                    }
                    else if(b==false) {
                        databaseAccess.UpdateQty("0",Integer.parseInt(mItem.getF_id()));
                        CartFullFragment.setPrice();
                        count=1;
                        mTxtSelectionCount.setText(String.valueOf(count));
                        SharedPreferencesHelper.setIsSendMe(false,mContext);
                    }
                }
            });

        }

    }

    public CartFriendsAdapter(Context context, List<Friends> friends) {
        this.mContext = context;
        this.friends = friends;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_cart_friend, parent, false);


        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Friends friend = friends.get(position);
        if(position==friends.size()-1)
        {
            holder.mBtnRemove.setVisibility(View.INVISIBLE);
            holder.mCheckSelf.setVisibility(View.VISIBLE);
            //holder.mCheckSelf.setChecked(true);
        }
        holder.mthumb_title.setText(friend.getFr_name());
        if(friend.getFr_qty()!=null)
        {
            holder.mTxtSelectionCount.setText(friend.getFr_qty());
            try {
                holder.count = Integer.parseInt(friend.getFr_qty());
            }catch (Exception e)
            {
                holder.mTxtSelectionCount.setText("1");
            }
        }
        else
        {
            holder.mTxtSelectionCount.setText("1");
        }
        holder.mBtnRemove.setTag(position);
        //holder.count= Integer.parseInt(friend.getFr_qty());
        holder.mItem=friend;

        if(position==friends.size()-1)
        {
            if(SharedPreferencesHelper.getIsSendMe(mContext)==true)
            {
                holder.mCheckSelf.setChecked(true);
            }else {
                holder.mCheckSelf.setChecked(false);
            }
        }

        int qty= Integer.parseInt(holder.mTxtSelectionCount.getText().toString());
        if(qty==0 || qty==1)
        {
            holder.mFabDelete.setClickable(false);
            holder.count=1;
            holder.mTxtSelectionCount.setText(String.valueOf(holder.count));
        }
        else{
            holder.mFabDelete.setClickable(true);
        }

    }


    @Override
    public int getItemCount() {
        return friends.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private CartFriendsAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final CartFriendsAdapter.ClickListener clickListener) {
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