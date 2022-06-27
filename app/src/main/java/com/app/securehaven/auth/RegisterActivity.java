package com.app.securehaven.auth;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.app.securehaven.DashboardActivity;
import com.app.securehaven.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_name, et_contact, et_email, et_password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        et_name = findViewById(R.id.register_et_name);
        et_contact = findViewById(R.id.register_et_contact);
        et_email = findViewById(R.id.register_et_email);
        et_password = findViewById(R.id.register_et_password);
        Button register_btn_register = findViewById(R.id.register_btn_register);
        Button register_btn_login = findViewById(R.id.register_btn_login);

        register_btn_register.setOnClickListener(v -> {
            String name = et_name.getText().toString().trim();
            String contact = et_contact.getText().toString().trim();
            String email = et_email.getText().toString().trim();
            String password = et_password.getText().toString().trim();
            String phoneInfo = Build.PRODUCT+"\n"+Build.BRAND+"\n"+Build.DEVICE+"\n"+
                    Build.BOARD+"\n"+Build.MANUFACTURER+"\n"+Build.MODEL+"\n"+Build.SERIAL;

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            User user = new User(name, contact, email, phoneInfo);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(task1 -> {
                                        if(task1.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, "User has been registered successfully",
                                                    Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(RegisterActivity.this, DashboardActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(RegisterActivity.this, "Failed to register! Please try again",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    });
        });

        register_btn_login.setOnClickListener(v -> {
            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
            finish();
            startActivity(i);
        });

    }
}