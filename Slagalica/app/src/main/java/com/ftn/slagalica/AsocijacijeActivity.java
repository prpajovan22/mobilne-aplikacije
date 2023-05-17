package com.ftn.slagalica;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AsocijacijeActivity extends AppCompatActivity {

    private TextView countdownText;
    private Button countdownButton;

    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds = 10000; // 10 sec
    private boolean timeRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asocijacije);

        startStop();

        countdownText = findViewById(R.id.countdownText);

        /*countdownButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startStop();
            }

        });*/
    }

    public void startStop(){
        if (timeRunning){
            stopTimer();
        }else{
            startTimer();
        }
    }
    public void startTimer(){
        countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMilliseconds =l;
                updateTimer();
            }

            @Override
            public void onFinish() {

            }
        }.start();
        timeRunning = true;
    }

    public void stopTimer(){
        countDownTimer.cancel();
        timeRunning = false;
    }
    public void updateTimer(){
        int minutes = (int) timeLeftInMilliseconds / 60000;
        int seconds = (int) timeLeftInMilliseconds % 60000 / 1000;

        String timeLeftText;

        timeLeftText = "" + minutes;
        timeLeftText += ":";

        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;
        countdownText.setText(timeLeftText);

    }

}
