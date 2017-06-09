package com.br.projeto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.br.projeto.dao.DAO;
import com.br.projeto.modelo.Categoria;

import java.util.ArrayList;

public class FormProd extends AppCompatActivity {
    // Declarar
    private EditText edtNomeProd;
    Spinner spnCateg;
    private EditText editPrecoCusto;
    private EditText editPrecoVenda;
    private EditText quantidade;
    private EditText DataCadastro;
    private Button btnAdProd;
    public Button btnListProd;
    private Button btnAdCat;
    DAO dao;
    DAO helper = new DAO(this);
    ArrayList<Categoria> arrayListCategoria;
    ArrayAdapter<Categoria> arrayAdapterCategoria;

    //Array Adapter para as Listas
    public ArrayAdapter<String> adpCateg;
    public ArrayAdapter<String> adpProd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_prod);
        // Pega as partes do Layout
        edtNomeProd = (EditText) findViewById(R.id.edtNomeProd);
        spnCateg = (Spinner) findViewById(R.id.spnCateg);
        editPrecoCusto = (EditText) findViewById(R.id.editPrecoCusto);
        editPrecoVenda = (EditText) findViewById(R.id.editPrecoVenda);
        quantidade = (EditText) findViewById(R.id.quantidade);
        DataCadastro = (EditText) findViewById(R.id.DataCadastro);
        btnAdProd = (Button) findViewById(R.id.btnAdProd);
        btnListProd = (Button) findViewById(R.id.btnListProd);


        // Categoria
        Button botaoCateg = (Button) findViewById(R.id.btnAdCat);

        // Configura a Ação de clique
        botaoCateg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent abrirCateg = new Intent(FormProd.this, NovaCategoria.class);
                // solicitar para abir
                startActivity(abrirCateg);

            }
        });

    }

    public void populaLista() {
        dao = new DAO(FormProd.this);
        arrayListCategoria = dao.selectAllCategoria();
        dao.close();

        if (spnCateg != null) {
            arrayAdapterCategoria = new ArrayAdapter<Categoria>(FormProd.this,
                    android.R.layout.simple_spinner_dropdown_item, arrayListCategoria);
            spnCateg.setAdapter(arrayAdapterCategoria);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        populaLista();
    }
}