package com.example.neurakey;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.text.TextWatcher;
import android.text.Editable;
import java.util.HashMap;
import java.util.Map;

public class STA extends AppCompatActivity {
    private TextView txtSTAPromptQ;
    private EditText etxtSTAPromptA;
    private Map<Character, Long> keyDownTimes = new HashMap<Character, Long>();
    private Button btnSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sta);

        etxtSTAPromptA = findViewById(R.id.etxtSTAPromptA);
        etxtSTAPromptA.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                System.out.println("Before");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("On");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() > 0 && s.charAt(s.length() - 1) == ' ') {
                    System.out.println("After");   //dp something
                }
            }
        });

        etxtSTAPromptA.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                char keyChar = (char) event.getUnicodeChar();
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    long keyDownTime = System.currentTimeMillis();
                    keyDownTimes.put(keyChar, keyDownTime);
                } else if (event.getAction() == KeyEvent.ACTION_UP) {
                    long keyUpTime = System.currentTimeMillis();
                    long duration = keyUpTime - keyDownTimes.get(keyChar);
                    System.out.println("Key down duration for " + keyChar + ": " + duration + " ms");
                    keyDownTimes.remove(keyChar);
                }
                return false;
            }
        });

        txtSTAPromptQ = findViewById(R.id.txtSTAPromptQ);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
    }

    public class subclass {
        Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnSubmit) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new STAResults()).commit();
                btnSubmit.setVisibility(View.GONE);
            }
        }
    }
}
