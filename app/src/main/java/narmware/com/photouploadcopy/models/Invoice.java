package narmware.com.photouploadcopy.models;

/**
 * Created by savvy on 1/10/2018.
 */

public class Invoice {
    String inv_no;
    String inv_id;
    String inv_date;
    String inv_status;
    String inv_total_amt;
    String size;
    String album_cnt;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getAlbum_cnt() {
        return album_cnt;
    }

    public void setAlbum_cnt(String album_cnt) {
        this.album_cnt = album_cnt;
    }

    public String getInv_id() {
        return inv_id;
    }

    public void setInv_id(String inv_id) {
        this.inv_id = inv_id;
    }

    public String getInv_no() {
        return inv_no;
    }

    public void setInv_no(String inv_no) {
        this.inv_no = inv_no;
    }

    public String getInv_date() {
        return inv_date;
    }

    public void setInv_date(String inv_date) {
        this.inv_date = inv_date;
    }

    public String getInv_status() {
        return inv_status;
    }

    public void setInv_status(String inv_status) {
        this.inv_status = inv_status;
    }

    public String getInv_total_amt() {
        return inv_total_amt;
    }

    public void setInv_total_amt(String inv_total_amt) {
        this.inv_total_amt = inv_total_amt;
    }
}
