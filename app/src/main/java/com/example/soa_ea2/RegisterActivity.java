package com.example.soa_ea2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private Intent intent;
    private ProgressBar spinner;
    private Button btnLogin, btnRegister;
    private TextInputEditText inputEmail, inputPassword, inputDNI, inputName, inputLastName, inputComision;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);

        spinner = findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        inputDNI = findViewById(R.id.input_dni);
        inputName = findViewById(R.id.input_name);
        inputEmail = findViewById(R.id.input_email);
        inputLastName = findViewById(R.id.input_lastname);
        inputPassword = findViewById(R.id.input_password);
        inputComision = findViewById(R.id.input_comision);
    }

    @Override
    protected void onStart() {
        super.onStart();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Conexion connection = new Conexion();

                String email = inputEmail.getText() + "";
                String password = inputPassword.getText() + "";
                String dni = inputDNI.getText() + "";
                String name = inputName.getText() + "";
                String lastName = inputLastName.getText() + "";
                String comision = inputComision.getText() + "";

                User user = new User(name, lastName, dni, email, password, comision);

                if(connection.checkConnection(RegisterActivity.this) && user.checkFroRegister(RegisterActivity.this)) {
                    spinner.setVisibility(View.VISIBLE);
                    Toast.makeText(RegisterActivity.this, "msg", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() { }
}