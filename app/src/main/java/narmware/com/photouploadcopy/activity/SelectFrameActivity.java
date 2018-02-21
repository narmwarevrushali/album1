package narmware.com.photouploadcopy.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import narmware.com.photouploadcopy.R;
import narmware.com.photouploadcopy.fragment.AddressFragment;
import narmware.com.photouploadcopy.fragment.AlbumFragment;
import narmware.com.photouploadcopy.fragment.CartEmptyFragment;
import narmware.com.photouploadcopy.fragment.CartFullFragment;
import narmware.com.photouploadcopy.fragment.FriendsFragment;
import narmware.com.photouploadcopy.fragment.FriendsProfileFragment;
import narmware.com.photouploadcopy.fragment.HomeFragment;
import narmware.com.photouploadcopy.fragment.ProfileFragment;
import narmware.com.photouploadcopy.fragment.SelectFrameFragment;


public class SelectFrameActivity extends AppCompatActivity implements SelectFrameFragment.OnFragmentInteractionListener,FriendsProfileFragment.OnFragmentInteractionListener,FriendsFragment.OnFragmentInteractionListener,HomeFragment.OnFragmentInteractionListener,AddressFragment.OnFragmentInteractionListener,AlbumFragment.OnFragmentInteractionListener,CartEmptyFragment.OnFragmentInteractionListener,CartFullFragment.OnFragmentInteractionListener,ProfileFragment.OnFragmentInteractionListener{

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    TextView mTxtTitle;
    ImageView mImgLogo;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_frame);
        getSupportActionBar().hide();
        setFragment(new SelectFrameFragment());

       // mTxtTitle= (TextView) findViewById(R.id.txt_title);
        mImgLogo= (ImageView) findViewById(R.id.img_logo);
        //mImgLogo.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void setFragment(Fragment fragment)
    {
        mFragmentManager=getSupportFragmentManager();
        mFragmentTransaction=mFragmentManager.beginTransaction();
        //mFragmentTransaction.addToBackStack("null");
        mFragmentTransaction.replace(R.id.fragment_container,fragment);
        mFragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(String title) {


        if(title.equals("Home"))
        {
            mImgLogo.setVisibility(View.VISIBLE);
            mTxtTitle.setVisibility(View.INVISIBLE);
        }
        else
        {
            mImgLogo.setVisibility(View.INVISIBLE);
            //mTxtTitle.setVisibility(View.VISIBLE);
           // mTxtTitle.setText(title);
        }
    }

   /* @Override
    public void onBackPressed() {

        // Toast.makeText(this, "OnBackPress", Toast.LENGTH_SHORT).show();

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Your changes will be discard!")
                .setConfirmText("Yes,discard it!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                      *//*  sDialog
                                .setTitleText("Discarded!")
                                .setContentText("Your imaginary file has been deleted!")
                                .setConfirmText("OK")
                                .setConfirmClickListener(null)
                                .showCancelButton(false)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);*//*

                       DatabaseAccess databaseAccess = DatabaseAccess.getInstance(SelectFrameActivity.this);
                        databaseAccess.open();
                        databaseAccess.deleteAll();
                        finish();

                        SharedPreferencesHelper.setAlbumId(null,SelectFrameActivity.this);
                    }
                })
                .showCancelButton(true)
                .setCancelText("Cancel")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();
        //databaseAccess.deleteAll();
    }*/
}
