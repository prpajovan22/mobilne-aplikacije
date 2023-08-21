package com.ftn.slagalica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import models.User;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText editTextEmail,editTextPassword,editTextUsername;
    Button buttonReg;
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.passowrd);
        editTextUsername = findViewById(R.id.username);
        buttonReg = findViewById(R.id.buttonRegister);

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password, username;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                username = String.valueOf(editTextUsername.getText());

                if (TextUtils.isEmpty(email)) {
                    editTextEmail.setError("Enter email");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    editTextPassword.setError("Enter password");
                    return;
                }

                if (TextUtils.isEmpty(username)) {
                    editTextUsername.setError("Enter username");
                    return;
                }
                auth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
                    boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                    if (!isNewUser){
                        Toast.makeText(RegisterActivity.this, "email is in use", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(userCreatedTask -> {
                       if(!userCreatedTask.isSuccessful()){
                           Toast.makeText(RegisterActivity.this, "fail to register user", Toast.LENGTH_SHORT).show();
                           return;
                       }
                        User user = new User();
                       user.setUsername(username);
                       user.setToken(5);
                        firestore.collection("Users")
                                .document(auth.getCurrentUser().getUid())
                                .set(user)
                                .addOnCompleteListener(userCreatedInFirestoreTask -> {
                                    startActivity(new Intent(getApplicationContext(),Login.class));
                                });
                    });
                });
            }

        });
    }
}


        /*



                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResault>()) {
                    if (shouldUpRecreateTask().isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(Tag, "createUserWithEmail:success")
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                                ).show()
                        updateUI(null)
                    }
                }
            }
        });*/


