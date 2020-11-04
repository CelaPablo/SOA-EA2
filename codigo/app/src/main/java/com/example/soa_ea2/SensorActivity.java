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

import com.example.soa_ea2.services.RefreshToken;

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

    /*
    Funcion para cambiar el titulo y la imagen que se muestra en la Activity,
    dependiendo del sensor seleccionado
    */
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
            if(event.values.length == 3)
                sensorAcelerometroGiroscopo(event.values[0], event.values[1] , event.values[2]);
            else
                sensorProximidadLuz(event.values[0]);
        }
    }

    /*
    Funcion para mostar y guardar datos de los sensores de acelerometro y giroscopo.
    Los valores se guardan cada medio segundo.
    */
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

    /*
    Funcion para mostar y guardar datos de los sensores de proximidad y de luz.
    Los valores se guardan cada medio segundo, si estos son distintos de 0.
    */
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
        stopSensor();
        RefreshToken.getInstance().doStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startSensor();
        if(!RefreshToken.getInstance().isRunning())
            RefreshToken.getInstance().start();
    }

    private void stopSensor() {
        manager.unregisterListener(this, manager.getDefaultSensor(sensorType));
    }

    private void startSensor() {
        manager.registerListener(this, manager.getDefaultSensor(sensorType), SensorManager.SENSOR_DELAY_NORMAL);
    }

    /* Funcion para guardar datos en SharedPreferences */
    private void saveInSharedPreferences(String newSharedData) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(String.valueOf(index++), newSharedData);
        editor.putInt(Constantes.INDEX, index);
        editor.apply();
    }
}