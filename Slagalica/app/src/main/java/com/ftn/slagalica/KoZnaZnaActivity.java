package com.ftn.slagalica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import firebase_models.KoZnaZnaAnswerModel;
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
    private int pointsPlayer = 0;
    private CountDownTimer countDownTimer;
    private Handler handler = new Handler();

    private DatabaseReference realTimeDatabase;

    private FirebaseAuth mAuth;


    private String player1UserId ;
    private String player2UserId,gameId ;

    private List<KoZnaZnaAnswerModel> answers = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ko_zna_zna);
        realTimeDatabase = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        player1UserId = mAuth.getCurrentUser().getUid();

        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        player2UserId = preferences.getString(Constants.OPONENT_ID,"");

        gameId = preferences.getString(Constants.SHARED_PREFERENCES_GAME_ID,"");

        button1 = findViewById(R.id.propustiKoZnaZna);
        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);
        question = findViewById(R.id.question);

        answer1.setBackgroundColor(Color.GRAY);
        answer2.setBackgroundColor(Color.GRAY);
        answer3.setBackgroundColor(Color.GRAY);
        answer4.setBackgroundColor(Color.GRAY);


        answer1.setOnClickListener(v -> checkAnswer(answer1.getText().toString()));
        answer2.setOnClickListener(v -> checkAnswer(answer2.getText().toString()));
        answer3.setOnClickListener(v -> checkAnswer(answer3.getText().toString()));
        answer4.setOnClickListener(v -> checkAnswer(answer4.getText().toString()));

        button1.setOnClickListener(b -> {
            answer1.setEnabled(false);
            answer2.setEnabled(false);
            answer3.setEnabled(false);
            answer4.setEnabled(false);
        });

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
           }
           realTimeDatabase.child(Constants.GAME_COLLECTION)
                    .child(gameId).child(Constants.SHARED_PREFERENCES_KOZNAZNA_ID)
                    .child(String.valueOf(repetitionCount)).addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                            answers.add(snapshot.getValue(KoZnaZnaAnswerModel.class));
                            if(answers.size() == 2){
                                Long player1Timestamp;
                                Long player2Timestamp;
                                if(snapshot.getKey().equals(mAuth.getCurrentUser().getUid())){
                                    player1Timestamp = Long.valueOf(answers.get(1).getTimeStamp());
                                    player2Timestamp = Long.valueOf(answers.get(0).getTimeStamp());

                                }else{
                                    player1Timestamp = Long.valueOf(answers.get(0).getTimeStamp());
                                    player2Timestamp = Long.valueOf(answers.get(1).getTimeStamp());
                                }

                                if (player1Timestamp < player2Timestamp) {
                                    assignPoints(gameId, player1UserId, 10);
                                    assignPoints(gameId, player2UserId, 5);
                                } else if (player1Timestamp > player2Timestamp) {
                                    assignPoints(gameId, player2UserId, 10);
                                    assignPoints(gameId, player1UserId, 5);
                                } else{
                                    assignPoints(gameId, player2UserId, 5);
                                    assignPoints(gameId, player1UserId, 5);
                                }
                                realTimeDatabase.removeEventListener(this);
                            }
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }
                   });
        });

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

                            answer1.setBackgroundColor(Color.GRAY);
                            answer2.setBackgroundColor(Color.GRAY);
                            answer3.setBackgroundColor(Color.GRAY);
                            answer4.setBackgroundColor(Color.GRAY);

                            answer1.setEnabled(true);
                            answer2.setEnabled(true);
                            answer3.setEnabled(true);
                            answer4.setEnabled(true);

                            startTimer();
                        }
                    },1000);
                } else {
                    Intent intent = new Intent(KoZnaZnaActivity.this, MainActivity.class);
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

//
//
//
    private void checkAnswer(String selectedAnswer){

        boolean isCorrectAnswer = false;

        String correctAnswer = visePitanja.get(repetitionCount).getCorrectAnswer();

        if(selectedAnswer.equals(correctAnswer)){
            Button selectedButton = getButtonForAnswerText(selectedAnswer);
            selectedButton.setBackgroundColor(Color.GREEN);
            isCorrectAnswer = true;
        }else{
            highlightCorrectAndIncorrectAnswers(correctAnswer);
            Button selectedButton = getButtonForAnswerText(selectedAnswer);
            selectedButton.setBackgroundColor(Color.RED);
        }

        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        String userId = preferences.getString(Constants.SHARED_PREFERENCES_USER_ID,"");
        String gameId = preferences.getString(Constants.SHARED_PREFERENCES_GAME_ID,"");
        String koId = preferences.getString(Constants.SHARED_PREFERENCES_KOZNAZNA_ID,"");

        realTimeDatabase.child(Constants.GAME_COLLECTION).child(gameId).child(Constants.SHARED_PREFERENCES_KOZNAZNA_ID)
                .child(String.valueOf(repetitionCount)).child(userId).setValue(new KoZnaZnaAnswerModel(isCorrectAnswer, String.valueOf(Instant.now().toEpochMilli())));

        realTimeDatabase.child(Constants.SHARED_PREFERENCES_KOZNAZNA_ID).child(koId);

        answer1.setEnabled(false);
        answer2.setEnabled(false);
        answer3.setEnabled(false);
        answer4.setEnabled(false);

    }

    private void assignPoints(String gameId, String playerId, int points) {
        DatabaseReference userRef = realTimeDatabase.child(Constants.USER_COLLECTION).child(playerId);
        DatabaseReference pointsRef = userRef.child("bodovi1");

        pointsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer currentPoints = dataSnapshot.getValue(Integer.class);
                if (currentPoints != null) {
                    int newPoints = currentPoints + points;
                    pointsRef.setValue(newPoints);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }


    /*
    private void checkAnswer(String selectedAnswer) {
        boolean isCorrectAnswer = false;

        String correctAnswer = visePitanja.get(repetitionCount).getCorrectAnswer();

        if (selectedAnswer.equals(correctAnswer)) {
            Button selectedButton = getButtonForAnswerText(selectedAnswer);
            selectedButton.setBackgroundColor(Color.GREEN);
            isCorrectAnswer = true;
        } else {
            highlightCorrectAndIncorrectAnswers(correctAnswer);
            Button selectedButton = getButtonForAnswerText(selectedAnswer);
            selectedButton.setBackgroundColor(Color.RED);
        }

        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        String userId = preferences.getString(Constants.SHARED_PREFERENCES_USER_ID, "");
        String gameId = preferences.getString(Constants.SHARED_PREFERENCES_GAME_ID, "");
        String koId = preferences.getString(Constants.SHARED_PREFERENCES_KOZNAZNA_ID, "");

        long timestamp = Instant.now().toEpochMilli();

        realTimeDatabase.child(Constants.GAME_COLLECTION).child(gameId).child(Constants.SHARED_PREFERENCES_KOZNAZNA_ID)
                .child(String.valueOf(repetitionCount)).child(userId).setValue(new KoZnaZnaAnswerModel(isCorrectAnswer, String.valueOf(timestamp)));

        answer1.setEnabled(false);
        answer2.setEnabled(false);
        answer3.setEnabled(false);
        answer4.setEnabled(false);

        if (bothPlayersAnswered(gameId, koId)) {
            long player1Timestamp = getTimestampForPlayer(gameId, koId, player1UserId);
            long player2Timestamp = getTimestampForPlayer(gameId, koId, player2UserId);

            if (player1Timestamp < player2Timestamp) {
                assignPoints(gameId, player1UserId, 5);
                assignPoints(gameId, player2UserId, 3);
            } else if (player1Timestamp > player2Timestamp) {
                assignPoints(gameId, player2UserId, 5);
                assignPoints(gameId, player1UserId, 3);
            }
        }
    }


    private boolean bothPlayersAnswered(String gameId, String koId) {
        DatabaseReference player1Ref = realTimeDatabase.child(Constants.GAME_COLLECTION)
                .child(gameId).child(Constants.SHARED_PREFERENCES_KOZNAZNA_ID)
                .child(String.valueOf(repetitionCount)).child(player1UserId);

        DatabaseReference player2Ref = realTimeDatabase.child(Constants.GAME_COLLECTION)
                .child(gameId).child(Constants.SHARED_PREFERENCES_KOZNAZNA_ID)
                .child(String.valueOf(repetitionCount)).child(player2UserId);

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean player1Answered = dataSnapshot.child(player1UserId).exists();
                boolean player2Answered = dataSnapshot.child(player2UserId).exists();

                if (player1Answered && player2Answered) {
            long player1Timestamp = dataSnapshot.child(player1UserId)
                    .child("timeStamp").getValue(Long.class);
            long player2Timestamp = dataSnapshot.child(player2UserId)
                    .child("timeStamp").getValue(Long.class);

            if (player1Timestamp < player2Timestamp) {
                assignPoints(gameId, player1UserId, 5);
                assignPoints(gameId, player2UserId, 3);
            } else if (player1Timestamp > player2Timestamp) {
                assignPoints(gameId, player2UserId, 5);
                assignPoints(gameId, player1UserId, 3);
            } else {
                assignPoints(gameId, player1UserId, 3);
                assignPoints(gameId, player2UserId, 3);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        };

        DatabaseReference answersRef = realTimeDatabase.child(Constants.GAME_COLLECTION)
                .child(gameId).child(Constants.SHARED_PREFERENCES_KOZNAZNA_ID)
                .child(String.valueOf(repetitionCount));

        answersRef.addListenerForSingleValueEvent(listener);

        return false;
    }

    private long getTimestampForPlayer(String gameId, String koId, String playerId) {
        DatabaseReference timestampRef = realTimeDatabase.child(Constants.GAME_COLLECTION)
                .child(gameId).child(Constants.SHARED_PREFERENCES_KOZNAZNA_ID)
                .child(String.valueOf(repetitionCount)).child(playerId).child("timeStamp");

        return timestampRef.getValue(Long.class);
    }

    private void assignPoints(String gameId, String playerId, int points) {
        DatabaseReference userRef = realTimeDatabase.child(Constants.USER_COLLECTION).child(playerId);
        DatabaseReference pointsRef = userRef.child("bodovi1");

        pointsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer currentPoints = dataSnapshot.getValue(Integer.class);
                if (currentPoints != null) {
                    int newPoints = currentPoints + points;
                    pointsRef.setValue(newPoints);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }
*/
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

}
