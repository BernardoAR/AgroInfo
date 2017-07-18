package com.br.agroinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.br.agroinfo.modelo.Produto;
import com.br.agroinfo.modelo.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;

public class DetalhesProduto extends AppCompatActivity {
    TextView textNomeProduto, textPrecoVenda, textQuantidadeDisp, textNomeEmp, textEndereco, textTelefoneCont;
    Produto mostraProduto;
    BigDecimal precoVendas;
    String id, idus;
    String nomeProd, possEst, empVen, tel, end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_produto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView titulo = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titulo.setText("DETALHES DO PRODUTO");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent abrirVisualizacao = getIntent();
        mostraProduto = (Produto) abrirVisualizacao.getSerializableExtra("Produto-enviado");

        //Resgatar Componentes
        textNomeProduto = (TextView) findViewById(R.id.textNomeProduto);
        textPrecoVenda = (TextView) findViewById(R.id.textPrecoVenda);
        textQuantidadeDisp = (TextView) findViewById(R.id.textQuantidadeDisp);
        textNomeEmp = (TextView) findViewById(R.id.textNomeEmp);
        textEndereco = (TextView) findViewById(R.id.textEndereco);
        textTelefoneCont = (TextView) findViewById(R.id.textTelefoneCont);

        if (mostraProduto != null){
            id = mostraProduto.getId_produto();
            nomeProd = ("Nome do Produto: " + mostraProduto.getNomeProduto());
            textNomeProduto.setText(nomeProd);
            int quantidade = mostraProduto.getQuantidade();
            if (quantidade >= 1){
                possEst = ("Possui Estoque: Possui");
                textQuantidadeDisp.setText(possEst);
            } else {
                possEst = ("Possui Estoque: Não possui");
                textQuantidadeDisp.setText(possEst);
            }
            float precoVenda = mostraProduto.getPrecoVenda();
            precoVendas = Publico.Casas(precoVenda);
            textPrecoVenda.setText("Preço de Venda: " + String.valueOf(precoVendas) + "R$");
            pegaDadoExt();
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Publico.Intente(DetalhesProduto.this, PesquisarProduto.class);
        finish();
    }

    private void pegaDadoExt() {
        FormularioLogin.databaseReference.child("Produto").child("Produtos").child(id).child("Usuario").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                idus = dataSnapshot.getValue().toString();
                pegaDadosExternos();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void pegaDadosExternos() {
        FormularioLogin.databaseReference.child("Usuario").child(idus).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario u = dataSnapshot.getValue(Usuario.class);
                empVen = ("Empresário/Vendedor: " + u.getNome());
                textNomeEmp.setText(empVen);
                if (!u.getTelefone().trim().isEmpty()){
                    tel = ("Telefone: " + u.getTelefone());
                    textTelefoneCont.setText(tel);
                } else {
                    tel = ("Telefone: Não Informado");
                    textTelefoneCont.setText(tel);
                }
                if (!u.getEndereco().trim().isEmpty()){
                    end = ("Endereço: " + u.getEndereco());
                    textEndereco.setText(end);
                } else {
                    end = ("Endereço: Não Informado");
                    textEndereco.setText(end);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
