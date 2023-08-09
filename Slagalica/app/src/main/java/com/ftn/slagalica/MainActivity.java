package com.ftn.slagalica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    /*private Button login;
    private Button start;
    private Button register;
    private Button profil;
    private Button logout;
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        Button profil = findViewById(R.id.profile);
        Button start = findViewById(R.id.start);
        Button login = findViewById(R.id.login);
        Button register = findViewById(R.id.register);
        Button logout = findViewById(R.id.logout);

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
                Intent intent = new Intent(MainActivity.this, KoZnaZnaActivity.class);

                startActivity(intent);
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

    private void logoutUser(){
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
        if (currentUser!= null){
            login.setVisibility(View.GONE);
            register.setVisibility(View.GONE);
            profile.setVisibility(View.VISIBLE);
            logout.setVisibility(View.VISIBLE);
        }else{
            login.setVisibility(View.VISIBLE);
            register.setVisibility(View.VISIBLE);
            profile.setVisibility(View.GONE);
            logout.setVisibility(View.GONE);
        }
    }

    protected void onDestroy(){
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
    }




}