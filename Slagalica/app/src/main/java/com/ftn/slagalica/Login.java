package com.ftn.slagalica;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;

public class Login extends AppCompatActivity {

    EditText email_login,password_login;
    Button loginButton;
    FirebaseApp firebaseDatabase,refrence;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_login = findViewById(R.id.email);
        password_login = findViewById(R.id.passowrd);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseDatabase = FirebaseApp.getInstance();
                //refrence = FirebaseApp.getPersistenceKey(email_login,password_login);

            }
        });
    }
}