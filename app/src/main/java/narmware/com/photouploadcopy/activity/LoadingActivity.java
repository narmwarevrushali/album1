package narmware.com.photouploadcopy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.daimajia.numberprogressbar.NumberProgressBar;

import narmware.com.photouploadcopy.R;

public class LoadingActivity extends AppCompatActivity {
    public static Activity activity;
    public static NumberProgressBar mHorizontalProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loader);
        getSupportActionBar().hide();
        activity=this;

        init();
    }

    private void init() {
         mHorizontalProgress = (NumberProgressBar) findViewById(R.id.number_progress);
        mHorizontalProgress.setMax(100);
    }

    @Override
    public void onBackPressed() {
    }
}
