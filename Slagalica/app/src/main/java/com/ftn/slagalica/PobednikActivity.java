package com.ftn.slagalica;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import firebase_models.GameFirebaseModel;
import utils.Constants;

public class PobednikActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String player1UserId, player2UserId, gameId;
    private boolean isMyTurn;

    private TextView player1UsernameTextView, player1PointsTextView,
            player2UsernameTextView, player2PointsTextView,
            winnerUsernameTextView, winnerPointsTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pobednik);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        player1UsernameTextView = findViewById(R.id.player1Username);
        player1PointsTextView = findViewById(R.id.player1Points);
        player2UsernameTextView = findViewById(R.id.player2Username);
        player2PointsTextView = findViewById(R.id.player2Points);
        winnerUsernameTextView = findViewById(R.id.winnerUsername);
        winnerPointsTextView = findViewById(R.id.winnerPoints);

        player1UserId = mAuth.getCurrentUser().getUid();
        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        player2UserId = preferences.getString(Constants.OPONENT_ID, "");
        isMyTurn = preferences.getBoolean(Constants.SHARED_PREFERENCES_IS_PLAYER_1, false);
        gameId = preferences.getString(Constants.SHARED_PREFERENCES_GAME_ID, "");

        mDatabase.child(Constants.GAME_COLLECTION).child(gameId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GameFirebaseModel model = dataSnapshot.getValue(GameFirebaseModel.class);

                SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
                String player1Username = preferences.getString(Constants.SHARED_PREFERENCES_USER_USERNAME, "");
                String player2Username = preferences.getString(Constants.OPONENT_USERNAME, "");


                player2UsernameTextView.setText(player2Username);
                player2PointsTextView.setText("bodovi2: " + model.getBodovi2());

                player1UsernameTextView.setText(player1Username);
                player1PointsTextView.setText("bodovi1: " + model.getBodovi1());

                int player1Points = model.getBodovi1();
                int player2Points = model.getBodovi2();

                if (player1Points > player2Points) {
                    winnerUsernameTextView.setText(player1Username);
                    winnerPointsTextView.setText("bodovi1: " + player1Points);
                } else if (player2Points > player1Points) {
                    winnerUsernameTextView.setText(player2Username);
                    winnerPointsTextView.setText("bodovi2: " + player2Points);
                } else {
                    winnerUsernameTextView.setText("It's a tie!");
                    winnerPointsTextView.setText("bodovi1: " + player1Points);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
