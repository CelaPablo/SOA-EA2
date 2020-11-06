package com.example.soa_ea2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.soa_ea2.services.RefreshToken;
import com.example.soa_ea2.services.ServiceHTTP;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private Intent intent;
    public IntentFilter filter;
    private CallbackEvent receiver = new CallbackEvent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button luz = findViewById(R.id.btn_luz);
        Button giroscopo = findViewById(R.id.btn_giroscopo);
        Button proximidad = findViewById(R.id.btn_proximidad);
        Button acelerometro = findViewById(R.id.btn_acelerometro);

        Button logout = findViewById(R.id.btn_logout);

        configurarBroadcastReciever();

        luz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventRequest("Sensor de Luz activado");
                intent = new Intent(MainActivity.this, SensorActivity.class);
                intent.putExtra("type", Sensor.TYPE_LIGHT);
                intent.putExtra("shared", Constantes.SHARED_LUZ);
                startActivity(intent);
                finish();
            }
        });

        giroscopo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventRequest("Sensor Giroscopo activado");
                intent = new Intent(MainActivity.this, SensorActivity.class);
                intent.putExtra("type", Sensor.TYPE_GYROSCOPE);
                intent.putExtra("shared", Constantes.SHARED_GIROSCOPO);
                startActivity(intent);
                finish();
            }
        });

        proximidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventRequest("Sensor de Proximidad activado");
                intent = new Intent(MainActivity.this, SensorActivity.class);
                intent.putExtra("type", Sensor.TYPE_PROXIMITY);
                intent.putExtra("shared", Constantes.SHARED_PROXIMIDAD);
                startActivity(intent);
                finish();
            }
        });

        acelerometro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventRequest("Sensor Acelerometro activado");
                intent = new Intent(MainActivity.this, SensorActivity.class);
                intent.putExtra("type", Sensor.TYPE_ACCELEROMETER);
                intent.putExtra("shared", Constantes.SHARED_ACELEROMETRO);
                startActivity(intent);
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = User.getInstance();
                user.setDefaultUSer();
                RefreshToken.getInstance().doStop();
                intent = new Intent(MainActivity.this, LoginActivity.class);
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
        RefreshToken.getInstance().doStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        configurarBroadcastReciever();
        RefreshToken refresh = RefreshToken.getInstance();
        if(!refresh.isRunning())
            refresh.doResume();
    }

    private void configurarBroadcastReciever() {
        filter = new IntentFilter(Constantes.RESPONSE_EVENT);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(receiver, filter);
    }

    private void eventRequest(String description) {
        JSONObject object = new JSONObject();
        User user = User.getInstance();
        Conexion connection = new Conexion();

        if(connection.checkConnection(MainActivity.this)) {
            String token = user.getToken();

            try {
                object.put("env", Constantes.ENV);
                object.put("description", description);
                object.put("type_events", Constantes.TYPE_EVENT);

                Intent intent = new Intent(MainActivity.this, ServiceHTTP.class);
                intent.putExtra("token", token);
                intent.putExtra("uri", Constantes.URL_EVENTS);
                intent.putExtra("dataJson", object.toString());
                intent.putExtra("typeRequest", Constantes.METODO_POST);
                intent.putExtra("operation", Constantes.RESPONSE_EVENT);

                startService(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /* Callback que recibe la respuesta - Solo muestra la respuesta en un Log */
    public static class CallbackEvent extends BroadcastReceiver {
        public void onReceive (Context context, Intent intent){
            String dataJsonString = intent.getStringExtra("dataJson");
            assert dataJsonString != null;
            Log.i("## RESPONSE EVENT ##", dataJsonString);
        }
    }
}