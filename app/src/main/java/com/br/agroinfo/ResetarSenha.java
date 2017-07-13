package com.br.agroinfo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ResetarSenha extends AppCompatActivity {
    TextInputEditText edtEmail;
    Button btnResetar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetar_senha);
        edtEmail = (TextInputEditText) findViewById(R.id.edtEmail);
        btnResetar = (Button) findViewById(R.id.btnResetar);
        eventoClique();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ResetarSenha.this, FormularioLogin.class);
        startActivity(i);
    }
    private void eventoClique() {
        btnResetar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                resetarSenha(email);
            }
        });
    }

    private void resetarSenha(String email) {
        FormularioLogin.autent.sendPasswordResetEmail(email).addOnCompleteListener(ResetarSenha.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    alerta("Um e-mail de redefinição de edtSenha foi enviado");
                    finish();
                } else {
                    alerta("E-mail não encontrado");
                }
            }
        });
    }

    private void alerta(String s) {
        Toast.makeText(ResetarSenha.this, s, Toast.LENGTH_SHORT).show();
    }

}
