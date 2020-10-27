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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    private String shared;
    private int sensorType, index;

    private ImageView image;
    private SensorManager manager;
    private TextView title,valor1, valor2, valor3;

    private SharedPreferences preferences;

    private long timeNow, timeOld;

    private String valueOne = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        timeOld = System.currentTimeMillis();

        Intent intent = getIntent();
        shared = intent.getStringExtra("shared");
        sensorType = intent.getIntExtra("type", 1);
        manager = (SensorManager) getSystemService(SENSOR_SERVICE);

        preferences = getSharedPreferences(shared, MODE_PRIVATE);
        index = preferences.getInt(Constantes.INDEX, 0);

        title = findViewById(R.id.txtTitle);
        valor1 = findViewById(R.id.txtValue1);
        valor2 = findViewById(R.id.txtValue2);
        valor3 = findViewById(R.id.txtValue3);

        image = findViewById(R.id.image);
        Button back = findViewById(R.id.btn_back);
        Button events = findViewById(R.id.btn_event);

        changeTitleAndImage(sensorType);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SensorActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        events.setOnClickListener(new View.OnClickListener() {
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
                title.setText(R.string.txt_luz);
                break;
            case Sensor.TYPE_GYROSCOPE:
                image.setBackgroundResource(R.drawable.giroscopo);
                title.setText(R.string.txt_giroscopo);
                break;
            case Sensor.TYPE_PROXIMITY:
                image.setBackgroundResource(R.drawable.proximidad);
                title.setText(R.string.txt_proximidad);
                break;
            case Sensor.TYPE_ACCELEROMETER:
                image.setBackgroundResource(R.drawable.acelerometro);
                title.setText(R.string.txt_acelerometro);
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
                case Sensor.TYPE_PROXIMITY:
                    sensorProximidadLuz(event.values[0]);
                    break;
                case Sensor.TYPE_GYROSCOPE:
                case Sensor.TYPE_ACCELEROMETER:
                    if(event.values.length == 3) {
                        sensorAcelerometroGiroscopo(event.values[0], event.values[1] , event.values[2]);
                    } else {
                        sensorAcelerometroGiroscopo(event.values[0], event.values[0] , event.values[0]);
                    }
                    break;
            }
        }
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void sensorAcelerometroGiroscopo(float value, float value1, float value2) {
        timeNow = System.currentTimeMillis();
        valueOne = " X: " + String.format("%.3f", value);
        String valueTwo = " Y: " + String.format("%.3f", value1);
        String valueThree = " Z: " + String.format("%.3f", value2);
        String valueToSave = valueOne + valueTwo + valueThree;

        if(timeNow - timeOld > Constantes.MILLIS) {
            valor1.setText(valueOne);
            valor2.setText(valueTwo);
            valor3.setText(valueThree);
            timeOld = System.currentTimeMillis();
            saveInSharedPreferences(valueToSave);
        }
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void sensorProximidadLuz(float value) {
        timeNow = System.currentTimeMillis();
        valueOne = " X: " + String.format("%.3f", value);

        if(timeNow - timeOld > Constantes.MILLIS && value != 0) {
            valor1.setText(valueOne);
            saveInSharedPreferences(valueOne);
            timeOld = System.currentTimeMillis();
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
        manager.registerListener(this, manager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);
        manager.registerListener(this, manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL);
        manager.registerListener(this, manager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);
        manager.registerListener(this, manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void saveInSharedPreferences(String newSharedData) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(String.valueOf(index++), newSharedData);
        editor.putInt(Constantes.INDEX, index);
        editor.apply();
    }
}