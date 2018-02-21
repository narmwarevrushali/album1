package narmware.com.photouploadcopy.models;

/**
 * Created by savvy on 2/16/2018.
 */

public class CouponResponse {
    String response;
    Coupon[] data;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Coupon[] getData() {
        return data;
    }

    public void setData(Coupon[] data) {
        this.data = data;
    }
}
