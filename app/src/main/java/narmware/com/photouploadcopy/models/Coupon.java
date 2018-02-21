package narmware.com.photouploadcopy.models;

/**
 * Created by savvy on 2/16/2018.
 */

public class Coupon {
    String coupon_name,coupon_type,coupon_value,min_amt;

    public String getCoupon_name() {
        return coupon_name;
    }

    public void setCoupon_name(String coupon_name) {
        this.coupon_name = coupon_name;
    }

    public String getCoupon_type() {
        return coupon_type;
    }

    public void setCoupon_type(String coupon_type) {
        this.coupon_type = coupon_type;
    }

    public String getCoupon_value() {
        return coupon_value;
    }

    public void setCoupon_value(String coupon_value) {
        this.coupon_value = coupon_value;
    }

    public String getMin_amt() {
        return min_amt;
    }

    public void setMin_amt(String min_amt) {
        this.min_amt = min_amt;
    }
}
