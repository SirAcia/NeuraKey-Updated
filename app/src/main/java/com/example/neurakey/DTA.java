package com.example.neurakey;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class DTA extends AppCompatActivity {

    private TextView txtDTAPromptQ;
    private EditText etxtDTAPromptA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dta);

        etxtDTAPromptA = findViewById(R.id.etxtDTAPromptA);
        txtDTAPromptQ = findViewById(R.id.txtDTAPromptQ);

    }
}