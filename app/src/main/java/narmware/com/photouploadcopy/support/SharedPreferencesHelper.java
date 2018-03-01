package narmware.com.photouploadcopy.support;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import narmware.com.photouploadcopy.helpers.Constants;

/**
 * Created by comp16 on 12/19/2017.
 */

public class SharedPreferencesHelper {
    private static final String IMAGES_PREF="images";
    private static final String LOGIN_PREF="login";
    private static final String USER_NAME="name";
    private static final String USER_EMAIL="email";
    private static final String USER_PROF_PIC="picture";
    private static final String USER_ID="id";

    //profile address
    private static final String USER_ADDR="address";
    private static final String USER_STATE="state";
    private static final String USER_DIST="dist";
    private static final String USER_CITY="city";
    private static final String USER_PIN="pin";
    private static final String USER_MOBILE="mobile";

    //album
    private static final String ALBUM_ID="a_id";
    private static final String ALBUM_PRICE="a_price";
    private static final String ALBUM_SIZE="a_size";

    //frame
    private static final String FRAME_ID="frame_id";
    private static final String FRAME_PATH="frame_path";
    private static final String FRAME_PRICE="frame_price";

    //cart invoice
    private static final String INVOICE_ID="inv_id";
    private static final String IS_SEND_ME="isSendMe";
    private static final String SELF_FRND_ID="self_id";

    private static final String SELECTED_FRAGMENT="fragment";

    private static final String PAYMENT_STATUS="status";

    private static final String PIN_STATUS="pin_status";
    private static final String FIRST_INSTALL="install";

//coupon
private static final String COUPON_NAME="coupon_name";
    private static final String COUPON_PRICE="coupon_price";
    private static final String COUPON_MIN_PRICE="coupon_min";

    private static final String TOTAL_PRICE="total";

    private static final String SHIPPING_CHARGES="shipping";

    public static void setShippingCharges(int shipping, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putInt(SHIPPING_CHARGES,shipping);
        edit.commit();
    }

    public static int getShippingCharges(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        int shipping=pref.getInt(SHIPPING_CHARGES,0);
        return shipping;
    }

    public static void setTotalPrice(int total, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putInt(TOTAL_PRICE,total);
        edit.commit();
    }

    public static int getTotalPrice(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        int total=pref.getInt(TOTAL_PRICE,0);
        return total;
    }

    public static void setCouponMinPrice(String coupon_min, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(COUPON_MIN_PRICE,coupon_min);
        edit.commit();
    }

    public static String getCouponMinPrice(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String coupon_price=pref.getString(COUPON_MIN_PRICE,null);
        return coupon_price;
    }

    public static void setCouponPrice(String coupon_price, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(COUPON_PRICE,coupon_price);
        edit.commit();
    }

    public static String getCouponPrice(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String coupon_price=pref.getString(COUPON_PRICE,null);
        return coupon_price;
    }

    public static void setCouponName(String coupon_name, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(COUPON_NAME,coupon_name);
        edit.commit();
    }

    public static String getCouponName(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String coupon_name=pref.getString(COUPON_NAME,null);
        return coupon_name;
    }

    public static void setPinStatus(String pin_status, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(PIN_STATUS,pin_status);
        edit.commit();
    }

    public static String getPinStatus(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String pin_status=pref.getString(PIN_STATUS, Constants.INVALID);
        return pin_status;
    }

    public static void setSelfFrndId(String self_id, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(SELF_FRND_ID,self_id);
        edit.commit();
    }

    public static String getSelfFrndId(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String self_id=pref.getString(SELF_FRND_ID,null);
        return self_id;
    }

    public static void setAlbumSize(String a_size, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(ALBUM_SIZE,a_size);
        edit.commit();
    }

    public static String getAlbumSize(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String a_size=pref.getString(ALBUM_SIZE,null);
        return a_size;
    }

    public static void setPaymentStatus(String status, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(PAYMENT_STATUS,status);
        edit.commit();
    }

    public static String getPaymentStatus(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String status=pref.getString(PAYMENT_STATUS,"unpaid");
        return status;
    }

    public static void setSelectedFragment(String fragment, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(SELECTED_FRAGMENT,fragment);
        edit.commit();
    }

    public static String getSelectedFragment(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String fragment=pref.getString(SELECTED_FRAGMENT,null);
        return fragment;
    }

    public static void setIsSendMe(boolean isSendMe, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putBoolean(IS_SEND_ME,isSendMe);
        edit.commit();
    }

    public static boolean getIsSendMe(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        boolean isSendMe=pref.getBoolean(IS_SEND_ME,true);
        return isSendMe;
    }


    public static void setInvoiceId(String inv_id, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(INVOICE_ID,inv_id);
        edit.commit();
    }

    public static String getInvoiceId(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String inv_id=pref.getString(INVOICE_ID,null);
        return inv_id;
    }

    public static void setFramePath(String frame_path, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(FRAME_PATH,frame_path);
        edit.commit();
    }

    public static String getFramePath(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String frame_path=pref.getString(FRAME_PATH,null);
        return frame_path;
    }

    public static void setFramePrice(String frame_price, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(FRAME_PRICE,frame_price);
        edit.commit();
    }

    public static String getFramePrice(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String frame_price=pref.getString(FRAME_PRICE,null);
        return frame_price;
    }

    public static void setAlbumPrice(String a_price, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(ALBUM_PRICE,a_price);
        edit.commit();
    }

    public static String getAlbumPrice(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String a_price=pref.getString(ALBUM_PRICE,null);
        return a_price;
    }

    public static void setFrameId(String frame_id, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(FRAME_ID,frame_id);
        edit.commit();
    }

    public static String getFrameId(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String frame_id=pref.getString(FRAME_ID,null);
        return frame_id;
    }

    public static void setAlbumId(String a_id, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(ALBUM_ID,a_id);
        edit.commit();
    }

    public static String getAlbumId(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String a_id=pref.getString(ALBUM_ID,null);
        return a_id;
    }


    public static void setUserMobile(String mobile, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(USER_MOBILE,mobile);
        edit.commit();
    }

    public static String getUserMobile(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String mobile=pref.getString(USER_MOBILE,null);
        return mobile;
    }

    public static void setUserPin(String pin, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(USER_PIN,pin);
        edit.commit();
    }

    public static String getUserPin(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String pin=pref.getString(USER_PIN,null);
        return pin;
    }

    public static void setUserCity(String city, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(USER_CITY,city);
        edit.commit();
    }

    public static String getUserCity(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String city=pref.getString(USER_CITY,null);
        return city;
    }

    public static void setUserDist(String dist, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(USER_DIST,dist);
        edit.commit();
    }

    public static String getUserDist(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String dist=pref.getString(USER_DIST,null);
        return dist;
    }


    public static void setUserState(String state, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(USER_STATE,state);
        edit.commit();
    }

    public static String getUserState(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String state=pref.getString(USER_STATE,null);
        return state;
    }


    public static void setUserAddr(String address, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(USER_ADDR,address);
        edit.commit();
    }

    public static String getUserAddr(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String address=pref.getString(USER_ADDR,null);
        return address;
    }

    public static void setUserId(String id, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(USER_ID,id);
        edit.commit();
    }

    public static String getUserId(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String id=pref.getString(USER_ID,null);
        return id;
    }

    public static void setIsAdmin(boolean is_admin, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putBoolean(IMAGES_PREF,is_admin);
        edit.commit();
    }

    public static boolean getIsAdmin(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        boolean is_admin=pref.getBoolean(IMAGES_PREF,false);
        return is_admin;
    }



    public static void setLogin(boolean login, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putBoolean(LOGIN_PREF,login);
        edit.commit();
    }

    public static boolean getLogin(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        boolean login=pref.getBoolean(LOGIN_PREF,false);
        return login;
    }


    public static void setUserName(String name, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(USER_NAME,name);
        edit.commit();
    }

    public static String getUserName(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String name=pref.getString(USER_NAME,null);
        return name;
    }

    public static void setUserEmail(String email, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(USER_EMAIL,email);
        edit.commit();
    }

    public static String getUserEmail(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String email=pref.getString(USER_EMAIL,null);
        return email;
    }

    public static void setUserProfPic(String prof_url, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(USER_PROF_PIC,prof_url);
        edit.commit();
    }

    public static String getUserProfPic(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String prof_url=pref.getString(USER_PROF_PIC,null);
        return prof_url;
    }
}
