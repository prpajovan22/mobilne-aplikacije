package com.ftn.slagalica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import utils.Constants;

public class ProfileActivity extends AppCompatActivity {

    private TextView TextViewIme,TextViewEmail;
    private ImageView profileImageView;
    private Button mainActivity,logoutActivity,capturePicture,selectPicture;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_SELECT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        TextViewIme = findViewById(R.id.imeKorisnika);
        TextViewEmail = findViewById(R.id.emailKorisnika);

        profileImageView = findViewById(R.id.profileImageView);

        logoutActivity = findViewById(R.id.logout);
        mainActivity = findViewById(R.id.mainActivity);
        capturePicture = findViewById(R.id.captureButton);
        selectPicture = findViewById(R.id.selectButton);

        String profileImageUrl = "my_image_url";

        Picasso.get().load(profileImageUrl).into(profileImageView);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            String userId = currentUser.getUid();

            db.collection(Constants.USER_COLLECTION).document(userId).get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document != null){
                                String ime = document.getString("username");

                                TextViewIme.setText(ime);
                                TextViewEmail.setText(mAuth.getCurrentUser().getEmail());

                            }
                        }

                    });
            logoutActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    logoutUser();
                    Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            });

            mainActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });

            capturePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    captureProfilePicture();
                }
            });

            selectPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectProfilePicture();
                }
            });
        }
    }
    private void logoutUser(){
        mAuth.signOut();
    }

    private void selectProfilePicture(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_SELECT);
    }

    private void captureProfilePicture(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    protected void onActivityResault(int requestCode, int resaultCode,Intent data){
        super.onActivityResult(requestCode,resaultCode,data);
        if(requestCode == REQUEST_IMAGE_CAPTURE && resaultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageFactory = (Bitmap) extras.get("data");
            // kako da se posalje na firebase
        }else if(requestCode == REQUEST_IMAGE_CAPTURE && resaultCode == RESULT_OK){
            Uri selectedImageUri = data.getData();
            // kako da se posalje na firebase
        }
    }

    // videti kako ucitati sliku sa firebase

}