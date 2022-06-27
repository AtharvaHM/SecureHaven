package com.app.securehaven.cam;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class loginWatch extends DeviceAdminReceiver {

    /**
     * This method is called once device admin is enabled
     */
    @Override
    public void onEnabled(@NonNull Context context, @NonNull Intent intent) {
        Toast.makeText(context,"Device Admin Enabled", Toast.LENGTH_SHORT).show();
        Log.i("loginWatch","Device Admin Enabled");
    }

    /**
     * This method is called once device admin is disabled
     */
    @Override
    public void onDisabled(@NonNull Context context, @NonNull Intent intent) {
        Toast.makeText(context,"Device Admin Disabled",Toast.LENGTH_SHORT).show();
        Log.i("loginWatch","Device Admin Disabled");
    }

    /***
     * This method will be called everytime an incorrect PIN is entered
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onPasswordFailed(@NonNull Context context, @NonNull Intent intent) {
        super.onPasswordFailed(context, intent);
        SecurityService.failedPasswordCount++;
        if(SecurityService.failedPasswordCount%2!=0){
            new Thread(() -> {
                CameraHandler camString=new CameraHandler();
                String encoded64=camString.takePic();
                new MailSend(encoded64);
                Log.d("loginWatch-passFailed", String.valueOf(SecurityService.failedPasswordCount));
                Log.d("loginWatch-passFailed",encoded64);
            }).start();
        }
    }
}