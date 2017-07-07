package com.br.agroinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.br.agroinfo.modelo.Produto;

import java.math.BigDecimal;

public class DetalhesProduto extends AppCompatActivity {
    TextView textNomeProduto, textPrecoVenda, textQuantidadeDisp, textNomeEmp, textEndereco, textTelefoneCont;
    Produto mostraProduto;
    BigDecimal precoVendas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_produto);

        Intent abrirVisualizacao = getIntent();
        mostraProduto = (Produto) abrirVisualizacao.getSerializableExtra("Produto-enviado");

/*
        //Resgatar Componentes
        textNomeProduto = (TextView) findViewById(R.id.textNomeProduto);
        textPrecoVenda = (TextView) findViewById(R.id.textPrecoVenda);
        textQuantidadeDisp = (TextView) findViewById(R.id.textQuantidadeDisp);
        textNomeEmp = (TextView) findViewById(R.id.textNomeEmp);
        textEndereco = (TextView) findViewById(R.id.textEndereco);
        textTelefoneCont = (TextView) findViewById(R.id.textTelefoneCont);

        if (mostraProduto != null){
            String id = mostraProduto.getId_produto();
            textNomeProduto.setText("Nome do Produto: " + mostraProduto.getNomeProduto());
            int quantidade = mostraProduto.getQuantidade();
            if (quantidade >= 1){
                textQuantidadeDisp.setText("Possui Estoque: Possui");
            } else { textQuantidadeDisp.setText("Possui Estoque: Possui"); }
            dao.getIDUsP(id);
            precoVendas = FormProd.casas(dao.precoVenda, 2);
            textPrecoVenda.setText("Preço de Venda: " + String.valueOf(precoVendas) + "R$");
            dao.getDetProd();
            textNomeEmp.setText("Empresário/Vendedor: " + dao.nome_emp);
            textTelefoneCont.setText("Telefone: " + dao.telefon);
            textEndereco.setText("Endereço: " + dao.enderexo);

        }
*/
    }
}
