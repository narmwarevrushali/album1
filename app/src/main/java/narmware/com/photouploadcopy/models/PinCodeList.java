package narmware.com.photouploadcopy.models;

/**
 * Created by savvy on 2/8/2018.
 */

public class PinCodeList {
    String response;
    PinCode[] data;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public PinCode[] getData() {
        return data;
    }

    public void setData(PinCode[] data) {
        this.data = data;
    }
}
