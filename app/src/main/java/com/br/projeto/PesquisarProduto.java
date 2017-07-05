package com.br.projeto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.br.projeto.dao.DAO;
import com.br.projeto.modelo.Produto;

import java.util.ArrayList;

public class PesquisarProduto extends AppCompatActivity {
    public static String pesquisar;

    EditText pesquisa;
    Button btnPesquisar;
    Produto produto;
    ArrayAdapter<Produto> arrayAdapterProd;
    ArrayList<Produto> arrayListProduto;
    ListView lstProd;
    DAO dao;
    long retornoDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar_produto);
        dao = new DAO(this);
        //Pega o que foi digitado no Edit
        pesquisa = (EditText) findViewById(R.id.pesquisa);
        btnPesquisar= (Button) findViewById(R.id.btnPesquisar);
        lstProd = (ListView) findViewById(R.id.lstProd);

        btnPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pesquisar = pesquisa.getText().toString();
                populaLista();
                if (arrayListProduto.isEmpty() || arrayListProduto == null){
                    Toast sem = Toast.makeText(PesquisarProduto.this, "Não foi encontrado resultados", Toast.LENGTH_SHORT);
                    sem.show();
                }
            }
        });

        lstProd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Produto produtoEnviado = (Produto) arrayAdapterProd.getItem(position);

                Intent abrirVisualizacao = new Intent(PesquisarProduto.this, DetalhesProduto.class);
                abrirVisualizacao.putExtra("Produto-enviado",produtoEnviado);
                // solicitar para abir
                startActivity(abrirVisualizacao);
            }
        });
    }

    public void populaLista(){
        dao = new DAO(PesquisarProduto.this);
        arrayListProduto = dao.selectAllProdutos();
        dao.close();

        if(lstProd != null){
            arrayAdapterProd = new ArrayAdapter<Produto>(PesquisarProduto.this,
                    android.R.layout.simple_list_item_1,arrayListProduto);
            lstProd.setAdapter(arrayAdapterProd);
        }
    }

}
