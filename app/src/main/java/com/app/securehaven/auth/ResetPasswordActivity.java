package com.app.securehaven.auth;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.app.securehaven.R;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText et_email;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        et_email = findViewById(R.id.reset_password_et_email);
        Button btn_reset_password = findViewById(R.id.reset_password_btn_reset_password);
        Button btn_back = findViewById(R.id.reset_password_btn_back);

        mAuth= FirebaseAuth.getInstance();

        btn_back.setOnClickListener(v -> {
            Intent i = new Intent(ResetPasswordActivity.this, LoginActivity.class);
            finish();
            startActivity(i);
        });

        btn_reset_password.setOnClickListener(v -> {
            String email = et_email.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplication(), "Enter your registered email id",
                        Toast.LENGTH_SHORT).show();
            } else {
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(ResetPasswordActivity.this, "We have sent instructions to reset your password!",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ResetPasswordActivity.this, "Failed to send reset email!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }
}