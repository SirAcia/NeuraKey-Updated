package com.example.neurakey;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.text.TextWatcher;
import android.text.Editable;
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
                    System.out.println("Average hold time: " + avgHoldTime + " ms");
                    System.out.println("Total time: " + totalTime + " ms");
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
        public double readabilityScore(String text) {
            if (text == null || text.isEmpty()) {
                return 0;
            }
            double chars = charsCount(text);
            double words = wordsCount(text);
            double sentences = sentencesCount(text);
            double readability = Math.round((4.71 * chars / words + 0.5 * words / sentences - 21.43) * 100.0) / 100.0;
            return Math.max(0.0, readability);   // if readability is less than 0, we return 0
        }

        public double charsCount(String text) {
            if (text == null || text.isEmpty()) {
                return 0L;
            }
            Pattern charPattern = Pattern.compile("\\S");
            Matcher charMatcher = charPattern.matcher(text);

            // check this part of code
            int charFrom = 0;
            int charCount = 0;
            while (charMatcher.find(charFrom)) {
                charCount++;
                charFrom = charMatcher.start() + 1;
            }
            return Math.max(0.0, charCount); // if character count is less than 0, we return 0
        }


        public double wordsCount(String text) {
            if (text == null || text.isEmpty()) {
                return 0L;
            }
            Pattern wordPattern = Pattern.compile("\\S+");
            Matcher wordMatcher = wordPattern.matcher(text);
            // check this part of code
            int wordFrom = 0;
            int wordCount = 0;
            while (wordMatcher.find(wordFrom)) {
                wordCount++;
                wordFrom = wordMatcher.start() + 1;
            }
            return Math.max(0.0, wordCount); // if word count is less than 0, we return 0
        }

        public double sentencesCount(String text) {
            if (text == null || text.isEmpty()) {
                return 0L;
            }
            Pattern sentencePattern = Pattern.compile("[^.?!]+");
            Matcher sentenceMatcher = sentencePattern.matcher(text);
            // check this part of code
            int sentenceFrom = 0;
            int sentenceCount = 0;
            while (sentenceMatcher.find(sentenceFrom)) {
                sentenceCount++;
                sentenceFrom = sentenceMatcher.start() + 1;
            }
            return Math.max(0.0, sentenceCount); // if sentence count is less than 0, we return 0
        }

        public String ageBracket(int readabilityTruncated) {
            int lowerAge = readabilityTruncated + 5;
            int upperAge = readabilityTruncated > 13 ? 22 : readabilityTruncated + 6;
            return String.format("%d-%d", lowerAge, upperAge);
        }

        public void main(String[] args) {
            if (args.length != 1) {
                System.out.println("You must provide a valid path for the file containing your text.");
                return;
            }
            /* try {
                final Paths path = Paths.get(args[0]);
                final var text = Files.readString(path);
                System.out.println("The text is:");
                System.out.println(text);
                System.out.println();
                System.out.println(formattedReadabilityData(text));
            } catch (InvalidPathException ipe) {
                System.out.println("Invalid path");
            } catch (IOException ioe) {
                System.out.println("Cannot read file");
            }  */
        }
    }
}