package com.br.agroinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.br.agroinfo.dao.Conexao;
import com.br.agroinfo.modelo.Categoria;
import com.br.agroinfo.modelo.Produto;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Lista_produtos extends AppCompatActivity {

    ListView listProdutos;
    private List<Produto> listProduto = new ArrayList<>() ;
    private ArrayAdapter<Produto> arrayAdapterProduto;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_produtos);
        inicFirebase();
        populaLista();
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

                Produto produtoEnviado = arrayAdapterProduto.getItem(position);

                Intent abrirEdicao = new Intent(Lista_produtos.this, AlterarProduto.class);
                abrirEdicao.putExtra("Produto-enviado",produtoEnviado);
                // solicitar para abir
                startActivity(abrirEdicao);
            }
        });

    }
    private void inicFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        usuario = Conexao.getFirebaseUser();
    }
    private void populaLista() {
        databaseReference.child("Produto").child("Produtos").orderByChild("Usuario").equalTo(usuario.getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listProduto.clear();
                for (DataSnapshot objSnapshot:dataSnapshot.getChildren()) {
                    Produto p = objSnapshot.getValue(Produto.class);
                    listProduto.add(p);
                }
                arrayAdapterProduto = new ArrayAdapter<>(Lista_produtos.this,
                        android.R.layout.simple_list_item_1, listProduto);
                listProdutos.setAdapter(arrayAdapterProduto);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Lista_produtos.this, FormProd.class);
        startActivity(i);
    }

}
