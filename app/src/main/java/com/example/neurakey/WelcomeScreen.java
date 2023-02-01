package com.example.neurakey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WelcomeScreen extends AppCompatActivity {

    private Button btnSTA;
    private Button btnDTA;
    private TextView txtWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcomescreen);

        txtWelcome = findViewById(R.id.txtWelcome);
        btnSTA = findViewById(R.id.btnSTA);
        btnDTA = findViewById(R.id.btnDTA);

        btnSTA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(WelcomeScreen.this,STA.class);
                startActivity(intent);
            }
        });

        btnDTA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(WelcomeScreen.this,DTA.class);
                startActivity(intent);
            }
        });
    }
}