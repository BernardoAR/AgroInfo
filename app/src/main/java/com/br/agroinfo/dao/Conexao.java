package com.br.agroinfo.dao;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Bernardo on 05/07/2017.
 */

public class Conexao {
    private static FirebaseAuth firebaseAuth;
    private static FirebaseAuth.AuthStateListener authStateListener;
    private static FirebaseUser firebaseUser;

    private Conexao(){

    }
    public static FirebaseAuth getFirebaseAuth(){
        if (firebaseAuth == null){
            inicializarFirebaseAuth();
        }
        return firebaseAuth;
    }
    //INICIALIZAR
    private static void inicializarFirebaseAuth() {
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser usuario = firebaseAuth.getCurrentUser();
                if (usuario != null){
                    firebaseUser = usuario;
                }
            }
        };
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    //Iniciar FirebaseUser
    public static FirebaseUser getFirebaseUser(){
        return firebaseUser;
    }
    //Deslogar
    public static void deslogar(){
        firebaseAuth.signOut();
    }
}
