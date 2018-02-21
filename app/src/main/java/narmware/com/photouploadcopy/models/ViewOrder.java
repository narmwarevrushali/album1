package narmware.com.photouploadcopy.models;

/**
 * Created by savvy on 1/31/2018.
 */

public class ViewOrder {

    String order_img_path;
    OrderFrnds[] friends;
    OrderImages[] images;

    public OrderFrnds[] getFriends() {
        return friends;
    }

    public void setFriends(OrderFrnds[] friends) {
        this.friends = friends;
    }

    public OrderImages[] getImages() {
        return images;
    }

    public void setImages(OrderImages[] images) {
        this.images = images;
    }

    public ViewOrder(String order_img_path) {
        this.order_img_path = order_img_path;
    }

    public String getOrder_img_path() {
        return order_img_path;
    }

    public void setOrder_img_path(String order_img_path) {
        this.order_img_path = order_img_path;
    }
}
