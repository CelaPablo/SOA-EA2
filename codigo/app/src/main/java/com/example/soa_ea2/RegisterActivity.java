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

    private User user;

    private Intent intent;
    private ProgressBar spinner;
    private TextInputEditText inputEmail, inputPassword, inputDNI, inputName, inputLastName, inputComision;

    public IntentFilter filter;
    private CallbackRegister receiver = new CallbackRegister();

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

        user = User.getInstance();

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

                user.setDni(inputDNI.getText() + "");
                user.setName(inputName.getText() + "");
                user.setEmail(inputEmail.getText() + "");
                user.setLastname(inputLastName.getText() + "");
                user.setPassword(inputPassword.getText() + "");
                user.setComision(inputComision.getText() + "");

                if(connection.checkConnection(RegisterActivity.this) &&
                        user.checkFroRegister(RegisterActivity.this)) {
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
            object.put("env", Constantes.ENV);
            object.put("name", user.getName());
            object.put("email", user.getEmail());
            object.put("password", user.getPassword());
            object.put("lastname", user.getLastname());
            object.put("dni", Integer.parseInt(user.getDni()));
            object.put("commission", Integer.parseInt(user.getComision()));

            Intent intent = new Intent(RegisterActivity.this, ServiceHTTP.class);
            intent.putExtra("token", "");
            intent.putExtra("dataJson", object.toString());
            intent.putExtra("uri", Constantes.URL_REGISTER);
            intent.putExtra("typeRequest", Constantes.METODO_POST);
            intent.putExtra("operation", Constantes.RESPONSE_REGISTER);

            startService(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* Callback donde se recibe la respuesta del hilo donde corre el service. */
    public class CallbackRegister extends BroadcastReceiver {
        public void onReceive (Context context, Intent intent){
            try{
                String dataJsonString = intent.getStringExtra("dataJson");
                assert dataJsonString != null;
                JSONObject data = new JSONObject(dataJsonString);

                user.setToken(data.getString("token"));
                user.setTokenRefresh(data.getString("token_refresh"));

                Intent i = new Intent(context, MainActivity.class);
                RefreshToken.getInstance().start();
                startActivity(i);
                finish();
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}