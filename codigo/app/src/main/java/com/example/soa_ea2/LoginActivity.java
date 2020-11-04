package com.example.soa_ea2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.example.soa_ea2.services.RefreshToken;
import com.example.soa_ea2.services.ServiceHTTP;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private User user;
    private Intent intent;
    private ProgressBar spinner;
    private Button btnLogin, btnRegister;
    private TextInputEditText inputEmail, inputPassword;

    public IntentFilter filter;
    private CallbackLogin receiver = new CallbackLogin();

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

        configurarBroadcastReciever();

        user = User.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Conexion connection = new Conexion();

                user.setEmail(inputEmail.getText() + "");
                user.setPassword(inputPassword.getText() + "");

                if(connection.checkConnection(LoginActivity.this) &&
                        user.checkForLogin(LoginActivity.this)) {
                    spinner.setVisibility(View.VISIBLE);
                    loginRequest(user);
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
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
        filter = new IntentFilter(Constantes.RESPONSE_LOGIN);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(receiver, filter);
    }

    private void loginRequest(final User user) {
        JSONObject object = new JSONObject();

        try {
            object.put("env", Constantes.ENV);
            object.put("email", user.getEmail());
            object.put("password", user.getPassword());

            Intent intent = new Intent(LoginActivity.this, ServiceHTTP.class);

            intent.putExtra("token", "");
            intent.putExtra("uri", Constantes.URL_LOGIN);
            intent.putExtra("dataJson", object.toString());
            intent.putExtra("typeRequest", Constantes.METODO_POST);
            intent.putExtra("operation", Constantes.RESPONSE_LOGIN);

            startService(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* Callback que recibe la respuesta del servidor */
    public class CallbackLogin extends BroadcastReceiver {
        public void onReceive (Context context, Intent intent){
            try{
                spinner.setVisibility(View.GONE);

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