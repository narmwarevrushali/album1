package narmware.com.photouploadcopy.models;

/**
 * Created by savvy on 1/10/2018.
 */

public class InvoiceResponse {
    Invoice[] data;
    String response;

    public Invoice[] getData() {
        return data;
    }

    public void setData(Invoice[] data) {
        this.data = data;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
