package narmware.com.photouploadcopy.models;

/**
 * Created by savvy on 1/10/2018.
 */

public class ViewOrderResponse {
    ViewOrder data;
    String response;

    public ViewOrder getData() {
        return data;
    }

    public void setData(ViewOrder data) {
        this.data = data;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
