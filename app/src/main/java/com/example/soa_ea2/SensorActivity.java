package com.example.soa_ea2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    private String shared;
    private int sensorType, index;

    private ImageView image;
    private Button back, eventos;
    private SensorManager manager;
    private TextView title,valor1, valor2, valor3;

    private SharedPreferences preferences;

    private long timeNow, timeOld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        Intent intent = getIntent();
        shared = intent.getStringExtra("shared");
        sensorType = intent.getIntExtra("type", 1);

        preferences = getSharedPreferences(shared, MODE_PRIVATE);
        index = preferences.getInt(Constantes.INDEX, 0);

        manager = (SensorManager) getSystemService(SENSOR_SERVICE);

        title = findViewById(R.id.txtTitle);
        valor1 = findViewById(R.id.txtValue1);
        valor2 = findViewById(R.id.txtValue2);
        valor3 = findViewById(R.id.txtValue3);

        image = findViewById(R.id.image);
        back = findViewById(R.id.btn_back);
        eventos = findViewById(R.id.btn_event);

        timeOld = System.currentTimeMillis();

        changeTitleAndImage(sensorType);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SensorActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        eventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SensorActivity.this, EventActivity.class);
                i.putExtra("shared", shared);
                startActivity(i);
            }
        });
    }

    private void changeTitleAndImage(int sensorType) {
        switch (sensorType) {
            case Sensor.TYPE_LIGHT:
                image.setBackgroundResource(R.drawable.luz);
                title.setText("SENSOR DE LUZ");
                break;
            case Sensor.TYPE_GYROSCOPE:
                image.setBackgroundResource(R.drawable.giroscopo);
                title.setText("GIROSCOPO");
                break;
            case Sensor.TYPE_PROXIMITY:
                image.setBackgroundResource(R.drawable.proximidad);
                title.setText("PROXIMIDAD");
                break;
            case Sensor.TYPE_ACCELEROMETER:
                image.setBackgroundResource(R.drawable.acelerometro);
                title.setText("ACELEROMETRO");
                break;
        }
    }

    @Override
    public void onBackPressed() { }

    @SuppressLint("NewApi")
    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {
            switch (sensorType){
                case Sensor.TYPE_LIGHT:
                    sensorLuz(event.values[0]);
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    sensorGiroscopo(event.values[0], event.values[1], event.values[2]);
                    break;
                case Sensor.TYPE_PROXIMITY:
                    sensorProximidad(event.values[0]);
                    break;
                case Sensor.TYPE_ACCELEROMETER:
                    sensorAcelerometro(event.values[0], event.values[1], event.values[2]);
                    break;
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void sensorAcelerometro(float value, float value1, float value2) {
        timeNow = System.currentTimeMillis();
        valor1.setText("X: " + value);
        valor2.setText("Y: " + value1);
        valor3.setText("Z: " + value2);
        if(timeNow - timeOld > Constantes.MILLIS) {
            timeOld = System.currentTimeMillis();
            saveInSharedPreferences("X: " + value + " Y: " + value1 + " Z: " + value2);
        }
    }

    @SuppressLint("SetTextI18n")
    private void sensorProximidad(float value) {
        timeNow = System.currentTimeMillis();
        valor1.setText("X: " + value);
        if(timeNow - timeOld > Constantes.MILLIS && value > 0) {
            timeOld = System.currentTimeMillis();
            saveInSharedPreferences("X: " + value);
        }
    }

    @SuppressLint("SetTextI18n")
    private void sensorGiroscopo(float value, float value1, float value2) {
        timeNow = System.currentTimeMillis();
        valor1.setText("X: " + value);
        valor2.setText("Y: " + value1);
        valor3.setText("Z: " + value2);
        if(timeNow - timeOld > Constantes.MILLIS) {
            timeOld = System.currentTimeMillis();
            saveInSharedPreferences("X: " + value + " Y: " + value1 + " Z: " + value2);
        }
    }

    @SuppressLint("SetTextI18n")
    private void sensorLuz(float value) {
        timeNow = System.currentTimeMillis();
        valor1.setText("X: " + value);
        if(timeNow - timeOld > Constantes.MILLIS && value > 0) {
            timeOld = System.currentTimeMillis();
            saveInSharedPreferences("X: " + value);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    @Override
    protected void onPause() {
        super.onPause();
        stopSensors();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startSensors();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startSensors();
    }

    private void stopSensors() {
        manager.unregisterListener(this, manager.getDefaultSensor(Sensor.TYPE_LIGHT));
        manager.unregisterListener(this, manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE));
        manager.unregisterListener(this, manager.getDefaultSensor(Sensor.TYPE_PROXIMITY));
        manager.unregisterListener(this, manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }

    private void startSensors() {
        manager.registerListener(this, manager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_GAME);
        manager.registerListener(this, manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_GAME);
        manager.registerListener(this, manager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_GAME);
        manager.registerListener(this, manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }

    private void saveInSharedPreferences(String newSharedData) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(String.valueOf(index++), newSharedData);
        editor.putInt(Constantes.INDEX, index);
        editor.apply();
    }
}