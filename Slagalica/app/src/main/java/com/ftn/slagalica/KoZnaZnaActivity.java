package com.ftn.slagalica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class KoZnaZnaActivity extends AppCompatActivity {

    /*
    private KoZnaZna koZnaZna;
    private ArrayList<Question> questions;
    private int currentQuestion = 0;
    private Handler handler = new Handler();
    */

    private TextView countdownText;
    private Button countdownButton;

    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds = 10000; // 10 sec
    private boolean timeRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ko_zna_zna);
        Button button1 = findViewById(R.id.odustani);

        Timer timer = new Timer();
        timer.startTimer();

        countdownText = findViewById(R.id.countdownText);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(KoZnaZnaActivity.this, MainActivity.class);

                startActivity(intent);
            }
        });
    }
}
