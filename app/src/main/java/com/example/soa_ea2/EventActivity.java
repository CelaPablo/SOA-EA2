package com.example.soa_ea2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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

    private void completeList(Intent intent) {
        String shared = intent.getStringExtra("shared");
        SharedPreferences preferences = getSharedPreferences(shared, MODE_PRIVATE);
        int index = preferences.getInt(Constantes.INDEX, 0);

        int indice;
        for(indice = 0; indice <= index; indice++){
            String str = preferences.getString(indice + "", "");
            list.add(str);
        }
    }

    @Override
    public void onBackPressed() { }
}