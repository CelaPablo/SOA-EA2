package com.example.soa_ea2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.soa_ea2.services.RefreshToken;
import com.example.soa_ea2.services.ServiceHTTP;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private Intent intent;
    private ProgressBar spinner;
    private TextInputEditText inputEmail, inputPassword, inputDNI, inputName, inputLastName, inputComision;

    public IntentFilter filter;
    private ReceptorOperacion receiver = new ReceptorOperacion();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button btnLogin = findViewById(R.id.btn_login);
        Button btnRegister = findViewById(R.id.btn_register);

        spinner = findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        inputDNI = findViewById(R.id.input_dni);
        inputName = findViewById(R.id.input_name);
        inputEmail = findViewById(R.id.input_email);
        inputLastName = findViewById(R.id.input_lastname);
        inputPassword = findViewById(R.id.input_password);
        inputComision = findViewById(R.id.input_comision);

        configurarBroadcastReciever();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
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

                User user = User.getInstance();
                user.setName(name);
                user.setLastname(lastName);
                user.setDni(dni);
                user.setEmail(email);
                user.setPassword(password);
                user.setComision(comision);

                if(connection.checkConnection(RegisterActivity.this) && user.checkFroRegister(RegisterActivity.this)) {
                    spinner.setVisibility(View.VISIBLE);
                    registerRequest(user);
                }
            }
        });
    }

    @Override
    public void onBackPressed() { }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        configurarBroadcastReciever();
    }

    private void configurarBroadcastReciever() {
        filter = new IntentFilter(Constantes.RESPONSE_REGISTER);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(receiver, filter);
    }

    private void registerRequest(final User user) {
        JSONObject object = new JSONObject();

        try {
            object.put("email", user.getEmail());
            object.put("password", user.getPassword());
            object.put("dni", Integer.parseInt(user.getDni()));
            object.put("name", user.getName());
            object.put("lastname", user.getLastname());
            object.put("commission", Integer.parseInt(user.getComision()));
            object.put("env", Constantes.ENV);

            Intent intent = new Intent(RegisterActivity.this, ServiceHTTP.class);
            intent.putExtra("dataJson", object.toString());
            intent.putExtra("uri", Constantes.URL_REGISTER);
            intent.putExtra("operation", Constantes.RESPONSE_REGISTER);
            intent.putExtra("typeRequest", Constantes.METODO_POST);
            intent.putExtra("token", "");

            startService(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public class ReceptorOperacion extends BroadcastReceiver {
        public void onReceive (Context context, Intent intent){
            try{
                String dataJsonString = intent.getStringExtra("dataJson");
                JSONObject data = new JSONObject(dataJsonString);

                User user = User.getInstance();
                user.setToken(data.getString("token"));
                user.setTokenRefresh(data.getString("token_refresh"));

                Intent i = new Intent(context, MainActivity.class);
                new RefreshToken().start();
                startActivity(i);
                finish();

            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}