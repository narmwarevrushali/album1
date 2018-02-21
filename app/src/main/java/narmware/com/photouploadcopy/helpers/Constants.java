package narmware.com.photouploadcopy.helpers;

/**
 * Created by Darshan on 5/26/2015.
 */
public class Constants {
    public static final int PERMISSION_REQUEST_CODE = 1000;
    public static final int PERMISSION_GRANTED = 1001;
    public static final int PERMISSION_DENIED = 1002;

    public static final int REQUEST_CODE = 2000;

    public static final int FETCH_STARTED = 2001;
    public static final int FETCH_COMPLETED = 2002;
    public static final int ERROR = 2005;

    /**
     * Request code for permission has to be < (1 << 8)
     * Otherwise throws java.lang.IllegalArgumentException: Can only use lower 8 bits for requestCode
     */
    public static final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 23;

    public static final String INTENT_EXTRA_ALBUM = "album";
    public static final String INTENT_EXTRA_IMAGES = "images";
    public static final String INTENT_EXTRA_LIMIT = "limit";
    public static final int DEFAULT_LIMIT = 10;

    public static final int DEFAULT_HEIGHT = 400;
    public static final int DEFAULT_WIDTH = 600;
    //Maximum number of images that can be selected at a time
    public static int limit;

    public static final int ALREADY_PRESENT = 100;
    public static final int NEW_ENTRY = 200;
    public static final int SQL_ERROR =300;
    public static final int PROFILE_RESPONSE = 400;


    public static final String INVOICE_ID = "inv_id";
    public static final String ALBUM_ID = "a_id";
    public static final String FRAME_ID = "frame_id";

    public static final String IMAGE_ID = "img_id";
    public static final String IMAGE_NAME = "name";
    public static final String IMAGE_PATH = "image";

    public static final String JSON_STRING = "json_string";

    //Image fields
    public static final String IMG_ID = "id";
    public static final String IMG_NAME = "name";
    public static final String IMG_PATH = "path";
    public static final String IMG_SELECTED = "isSelected";
    public static final String IMG_ALBUM = "album";
    public static final String IMG_HEIGHT = "height";
    public static final String IMG_WIDTH = "width";

    //Friend profile fields
    public static final String FRND_ADDR = "addr";
    public static final String FRND_STATE = "state";
    public static final String FRND_DIST = "dist";
    public static final String FRND_CITY = "city";
    public static final String FRND_PIN = "pin";
    public static final String FRND_MOBILE = "mobile";
    public static final String FRND_NAME= "fr_name";
    public static final String FRND_EMAIL = "fr_email";
    public static final String FRND_SERVER_ID= "f_server_id";
    public static final String FRND_FRAME_QTY = "fr_qty";
    public static final String FRND_CART_FLAG = "cart_flag";

    //user profile fields
    public static final String USER_ID = "user_id";
    public static final String USER_ADDR = "address";
    public static final String USER_STATE= "state";
    public static final String USER_DIST= "dist";
    public static final String USER_CITY = "city";
    public static final String USER_PIN = "pin";
    public static final String USER_MOBILE = "mobile";

    public static final String SEND_TO_ME="Self";

    public static final String UNPAID="unpaid";
    public static final String PAID="paid";

    public static final String VALID="VALID";
    public static final String INVALID="INVALID";

    public static final String PIN_CODE="pin_code";

    public static final String VERSION_CODE="version";
    public static final String VERSION_ID="v_id";

    public static final String FLAT_DISC="Flat";
    public static final String PERCENT_DISC="Percentage";

}
