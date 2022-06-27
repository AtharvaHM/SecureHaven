package com.app.securehaven;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.app.securehaven.auth.LoginActivity;
import com.app.securehaven.cam.CameraMainActivity;
import com.app.securehaven.track.MapsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class DashboardActivity extends AppCompatActivity {

    private CardView cameraCard, imeiCard, smsCard, mapCard, profileCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        cameraCard = findViewById(R.id.dashboard_cameraCard);
        imeiCard= findViewById(R.id.dashboard_imeiCard);
        smsCard = findViewById(R.id.dashboard_smsCard);
        mapCard = findViewById(R.id.dashboard_mapCard);
        profileCard = findViewById(R.id.dashboard_profileCard);
        ImageButton imgBtn_logout = findViewById(R.id.dashboard_imgBtn_logout);

        imgBtn_logout.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                FirebaseAuth.getInstance().signOut();
            }
            Intent i = new Intent(DashboardActivity.this, LoginActivity.class);
            finish();  //Kill the activity from which you will go to next activity
            startActivity(i);
        });

        cameraCard.setOnClickListener(v -> {
            Intent i = new Intent(DashboardActivity.this, CameraMainActivity.class);
            finish();
            startActivity(i);
        });

//        imeiCard.setOnClickListener(v -> {
//            Intent i = new Intent(DashboardActivity.this, LoginActivity.class);
//            finish();
//            startActivity(i);
//        });

//        smsCard.setOnClickListener(v -> {
//            Intent i = new Intent(DashboardActivity.this, LoginActivity.class);
//            finish();
//            startActivity(i);
//        });

        mapCard.setOnClickListener(v -> {
            Intent i = new Intent(DashboardActivity.this, MapsActivity.class);
            finish();
            startActivity(i);
        });

        profileCard.setOnClickListener(v -> {
            Intent i = new Intent(DashboardActivity.this, ProfileActivity.class);
            finish();
            startActivity(i);
        });
    }
}