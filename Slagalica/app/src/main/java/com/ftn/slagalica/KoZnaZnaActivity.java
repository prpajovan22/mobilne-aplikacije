package com.ftn.slagalica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class KoZnaZnaActivity extends AppCompatActivity {

    private Button button1;
    private TextView countdownTextView;
    private static final int TOTAL_REPETITIONS = 5;
    private static final long COUNTDOWN_INTERVAL = 3000; // 3 seconds
    private static final long COUNTDOWN_DURATION = 5000; // 5 seconds

    private boolean isButtonEnabled = true;

    private int repetitionCount = 0;
    private CountDownTimer countDownTimer;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ko_zna_zna);
        button1 = findViewById(R.id.odustani);

        countdownTextView = findViewById(R.id.countdownText);
        startTimer();

    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(COUNTDOWN_DURATION, COUNTDOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdownTextView.setText(String.valueOf(millisUntilFinished / 1000));
            }
            @Override
            public void onFinish() {
                repetitionCount++;

                if (repetitionCount < TOTAL_REPETITIONS) {
                    startTimer();
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
