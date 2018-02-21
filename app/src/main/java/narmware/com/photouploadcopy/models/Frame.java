package narmware.com.photouploadcopy.models;

/**
 * Created by savvy on 1/5/2018.
 */

public class Frame {

    String frame_id,frame_title,frame_path,frame_price;


  /*  public Frame(String frame_id, String frame_title, String frame_path) {
        this.frame_id = frame_id;
        this.frame_title = frame_title;
        this.frame_path = frame_path;
    }*/

    public String getFrame_price() {
        return frame_price;
    }

    public void setFrame_price(String frame_price) {
        this.frame_price = frame_price;
    }

    public String getFrame_id() {
        return frame_id;
    }

    public void setFrame_id(String frame_id) {
        this.frame_id = frame_id;
    }

    public String getFrame_title() {
        return frame_title;
    }

    public void setFrame_title(String frame_title) {
        this.frame_title = frame_title;
    }

    public String getFrame_path() {
        return frame_path;
    }

    public void setFrame_path(String frame_path) {
        this.frame_path = frame_path;
    }
}
