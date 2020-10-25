package com.example.soa_ea2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
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

    private Button back;
    private int sensorType;
    private ImageView image;
    private SensorManager manager;
    private TextView title,valor1, valor2, valor3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        Intent intent = getIntent();
        sensorType = intent.getIntExtra("type", 1);

        manager = (SensorManager) getSystemService(SENSOR_SERVICE);

        title = findViewById(R.id.txtTitle);
        valor1 = findViewById(R.id.txtValue1);
        valor2 = findViewById(R.id.txtValue2);
        valor3 = findViewById(R.id.txtValue3);

        image = findViewById(R.id.image);
        back = findViewById(R.id.btn_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SensorActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
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
        image.setBackgroundResource(R.drawable.acelerometro);
        title.setText("ACELEROMETRO");
        valor1.setText("X :" + value);
        valor2.setText("Y :" + value1);
        valor3.setText("Z :" + value2);
        timeout();
    }

    @SuppressLint("SetTextI18n")
    private void sensorProximidad(float value) {
        image.setBackgroundResource(R.drawable.proximidad);
        title.setText("PROXIMIDAD");
        valor1.setText("X :" + value);
        timeout();
    }

    @SuppressLint("SetTextI18n")
    private void sensorGiroscopo(float value, float value1, float value2) {
        image.setBackgroundResource(R.drawable.giroscopo);
        title.setText("GIROSCOPO");
        valor1.setText("X :" + value);
        valor2.setText("Y :" + value1);
        valor3.setText("Z :" + value2);
        timeout();
    }

    @SuppressLint("SetTextI18n")
    private void sensorLuz(float value) {
        image.setBackgroundResource(R.drawable.luz);
        title.setText("SENSOR DE LUZ");
        valor1.setText("X :" + value);
        timeout();
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

    private void timeout() {
        new CountDownTimer(500, 1000) {
            public void onTick(long millisUntilFinished) { }
            public void onFinish() {}
        }.start();
    }
}