package narmware.com.photouploadcopy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import narmware.com.photouploadcopy.R;
import narmware.com.photouploadcopy.support.NewtonCradleLoading;

public class LoadingActivity extends AppCompatActivity {
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        activity=this;
        NewtonCradleLoading loading= (NewtonCradleLoading) findViewById(R.id.newton);
        loading.start();
    }


}
