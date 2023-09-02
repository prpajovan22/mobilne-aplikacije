package com.ftn.slagalica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

import firebase_models.GameFirebaseModel;
import firebase_models.UserFirebaseModel;
import models.Asocijacija;
import utils.Constants;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference realTimeDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        realTimeDatabase = FirebaseDatabase.getInstance().getReference();

        Button profil = findViewById(R.id.profile);
        Button start = findViewById(R.id.start);
        Button login = findViewById(R.id.login);
        Button register = findViewById(R.id.register);
        Button logout = findViewById(R.id.logout);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            UserFirebaseModel model = new UserFirebaseModel("Initial");
            realTimeDatabase.child(Constants.USER_COLLECTION).child(userId).setValue(model);
            realTimeDatabase.child(Constants.USER_COLLECTION).child(userId).child("gameId").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue(String.class).equals("Initial")) {
                        return;
                    }
                    SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(Constants.SHARED_PREFERENCES_GAME_ID, snapshot.getValue(String.class));
                    editor.putBoolean(Constants.SHARED_PREFERENCES_IS_PLAYER_1, false);
                    editor.apply();
                    Intent intent = new Intent(MainActivity.this, AsocijacijeActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
                recreate();
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
                    String userId = preferences.getString(Constants.SHARED_PREFERENCES_USER_ID, "");

                    DocumentReference userRef = FirebaseFirestore.getInstance()
                            .collection(Constants.USER_COLLECTION)
                            .document(userId);
                    userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    long userTokenCount = document.getLong("token");

                                    if (userTokenCount > 0) {
                                        userTokenCount--;
                                        userRef.update("token", userTokenCount)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        realTimeDatabase.child(Constants.USER_COLLECTION).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                for (DataSnapshot s : snapshot.getChildren()) {
                                                                    UserFirebaseModel model = s.getValue(UserFirebaseModel.class);
                                                                    String userId = s.getKey();
                                                                    if (!userId.equals(mAuth.getCurrentUser().getUid())) {
                                                                        realTimeDatabase.child(Constants.USER_COLLECTION).child(userId).child("gameId").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                Intent intent = new Intent(MainActivity.this, AsocijacijeActivity.class);
                                                                                SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
                                                                                SharedPreferences.Editor editor = preferences.edit();
                                                                                editor.putString(Constants.OPONENT_ID, userId);
                                                                                editor.apply();
                                                                                startActivity(intent);
                                                                                realTimeDatabase.removeEventListener(this);
                                                                            }

                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                                            }
                                                                        });
                                                                        String gameId = UUID.randomUUID().toString();
                                                                        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
                                                                        SharedPreferences.Editor editor = preferences.edit();
                                                                        editor.putString(Constants.SHARED_PREFERENCES_GAME_ID, gameId);
                                                                        editor.putBoolean(Constants.SHARED_PREFERENCES_IS_PLAYER_1, true);
                                                                        editor.apply();

                                                                        realTimeDatabase.child(Constants.USER_COLLECTION).child(userId).child("gameId").setValue(gameId);
                                                                        realTimeDatabase.child(Constants.GAME_COLLECTION).child(gameId).setValue(new GameFirebaseModel("test1", "test2", "test3", 0, 0));

                                                                        realTimeDatabase.removeEventListener(this);
                                                                        break;
                                                                    }
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(MainActivity.this, "Failed to update token count.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(MainActivity.this, "No tokens available.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "Failed to fetch user data.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Login.class);

                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);

                startActivity(intent);
            }
        });


    }

    private void logoutUser() {
        mAuth.signOut();
    }


    @Override
    protected void onStart() {
        super.onStart();

        Button login = findViewById(R.id.login);
        Button register = findViewById(R.id.register);
        Button profile = findViewById(R.id.profile);
        Button logout = findViewById(R.id.logout);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            login.setVisibility(View.GONE);
            register.setVisibility(View.GONE);
            profile.setVisibility(View.VISIBLE);
            logout.setVisibility(View.VISIBLE);
        } else {
            login.setVisibility(View.VISIBLE);
            register.setVisibility(View.VISIBLE);
            profile.setVisibility(View.GONE);
            logout.setVisibility(View.GONE);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
    }


}