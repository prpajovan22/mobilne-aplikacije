package com.ftn.slagalica;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AsocijacijeActivity extends AppCompatActivity {

    private TextView countdownText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asocijacije);

        Timer timer = new Timer();
        timer.startTimer();

        countdownText = findViewById(R.id.countdownText);
        timer.getTime();

    }

}
