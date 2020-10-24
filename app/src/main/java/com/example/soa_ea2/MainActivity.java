package com.example.soa_ea2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button newGame, scores;
    private TextView token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoginActivity.login.finish();
        if(RegisterActivity.active)
            RegisterActivity.register.finish();

        newGame = findViewById(R.id.btn_new_game);
        scores = findViewById(R.id.btn_scores);

        token = findViewById(R.id.superid);

        User user = User.getInstance();

        token.setText(user.getToken());
    }

    @Override
    public void onBackPressed() { }

}