package com.br.agroinfo;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ResetarSenha extends AppCompatActivity {
    EditText edtEmail;
    Button btnResetar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetar_senha);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView titulo = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titulo.setText("RESETAR SENHA");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        btnResetar = (Button) findViewById(R.id.btnResetar);
        eventoClique();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Publico.Intente(ResetarSenha.this, FormularioLogin.class);
        finish();
    }
    private void eventoClique() {
        btnResetar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                if (!email.isEmpty() && FormularioCadastro.eValido(email)){
                    resetarSenha(email);
                } else {
                    Publico.Alerta(ResetarSenha.this, "É necessário possuir o campo preenchido, com método de email");
                }

            }
        });
    }

    private void resetarSenha(String email) {
        FormularioLogin.autent.sendPasswordResetEmail(email).addOnCompleteListener(ResetarSenha.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Publico.Alerta(ResetarSenha.this, "Um e-mail de redefinição de edtSenha foi enviado");
                    finish();
                } else {
                    Publico.Alerta(ResetarSenha.this,"E-mail não encontrado");
                }
            }
        });
    }
}
