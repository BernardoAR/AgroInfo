package com.br.agroinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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
    private List<Categoria> listCategoria = new ArrayList<>() ;
    private ArrayAdapter<Categoria> arrayAdapterCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_categoria);
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
                // Inserir os detalhes no BD
                Categoria c = new Categoria();
                c.setId_categoria(UUID.randomUUID().toString());
                c.setNova_categoria(categoriaStr.toUpperCase());
                FormularioLogin.databaseReference.child("Categoria").child(FormularioLogin.usuario.getUid()).child(c.getId_categoria()).setValue(c);
                Publico.Alerta(NovaCategoria.this, "Categoria Salva com Sucesso");
                edtNovaCat.setText("");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Publico.Intente(NovaCategoria.this, FormProd.class);
        finish();
    }
    // Pegar os Valores
    private void populaLista() {
        FormularioLogin.databaseReference.child("Categoria").child(FormularioLogin.usuario.getUid())
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

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}

