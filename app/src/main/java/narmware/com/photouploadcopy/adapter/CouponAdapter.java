package narmware.com.photouploadcopy.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import narmware.com.photouploadcopy.R;
import narmware.com.photouploadcopy.fragment.CartFullFragment;
import narmware.com.photouploadcopy.helpers.Constants;
import narmware.com.photouploadcopy.models.Coupon;
import narmware.com.photouploadcopy.support.SharedPreferencesHelper;


/**
 * Created by Lincoln on 31/03/16.
 */

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.MyViewHolder>{

     List<Coupon> coupons;
    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView mTxtCpnAmt,mTxtMinAmt,mTxtCpnName;
        Coupon mItem;
        Button mBtnApply;

        public MyViewHolder(View view) {
            super(view);

            mTxtCpnAmt= (TextView) view.findViewById(R.id.txt_cpn_amt);
            mTxtMinAmt=view.findViewById(R.id.txt_min_amt);
            mTxtCpnName=view.findViewById(R.id.txt_cpn_name);
            mBtnApply=view.findViewById(R.id.btn_apply);

            mBtnApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final AppCompatActivity act = (AppCompatActivity) mContext;

                    SharedPreferencesHelper.setCouponName(mItem.getCoupon_name(),mContext);
                    SharedPreferencesHelper.setCouponMinPrice(mItem.getMin_amt(),mContext);
                    //SharedPreferencesHelper.setCouponMinPrice("4000",mContext);
                    CartFullFragment.mBtnCoupon.setText(mItem.getCoupon_name());

                    if(mItem.getCoupon_type().equals(Constants.FLAT_DISC))
                    {
                        SharedPreferencesHelper.setCouponPrice(mItem.getCoupon_value(),mContext);
                    }
                   if(mItem.getCoupon_type().equals(Constants.PERCENT_DISC)){

                        int totalAmt=SharedPreferencesHelper.getTotalPrice(mContext);
                        int percentAmt= Integer.parseInt(mItem.getCoupon_value());
                        int grandTotal=(totalAmt * percentAmt) / 100;
                        SharedPreferencesHelper.setCouponPrice(String.valueOf(grandTotal),mContext);
                    }
                    CartFullFragment.setPrice();
                    act.finish();

                }
            });

        }

    }

    public CouponAdapter(Context context, List<Coupon> coupons) {
        this.mContext = context;
        this.coupons = coupons;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_coupon, parent, false);


        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Coupon coupon = coupons.get(position);

        if(coupon.getCoupon_type().equals(Constants.FLAT_DISC))
        {
            holder.mTxtCpnAmt.setText("Get Rs."+coupon.getCoupon_value()+" Off");
            holder.mTxtCpnName.setText(coupon.getCoupon_name());
            holder.mTxtMinAmt.setText("Rs."+coupon.getMin_amt());
        }
        else{
            holder.mTxtCpnAmt.setText("Get "+coupon.getCoupon_value()+" % Off");
            holder.mTxtCpnName.setText(coupon.getCoupon_name());
            holder.mTxtMinAmt.setText("Rs."+coupon.getMin_amt());
        }

        holder.mItem=coupon;
    }


    @Override
    public int getItemCount() {
        return coupons.size();
    }

}