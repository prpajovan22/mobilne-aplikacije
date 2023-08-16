package com.ftn.slagalica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.List;

import models.Pitanje;
import models.Spojnice;
import utils.Constants;

public class SpojniceActivity extends AppCompatActivity {


    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable countdownRunnable;
    private int countdownTime = 30;
    //private TextView countdownText1;
    private String selectedButton;
    private Button leftSide1, leftSide2, leftSide3, leftSide4, rightSide1, rightSide2, rightSide3, rightSide4;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spojnice);

        //countdownText1.findViewById(R.id.countdownTextSpojnice  );

        leftSide1 = findViewById(R.id.A1);
        leftSide2 = findViewById(R.id.A2);
        leftSide3 = findViewById(R.id.B1);
        leftSide4 = findViewById(R.id.B2);

        rightSide1 = findViewById(R.id.C1);
        rightSide2 = findViewById(R.id.C2);
        rightSide3 = findViewById(R.id.D1);
        rightSide4 = findViewById(R.id.D2);

        firestore = FirebaseFirestore.getInstance();

        //startCountdown();

        firestore.collection(Constants.SPOJNICE_COLLECTION).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Spojnice> pitanja = task.getResult().toObjects(Spojnice.class);
                Collections.shuffle(pitanja);

                Spojnice spojnice = pitanja.get(0);
                Collections.shuffle(spojnice.getLeftColumn());
                leftSide1.setText(spojnice.getLeftColumn().get(0));
                leftSide2.setText(spojnice.getLeftColumn().get(1));
                leftSide3.setText(spojnice.getLeftColumn().get(2));
                leftSide4.setText(spojnice.getLeftColumn().get(3));

                Collections.shuffle(spojnice.getRightColumn());
                rightSide1.setText(spojnice.getRightColumn().get(0));
                rightSide2.setText(spojnice.getRightColumn().get(1));
                rightSide3.setText(spojnice.getRightColumn().get(2));
                rightSide4.setText(spojnice.getRightColumn().get(3));
            }
        });
        leftSide1.setOnClickListener(a -> {
            if (selectedButton != null) {
                leftSide1.setBackgroundColor(Color.RED);
                selectedButton = leftSide1.getText().toString();
            }
        });

        leftSide2.setOnClickListener(a -> {
            if (selectedButton != null) {
                leftSide2.setBackgroundColor(Color.RED);
                selectedButton = leftSide2.getText().toString();
            }
        });

        leftSide3.setOnClickListener(a -> {
            if (selectedButton != null) {
                leftSide3.setBackgroundColor(Color.RED);
                selectedButton = leftSide3.getText().toString();
            }
        });

        leftSide4.setOnClickListener(a -> {
            if (selectedButton != null) {
                leftSide4.setBackgroundColor(Color.RED);
                selectedButton = leftSide4.getText().toString();
            }
        });

        rightSide1.setOnClickListener(a -> {
            if (selectedButton != null) {
            }
        });

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
