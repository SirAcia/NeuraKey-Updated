package com.example.neurakey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class STA extends AppCompatActivity {

    private TextView txtSTAPromptQ;
    private EditText etxtSTAPromptA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sta);

        etxtSTAPromptA = findViewById(R.id.etxtSTAPromptA);
        txtSTAPromptQ = findViewById(R.id.txtSTAPromptQ);

    }

}
