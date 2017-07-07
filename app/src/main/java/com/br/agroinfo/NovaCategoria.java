package com.br.agroinfo;

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

import com.br.agroinfo.modelo.Categoria;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NovaCategoria extends AppCompatActivity {

    EditText nova_categoria;
    Button btnCadastrar;

    long retornoDB;
    ListView lstCategoria;
    private List<Categoria> listCategoria = new ArrayList<Categoria>() ;
    private ArrayAdapter<Categoria> arrayAdapterCategoria;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_categoria);
        inicFirebase();
        populaLista();

        //Resgatar Componentes
        nova_categoria = (EditText) findViewById(R.id.nova_categoria);
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
        lstCategoria = (ListView) findViewById(R.id.lstCategorias);

        lstCategoria.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                c.setId_categoria(UUID.randomUUID().toString());
                c.setNova_categoria(categoriastr);
                databaseReference.child("Categoria").child(MenuP.usuario.getUid()).child(c.getId_categoria()).setValue(c);
            }
        });
    }
    private void inicFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    // Pegar os Valores
    private void populaLista() {
        DatabaseReference db  = databaseReference.child("Categoria").child(MenuP.usuario.getUid());
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listCategoria.clear();
                for (DataSnapshot objSnapshot:dataSnapshot.getChildren()) {
                    Categoria c = objSnapshot.getValue(Categoria.class);
                    listCategoria.add(c);
                }
                arrayAdapterCategoria = new ArrayAdapter<Categoria>(NovaCategoria.this,
                        android.R.layout.simple_list_item_1, listCategoria);
                lstCategoria.setAdapter(arrayAdapterCategoria);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

