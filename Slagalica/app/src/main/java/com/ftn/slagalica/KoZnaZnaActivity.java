package com.ftn.slagalica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class KoZnaZnaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ko_zna_zna);
        Button button1 = findViewById(R.id.odustani);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(KoZnaZnaActivity.this, MainActivity.class);

                startActivity(intent);
            }
        });
    }
}