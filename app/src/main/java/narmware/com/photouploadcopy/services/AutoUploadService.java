package narmware.com.photouploadcopy.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AutoUploadService extends Service {

    Context context;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        //Toast.makeText(this, " AutoUploadService Created ", Toast.LENGTH_LONG).show();
        context=getApplicationContext();
    }

    @Override
    public void onStart(Intent intent, int startId) {

        Log.e("Service","called");
       // Toast.makeText(this, " AutoUploadService Started", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
       // Toast.makeText(this, "Servics Stopped", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }
}