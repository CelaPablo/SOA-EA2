package com.example.soa_ea2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button acelerometro, giroscopo, proximidad, luz, eventos;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        luz = findViewById(R.id.btn_luz);
        giroscopo = findViewById(R.id.btn_giroscopo);
        proximidad = findViewById(R.id.btn_proximidad);
        acelerometro = findViewById(R.id.btn_acelerometro);

        eventos = findViewById(R.id.btn_eventos);

        luz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, SensorActivity.class);
                intent.putExtra("type", Sensor.TYPE_LIGHT);
                startActivity(intent);
                finish();
            }
        });

        giroscopo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, SensorActivity.class);
                intent.putExtra("type", Sensor.TYPE_GYROSCOPE);
                startActivity(intent);
                finish();
            }
        });

        proximidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, SensorActivity.class);
                intent.putExtra("type", Sensor.TYPE_PROXIMITY);
                startActivity(intent);
                finish();
            }
        });

        acelerometro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

}