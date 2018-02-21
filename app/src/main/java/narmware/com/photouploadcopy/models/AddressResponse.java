package narmware.com.photouploadcopy.models;

/**
 * Created by savvy on 1/11/2018.
 */

public class AddressResponse {

    Address[] data;
    String response;

    public Address[] getData() {
        return data;
    }

    public void setData(Address[] data) {
        this.data = data;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
