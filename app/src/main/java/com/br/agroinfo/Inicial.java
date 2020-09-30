package com.br.agroinfo;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.br.agroinfo.dao.Conexao;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Inicial extends AppCompatActivity {
    public static FirebaseAuth autent;
    public static FirebaseUser usuario;
    public static FirebaseDatabase firebaseDatabase;
    public static DatabaseReference databaseReference;
    //Chamado serve para ver se a persistência já foi chamada
    public static boolean chamado = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);
    }

    @Override
    protected void onStart() {
        super.onStart();
        inicializarFirebase();
        autent = Conexao.getFirebaseAuth();
        CountDownTimer ctd = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                if (autent.getCurrentUser() != null){
                    usuario = autent.getCurrentUser();
                    Publico.Intente(Inicial.this, MenuP.class);
                    finish();
                } else {
                    Publico.Intente(Inicial.this, FormularioLogin.class);
                    finish();
                }
            }
        };
        ctd.start();

    }
    private void inicializarFirebase() {
        FirebaseApp.initializeApp(Inicial.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        if (!chamado){
            firebaseDatabase.setPersistenceEnabled(true);
            chamado = true;
        }
        databaseReference = firebaseDatabase.getReference();
    }
}
