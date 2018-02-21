package narmware.com.photouploadcopy.models;

/**
 * Created by savvy on 2/16/2018.
 */

public class UserAlbumResponse {
    String response;
    UserAlbum[] data;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public UserAlbum[] getData() {
        return data;
    }

    public void setData(UserAlbum[] data) {
        this.data = data;
    }
}
