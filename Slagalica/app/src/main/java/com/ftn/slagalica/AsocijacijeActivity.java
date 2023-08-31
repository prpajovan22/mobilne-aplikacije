package com.ftn.slagalica;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
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
import java.util.logging.Logger;

import firebase_models.KoZnaZnaAnswerModel;
import models.Asocijacija;
import models.Pitanje;
import models.Solutions;
import utils.Constants;

public class AsocijacijeActivity extends AppCompatActivity {

    private TextView countdownText;
    private Integer currentRound = 0;

    private Integer currentPhaze = 0;
    private CountDownTimer countDownTimer;

    private List<Asocijacija> asocijacije;
    private FirebaseFirestore firestore;

    private EditText finalAnswerA, finalAnswerB, finalAnswerC, finalAnswerD, finalSolution;

    private Button a1, a2, a3, a4, b1, b2, b3, b4, c1, c2, c3, c4, d1, d2, d3, d4, propusti;

    private boolean switchedToAnotherActivity = false;
    private boolean answerIsCorrect = false;

    private FirebaseAuth mAuth;
    private String player1UserId,player2UserId,gameId;
    private boolean isMyTurn = false;

    private boolean openFieldInCurrentTurn = false;

    private DatabaseReference realTimeDatabase;

    private List<String> listAreOpenA = new ArrayList<>();

    private List<String> listAreOpenB = new ArrayList<>();

    private List<String> listAreOpenC = new ArrayList<>();

    private List<String> listAreOpenD = new ArrayList<>();

    private boolean isColumnAAnswerd = false;

    private boolean isColumnBAnswerd = false;

    private boolean isColumnCAnswerd = false;

    private boolean isColumnDAnswerd = false;

    @SuppressLint("SuspiciousIndentation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asocijacije);

        propusti = findViewById(R.id.zavrsi);

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

        realTimeDatabase = FirebaseDatabase.getInstance().getReference();

        finalAnswerA = findViewById(R.id.aKolonaAsocijacija);
        finalAnswerB = findViewById(R.id.bKolonaAsocijacija);
        finalAnswerC = findViewById(R.id.cKolonaAsocijacija);
        finalAnswerD = findViewById(R.id.dKolonaAsocijacija);

        finalSolution = findViewById(R.id.resenje);

        countdownText = findViewById(R.id.countdownText);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        player1UserId = mAuth.getCurrentUser().getUid();
        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        player2UserId = preferences.getString(Constants.OPONENT_ID, "");
        isMyTurn = preferences.getBoolean(Constants.SHARED_PREFERENCES_IS_PLAYER_1, false);
        gameId = preferences.getString(Constants.SHARED_PREFERENCES_GAME_ID, "");

        //initializeOverlay(isMyTurn);

        realTimeDatabase.child(Constants.GAME_COLLECTION)
                .child(gameId).child(Constants.SHARED_PREFERENCES_ASOCIJACIJE_ID)
                .child(String.valueOf(currentRound)).child("switchTurn").addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                            isMyTurn = !isMyTurn;
                            if(!isMyTurn){
                                openFieldInCurrentTurn=false;
                            }
                            propusti.setEnabled(isMyTurn);
                            //initializeOverlay(isMyTurn);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                realTimeDatabase.child(Constants.GAME_COLLECTION)
                .child(gameId).child(Constants.SHARED_PREFERENCES_ASOCIJACIJE_ID)
                .child(String.valueOf(currentRound)).child("fields").addChildEventListener(new ChildEventListener() {


                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if(snapshot.getKey().equals("a1")){
                            Asocijacija asocijacija = asocijacije.get(currentRound);
                            a1.setText(asocijacija.getA1());
                            a1.setEnabled(false);
                            listAreOpenA.add("a1");
                        } else if (snapshot.getKey().equals("a2")) {
                            Asocijacija asocijacija = asocijacije.get(currentRound);
                            a2.setText(asocijacija.getA2());
                            a2.setEnabled(false);
                            listAreOpenA.add("a2");
                        }else if (snapshot.getKey().equals("a3")) {
                            Asocijacija asocijacija = asocijacije.get(currentRound);
                            a3.setText(asocijacija.getA3());
                            a3.setEnabled(false);
                            listAreOpenA.add("a3");
                        }else if (snapshot.getKey().equals("a4")) {
                            Asocijacija asocijacija = asocijacije.get(currentRound);
                            a4.setText(asocijacija.getA4());
                            a4.setEnabled(false);
                            listAreOpenA.add("a4");
                        }else if (snapshot.getKey().equals("b1")) {
                            Asocijacija asocijacija = asocijacije.get(currentRound);
                            b1.setText(asocijacija.getB1());
                            b1.setEnabled(false);
                            listAreOpenA.add("b1");
                        }else if (snapshot.getKey().equals("b2")) {
                            Asocijacija asocijacija = asocijacije.get(currentRound);
                            b2.setText(asocijacija.getB2());
                            b2.setEnabled(false);
                            listAreOpenA.add("b3");
                        }else if (snapshot.getKey().equals("b3")) {
                            Asocijacija asocijacija = asocijacije.get(currentRound);
                            b3.setText(asocijacija.getB3());
                            b3.setEnabled(false);
                            listAreOpenA.add("b3");
                        }else if (snapshot.getKey().equals("b4")) {
                            Asocijacija asocijacija = asocijacije.get(currentRound);
                            b4.setText(asocijacija.getB4());
                            b4.setEnabled(false);
                            listAreOpenA.add("b4");
                        }else if (snapshot.getKey().equals("c1")) {
                            Asocijacija asocijacija = asocijacije.get(currentRound);
                            c1.setText(asocijacija.getC1());
                            c1.setEnabled(false);
                            listAreOpenA.add("c1");
                        }else if (snapshot.getKey().equals("c2")) {
                            Asocijacija asocijacija = asocijacije.get(currentRound);
                            c2.setText(asocijacija.getC2());
                            c2.setEnabled(false);
                            listAreOpenA.add("c2");
                        }else if (snapshot.getKey().equals("c3")) {
                            Asocijacija asocijacija = asocijacije.get(currentRound);
                            c3.setText(asocijacija.getC3());
                            c3.setEnabled(false);
                            listAreOpenA.add("c3");
                        }else if (snapshot.getKey().equals("c4")) {
                            Asocijacija asocijacija = asocijacije.get(currentRound);
                            c4.setText(asocijacija.getC4());
                            c4.setEnabled(false);
                            listAreOpenA.add("c4");
                        }else if (snapshot.getKey().equals("d1")) {
                            Asocijacija asocijacija = asocijacije.get(currentRound);
                            d1.setText(asocijacija.getD1());
                            d1.setEnabled(false);
                            listAreOpenA.add("ad1");
                        }else if (snapshot.getKey().equals("d2")) {
                            Asocijacija asocijacija = asocijacije.get(currentRound);
                            d2.setText(asocijacija.getD2());
                            d2.setEnabled(false);
                            listAreOpenA.add("d2");
                        }else if (snapshot.getKey().equals("d3")) {
                            Asocijacija asocijacija = asocijacije.get(currentRound);
                            d3.setText(asocijacija.getD3());
                            d3.setEnabled(false);
                            listAreOpenA.add("d3");
                        }else if (snapshot.getKey().equals("d4")) {
                            Asocijacija asocijacija = asocijacije.get(currentRound);
                            d4.setText(asocijacija.getD4());
                            d4.setEnabled(false);
                            listAreOpenA.add("d4");
                        }else if (snapshot.getKey().equals("solutionA")) {
                            Asocijacija asocijacija = asocijacije.get(currentRound);
                            finalAnswerA.setText(asocijacija.getSolutionA());
                            finalAnswerA.setEnabled(false);
                        }else if (snapshot.getKey().equals("solutionB")) {
                            Asocijacija asocijacija = asocijacije.get(currentRound);
                            finalAnswerB.setText(asocijacija.getSolutionB());
                            finalAnswerB.setEnabled(false);
                        }else if (snapshot.getKey().equals("solutionC")) {
                            Asocijacija asocijacija = asocijacije.get(currentRound);
                            finalAnswerC.setText(asocijacija.getSolutionC());
                            finalAnswerC.setEnabled(false);
                        }else if (snapshot.getKey().equals("solutionD")) {
                            Asocijacija asocijacija = asocijacije.get(currentRound);
                            finalAnswerD.setText(asocijacija.getSolutionD());
                            finalAnswerD.setEnabled(false);
                        }else if (snapshot.getKey().equals("finalAnswer")) {
                            Asocijacija asocijacija = asocijacije.get(currentRound);
                            finalSolution.setText(asocijacija.getFinalAnswer());
                            finalSolution.setEnabled(false);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        firestore.collection(Constants.ASOCIATION_COLLECTION).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Asocijacija> asocijacije1 = task.getResult().toObjects(Asocijacija.class);
                asocijacije1.addAll(asocijacije1);
                asocijacije = asocijacije1;
            }
        });

        propusti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSwitchTurnButtonClick(v);
            }
        });

        a1.setOnClickListener(a -> {
            if(openFieldInCurrentTurn){
                return;
            }
            openFieldInCurrentTurn = true;
                realTimeDatabase
                        .child(Constants.GAME_COLLECTION)
                        .child(gameId)
                        .child(Constants.SHARED_PREFERENCES_ASOCIJACIJE_ID)
                        .child(String.valueOf(currentRound))
                        .child("fields")
                        .child("a1")
                        .setValue(mAuth.getCurrentUser().getUid());
        });
        a2.setOnClickListener(a -> {
            if(openFieldInCurrentTurn){
                return;
            }
            openFieldInCurrentTurn = true;
            realTimeDatabase
                    .child(Constants.GAME_COLLECTION)
                    .child(gameId)
                    .child(Constants.SHARED_PREFERENCES_ASOCIJACIJE_ID)
                    .child(String.valueOf(currentRound))
                    .child("fields")
                    .child("a2")
                    .setValue(mAuth.getCurrentUser().getUid());
        });
        a3.setOnClickListener(a -> {
            if(openFieldInCurrentTurn){
                return;
            }
            openFieldInCurrentTurn = true;
            realTimeDatabase
                    .child(Constants.GAME_COLLECTION)
                    .child(gameId)
                    .child(Constants.SHARED_PREFERENCES_ASOCIJACIJE_ID)
                    .child(String.valueOf(currentRound))
                    .child("fields")
                    .child("a3")
                    .setValue(mAuth.getCurrentUser().getUid());
        });
        a4.setOnClickListener(a -> {
            if(openFieldInCurrentTurn){
                return;
            }
            openFieldInCurrentTurn = true;
            realTimeDatabase
                    .child(Constants.GAME_COLLECTION)
                    .child(gameId)
                    .child(Constants.SHARED_PREFERENCES_ASOCIJACIJE_ID)
                    .child(String.valueOf(currentRound))
                    .child("fields")
                    .child("a4")
                    .setValue(mAuth.getCurrentUser().getUid());
        });
        b1.setOnClickListener(a -> {
            if(openFieldInCurrentTurn){
                return;
            }
            openFieldInCurrentTurn = true;
            realTimeDatabase
                    .child(Constants.GAME_COLLECTION)
                    .child(gameId)
                    .child(Constants.SHARED_PREFERENCES_ASOCIJACIJE_ID)
                    .child(String.valueOf(currentRound))
                    .child("fields")
                    .child("b1")
                    .setValue(mAuth.getCurrentUser().getUid());
        });
        b2.setOnClickListener(a -> {
            if(openFieldInCurrentTurn){
                return;
            }
            openFieldInCurrentTurn = true;
            realTimeDatabase
                    .child(Constants.GAME_COLLECTION)
                    .child(gameId)
                    .child(Constants.SHARED_PREFERENCES_ASOCIJACIJE_ID)
                    .child(String.valueOf(currentRound))
                    .child("fields")
                    .child("b2")
                    .setValue(mAuth.getCurrentUser().getUid());
        });
        b3.setOnClickListener(a -> {
            if(openFieldInCurrentTurn){
                return;
            }
            openFieldInCurrentTurn = true;
            realTimeDatabase
                    .child(Constants.GAME_COLLECTION)
                    .child(gameId)
                    .child(Constants.SHARED_PREFERENCES_ASOCIJACIJE_ID)
                    .child(String.valueOf(currentRound))
                    .child("fields")
                    .child("b3")
                    .setValue(mAuth.getCurrentUser().getUid());
        });
        b4.setOnClickListener(a -> {
            if(openFieldInCurrentTurn){
                return;
            }
            openFieldInCurrentTurn = true;
            realTimeDatabase
                    .child(Constants.GAME_COLLECTION)
                    .child(gameId)
                    .child(Constants.SHARED_PREFERENCES_ASOCIJACIJE_ID)
                    .child(String.valueOf(currentRound))
                    .child("fields")
                    .child("b4")
                    .setValue(mAuth.getCurrentUser().getUid());
        });
        c1.setOnClickListener(c -> {
            if(openFieldInCurrentTurn){
                return;
            }
            openFieldInCurrentTurn = true;
            realTimeDatabase
                    .child(Constants.GAME_COLLECTION)
                    .child(gameId)
                    .child(Constants.SHARED_PREFERENCES_ASOCIJACIJE_ID)
                    .child(String.valueOf(currentRound))
                    .child("fields")
                    .child("c1")
                    .setValue(mAuth.getCurrentUser().getUid());
        });
        c2.setOnClickListener(a -> {
            if(openFieldInCurrentTurn){
                return;
            }
            openFieldInCurrentTurn = true;
            realTimeDatabase
                    .child(Constants.GAME_COLLECTION)
                    .child(gameId)
                    .child(Constants.SHARED_PREFERENCES_ASOCIJACIJE_ID)
                    .child(String.valueOf(currentRound))
                    .child("fields")
                    .child("c2")
                    .setValue(mAuth.getCurrentUser().getUid());
        });
        c3.setOnClickListener(a -> {
            if(openFieldInCurrentTurn){
                return;
            }
            openFieldInCurrentTurn = true;
            realTimeDatabase
                    .child(Constants.GAME_COLLECTION)
                    .child(gameId)
                    .child(Constants.SHARED_PREFERENCES_ASOCIJACIJE_ID)
                    .child(String.valueOf(currentRound))
                    .child("fields")
                    .child("c3")
                    .setValue(mAuth.getCurrentUser().getUid());
        });
        c4.setOnClickListener(a -> {
            if(openFieldInCurrentTurn){
                return;
            }
            openFieldInCurrentTurn = true;
            realTimeDatabase
                    .child(Constants.GAME_COLLECTION)
                    .child(gameId)
                    .child(Constants.SHARED_PREFERENCES_ASOCIJACIJE_ID)
                    .child(String.valueOf(currentRound))
                    .child("fields")
                    .child("c4")
                    .setValue(mAuth.getCurrentUser().getUid());
        });
        d1.setOnClickListener(a -> {
            if(openFieldInCurrentTurn){
                return;
            }
            openFieldInCurrentTurn = true;
            realTimeDatabase
                    .child(Constants.GAME_COLLECTION)
                    .child(gameId)
                    .child(Constants.SHARED_PREFERENCES_ASOCIJACIJE_ID)
                    .child(String.valueOf(currentRound))
                    .child("fields")
                    .child("d1")
                    .setValue(mAuth.getCurrentUser().getUid());
        });
        d2.setOnClickListener(a -> {
            if(openFieldInCurrentTurn){
                return;
            }
            openFieldInCurrentTurn = true;
            realTimeDatabase
                    .child(Constants.GAME_COLLECTION)
                    .child(gameId)
                    .child(Constants.SHARED_PREFERENCES_ASOCIJACIJE_ID)
                    .child(String.valueOf(currentRound))
                    .child("fields")
                    .child("d2")
                    .setValue(mAuth.getCurrentUser().getUid());
        });
        d3.setOnClickListener(a -> {
            if(openFieldInCurrentTurn){
                return;
            }
            openFieldInCurrentTurn = true;
            realTimeDatabase
                    .child(Constants.GAME_COLLECTION)
                    .child(gameId)
                    .child(Constants.SHARED_PREFERENCES_ASOCIJACIJE_ID)
                    .child(String.valueOf(currentRound))
                    .child("fields")
                    .child("d3")
                    .setValue(mAuth.getCurrentUser().getUid());
        });
        d4.setOnClickListener(a -> {
            if(openFieldInCurrentTurn){
                return;
            }
            openFieldInCurrentTurn = true;
            realTimeDatabase
                    .child(Constants.GAME_COLLECTION)
                    .child(gameId)
                    .child(Constants.SHARED_PREFERENCES_ASOCIJACIJE_ID)
                    .child(String.valueOf(currentRound))
                    .child("fields")
                    .child("d4")
                    .setValue(mAuth.getCurrentUser().getUid());
        });
        startCountdown();

        finalAnswerA.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    boolean isCorrectAnswer = checkEnteredWord(finalAnswerA.getText().toString().trim(), Solutions.FINAL_ANSWER_A);
                    if(isCorrectAnswer){
                        // todo: Dodati obradu signala iz firebase  else if (snapshot.getKey().equals("solutionA")) {
                        //                            Asocijacija asocijacija = asocijacije.get(currentRound);
                        //                            d4.setText(asocijacija.getD4());
                        //                            d4.setEnabled(false);
                        realTimeDatabase
                                .child(Constants.GAME_COLLECTION)
                                .child(gameId)
                                .child(Constants.SHARED_PREFERENCES_ASOCIJACIJE_ID)
                                .child(String.valueOf(currentRound))
                                .child("fields")
                                .child("solutionA")
                                .setValue(mAuth.getCurrentUser().getUid());
                        int points = 2;
                        points+= 4 - listAreOpenA.size();
                        assignPoints(mAuth.getCurrentUser().getUid(),points);

                    }else{
                        realTimeDatabase.child(Constants.GAME_COLLECTION)
                                .child(gameId).child(Constants.SHARED_PREFERENCES_ASOCIJACIJE_ID).child(String.valueOf(currentRound)).child("switchTurn").setValue(String.valueOf(Instant.now().toEpochMilli()));
                    }
                    return true;
                }
                return false;
            }
        });

        finalAnswerB.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    boolean isCorrectAnswer = checkEnteredWord(finalAnswerB.getText().toString().trim(), Solutions.FINAL_ANSWER_B);
                    if(isCorrectAnswer){
                        realTimeDatabase
                                .child(Constants.GAME_COLLECTION)
                                .child(gameId)
                                .child(Constants.SHARED_PREFERENCES_ASOCIJACIJE_ID)
                                .child(String.valueOf(currentRound))
                                .child("fields")
                                .child("solutionB")
                                .setValue(mAuth.getCurrentUser().getUid());
                        int points = 2;
                        points+= 4 - listAreOpenB.size();
                        assignPoints(mAuth.getCurrentUser().getUid(),points);
                    }else{
                        realTimeDatabase.child(Constants.GAME_COLLECTION)
                                .child(gameId).child(Constants.SHARED_PREFERENCES_ASOCIJACIJE_ID).child(String.valueOf(currentRound)).child("switchTurn").setValue(String.valueOf(Instant.now().toEpochMilli()));
                    }
                    return true;
                }
                return false;
            }
        });

        finalAnswerC.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    boolean isCorrectAnswer = checkEnteredWord(finalAnswerC.getText().toString().trim(), Solutions.FINAL_ANSWER_C);
                    if(isCorrectAnswer){
                        realTimeDatabase
                                .child(Constants.GAME_COLLECTION)
                                .child(gameId)
                                .child(Constants.SHARED_PREFERENCES_ASOCIJACIJE_ID)
                                .child(String.valueOf(currentRound))
                                .child("fields")
                                .child("solutionC")
                                .setValue(mAuth.getCurrentUser().getUid());
                        int points = 2;
                        points+= 4 - listAreOpenC.size();
                        assignPoints(mAuth.getCurrentUser().getUid(),points);
                    }else{
                        realTimeDatabase.child(Constants.GAME_COLLECTION)
                                .child(gameId).child(Constants.SHARED_PREFERENCES_ASOCIJACIJE_ID).child(String.valueOf(currentRound)).child("switchTurn").setValue(String.valueOf(Instant.now().toEpochMilli()));
                    }
                    return true;
                }
                return false;
            }
        });

        finalAnswerD.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    boolean isCorrectAnswer = checkEnteredWord(finalAnswerD.getText().toString().trim(), Solutions.FINAL_ANSWER_D);
                    if(isCorrectAnswer){
                        realTimeDatabase
                                .child(Constants.GAME_COLLECTION)
                                .child(gameId)
                                .child(Constants.SHARED_PREFERENCES_ASOCIJACIJE_ID)
                                .child(String.valueOf(currentRound))
                                .child("fields")
                                .child("solutionD")
                                .setValue(mAuth.getCurrentUser().getUid());
                        int points = 2;
                        points+= 4 - listAreOpenD.size();
                        assignPoints(mAuth.getCurrentUser().getUid(),points);
                    }else{
                        realTimeDatabase.child(Constants.GAME_COLLECTION)
                                .child(gameId).child(Constants.SHARED_PREFERENCES_ASOCIJACIJE_ID).child(String.valueOf(currentRound)).child("switchTurn").setValue(String.valueOf(Instant.now().toEpochMilli()));
                    }
                    return true;
                }
                return false;
            }
        });

        finalSolution.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    boolean isCorrectAnswer = checkEnteredWord(finalSolution.getText().toString().trim(), Solutions.FINAL_ANSWER);
                    if(isCorrectAnswer){
                        realTimeDatabase
                                .child(Constants.GAME_COLLECTION)
                                .child(gameId)
                                .child(Constants.SHARED_PREFERENCES_ASOCIJACIJE_ID)
                                .child(String.valueOf(currentRound))
                                .child("fields")
                                .child("finalAnswer")
                                .setValue(mAuth.getCurrentUser().getUid());
                        int points = 7;
                        if(listAreOpenA.size() == 0){
                            points+= 6;
                        }else{
                            points+=4-listAreOpenA.size();
                        }
                        if(listAreOpenB.size() == 0){
                            points+= 6;
                        }else{
                            points+=4-listAreOpenB.size();
                        }if(listAreOpenC.size() == 0){
                            points+= 6;
                        }else{
                            points+=4-listAreOpenC.size();
                        }if(listAreOpenD.size() == 0){
                            points+= 6;
                        }else{
                            points+=4-listAreOpenD.size();
                        }
                        assignPoints(mAuth.getCurrentUser().getUid(),points);
                        isColumnAAnswerd = true;
                    }else{
                        realTimeDatabase.child(Constants.GAME_COLLECTION)
                                .child(gameId).child(Constants.SHARED_PREFERENCES_ASOCIJACIJE_ID).child(String.valueOf(currentRound)).child("switchTurn").setValue(String.valueOf(Instant.now().toEpochMilli()));
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private boolean checkEnteredWord(String enterWord, Solutions answer) {
        if (TextUtils.isEmpty(enterWord)) {
            return false;
        }
        Asocijacija asocijacija = asocijacije.get(currentRound);
        if (answer.equals(Solutions.FINAL_ANSWER_A) && asocijacija.getSolutionA().equals(enterWord)) {
            finalAnswerA.setEnabled(false);
            a1.setText(asocijacija.getA1());
            a1.setEnabled(false);
            a2.setText(asocijacija.getA2());
            a2.setEnabled(false);
            a3.setText(asocijacija.getA3());
            a3.setEnabled(false);
            a4.setText(asocijacija.getA4());
            a4.setEnabled(false);
            return true;

        } else if (answer.equals(Solutions.FINAL_ANSWER_B) && asocijacija.getSolutionB().equals(enterWord)) {
            finalAnswerB.setEnabled(false);
            b1.setText(asocijacija.getB1());
            b1.setEnabled(false);
            b2.setText(asocijacija.getB2());
            b2.setEnabled(false);
            b3.setText(asocijacija.getB3());
            b3.setEnabled(false);
            b4.setText(asocijacija.getB4());
            b4.setEnabled(false);
            return true;
        } else if (answer.equals(Solutions.FINAL_ANSWER_C) && asocijacija.getSolutionC().equals(enterWord)) {
            finalAnswerC.setEnabled(false);
            c1.setText(asocijacija.getC1());
            c1.setEnabled(false);
            c2.setText(asocijacija.getC2());
            c2.setEnabled(false);
            c3.setText(asocijacija.getC3());
            c3.setEnabled(false);
            c4.setText(asocijacija.getC4());
            c4.setEnabled(false);
            return true;
        } else if (answer.equals(Solutions.FINAL_ANSWER_D) && asocijacija.getSolutionD().equals(enterWord)) {
            finalAnswerD.setEnabled(false);
            d1.setText(asocijacija.getD1());
            d1.setEnabled(false);
            d2.setText(asocijacija.getD2());
            d2.setEnabled(false);
            d3.setText(asocijacija.getD3());
            d3.setEnabled(false);
            d4.setText(asocijacija.getD4());
            d4.setEnabled(false);
            return true;
        } else if (answer.equals(Solutions.FINAL_ANSWER) && asocijacija.getFinalAnswer().equals(enterWord)) {
            answerIsCorrect = true;
            finalSolution.setEnabled(false);
            finalAnswerA.setText(asocijacija.getSolutionA());
            finalAnswerA.setEnabled(false);
            finalAnswerB.setText(asocijacija.getSolutionB());
            finalAnswerB.setEnabled(false);
            finalAnswerC.setText(asocijacija.getSolutionC());
            finalAnswerC.setEnabled(false);
            finalAnswerD.setText(asocijacija.getSolutionC());
            finalAnswerD.setEnabled(false);
            a1.setText(asocijacija.getA1());
            a1.setEnabled(false);
            a2.setText(asocijacija.getA2());
            a2.setEnabled(false);
            a3.setText(asocijacija.getA3());
            a3.setEnabled(false);
            a4.setText(asocijacija.getA4());
            a4.setEnabled(false);
            b1.setText(asocijacija.getB1());
            b1.setEnabled(false);
            b2.setText(asocijacija.getB2());
            b2.setEnabled(false);
            b3.setText(asocijacija.getB3());
            b3.setEnabled(false);
            b4.setText(asocijacija.getB4());
            b4.setEnabled(false);
            c1.setText(asocijacija.getC1());
            c1.setEnabled(false);
            c2.setText(asocijacija.getC2());
            c2.setEnabled(false);
            c3.setText(asocijacija.getC3());
            c3.setEnabled(false);
            c4.setText(asocijacija.getC4());
            c4.setEnabled(false);
            d1.setText(asocijacija.getD1());
            d1.setEnabled(false);
            d2.setText(asocijacija.getD2());
            d2.setEnabled(false);
            d3.setText(asocijacija.getD3());
            d3.setEnabled(false);
            d4.setText(asocijacija.getD4());
            d4.setEnabled(false);
            if (currentRound == 0) {
                countDownTimer.cancel();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
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
            }
            return true;
        }
        return false;
    }

    private void startCountdown() {
        countDownTimer = new CountDownTimer(2 * 60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdownText.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                if (!switchedToAnotherActivity) {
                    if (countDownTimer.equals(0) && currentRound == 0 || answerIsCorrect && currentRound == 0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                resetTimer();
                                fetchDataAndUpdateUI();
                            }
                        }, 5000);
                    } else if (answerIsCorrect && currentRound == 1) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                switchToAnotherActivity();
                            }
                        }, 5000);
                    } else {
                        switchToAnotherActivity();
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

    private void fetchDataAndUpdateUI() {
                isMyTurn = !isMyTurn;

                currentRound = 1;
                a1.setText("A1");
                a1.setEnabled(true);
                a2.setText("A2");
                a2.setEnabled(true);
                a3.setText("A3");
                a3.setEnabled(true);
                a4.setText("A4");
                a4.setEnabled(true);
                b1.setText("B1");
                b1.setEnabled(true);
                b2.setText("B2");
                b2.setEnabled(true);
                b3.setText("B3");
                b3.setEnabled(true);
                b4.setText("B4");
                b4.setEnabled(true);
                c1.setText("C1");
                c1.setEnabled(true);
                c2.setText("C2");
                c2.setEnabled(true);
                c3.setText("C3");
                c3.setEnabled(true);
                c4.setText("C3");
                c4.setEnabled(true);
                d1.setText("D1");
                d1.setEnabled(true);
                d2.setText("D2");
                d3.setEnabled(true);
                d3.setText("D3");
                d3.setEnabled(true);
                d4.setText("D4");
                d4.setEnabled(true);
                finalAnswerA.setText("finalAnswerA");
                finalAnswerA.setEnabled(true);
                finalAnswerB.setText("finalAnswerB");
                finalAnswerB.setEnabled(true);
                finalAnswerC.setText("finalAnswerC");
                finalAnswerC.setEnabled(true);
                finalAnswerD.setText("finalAnswerD");
                finalAnswerD.setEnabled(true);
                finalSolution.setText("finalSolution");
                finalSolution.setEnabled(true);

                startCountdown();

    }

    private void switchToAnotherActivity() {
        Intent intent = new Intent(AsocijacijeActivity.this, SpojniceActivity.class);
        startActivity(intent);
        switchedToAnotherActivity = true;
    }


    private void enableTouchableViews(ViewGroup parentView, boolean enable) {
        for (View childView : parentView.getTouchables()) {
            childView.setEnabled(enable);
            childView.setClickable(enable);
        }
    }

    private void enableEditTextViews(ViewGroup parentView, boolean enable) {
        for (View childView : parentView.getTouchables()) {
            if (childView instanceof EditText) {
                childView.setEnabled(enable);
                childView.setClickable(enable);
            }
        }
    }

    private View createOverlayView() {
        View overlayView = new View(this);
        overlayView.setBackgroundColor(Color.TRANSPARENT);
        overlayView.setClickable(true);
        overlayView.setFocusable(true);
        overlayView.setTag("overlayViewTag");

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        overlayView.setLayoutParams(params);

        return overlayView;
    }

    private void initializeOverlay(boolean isMyTurn) {
        FrameLayout rootView = findViewById(android.R.id.content);
        View overlayView = rootView.findViewWithTag("overlayViewTag");

        if (!isMyTurn) {
            if (overlayView == null) {
                overlayView = createOverlayView();
                rootView.addView(overlayView);
            }
            enableTouchableViews(rootView, false);
            enableEditTextViews(rootView, true);
        } else {
            if (overlayView != null) {
                rootView.removeView(overlayView);
            }
            enableTouchableViews(rootView, true);
            enableEditTextViews(rootView, true);
        }
    }

    public void onSwitchTurnButtonClick(View view) {
        realTimeDatabase.child(Constants.GAME_COLLECTION)
                .child(gameId).child(Constants.SHARED_PREFERENCES_ASOCIJACIJE_ID)
                .child(String.valueOf(currentRound)).child("switchTurn").setValue(String.valueOf(Instant.now().toEpochMilli()));
    }

    private void assignPoints(String playerId, int points) {
        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        String gameID = preferences.getString(Constants.SHARED_PREFERENCES_GAME_ID, "");
        DatabaseReference userRef = realTimeDatabase.child(Constants.GAME_COLLECTION).child(gameID);

        String pointsKey = playerId.equals(player1UserId) ? "bodovi1" : "bodovi2";

        DatabaseReference pointsRef = userRef.child(pointsKey);

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
}
