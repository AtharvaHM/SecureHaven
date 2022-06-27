package com.app.securehaven.cam;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.app.securehaven.DashboardActivity;
import com.app.securehaven.R;

public class CameraMainActivity extends AppCompatActivity {

    private CheckBox adminEnabled;
    private DevicePolicyManager devicePolicyManager;
    private TextView statusTV;
    private TextView countTV;
    private EditText emailET;
    private ComponentName compName;
    private boolean isAdminActive;

    @Override
    protected void onResume() {
        super.onResume();
        isAdminActive = devicePolicyManager.isAdminActive(compName);
        if(SecurityService.senderEmail!=null)
            emailET.setHint(SecurityService.senderEmail);
        if(isAdminActive){
            statusTV.setText("ON");
            statusTV.setTextColor(Color.GREEN);
        }
        else{
            statusTV.setText("OFF");
            statusTV.setTextColor(Color.RED);
        }
        countTV.setText(String.valueOf(SecurityService.failedPasswordCount));
        //First Set the CheckBox
        if(!isAdminActive && adminEnabled.isChecked()){
            adminEnabled.toggle();
        }
        else if(isAdminActive && !adminEnabled.isChecked()){
            adminEnabled.toggle();
        }
        else if(!isAdminActive)
            Toast.makeText(CameraMainActivity.this,"Admin Privilege Needed",Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O) //Requires minimum Oreo
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_main);

        adminEnabled=findViewById(R.id.adminAccessCheckBox);
        emailET=findViewById(R.id.et_email);
        Button tickIV = findViewById(R.id.tv_tick);
        statusTV=findViewById(R.id.tv_status);
        countTV=findViewById(R.id.tv_count);
        TextView reset = findViewById(R.id.tv_Reset);
        Button btnBack = findViewById(R.id.camera_main_btn_back);

        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        compName = new ComponentName(this,loginWatch.class);

        if(!SecurityService.serviceRunning){
            startForegroundService(new Intent(CameraMainActivity.this, SecurityService.class));
        }

        btnBack.setOnClickListener(v -> {
            Intent i = new Intent(CameraMainActivity.this, DashboardActivity.class);
            finish();
            startActivity(i);
        });

        tickIV.setOnClickListener(v -> {
            SecurityService.senderEmail=emailET.getText().toString();
            emailET.setHint(SecurityService.senderEmail);
            Toast.makeText(CameraMainActivity.this,"Email Set:"+SecurityService.senderEmail,Toast.LENGTH_SHORT).show();
        });

        reset.setOnClickListener(v -> {
            SecurityService.failedPasswordCount=0;
            countTV.setText(String.valueOf(0));
        });

        adminEnabled.setOnClickListener(v -> {
            if(!isAdminActive){
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Admin Access Needed To Check If Incorrect PIN Entered");
                startActivityForResult(intent,3 );
                isAdminActive = devicePolicyManager.isAdminActive(compName);
            } else {
                devicePolicyManager.removeActiveAdmin(compName);
                isAdminActive = false;
                statusTV.setText("OFF");
                statusTV.setTextColor(Color.RED);
                Toast.makeText(CameraMainActivity.this,"Admin Privilege Revoked",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 3) {
            if (resultCode == RESULT_OK)
                Toast.makeText(CameraMainActivity.this, "Admin Privilege Granted", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(CameraMainActivity.this, "Failed to Enable Admin Privilege", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}