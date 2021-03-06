package com.br.agroinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.br.agroinfo.modelo.Categoria;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NovaCategoria extends AppCompatActivity {

    EditText edtNovaCat;
    Button btnCadastrar;
    ListView lstCategoria;
    String categoriaStr;
    ValueEventListener lista;
    private List<Categoria> listCategoria = new ArrayList<>() ;
    private ArrayAdapter<Categoria> arrayAdapterCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_categoria);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView titulo = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titulo.setText("NOVA CATEGORIA");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        populaLista();

        //Resgatar Componentes
        edtNovaCat = (EditText) findViewById(R.id.nova_categoria);
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
        lstCategoria = (ListView) findViewById(R.id.lstCategorias);

        lstCategoria.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Categoria categoriaEnviada = arrayAdapterCategoria.getItem(position);

                Intent abrirEdicao = new Intent(NovaCategoria.this, AlterarCategoria.class);
                abrirEdicao.putExtra("Categoria-enviada",categoriaEnviada);
                // solicitar para abir
                startActivity(abrirEdicao);
                finish();
            }
        });

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoriaStr = edtNovaCat.getText().toString();
                if (!categoriaStr.isEmpty()){
                    Inicial.databaseReference.child("Categoria").child(Inicial.usuario.getUid()).orderByChild("nova_categoria").equalTo(categoriaStr.toUpperCase())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        Publico.Alerta(NovaCategoria.this, "Categoria já existe!");
                                    } else {
                                        // Inserir os detalhes no BD
                                        Categoria c = new Categoria();
                                        c.setId_categoria(UUID.randomUUID().toString());
                                        c.setNova_categoria(categoriaStr.toUpperCase());
                                        Inicial.databaseReference.child("Categoria").child(Inicial.usuario.getUid()).child(c.getId_categoria()).setValue(c);
                                        Publico.Alerta(NovaCategoria.this, "Categoria Salva com Sucesso");
                                        edtNovaCat.setText("");
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {}
                            });
                } else {
                    Publico.Alerta(NovaCategoria.this, "É necessário preencher o campo com o nome de uma Categoria");
                }
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Publico.Intente(NovaCategoria.this, FormProd.class);
        finish();
    }
    // Pegar os Valores
    private void populaLista() {
        lista = Inicial.databaseReference.child("Categoria").child(Inicial.usuario.getUid()).orderByChild("nova_categoria")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listCategoria.clear();
                for (DataSnapshot objSnapshot:dataSnapshot.getChildren()) {
                    Categoria c = objSnapshot.getValue(Categoria.class);
                    listCategoria.add(c);
                }
                arrayAdapterCategoria = new ArrayAdapter<>(NovaCategoria.this,
                        android.R.layout.simple_list_item_1, listCategoria);
                lstCategoria.setAdapter(arrayAdapterCategoria);
                checaPopulacao();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
    private void checaPopulacao() {
        if (listCategoria.isEmpty() || listCategoria == null){
            Publico.Alerta(NovaCategoria.this, "Nenhuma categoria para ser exibida.");
        }
        Inicial.databaseReference.removeEventListener(lista);
    }
}

