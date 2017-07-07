package com.br.agroinfo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.br.agroinfo.modelo.Anotacao;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class alterar_anotacao extends AppCompatActivity {

    EditText alteracao, assunto;
    Button btnSalvarAnotacao;
    Anotacao altanotacao;
    Button btnExcluirAnotacao;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_anotacao);
        inicializarFirebase();

        Intent abrirEdicao = getIntent();
        altanotacao = (Anotacao) abrirEdicao.getSerializableExtra("Anotacao-enviada");


        //resgatar os componentes

        alteracao = (EditText) findViewById(R.id.alteracao);
        assunto = (EditText) findViewById(R.id.edtAssuntos);
        btnSalvarAnotacao = (Button) findViewById(R.id.btnSalvarAnotacao);
        btnExcluirAnotacao = (Button) findViewById(R.id.btnExcluirAnotacao);

        if (altanotacao != null) {
            assunto.setText(altanotacao.getNovo_assunto());
            alteracao.setText(altanotacao.getNova_anotacao());
        }

        btnSalvarAnotacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Anotacao a = new Anotacao();
                a.setId_anotacao(altanotacao.getId_anotacao());
                a.setNovo_assunto(assunto.getText().toString());
                a.setNova_anotacao(alteracao.getText().toString());
                databaseReference.child("Anotacao").child(MenuP.usuario.getUid()).child(a.getId_anotacao()).setValue(a);
                alerta("Alterado com Sucesso");
                limparCampos();
                finish();
            }
        });
        btnExcluirAnotacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Anotacao a = new Anotacao();
                a.setId_anotacao(altanotacao.getId_anotacao());
                databaseReference.child("Anotacao").child(MenuP.usuario.getUid()).child(a.getId_anotacao()).removeValue();
                alerta("Excluído com Sucesso");
                limparCampos();
                finish();

            }
        });
    }

    private void alerta(String mensagem) {
        Toast.makeText(alterar_anotacao.this, mensagem, Toast.LENGTH_SHORT).show();
    }

    private void limparCampos() {
        assunto.setText("");
        alteracao.setText("");
    }

    private void inicializarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
}


