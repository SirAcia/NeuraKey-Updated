package com.example.neurakey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class STA extends AppCompatActivity {
    private TextView txtSTAPromptA;
    private TextView txtSTAPromptQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sta);

        txtSTAPromptA = findViewById(R.id.txtSTAPromptA);
        txtSTAPromptQ = findViewById(R.id.txtSTAPromptQ);

    }

}
