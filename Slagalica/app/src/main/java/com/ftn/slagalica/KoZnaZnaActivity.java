package com.ftn.slagalica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.List;

import models.Pitanje;
import utils.Constants;

public class KoZnaZnaActivity extends AppCompatActivity {

    private Integer timeleft = 5;
    private List<Pitanje> visePitanja;
    private Button button1, answer1, answer2, answer3, answer4;
    private TextView countdownTextView, question;
    private static final int TOTAL_REPETITIONS = 2;
    private static final long COUNTDOWN_INTERVAL = 1000; // 5 seconds
    private static final long COUNTDOWN_DURATION = 5000; // 5 seconds
    private boolean isButtonEnabled = true;
    private Handler handlerForSleep = new Handler(Looper.getMainLooper());
    private FirebaseFirestore firestore;
    private int repetitionCount = 0;
    private CountDownTimer countDownTimer;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ko_zna_zna);
        button1 = findViewById(R.id.odustani);
        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);
        question = findViewById(R.id.question);

        countdownTextView = findViewById(R.id.countdownText);

        firestore = FirebaseFirestore.getInstance();

        firestore.collection(Constants.QUESTIONS_COLLECTION).get().addOnCompleteListener(task -> {
           if(task.isSuccessful()){
               List<Pitanje> pitanja = task.getResult().toObjects(Pitanje.class);
               int iterator = 0;
               for(DocumentSnapshot documentSnapshot: task.getResult()){
                   pitanja.get(iterator++).setId(documentSnapshot.getId());
               }
               Collections.shuffle(pitanja);
               visePitanja = pitanja.subList(0,2);
               startTimer();
               Pitanje pitanje = visePitanja.get(repetitionCount);
               question.setText(pitanje.getQuestion());
               answer1.setText(pitanje.getOdgovor1());
               answer2.setText(pitanje.getOdgovor2());
               answer3.setText(pitanje.getOdgovor3());
               answer4.setText(pitanje.getOdgovor4());

               answer1.setOnClickListener(v -> checkAnswer(answer1.getText().toString()));
               answer2.setOnClickListener(v -> checkAnswer(answer2.getText().toString()));
               answer3.setOnClickListener(v -> checkAnswer(answer3.getText().toString()));
               answer4.setOnClickListener(v -> checkAnswer(answer4.getText().toString()));
           }
        });

    }
//
//
//Ovde proveravam resenje
//
    private void checkAnswer(String selectedAnswer){
        countDownTimer.cancel();

        String correctAnswer = visePitanja.get(repetitionCount).getCorrectAnswer();

        if(selectedAnswer.equals(correctAnswer)){
            Button selectedButton = getButtonForAnswerText(selectedAnswer);
            selectedButton.setBackgroundColor(Color.GREEN);
        }else{
            highlightCorrectAndIncorrectAnswers(correctAnswer);
            Button selectedButton = getButtonForAnswerText(selectedAnswer);
            selectedButton.setBackgroundColor(Color.RED);
        }
        // ovde treba da nastavim kviz
    }

    //
    //
    //
    //
    private Button getButtonForAnswerText(String answerText) {
        if (answer1.getText().toString().equals(answerText)) {
            return answer1;
        } else if (answer2.getText().toString().equals(answerText)) {
            return answer2;
        } else if (answer3.getText().toString().equals(answerText)) {
            return answer3;
        } else if (answer4.getText().toString().equals(answerText)) {
            return answer4;
        }
        return null;
    }

    private void highlightCorrectAndIncorrectAnswers(String selectedAnswer) {
        String correctAnswer = visePitanja.get(repetitionCount).getCorrectAnswer();
        Button correctButton = getButtonForAnswerText(correctAnswer);
        Button selectedButton = getButtonForAnswerText(selectedAnswer);

        for (Button button : new Button[]{answer1, answer2, answer3, answer4}) {
            if (button.equals(correctButton)) {
                button.setBackgroundColor(Color.GREEN);
            } else if (!button.equals(selectedButton)) {
                button.setBackgroundColor(Color.RED);
            }
        }
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(COUNTDOWN_DURATION, COUNTDOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdownTextView.setText(String.valueOf(timeleft--));
            }
            @Override
            public void onFinish() {
                repetitionCount++;

                if (repetitionCount < TOTAL_REPETITIONS) {

                    timeleft = 5;
                    answer1.setEnabled(false);
                    answer2.setEnabled(false);
                    answer3.setEnabled(false);
                    answer4.setEnabled(false);
                    handlerForSleep.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Pitanje pitanje = visePitanja.get(repetitionCount);
                            question.setText(pitanje.getQuestion());
                            answer1.setText(pitanje.getOdgovor1());
                            answer2.setText(pitanje.getOdgovor2());
                            answer3.setText(pitanje.getOdgovor3());
                            answer4.setText(pitanje.getOdgovor4());

                            answer1.setOnClickListener(v -> checkAnswer(answer1.getText().toString()));
                            answer2.setOnClickListener(v -> checkAnswer(answer2.getText().toString()));
                            answer3.setOnClickListener(v -> checkAnswer(answer3.getText().toString()));
                            answer4.setOnClickListener(v -> checkAnswer(answer4.getText().toString()));

                            answer1.setEnabled(true);
                            answer2.setEnabled(true);
                            answer3.setEnabled(true);
                            answer4.setEnabled(true);

                            startTimer();
                        }
                    },3000);
                } else {
                    Intent intent = new Intent(KoZnaZnaActivity.this, AsocijacijeActivity.class);
                    startActivity(intent);
                    finish();
                }

            }

        }.start();
    }
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        handler.removeCallbacksAndMessages(null);
    }

}
