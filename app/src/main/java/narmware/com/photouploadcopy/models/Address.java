package narmware.com.photouploadcopy.models;

/**
 * Created by savvy on 1/2/2018.
 */

public class Address {

    String address;
    String state;
    String city;
    String pin;
    String dist;
    String user_id;
    String mobile;
    String response;

    public Address(String address, String state, String city, String pin, String dist, String user_id, String mobile) {
        this.address = address;
        this.state = state;
        this.city = city;
        this.pin = pin;
        this.dist = dist;
        this.user_id = user_id;
        this.mobile = mobile;
        this.response = response;
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
