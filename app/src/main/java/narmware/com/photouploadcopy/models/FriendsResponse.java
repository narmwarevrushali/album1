package narmware.com.photouploadcopy.models;

/**
 * Created by savvy on 1/4/2018.
 */

public class FriendsResponse {
    Friends[] data;
    String response;

    public Friends[] getData() {
        return data;
    }

    public void setData(Friends[] data) {
        this.data = data;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
