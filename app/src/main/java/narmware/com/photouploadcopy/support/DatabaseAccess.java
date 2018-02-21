package narmware.com.photouploadcopy.support;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import narmware.com.photouploadcopy.helpers.Constants;
import narmware.com.photouploadcopy.models.Address;
import narmware.com.photouploadcopy.models.Friends;
import narmware.com.photouploadcopy.models.Image;
import narmware.com.photouploadcopy.models.PinCode;
import narmware.com.photouploadcopy.models.VersionCode;


public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;
    List<Friends> friendsList;
    Friends friends;
    List<Image> quesAnsList;
    ArrayList<Address> userProfile;
    ArrayList<VersionCode> versionCodes;

    ArrayList<PinCode> pinCodeList;

    //QuizPojo quesDetails;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public void setImages(String id,String name,String path,String isSelected,String album,String height,String width) {

        ContentValues values = new ContentValues();
        values.put(Constants.IMG_ID, id);
        values.put(Constants.IMG_NAME, name);
        values.put(Constants.IMG_PATH, path);
        values.put(Constants.IMG_SELECTED, isSelected);
        values.put(Constants.IMG_ALBUM,album);
        values.put(Constants.IMG_HEIGHT,height);
        values.put(Constants.IMG_WIDTH,width);

        database.insert("image", null, values);
        //database.close();
    }

    public List<Image> getAllDetails() {
        quesAnsList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM image", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            //  String str=Integer.toString(cursor.getInt(0))+" "+cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3);
             long id= Long.parseLong(cursor.getString(0));
             String name=cursor.getString(1);
             String path=cursor.getString(2);
             boolean isSelected= Boolean.parseBoolean(cursor.getString(3));
            String album=cursor.getString(4);
            String height=cursor.getString(5);
            String width=cursor.getString(6);

            Image quesDetails = new Image(id,name,path,isSelected,album,height,width);
                quesAnsList.add(quesDetails);

            cursor.moveToNext();
        }
        cursor.close();
        //database.close();
        return quesAnsList;
    }

    public List<Image> getSelectedImages(String albumName) {
        quesAnsList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM image where isSelected = 'true' and album = '"+ albumName+"'", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            //  String str=Integer.toString(cursor.getInt(0))+" "+cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3);
            long id= Long.parseLong(cursor.getString(0));
            String name=cursor.getString(1);
            String path=cursor.getString(2);
            boolean isSelected= Boolean.parseBoolean(cursor.getString(3));
            String album=cursor.getString(4);
            String height=cursor.getString(5);
            String width=cursor.getString(6);

            if(cursor.getString(0)!="id") {
                Image quesDetails = new Image(id, name, path, isSelected, album, height, width);
                quesAnsList.add(quesDetails);
            }

            cursor.moveToNext();
        }
        cursor.close();
        //database.close();
        return quesAnsList;
    }
    public List<Image> getSelectedImagesOfOtherAlbums(String albumName) {
        quesAnsList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM image where isSelected = 'true' and album <> '"+ albumName+"'", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            //  String str=Integer.toString(cursor.getInt(0))+" "+cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3);
            long id= Long.parseLong(cursor.getString(0));
            String name=cursor.getString(1);
            String path=cursor.getString(2);
            boolean isSelected= Boolean.parseBoolean(cursor.getString(3));
            String album=cursor.getString(4);
            String height=cursor.getString(5);
            String width=cursor.getString(6);

            Image quesDetails = new Image(id,name,path,isSelected,album,height,width);
            quesAnsList.add(quesDetails);

            cursor.moveToNext();
        }
        cursor.close();
        //database.close();
        return quesAnsList;
    }

    public void UpdateQty(String qty, int f_id) {
        ContentValues values = new ContentValues();
        values.put(Constants.FRND_FRAME_QTY, qty);

        database.update("friend_profile", values,"f_server_id=" + f_id, null);
    }

    public void UpdateCartFlag(int flag, int f_id) {
        ContentValues values = new ContentValues();
        values.put(Constants.FRND_CART_FLAG, flag);

        database.update("friend_profile", values, "f_server_id=" + f_id, null);
    }


    public void SetDefaultAnswer() {
        ContentValues values = new ContentValues();
        values.put("given_opt", " ");

        database.update("question", values, "given_opt <> ' '", null);
        database.close();
    }


    public void setFrndsProfile(String addr,String state,String dist,String city,String pin,String mobile,String name,String email,String f_server_id,String fr_qty,int cart_flag) {

        ContentValues values = new ContentValues();
        values.put(Constants.FRND_ADDR, addr);
        values.put(Constants.FRND_STATE,state);
        values.put(Constants.FRND_DIST,dist);
        values.put(Constants.FRND_CITY,city);
        values.put(Constants.FRND_PIN,pin);
        values.put(Constants.FRND_MOBILE,mobile);
        values.put(Constants.FRND_NAME,name);
        values.put(Constants.FRND_EMAIL,email);
        values.put(Constants.FRND_SERVER_ID,f_server_id);
        values.put(Constants.FRND_FRAME_QTY,fr_qty);
        values.put(Constants.FRND_CART_FLAG,cart_flag);

        database.insert("friend_profile", null, values);
        //database.close();
    }

    public void UpdateFrndsProfile(String addr,String state,String dist,String city,String pin,String mobile,String name,String email,int f_id) {

        ContentValues values = new ContentValues();
        values.put("addr", addr);
        values.put("state",state);
        values.put("dist",dist);
        values.put("city",city);
        values.put("pin",pin);
        values.put("mobile",mobile);
        values.put("fr_name",name);
        values.put("fr_email",email);

        database.update("friend_profile", values, "f_server_id=" + f_id, null);

        //database.close();
    }

    public List<Friends> getFriends() {
        friendsList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM friend_profile", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            //  String str=Integer.toString(cursor.getInt(0))+" "+cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3);
            int id = cursor.getInt(0);
            String addr = cursor.getString(1);
            String state = cursor.getString(2);
            String dist = cursor.getString(3);
            String city = cursor.getString(4);
            String pin = cursor.getString(5);
            String mobile = cursor.getString(6);
            String name = cursor.getString(7);
            String email = cursor.getString(8);
            String server_id = cursor.getString(9);
            String fr_qty = cursor.getString(10);
            int flag = cursor.getInt(11);

            if (id == -1)
            {

            }
            else if(name.equals(Constants.SEND_TO_ME))
            {
                //dont add this record
            }
            else {
                Friends friends = new Friends(id,addr, state, city, pin, dist, null, mobile, null,name,email,server_id,fr_qty,flag);
                friendsList.add(friends);
            }
            cursor.moveToNext();
        }
        cursor.close();
        //database.close();
        return friendsList;
    }

    public Friends getSingleFrnd(int f_id) {
        friendsList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM friend_profile where f_server_id ="+f_id, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            //  String str=Integer.toString(cursor.getInt(0))+" "+cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3);
            int id = cursor.getInt(0);
            String addr = cursor.getString(1);
            String state = cursor.getString(2);
            String dist = cursor.getString(3);
            String city = cursor.getString(4);
            String pin = cursor.getString(5);
            String mobile = cursor.getString(6);
            String name = cursor.getString(7);
            String email = cursor.getString(8);
            String server_id = cursor.getString(9);

            if (id == -1)
            {

            }
            else {
                friends = new Friends(id,addr, state, city, pin, dist, null, mobile, null,name,email,server_id,null,0);
                friendsList.add(friends);
            }
            cursor.moveToNext();
        }
        cursor.close();
        //database.close();
        return friends;
    }

    public void deleteSingleFriend(String f_server_id)
    {
        database.execSQL("delete from friend_profile where f_server_id = '"+f_server_id+"'");
    }

    public List<Friends> getCartSelectedFriends() {
        friendsList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM friend_profile where cart_flag = 1 ", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            //  String str=Integer.toString(cursor.getInt(0))+" "+cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3);
            int id = cursor.getInt(0);
            String addr = cursor.getString(1);
            String state = cursor.getString(2);
            String dist = cursor.getString(3);
            String city = cursor.getString(4);
            String pin = cursor.getString(5);
            String mobile = cursor.getString(6);
            String name = cursor.getString(7);
            String email = cursor.getString(8);
            String server_id = cursor.getString(9);
            String fr_qty = cursor.getString(10);
            int flag =cursor.getInt(11);

            if (id == -1)
            {

            }
            else {
                Friends friends = new Friends(id,addr, state, city, pin, dist, null, mobile, null,name,email,server_id,fr_qty,flag);
                friendsList.add(friends);
            }
            cursor.moveToNext();
        }
        cursor.close();
        //database.close();
        return friendsList;
    }

    public void setUserProfile(String addr,String state,String dist,String city,String pin,String mobile,String user_id) {

        ContentValues values = new ContentValues();
        values.put(Constants.USER_ADDR, addr);
        values.put(Constants.USER_STATE,state);
        values.put(Constants.USER_DIST,dist);
        values.put(Constants.USER_CITY,city);
        values.put(Constants.USER_PIN,pin);
        values.put(Constants.USER_MOBILE,mobile);
        values.put(Constants.USER_ID,user_id);

        database.insert("user_profile", null, values);
        //database.close();
    }
    public void UpdateUserProfile(String addr,String state,String dist,String city,String pin,String mobile,String user_id) {

        ContentValues values = new ContentValues();
        values.put(Constants.USER_ADDR, addr);
        values.put(Constants.USER_STATE,state);
        values.put(Constants.USER_DIST,dist);
        values.put(Constants.USER_CITY,city);
        values.put(Constants.USER_PIN,pin);
        values.put(Constants.USER_MOBILE,mobile);
        values.put(Constants.USER_ID,user_id);

        database.update("user_profile", values, "user_id=" + user_id, null);
        //database.close();
    }

    public void UpdateUserAddress(String addr,String user_id) {

        ContentValues values = new ContentValues();
        values.put(Constants.USER_ADDR, addr);

        database.update("user_profile", values, "user_id=" + user_id, null);
        //database.close();
    }
    public void UpdateUserMobile(String mobile,String user_id) {

        ContentValues values = new ContentValues();
        values.put(Constants.USER_MOBILE, mobile);

        database.update("user_profile", values, "user_id=" + user_id, null);
        //database.close();
    }
    public ArrayList<Address> getSingleUser() {
        userProfile=new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM user_profile", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            //  String str=Integer.toString(cursor.getInt(0))+" "+cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3);
            int id = cursor.getInt(0);
            String addr = cursor.getString(1);
            String state = cursor.getString(2);
            String city = cursor.getString(3);
            String pin= cursor.getString(4);
            String dist = cursor.getString(5);
            String u_id = cursor.getString(6);
            String mobile=cursor.getString(7);
            if (id == -1)
            {

            }
            else {
                Address user=new Address(addr,state,city,pin,dist,u_id,mobile);
                userProfile.add(user);
            }
            cursor.moveToNext();
        }
        cursor.close();
        //database.close();
        return userProfile;
    }

    public void setArea(String pincode,String state,String dist,String city) {

        ContentValues values = new ContentValues();
        values.put(Constants.PIN_CODE, pincode);
        values.put(Constants.USER_STATE, state);
        values.put(Constants.USER_DIST, dist);
        values.put(Constants.USER_CITY, city);

        database.insert("area", null, values);
        //database.close();
    }

    public void UpdateVersion(int versionCode,int v_id) {

        ContentValues values = new ContentValues();
        values.put(Constants.VERSION_CODE, versionCode);

        database.update("versions", values, "v_id=" + v_id, null);
        //database.close();
    }

    public ArrayList<VersionCode> getVersion() {
        versionCodes=new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM versions", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            //  String str=Integer.toString(cursor.getInt(0))+" "+cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3);
            int v_id = cursor.getInt(0);
            int version = cursor.getInt(1);

            VersionCode user=new VersionCode(v_id,version);
            versionCodes.add(user);

            cursor.moveToNext();
        }
        cursor.close();
        //database.close();
        return versionCodes;
    }

    public ArrayList<PinCode> getArea() {
        pinCodeList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM area", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            //  String str=Integer.toString(cursor.getInt(0))+" "+cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3);
            String pin = cursor.getString(0);
            String state = cursor.getString(1);
            String dist = cursor.getString(2);
            String city = cursor.getString(3);

            if (pin.equals(-1))
            {

            }
            else {
                PinCode pinCode = new PinCode(pin,state,dist,city);
                pinCodeList.add(pinCode);
            }
            cursor.moveToNext();
        }
        cursor.close();
        //database.close();
        return pinCodeList;
    }
    public void deleteAll()
    {
        database.execSQL("delete from image");

    }

    public void deleteSingle(long removedId,String albumName)
    {
        database.execSQL("delete from image where id = "+removedId +" and album = '"+albumName+"'");
    }

    public void deleteAllArea()
    {
        database.execSQL("delete from area");
    }

}