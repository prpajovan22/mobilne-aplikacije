package com.ftn.slagalica;

import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

public class Timer {


    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds = 10000; // 10 sec
    private boolean timeRunning;

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
                getTime();
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
    public String getTime(){
        int minutes = (int) timeLeftInMilliseconds / 60000;
        int seconds = (int) timeLeftInMilliseconds % 60000 / 1000;

        String timeLeftText;

        timeLeftText = "" + minutes;
        timeLeftText += ":";

        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;
        return timeLeftText;
    }
}
