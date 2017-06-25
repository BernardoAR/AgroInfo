package com.br.projeto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.br.projeto.dao.DAO;
import com.br.projeto.modelo.Produto;

import java.util.ArrayList;

public class Lista_produtos extends AppCompatActivity {

    ListView listProdutos;
    Produto produto;
    DAO dao;
    ArrayList<Produto> arrayListProduto;
    ArrayAdapter<Produto> arrayAdapterProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_produtos);

        // pega o botao
        Button botao = (Button) findViewById(R.id.btnNovoProd);

        //Pega a lista

        listProdutos = (ListView) findViewById(R.id.lstProdutos);

        // configurar a acao de click
        botao.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent abrirFormProd = new Intent(Lista_produtos.this, FormProd.class);
                // solicitar para abir
                startActivity(abrirFormProd);

            }
        });

        listProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Produto produtoEnviado = (Produto) arrayAdapterProduto.getItem(position);


                Intent abrirEdicao = new Intent(Lista_produtos.this, AlterarProduto.class);
                abrirEdicao.putExtra("Produto-enviado",produtoEnviado);
                // solicitar para abir
                startActivity(abrirEdicao);
            }
        });

    }

    public void populaLista(){
        dao = new DAO(Lista_produtos.this);
        arrayListProduto = dao.selectAllProduto();
        dao.close();

        if(listProdutos != null){
            arrayAdapterProduto = new ArrayAdapter<Produto>(Lista_produtos.this,
                    android.R.layout.simple_list_item_1,arrayListProduto);
            listProdutos.setAdapter(arrayAdapterProduto);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        populaLista();
    }

}
