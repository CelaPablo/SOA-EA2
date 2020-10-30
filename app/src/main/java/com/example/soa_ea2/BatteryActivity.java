package com.example.soa_ea2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

public class BatteryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, ifilter);

        assert batteryStatus != null;
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int batteryPct = (int) (level * 100 / (float)scale);

        TextView battery = findViewById(R.id.battery_text);
        String pct = "Bateria " + batteryPct + " %";

        battery.setText(pct);
        nextView();
    }

    @Override
    public void onBackPressed() { }

    private void nextView() {
        new CountDownTimer(2500, 1000) {

            public void onTick(long millisUntilFinished) { }

            public void onFinish() {
                Intent intent = new Intent(BatteryActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }
}