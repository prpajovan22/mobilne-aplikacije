package com.ftn.slagalica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import models.Pitanje;
import models.Spojnice;
import utils.Constants;

public class SpojniceActivity extends AppCompatActivity {

    private List<Spojnice> spojnice = new ArrayList<>();
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable countdownRunnable;
    private int countdownTime = 30;
    private int currentRound = 0;
    //private TextView countdownText1;
    private String selectedButton;
    private Button leftSide1, leftSide2, leftSide3, leftSide4,leftSide5, rightSide1, rightSide2, rightSide3, rightSide4
            ,rightSide5;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spojnice);

        //countdownText1.findViewById(R.id.countdownTextSpojnice);

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

        firestore = FirebaseFirestore.getInstance();

        //startCountdown();

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
                }
            }
        });

        leftSide3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftSide3.setBackgroundColor(Color.GREEN);
                selectedButton = leftSide3.getText().toString();
            }
        });

        leftSide4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftSide4.setBackgroundColor(Color.GREEN);
                selectedButton = leftSide4.getText().toString();
            }
        });

        leftSide5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftSide5.setBackgroundColor(Color.GREEN);
                selectedButton = leftSide5.getText().toString();
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

        if (rightButton.getText().toString().equals(trenutnaSpojnica.getAnswers().get(selectedText))){

            rightButton.setBackgroundColor(Color.GREEN);
            selectedButton = null;
            rightButton.setEnabled(false);
            //String leftButtonId = selectedText;

        } else {
            selectedButton = null;
            //String leftButtonId = selectedText;
        }
    }

    /*private void startCountdown() {
        countdownRunnable = new Runnable() {
            @Override
            public void run() {
                if (countdownTime > 0) {
                    String timeString = String.format("%02d:%02d", countdownTime / 60, countdownTime % 60);
                    countdownText1.setText(timeString);

                    countdownTime--;
                    handler.postDelayed(this, 1000);
                } else {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            countdownTime = 30;
                            startCountdown();
                        }
                    }, 5000);
                }
            }
        };


        handler.postDelayed(countdownRunnable, 1000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SpojniceActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 35000);
    }

    protected void onDestroy () {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }*/
}
