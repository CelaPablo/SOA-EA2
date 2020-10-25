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

import com.example.soa_ea2.services.ServiceHTTP;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private Intent intent;
    public IntentFilter filter;
    private ReceptorOperacion receiver = new ReceptorOperacion();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button luz = findViewById(R.id.btn_luz);
        Button giroscopo = findViewById(R.id.btn_giroscopo);
        Button proximidad = findViewById(R.id.btn_proximidad);
        Button acelerometro = findViewById(R.id.btn_acelerometro);

        Button eventos = findViewById(R.id.btn_eventos);

        configurarBroadcastReciever();

        luz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventRequest("Sensor de Luz activado");
                intent = new Intent(MainActivity.this, SensorActivity.class);
                intent.putExtra("type", Sensor.TYPE_LIGHT);
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
                startActivity(intent);
                finish();
            }
        });

        eventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                object.put("type_events", Constantes.TYPE_EVENT);
                object.put("description", description);

                Intent intent = new Intent(MainActivity.this, ServiceHTTP.class);
                intent.putExtra("dataJson", object.toString());
                intent.putExtra("uri", Constantes.URL_EVENTS);
                intent.putExtra("operation", Constantes.RESPONSE_EVENT);
                intent.putExtra("typeRequest", Constantes.METODO_POST);
                intent.putExtra("token", token);

                startService(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static class ReceptorOperacion extends BroadcastReceiver {
        public void onReceive (Context context, Intent intent){
            String dataJsonString = intent.getStringExtra("dataJson");
            assert dataJsonString != null;
            Log.i("RESPONSE EVENT", dataJsonString);
        }
    }
}