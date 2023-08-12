package com.ftn.slagalica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import models.Asocijacija;
import models.Pitanje;
import utils.Constants;

public class AsocijacijeActivity extends AppCompatActivity {

    private TextView countdownText;
    private Integer currentRound = 0;
    private Integer currentPhaze = 0;
    private CountDownTimer countDownTimer;

    private List<Asocijacija> asocijacije;
    private FirebaseFirestore firestore;
    private DatabaseReference

    private EditText finalAnswerA,finalAnswerB,finalAnswerC,finalAnswerD;

    private Button a1,a2,a3,a4,b1,b2,b3,b4,c1,c2,c3,c4,d1,d2,d3,d4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asocijacije);

        firestore = FirebaseFirestore.getInstance();

        a1 = findViewById(R.id.a1Asocijacija);
        a2 = findViewById(R.id.a2Asocijacija);
        a3 = findViewById(R.id.a3Asocijacija);
        a4 = findViewById(R.id.a4Asocijacija);

        b1 = findViewById(R.id.b1Asocijacija);
        b2 = findViewById(R.id.b2Asocijacija);
        b3 = findViewById(R.id.b3Asocijacija);
        b4 = findViewById(R.id.b4Asocijacija);

        d1 = findViewById(R.id.d1Asocijacija);
        d2 = findViewById(R.id.d2Asocijacija);
        d3 = findViewById(R.id.d3Asocijacija);
        d4 = findViewById(R.id.d4Asocijacija);

        c1 = findViewById(R.id.c1Asocijacija);
        c2 = findViewById(R.id.c2Asocijacija);
        c3 = findViewById(R.id.c3Asocijacija);
        c4 = findViewById(R.id.c4Asocijacija);

        finalAnswerA = findViewById(R.id.aKolonaAsocijacija);
        finalAnswerB = findViewById(R.id.bKolonaAsocijacija);
        finalAnswerC = findViewById(R.id.cKolonaAsocijacija);
        finalAnswerD = findViewById(R.id.dKolonaAsocijacija);

        countdownText = findViewById(R.id.countdownText);

        firestore.collection(Constants.ASOCIATION_COLLECTION).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                asocijacije = task.getResult().toObjects(Asocijacija.class);
                Collections.shuffle(asocijacije);
            }
        });

        a1.setOnClickListener(a -> {
            Asocijacija asocijacija = asocijacije.get(currentRound);
            a1.setText(asocijacija.getA1());
            a1.setEnabled(false);
            resetTimer();
        });

        a2.setOnClickListener(a -> {
            Asocijacija asocijacija = asocijacije.get(currentRound);
            a2.setText(asocijacija.getA2());
            a2.setEnabled(false);
            resetTimer();
        });
        a3.setOnClickListener(a -> {
            Asocijacija asocijacija = asocijacije.get(currentRound);
            a3.setText(asocijacija.getA3());
            a3.setEnabled(false);
            resetTimer();
        });
        a4.setOnClickListener(a -> {
            Asocijacija asocijacija = asocijacije.get(currentRound);
            a4.setText(asocijacija.getA4());
            a4.setEnabled(false);
            resetTimer();
        });
        b1.setOnClickListener(a -> {
            Asocijacija asocijacija = asocijacije.get(currentRound);
            b1.setText(asocijacija.getB1());
            b1.setEnabled(false);
        });

        b2.setOnClickListener(a -> {
            Asocijacija asocijacija = asocijacije.get(currentRound);
            b2.setText(asocijacija.getB2());
            b2.setEnabled(false);
        });
        b3.setOnClickListener(a -> {
            Asocijacija asocijacija = asocijacije.get(currentRound);
            b3.setText(asocijacija.getB3());
            b3.setEnabled(false);
        });
        b4.setOnClickListener(a -> {
            Asocijacija asocijacija = asocijacije.get(currentRound);
            b4.setText(asocijacija.getB4());
            b4.setEnabled(false);
        });

        c1.setOnClickListener(c -> {
            Asocijacija asocijacija = asocijacije.get(currentRound);
            c1.setText(asocijacija.getC1());
            c1.setEnabled(false);
        });

        c2.setOnClickListener(a -> {
            Asocijacija asocijacija = asocijacije.get(currentRound);
            c2.setText(asocijacija.getC2());
            c2.setEnabled(false);
        });
        c3.setOnClickListener(a -> {
            Asocijacija asocijacija = asocijacije.get(currentRound);
            c3.setText(asocijacija.getC3());
            c3.setEnabled(false);
        });
        c4.setOnClickListener(a -> {
            Asocijacija asocijacija = asocijacije.get(currentRound);
            c4.setText(asocijacija.getC4());
            c4.setEnabled(false);
        });

        d1.setOnClickListener(a -> {
            Asocijacija asocijacija = asocijacije.get(currentRound);
            d1.setText(asocijacija.getD1());
            d1.setEnabled(false);
        });

        d2.setOnClickListener(a -> {
            Asocijacija asocijacija = asocijacije.get(currentRound);
            d2.setText(asocijacija.getD2());
            d2.setEnabled(false);
        });
        d3.setOnClickListener(a -> {
            Asocijacija asocijacija = asocijacije.get(currentRound);
            d3.setText(asocijacija.getD3());
            d3.setEnabled(false);
        });
        d4.setOnClickListener(a -> {
            Asocijacija asocijacija = asocijacije.get(currentRound);
            d4.setText(asocijacija.getD4());
            d4.setEnabled(false);
        });

        finalAnswerA.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){
                    Log.e("Ovde je greska","Ovde je stalo");
                    return true;
                    //a3.setText(asocijacija.getSolutionA());
                    //a3.setEnabled(false);
                }
                return false;
            }
        });
        startCountdown();
    }

    private void startCountdown() {
        countDownTimer = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdownText.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                // Timer finished, switch to another activity
                Intent intent = new Intent(AsocijacijeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            startCountdown();
        }
    }

}
