package narmware.com.photouploadcopy;

import android.support.multidex.MultiDexApplication;

/**
 * Created by savvy on 12/27/2017.
 */

public class MyApplication extends MultiDexApplication {

    public static final String URL_SERVER = "http://www.narmware.com/kp/api/";
    public static final String URL_LOGIN = "logincheck.php";
    public static final String URL_PROFILE_UPDATE = "profile-update.php";
    public static final String URL_FRIEND_PROFILE = "friend-create.php";
    public static final String URL_UPDATE_FRIEND_PROFILE = "friend-update.php";
    public static final String URL_GET_ALL_FRNDS = "friend-list.php";
    public static final String URL_USER_PROFILE = "user-profile.php";
    public static final String URL_PHOTO_UPLOAD = "http://www.narmware.com/kp/api/photo-upload.php";
    public static final String URL_GET_ALBUM = "album.php";
    public static final String URL_GET_ALBUM_SIZE_PRICE = "album-size.php";
    public static final String URL_GET_FRAME = "frame.php";
    public static final String URL_SEND_FRAME_ALBUM = "attach-frame.php";
    public static final String URL_MAKE_PAYMENT = "bill-create.php";
    public static final String URL_DELETE_FRIEND = "friend-delete.php";

    public static final String URL_PAYMENT_GATEWAY = "payment.php?";//web view
    public static final String URL_INVOICE_DETAILS = "invoice.php";
    public static final String URL_PAID_BILL_DETAILS = "call-invoice.php?";
    public static final String URL_UNPAID_ORDER = "unpaid-bill.php";
    public static final String URL_GET_INVOICE_STATUS = "inv-status.php";
    public static final String URL_GET_PIN_STATUS = "pincode.php";
    public static final String URL_CHECK_VERSION = "version.php";
    public static final String URL_GET_PINCODES = "pincode_details.php";

    public static final String URL_DELETE_ALBUM= "http://www.narmware.com/kp/upload/delete-img.php";

    public static final String URL_COUPONS = "coupon.php";


}
