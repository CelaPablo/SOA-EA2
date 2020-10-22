package com.example.soa_ea2;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Intent intent;
    private ProgressBar spinner;
    private Button btnLogin, btnRegister;
    private TextInputEditText inputEmail, inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);

        spinner = findViewById(R.id.progressBar2);
        spinner.setVisibility(View.GONE);

        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
    }

    @Override
    protected void onStart() {
        super.onStart();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable email = inputEmail.getText();
                Editable password = inputPassword.getText();

                String pas = password + " " + BuildConfig.APPLICATION_ID;

                spinner.setVisibility(View.VISIBLE);

                Toast.makeText(LoginActivity.this, email + " " + pas, Toast.LENGTH_SHORT).show();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() { }
}