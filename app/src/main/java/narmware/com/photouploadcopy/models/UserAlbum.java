package narmware.com.photouploadcopy.models;

/**
 * Created by savvy on 1/5/2018.
 */

public class UserAlbum {
    String a_price,a_size,user_id,a_id,a_shippingcharge,response;

    public String getA_shippingcharge() {
        return a_shippingcharge;
    }

    public void setA_shippingcharge(String a_shippingcharge) {
        this.a_shippingcharge = a_shippingcharge;
    }

    public String getA_id() {
        return a_id;
    }

    public void setA_id(String a_id) {
        this.a_id = a_id;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getA_price() {
        return a_price;
    }

    public void setA_price(String a_price) {
        this.a_price = a_price;
    }

    public String getA_size() {
        return a_size;
    }

    public void setA_size(String a_size) {
        this.a_size = a_size;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
