package narmware.com.photouploadcopy.models;

/**
 * Created by savvy on 1/4/2018.
 */

public class FrameResponse {
    Frame[] data;
    String response;

    public Frame[] getData() {
        return data;
    }

    public void setData(Frame[] data) {
        this.data = data;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
