package com.ftn.slagalica;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.List;

import models.Pitanje;
import models.Spojnice;
import utils.Constants;

public class SpojniceActivity extends AppCompatActivity {

    private Button a1,a2,b1,b2,c1,c2,d1,d2;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spojnice);

        a1 = findViewById(R.id.A1);
        a2 = findViewById(R.id.A2);
        b1 = findViewById(R.id.B1);
        b2 = findViewById(R.id.B2);
        c1 = findViewById(R.id.C1);
        c2 = findViewById(R.id.C2);
        d1 = findViewById(R.id.D1);
        d2 = findViewById(R.id.D2);

        firestore = FirebaseFirestore.getInstance();

        firestore.collection(Constants.SPOJNICE_COLLECTION).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                List<Spojnice> pitanja = task.getResult().toObjects(Spojnice.class);
                Collections.shuffle(pitanja);
                Spojnice spojnice = new Spojnice();
                a1.setText(spojnice.getA());
                a2.setText(spojnice.getAa());
                b1.setText(spojnice.getB());
                b2.setText(spojnice.getBb());
                c1.setText(spojnice.getC());
                c2.setText(spojnice.getCc());
                d1.setText(spojnice.getD());
                d2.setText(spojnice.getDd());

            }
        });


    }

}