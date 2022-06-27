package com.app.securehaven.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.app.securehaven.DashboardActivity;
import com.app.securehaven.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText et_email, et_password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
            finish();
            startActivity(i);
        }

        et_email = findViewById(R.id.login_et_email);
        et_password = findViewById(R.id.login_et_password);
        Button btn_login = findViewById(R.id.login_btn_login);
        Button btn_reset_password = findViewById(R.id.login_btn_reset_password);
        Button btn_register = findViewById(R.id.login_btn_register);

        btn_register.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            finish();
            startActivity(i);
        });

        btn_reset_password.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, ResetPasswordActivity.class);
            finish();
            startActivity(i);
        });

        btn_login.setOnClickListener(v -> {
            String email = et_email.getText().toString().trim();
            final String password = et_password.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, task -> {
                        if (!task.isSuccessful()) {
                            if (password.length() < 6) {
                                et_password.setError(getString(R.string.minimum_password));
                            } else {
                                Toast.makeText(LoginActivity.this, getString(R.string.auth_failed),
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "User has been logged in successfully",
                                    Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
        });

    }
}