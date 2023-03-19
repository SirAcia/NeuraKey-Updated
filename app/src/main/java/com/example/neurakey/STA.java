package com.example.neurakey;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.text.TextWatcher;
import android.text.Editable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;


public class STA extends AppCompatActivity {
    private TextView txtSTAPromptQ;
    private EditText etxtSTAPromptA;
    private Map<Character, Long> keyDownTimes = new HashMap<Character, Long>();
    private TextView txtResultsLinguistic;
    private Button btnSubmit;
    private long totalHoldTime = 0;
    private int numKeyPresses = 0;
    private long startTime = 0;

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
                    if (startTime == 0) {
                        startTime = keyDownTime;
                    }
                } else if (event.getAction() == KeyEvent.ACTION_UP) {
                    long keyUpTime = System.currentTimeMillis();
                    long duration = keyUpTime - keyDownTimes.get(keyChar);
                    System.out.println("Key down duration for " + keyChar + ": " + duration + " ms");
                    totalHoldTime += duration;
                    numKeyPresses++;
                    keyDownTimes.remove(keyChar);
                }
                return false;
            }
        });

        txtSTAPromptQ = findViewById(R.id.txtSTAPromptQ);
        txtResultsLinguistic = findViewById(R.id.txtResultsLinguistic);

        txtSTAPromptQ = findViewById(R.id.txtSTAPromptQ);
        btnSubmit = findViewById(R.id.btnSubmit);
        EditText mEditText;
        mEditText = findViewById(R.id.etxtSTAPromptA);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            private static final String FILE_NAME = "UserInput.txt";

            @Override
            public void onClick(View v) {
                String text = mEditText.getText().toString();
                FileOutputStream fos = null;

                try {
                    fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                    fos.write(text.getBytes());

                    mEditText.getText().clear();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                if (v.getId() == R.id.btnSubmit) {
                    double avgHoldTime = totalHoldTime / (double) numKeyPresses;
                    long totalTime = System.currentTimeMillis() - startTime;
                    System.out.println("Average hold time: " + avgHoldTime + " ms"); //HOLD TIME OUTPUT
                    System.out.println("Total time: " + totalTime + " ms"); //TOTAL TIME OUTPUT
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.flContainer, new STAResults());
                    transaction.commit();
                    btnSubmit.setVisibility(View.GONE);
                    txtSTAPromptQ.setVisibility(View.GONE);
                    etxtSTAPromptA.setVisibility(View.GONE);
                    startTime = 0;
                }
            }
        });
    }

    public class linguisticAnalysis {
        private String readFromFile(Context context) {

            String ret = "";

            try {
                InputStream inputStream = context.openFileInput("UserInput.txt");

                if (inputStream != null) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString = "";
                    StringBuilder stringBuilder = new StringBuilder();

                    while ((receiveString = bufferedReader.readLine()) != null) {
                        stringBuilder.append("\n").append(receiveString);
                    }

                    inputStream.close();
                    ret = stringBuilder.toString();
                }
            } catch (FileNotFoundException e) {
                Log.e("login activity", "File not found: " + e.toString());
            } catch (IOException e) {
                Log.e("login activity", "Can not read file: " + e.toString());
            }
            return ret;
        }

    }

    public class LingAnalysis {

        private static final double DOUBLE0_5 = 0.5;
        private static final double DOUBLE4_71 = 4.71;
        private static final double DOUBLE21_43 = 21.43;

        public String readFileToString(String fName) throws IOException {
            return new String(Files.readAllBytes(Paths.get(fName)));
        }

        public void main(String[] args) {
            try {
                String text = readFileToString(args[0]);
                int scoreFloorPrecision = 2;  // args[1]

                int sentences = text.split("[?.!]+\\s*").length;
                int words = text.split("\\s*(?<!\\d)[-?.!,:;\\s]+|[-?.!,:;\\s]+(?!\\d)\\s*").length;
                int characters = text.replace("\\s", "").length();

                double score = DOUBLE4_71 * characters / words + DOUBLE0_5 * words / sentences - DOUBLE21_43;


            } catch (IOException err) {
                System.out.println(err.getMessage());
            }
        }

        public double truncate(double n, int decimals) {
            int whole = (int) (n * Math.pow(10, decimals));
            return whole / Math.pow(10, decimals);
        }
    }
}
