package com.br.projeto;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.br.projeto.dao.DAO;
import com.br.projeto.modelo.Anotacao;
import com.br.projeto.modelo.Categoria;

import java.util.ArrayList;

public class NovaCategoria extends AppCompatActivity {

    EditText nova_categoria;
    Button btnCadastrar;
    DAO dao;
    DAO helper = new DAO(this);
    long retornoDB;
    ListView listCategoria;
    ArrayList<Categoria> arrayListCategoria;
    ArrayAdapter<Categoria> arrayAdapterCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_categoria);

        //Resgatar Componentes
        nova_categoria = (EditText) findViewById(R.id.nova_categoria);
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
        listCategoria = (ListView) findViewById(R.id.lstCategorias);

        listCategoria.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Categoria categoriaEnviada = (Categoria) arrayAdapterCategoria.getItem(position);

                Intent abrirEdicao = new Intent(NovaCategoria.this, AlterarCategoria.class);
                abrirEdicao.putExtra("Categoria-enviada",categoriaEnviada);
                // solicitar para abir
                startActivity(abrirEdicao);
            }
        });

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Pegar Strings
                EditText nova_categoria = (EditText)findViewById(R.id.nova_categoria);

                // Colocar em Strings
                String categoriastr = nova_categoria.getText().toString();

                // Inserir os detalhes no BD
                Categoria c = new Categoria();
                c.setNova_categoria(categoriastr);

                helper.salvarCategoria(c);

                if (retornoDB == -1) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(NovaCategoria.this);
                    dialog.setTitle("AgroInfo");
                    dialog.setMessage("Erro ao cadastrar!");
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            NovaCategoria.this.finish();
                        }
                    });


                    dialog.show();

                } else {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(NovaCategoria.this);
                    dialog.setTitle("AgroInfo");
                    dialog.setMessage("Cadastrado com sucesso!");
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            NovaCategoria.this.finish();
                        }
                    });


                    dialog.show();
                }

            }
        });
    }
    public void populaLista(){
        dao = new DAO(NovaCategoria.this);
        arrayListCategoria = dao.selectAllCategoria();
        dao.close();

        if(listCategoria != null){
            arrayAdapterCategoria = new ArrayAdapter<Categoria>(NovaCategoria.this,
                    android.R.layout.simple_list_item_1,arrayListCategoria);
            listCategoria.setAdapter(arrayAdapterCategoria);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        populaLista();
    }
}

