package com.br.agroinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.br.agroinfo.modelo.Anotacao;



public class AlterarAnotacao extends AppCompatActivity {

    EditText edtAlteracao, edtAssunto;
    Button btnSalvarAnotacao, btnExcluirAnotacao;
    Anotacao altanotacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_anotacao);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView titulo = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titulo.setText("ALTERAR ANOTAÇÃO");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent abrirEdicao = getIntent();
        altanotacao = (Anotacao) abrirEdicao.getSerializableExtra("Anotacao-enviada");

        //resgatar os componentes
        edtAlteracao = (EditText) findViewById(R.id.alteracao);
        edtAssunto = (EditText) findViewById(R.id.edtAssuntos);
        btnSalvarAnotacao = (Button) findViewById(R.id.btnSalvarAnotacao);
        btnExcluirAnotacao = (Button) findViewById(R.id.btnExcluirAnotacao);

        if (altanotacao != null) {
            edtAssunto.setText(altanotacao.getNovo_assunto());
            edtAlteracao.setText(altanotacao.getNova_anotacao());
        }

        btnSalvarAnotacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Anotacao a = new Anotacao();
                a.setId_anotacao(altanotacao.getId_anotacao());
                a.setNovo_assunto(edtAssunto.getText().toString().replace('/', '-'));
                a.setNova_anotacao(edtAlteracao.getText().toString());
                FormularioLogin.databaseReference.child("Anotacao").child(FormularioLogin.usuario.getUid()).child(a.getId_anotacao()).setValue(a);
                Publico.Alerta(AlterarAnotacao.this, "Alterado com Sucesso");
                Publico.Intente(AlterarAnotacao.this, ListaAnotacoes.class);
                limparCampos();
                finish();
            }
        });
        btnExcluirAnotacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Anotacao a = new Anotacao();
                a.setId_anotacao(altanotacao.getId_anotacao());
                FormularioLogin.databaseReference.child("Anotacao").child(FormularioLogin.usuario.getUid()).child(a.getId_anotacao()).removeValue();
                Publico.Alerta(AlterarAnotacao.this, "Excluído com Sucesso");
                Publico.Intente(AlterarAnotacao.this, ListaAnotacoes.class);
                limparCampos();
                finish();
            }
        });
    }
    // Função do botão voltar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Publico.Intente(AlterarAnotacao.this, ListaAnotacoes.class);
        finish();
    }

    private void limparCampos() {
        edtAssunto.setText("");
        edtAlteracao.setText("");
    }
}


