package com.br.projeto;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.br.projeto.dao.DAO;
import com.br.projeto.modelo.Anotacao;


public class alterar_anotacao extends AppCompatActivity {

    EditText alteracao;
    Button btnSalvarAnotacao;
    Anotacao anotacao, altanotacao;
    Button btnExcluirAnotacao;
    DAO dao;
    long retornoDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_anotacao);

        Intent abrirEdicao = getIntent();
        altanotacao = (Anotacao) abrirEdicao.getSerializableExtra("Anotacao-enviada");
        anotacao = new Anotacao();
        dao = new DAO(alterar_anotacao.this);

        //resgatar os componentes

        alteracao = (EditText) findViewById(R.id.alteracao);
        btnSalvarAnotacao = (Button) findViewById(R.id.btnSalvarAnotacao);
        btnExcluirAnotacao = (Button) findViewById(R.id.btnExcluirAnotacao);

        if (altanotacao != null) {
            alteracao.setText(altanotacao.getNova_anotacao());
            anotacao.setId_anotacao(altanotacao.getId_anotacao());
        }

        btnSalvarAnotacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anotacao.setId_anotacao(altanotacao.getId_anotacao());
                anotacao.setNova_anotacao(alteracao.getText().toString());

                dao.alterarAnotacao(anotacao);
                dao.close();


                if (retornoDB == -1) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(alterar_anotacao.this);
                    dialog.setTitle("AgroInfo");
                    dialog.setMessage("Erro ao alterar!");
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alterar_anotacao.this.finish();
                        }
                    });


                    dialog.show();

                } else {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(alterar_anotacao.this);
                    dialog.setTitle("AgroInfo");
                    dialog.setMessage("Alterado com sucesso!");
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alterar_anotacao.this.finish();
                        }
                    });


                    dialog.show();
                }
            }
        });


    }
}


