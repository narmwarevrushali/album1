package narmware.com.photouploadcopy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import narmware.com.photouploadcopy.R;

public class LoadingActivity extends AppCompatActivity {
    public static Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loader);
        getSupportActionBar().hide();
        activity=this;

        init();
    }

    private void init() {

    }

    @Override
    public void onBackPressed() {
    }
}
