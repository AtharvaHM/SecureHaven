package com.app.securehaven;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.app.securehaven.auth.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button btn_back = findViewById(R.id.profile_btn_back);
        btn_back.setOnClickListener(v -> {
            Intent i = new Intent(ProfileActivity.this, DashboardActivity.class);
            finish();
            startActivity(i);
        });

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        String userID = firebaseUser.getUid();

        final TextView profile_tv_name = (TextView) findViewById(R.id.profile_tv_name);
        final TextView profile_tv_contact = (TextView) findViewById(R.id.profile_tv_contact);
        final TextView profile_tv_email = (TextView) findViewById(R.id.profile_tv_email);
        final TextView profile_tv_phone_info = (TextView) findViewById(R.id.profile_tv_phone_info);

        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if(userProfile != null) {
                    String name = userProfile.name;
                    String contact = userProfile.contact;
                    String email = userProfile.email;
                    String phoneInfo = userProfile.phoneInfo;

                    profile_tv_name.setText(name);
                    profile_tv_contact.setText(contact);
                    profile_tv_email.setText(email);
                    profile_tv_phone_info.setText(phoneInfo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Something wrong happened!",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}