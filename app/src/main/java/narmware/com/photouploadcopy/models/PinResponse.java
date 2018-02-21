package narmware.com.photouploadcopy.models;

/**
 * Created by savvy on 2/7/2018.
 */

public class PinResponse {

    String response;
    String pin_status;

    public String getPin_status() {
        return pin_status;
    }

    public void setPin_status(String pin_status) {
        this.pin_status = pin_status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
