package com.ftn.slagalica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import firebase_models.GameFirebaseModel;
import models.Pitanje;
import models.Spojnice;
import utils.Constants;

public class SpojniceActivity extends AppCompatActivity {

    private List<Spojnice> spojnice = new ArrayList<>();
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable countdownRunnable;
    private int countdownTime = 30;
    private int currentRound = 0;
    private TextView countdownText1;
    private String selectedButton;

    private CountDownTimer countDownTimer;

    private boolean switchedToAnotherActivity = false;

    private FirebaseAuth mAuth;
    private String player1UserId,player2UserId,gameId;
    private Button leftSide1, leftSide2, leftSide3, leftSide4,leftSide5, rightSide1, rightSide2, rightSide3, rightSide4
            ,rightSide5;

    private boolean isMyTurn = false;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spojnice);

        countdownText1.findViewById(R.id.countdownTextSpojnice);

        leftSide1 = findViewById(R.id.A1);
        leftSide2 = findViewById(R.id.A2);
        leftSide3 = findViewById(R.id.B1);
        leftSide4 = findViewById(R.id.B2);
        leftSide5 = findViewById(R.id.B3);

        rightSide1 = findViewById(R.id.C1);
        rightSide2 = findViewById(R.id.C2);
        rightSide3 = findViewById(R.id.D1);
        rightSide4 = findViewById(R.id.D2);
        rightSide5 = findViewById(R.id.C3);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        player1UserId = mAuth.getCurrentUser().getUid();
        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        player2UserId = preferences.getString(Constants.OPONENT_ID, "");
        isMyTurn = preferences.getBoolean(Constants.SHARED_PREFERENCES_IS_PLAYER_1, false);
        gameId = preferences.getString(Constants.SHARED_PREFERENCES_GAME_ID, "");

        startCountdown();


        firestore.collection(Constants.SPOJNICE_COLLECTION).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                spojnice = task.getResult().toObjects(Spojnice.class);
                Collections.shuffle(spojnice);
                
                Spojnice spojnica = spojnice.get(0);
                Collections.shuffle(spojnica.getLeftColumn());
                leftSide1.setText(spojnica.getLeftColumn().get(0));
                leftSide2.setText(spojnica.getLeftColumn().get(1));
                leftSide3.setText(spojnica.getLeftColumn().get(2));
                leftSide4.setText(spojnica.getLeftColumn().get(3));
                leftSide5.setText(spojnica.getLeftColumn().get(4));

                Collections.shuffle(spojnica.getRightColumn());
                rightSide1.setText(spojnica.getRightColumn().get(0));
                rightSide2.setText(spojnica.getRightColumn().get(1));
                rightSide3.setText(spojnica.getRightColumn().get(2));
                rightSide4.setText(spojnica.getRightColumn().get(3));
                rightSide5.setText(spojnica.getRightColumn().get(4));
            }
        });

        leftSide1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedButton == null){
                    leftSide1.setBackgroundColor(Color.GREEN);
                    selectedButton = leftSide1.getText().toString();
                    leftSide1.setEnabled(false);
                }
            }
        });

        leftSide2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedButton == null){
                    leftSide2.setBackgroundColor(Color.GREEN);
                    selectedButton = leftSide2.getText().toString();
                    leftSide1.setEnabled(false);
                }
            }
        });

        leftSide3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftSide3.setBackgroundColor(Color.GREEN);
                selectedButton = leftSide3.getText().toString();
                leftSide1.setEnabled(false);
            }
        });

        leftSide4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftSide4.setBackgroundColor(Color.GREEN);
                selectedButton = leftSide4.getText().toString();
                leftSide1.setEnabled(false);
            }
        });

        leftSide5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftSide5.setBackgroundColor(Color.GREEN);
                selectedButton = leftSide5.getText().toString();
                leftSide1.setEnabled(false);
            }
        });

        rightSide1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedButton != null) {
                    checkAndHandleMatch(rightSide1);
                }
            }
        });

        rightSide2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedButton != null) {
                    checkAndHandleMatch(rightSide2);
                }
            }
        });

        rightSide3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedButton != null) {
                    checkAndHandleMatch(rightSide3);
                }
            }
        });

        rightSide4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedButton != null) {
                    checkAndHandleMatch(rightSide4);
                }
            }
        });

        rightSide5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedButton != null) {
                    checkAndHandleMatch(rightSide5);
                }
            }
        });
    }
    private void checkAndHandleMatch(Button rightButton) {
        String selectedText = selectedButton;
        Spojnice trenutnaSpojnica = spojnice.get(currentRound);

        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        int currentPlayerPoints = preferences.getInt(Constants.SHARED_PREFERENCES_GAME_ID, 0);

        if (rightButton.getText().toString().equals(trenutnaSpojnica.getAnswers().get(selectedText))) {
            rightButton.setBackgroundColor(Color.GREEN);
            selectedButton = null;
            rightButton.setEnabled(false);
            currentPlayerPoints += 10;
        } else {
            selectedButton = null;
            currentPlayerPoints -= 10;
        }

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(Constants.SHARED_PREFERENCES_GAME_ID, currentPlayerPoints);
        editor.apply();

        String gameId = Constants.SHARED_PREFERENCES_GAME_ID;
        DatabaseReference gameRef = FirebaseDatabase.getInstance().getReference("games").child(gameId);
        int finalCurrentPlayerPoints = currentPlayerPoints;
        gameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    GameFirebaseModel game = snapshot.getValue(GameFirebaseModel.class);

                    if (game != null) {
                        if (player2UserId.equals(mAuth.getCurrentUser().getUid())) {
                            game.setBodovi2(finalCurrentPlayerPoints);
                        } else {
                            game.setBodovi1(finalCurrentPlayerPoints);
                        }

                        DatabaseReference gameUpdateRef = FirebaseDatabase.getInstance().getReference("games").child(gameId);
                        Map<String, Object> updateValues = new HashMap<>();
                        updateValues.put("bodovi1", game.getBodovi1());
                        updateValues.put("bodovi2", game.getBodovi2());

                        gameUpdateRef.updateChildren(updateValues)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void startCountdown() {
        countDownTimer = new CountDownTimer(2 * 60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdownText1.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                if (!switchedToAnotherActivity) {
                    if (currentRound == 0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                resetTimer();
                                fetchDataAndUpdateUI();
                            }
                        }, 5000);
                    } else if (currentRound == 1) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                switchToAnotherActivity();
                            }
                        }, 5000);
                    } else {
                    }
                } else {
                    resetTimer();
                    switchedToAnotherActivity = false;
                }
            }
        }.start();
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            startCountdown();
        }
    }

    protected void onDestroy () {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    private void switchToAnotherActivity() {
        Intent intent = new Intent(SpojniceActivity.this, MainActivity.class);
        startActivity(intent);
        switchedToAnotherActivity = true;
    }

    private void fetchDataAndUpdateUI() {
        isMyTurn = !isMyTurn;

        currentRound = 1;
        Spojnice spojnica = spojnice.get(1);
        leftSide1.setText(spojnica.getLeftColumn().get(0));
        leftSide1.setEnabled(true);
        leftSide2.setText(spojnica.getLeftColumn().get(1));
        leftSide2.setEnabled(true);
        leftSide3.setText(spojnica.getLeftColumn().get(2));
        leftSide3.setEnabled(true);
        leftSide4.setText(spojnica.getLeftColumn().get(3));
        leftSide4.setEnabled(true);
        leftSide5.setText(spojnica.getLeftColumn().get(4));
        leftSide5.setEnabled(true);

        Collections.shuffle(spojnica.getRightColumn());
        rightSide1.setText(spojnica.getRightColumn().get(0));
        rightSide1.setEnabled(true);
        rightSide2.setText(spojnica.getRightColumn().get(1));
        rightSide2.setEnabled(true);
        rightSide3.setText(spojnica.getRightColumn().get(2));
        rightSide3.setEnabled(true);
        rightSide4.setText(spojnica.getRightColumn().get(3));
        rightSide4.setEnabled(true);
        rightSide5.setText(spojnica.getRightColumn().get(4));
        rightSide5.setEnabled(true);

        startCountdown();

    }
}
