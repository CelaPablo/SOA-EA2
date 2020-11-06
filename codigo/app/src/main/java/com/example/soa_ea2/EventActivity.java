package com.example.soa_ea2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.soa_ea2.services.RefreshToken;

import java.util.ArrayList;

public class EventActivity extends AppCompatActivity {

    private ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Button back = findViewById(R.id.btn_back);
        ListView listView = findViewById(R.id.event_list);

        Intent intent = getIntent();
        completeList(intent);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.events_list, list);
        listView.setAdapter(adapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EventActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }


    /*
    Funcion que lee el archivo de SharedPreferences y
    guarda los valores en una lista para mostrarlos en pantalla
    */
    private void completeList(Intent intent) {
        String shared = intent.getStringExtra("shared");
        SharedPreferences preferences = getSharedPreferences(shared, MODE_PRIVATE);
        int index = preferences.getInt(Constantes.INDEX, 0);

        int indice;
        for(indice = 0; indice < index; indice++){
            String str = preferences.getString(indice + "", "");
            list.add(indice + ")" +str);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        RefreshToken.getInstance().doStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        RefreshToken refresh = RefreshToken.getInstance();
        if(!refresh.isRunning())
            refresh.doResume();
    }

    @Override
    public void onBackPressed() { }
}