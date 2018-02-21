package narmware.com.photouploadcopy.models;

/**
 * Created by savvy on 1/2/2018.
 */

public class Friends {

    int id;
    String f_id; //server id
    String address;
    String state;
    String city;
    String pin;
    String dist;
    String user_id;
    String mobile;
    String response;
    String fr_name;
    String fr_email;
    String fr_qty;
    int cart_flag;

    public Friends(int id,String address, String state, String city, String pin, String dist, String user_id, String mobile, String response,String fr_name,String fr_email,String f_server_id,String fr_qty,int cart_flag) {
        this.id=id;
        this.address = address;
        this.state = state;
        this.city = city;
        this.pin = pin;
        this.dist = dist;
        this.user_id = user_id;
        this.mobile = mobile;
        this.response = response;
        this.fr_email=fr_email;
        this.fr_name=fr_name;
        this.f_id=f_server_id;
        this.fr_qty=fr_qty;
        this.cart_flag=cart_flag;
    }

    public int getCart_flag() {
        return cart_flag;
    }

    public void setCart_flag(int cart_flag) {
        this.cart_flag = cart_flag;
    }

    public String getFr_qty() {
        return fr_qty;
    }

    public void setFr_qty(String fr_qty) {
        this.fr_qty = fr_qty;
    }

    public String getF_id() {
        return f_id;
    }

    public void setF_id(String f_id) {
        this.f_id = f_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFr_name() {
        return fr_name;
    }

    public void setFr_name(String fr_name) {
        this.fr_name = fr_name;
    }

    public String getFr_email() {
        return fr_email;
    }

    public void setFr_email(String fr_email) {
        this.fr_email = fr_email;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
