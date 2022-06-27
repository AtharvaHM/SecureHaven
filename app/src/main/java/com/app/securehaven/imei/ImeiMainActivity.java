package com.app.securehaven.imei;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.app.securehaven.DashboardActivity;
import com.app.securehaven.R;
import com.app.securehaven.auth.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.Button;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;

public class ImeiMainActivity extends AppCompatActivity {

    FirebaseFirestore dbRoot;
    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
    String userID = firebaseUser.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imei_main);

        Button btn_back = findViewById(R.id.imei_main_btn_back);
        btn_back.setOnClickListener(v -> {
            Intent i = new Intent(ImeiMainActivity.this, DashboardActivity.class);
            finish();
            startActivity(i);
        });

        Button btn_lost = findViewById(R.id.imei_main_btn_lost);
        btn_lost.setOnClickListener(v -> insertData());
    }

    private void insertData() {

        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if(userProfile != null) {
                    String name = userProfile.name;
                    String contact = userProfile.contact;
                    String email = userProfile.email;
                    String imei = telephonyManager.getDeviceId();

                    Map<String, String> items = new HashMap<>();
                    items.put("Name", name);
                    items.put("Email Address", email);
                    items.put("Contact No.", contact);
                    items.put("IMEI No.", imei);

                    dbRoot.collection("IMEI Database").add(items)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    Toast.makeText(getApplicationContext(), "Inserted Successfully",
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ImeiMainActivity.this, "Something wrong happened!",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}