package narmware.com.photouploadcopy.models;

/**
 * Created by savvy on 2/8/2018.
 */

public class VersionCode {
    int v_id,version;
    String v_title;
    String response;

    public VersionCode(int v_id, int version) {
        this.v_id = v_id;
        this.version = version;
        this.v_title = v_title;
    }

    public int getV_id() {
        return v_id;
    }

    public void setV_id(int v_id) {
        this.v_id = v_id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getV_title() {
        return v_title;
    }

    public void setV_title(String v_title) {
        this.v_title = v_title;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
