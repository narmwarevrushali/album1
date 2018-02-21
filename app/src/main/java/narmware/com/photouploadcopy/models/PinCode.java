package narmware.com.photouploadcopy.models;

/**
 * Created by savvy on 2/7/2018.
 */

public class PinCode {
    String pin_code;
    String state;
    String dist;
    String city;


    public PinCode(String pin_code, String state, String dist, String city) {
        this.pin_code = pin_code;
        this.state = state;
        this.dist = dist;
        this.city = city;
    }

    public String getPin_code() {
        return pin_code;
    }

    public void setPin_code(String pin_code) {
        this.pin_code = pin_code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
