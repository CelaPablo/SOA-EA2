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

    private Button back;
    private ListView listView;

    private int index, indice = 0;

    private String shared;
    private SharedPreferences preferences;

    private ArrayList<String> lista = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        back = findViewById(R.id.btn_back);
        listView = findViewById(R.id.event_list);

        Intent intent = getIntent();
        shared = intent.getStringExtra("shared");
        preferences = getSharedPreferences(shared, MODE_PRIVATE);
        index = preferences.getInt(Constantes.INDEX, 0);

        for(indice=0; indice<=index; indice++){
            String str = preferences.getString(indice + "", "");
            lista.add(str);
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.events_list, lista);
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

    @Override
    public void onBackPressed() { }
}