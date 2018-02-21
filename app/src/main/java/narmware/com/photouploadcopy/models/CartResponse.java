package narmware.com.photouploadcopy.models;

import java.util.List;

/**
 * Created by savvy on 1/4/2018.
 */

public class CartResponse {
    List<Friends> data;
    String response;
    int inv_id;
    String user_id;
    String a_id;
    String total_albums;
    String total_amount;
    String coupon;

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public List<Friends> getData() {
        return data;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public int getInv_id() {
        return inv_id;
    }

    public void setInv_id(int inv_id) {
        this.inv_id = inv_id;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public void setData(List<Friends> data) {
        this.data = data;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getA_id() {
        return a_id;
    }

    public void setA_id(String a_id) {
        this.a_id = a_id;
    }

    public String getTotal_albums() {
        return total_albums;
    }

    public void setTotal_albums(String total_albums) {
        this.total_albums = total_albums;
    }
}
