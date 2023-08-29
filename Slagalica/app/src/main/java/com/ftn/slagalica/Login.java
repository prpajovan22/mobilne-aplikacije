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
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import models.User;
import utils.Constants;

public class Login extends AppCompatActivity {

    private EditText email_login, password_login;
    private Button loginButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        email_login = findViewById(R.id.email);
        password_login = findViewById(R.id.passowrd);
        loginButton = findViewById(R.id.buttonLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_login.getText().toString().trim();
                String password = password_login.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login.this, "Please fill in all fields to login", Toast.LENGTH_SHORT).show();
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    firestore.collection(Constants.USER_COLLECTION)
                                                    .document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(documentSnapshot -> {
                                                User user = documentSnapshot.toObject(User.class);
                                                Log.e("SNAPSHOT_INSPECT",mAuth.getCurrentUser().getUid());
                                                SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = preferences.edit();
                                                editor.putString(Constants.SHARED_PREFERENCES_USER_ID,mAuth.getCurrentUser().getUid());
                                                editor.putString(Constants.SHARED_PREFERENCES_USER_USERNAME,user.getUsername());
                                                editor.apply();

                                                finish();
                                            });
                                } else {
                                    Toast.makeText(Login.this, "Login failed", Toast.LENGTH_SHORT).show();
                                    Log.e("Login", "Error" + task.getException().getMessage());
                                }
                            }
                        });
            }
        });
    }
}