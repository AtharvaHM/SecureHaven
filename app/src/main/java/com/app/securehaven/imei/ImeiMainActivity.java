package com.app.securehaven.imei;

import androidx.appcompat.app.AppCompatActivity;
import com.app.securehaven.DashboardActivity;
import com.app.securehaven.R;
import com.google.firebase.firestore.FirebaseFirestore;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;

public class ImeiMainActivity extends AppCompatActivity {

    FirebaseFirestore dbRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imei_main);

        dbRoot = FirebaseFirestore.getInstance();

        EditText et_name = (EditText) findViewById(R.id.imei_main_et_name);
        EditText et_contact = (EditText) findViewById(R.id.imei_main_et_contact);
        EditText et_email = (EditText) findViewById(R.id.imei_main_et_email);

        String name = et_name.getText().toString().trim();
        String contact = et_contact.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String imei = getIMEIDeviceId(getApplicationContext());

        Button btn_back = findViewById(R.id.imei_main_btn_back);
        btn_back.setOnClickListener(v -> {
            Intent i = new Intent(ImeiMainActivity.this, DashboardActivity.class);
            finish();
            startActivity(i);
        });

        Button btn_lost = findViewById(R.id.imei_main_btn_lost);
        btn_lost.setOnClickListener(v -> {

            Map<String, String> items = new HashMap<>();
            items.put("Name", name);
            items.put("Email Address", email);
            items.put("Contact No.", contact);
            items.put("IMEI No.", imei);

            dbRoot.collection("IMEI").add(items)
                    .addOnCompleteListener(task -> Toast.makeText(getApplicationContext(), "Inserted Successfully",
                            Toast.LENGTH_LONG).show());
        });
    }

    public static String getIMEIDeviceId(Context context) {
        String deviceId;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return "";
                }
            }
            assert mTelephony != null;
            if (mTelephony.getDeviceId() != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    deviceId = mTelephony.getImei();
                } else {
                    deviceId = mTelephony.getDeviceId();
                }
            } else {
                deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }
        Log.d("deviceId", deviceId);
        return deviceId;
    }
}