package com.br.agroinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_produto);
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
            textNomeProduto.setText("Nome do Produto: " + mostraProduto.getNomeProduto());
            int quantidade = mostraProduto.getQuantidade();
            if (quantidade >= 1){
                textQuantidadeDisp.setText("Possui Estoque: Possui");
            } else { textQuantidadeDisp.setText("Possui Estoque: Possui"); }
            float precoVenda = mostraProduto.getPrecoVenda();
            precoVendas = FormProd.casas(precoVenda, 2);
            textPrecoVenda.setText("Preço de Venda: " + String.valueOf(precoVendas) + "R$");
            pegaDadoExt();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(DetalhesProduto.this, PesquisarProduto.class);
        startActivity(i);
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
                textNomeEmp.setText("Empresário/Vendedor: " + u.getNome());
                if (!u.getTelefone().trim().isEmpty()){
                    textTelefoneCont.setText("Telefone: " + u.getTelefone());
                } else {
                    textTelefoneCont.setText("Telefone: Não Informado");
                }
                if (!u.getEndereco().trim().isEmpty()){
                    textEndereco.setText("Endereço: " + u.getEndereco());
                } else {
                    textEndereco.setText("Endereço: Não Informado");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
