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
import com.br.projeto.modelo.Anotacao;

import java.util.ArrayList;

public class Lista_anotacoes extends AppCompatActivity {

    ListView listAnotacoes;
    Anotacao anotacao;
    DAO dao;
    ArrayList<Anotacao> arrayListAnotacao;
    ArrayAdapter<Anotacao> arrayAdapterAnotacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_anotacoes);

        // pega o botao
        Button botao = (Button) findViewById(R.id.btnNovaAnotacao);

        //Pega a lista

        listAnotacoes = (ListView) findViewById(R.id.lstAnotacoes);

        // configurar a acao de click
        botao.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent abrirNova_anotacao = new Intent(Lista_anotacoes.this, Nova_anotacao.class);
                // solicitar para abir
                startActivity(abrirNova_anotacao);

            }
        });

        listAnotacoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Anotacao anotacaoEnviada = (Anotacao) arrayAdapterAnotacao.getItem(position);

                Intent abrirEdicao = new Intent(Lista_anotacoes.this, alterar_anotacao.class);
                abrirEdicao.putExtra("Anotacao-enviada",anotacaoEnviada);
                // solicitar para abir
                startActivity(abrirEdicao);
            }
        });
    }

    public void populaLista(){
        dao = new DAO(Lista_anotacoes.this);
        arrayListAnotacao = dao.selectAllAnotacao();
        dao.close();

        if(listAnotacoes != null){
            arrayAdapterAnotacao = new ArrayAdapter<Anotacao>(Lista_anotacoes.this,
                    android.R.layout.simple_list_item_1,arrayListAnotacao);
            listAnotacoes.setAdapter(arrayAdapterAnotacao);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        populaLista();
    }
}
