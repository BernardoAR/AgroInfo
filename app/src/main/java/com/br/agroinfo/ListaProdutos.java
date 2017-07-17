package com.br.agroinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.br.agroinfo.modelo.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListaProdutos extends AppCompatActivity {
    Button btnNovoProd;
    ListView listProdutos;
    private List<Produto> listProduto = new ArrayList<>() ;
    private ArrayAdapter<Produto> arrayAdapterProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_produtos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView titulo = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titulo.setText("Produtos");
        populaLista();
        // pega o botao
        btnNovoProd = (Button) findViewById(R.id.btnNovoProd);

        //Pega a lista

        listProdutos = (ListView) findViewById(R.id.lstProdutos);

        // configurar a acao de click
        btnNovoProd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Publico.Intente(ListaProdutos.this, FormProd.class);
                // solicitar para abir
                finish();

            }
        });

        listProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Produto produtoEnviado = arrayAdapterProduto.getItem(position);

                Intent abrirEdicao = new Intent(ListaProdutos.this, AlterarProduto.class);
                abrirEdicao.putExtra("Produto-enviado",produtoEnviado);
                // solicitar para abir
                startActivity(abrirEdicao);
                finish();
            }
        });

    }

    private void populaLista() {
        FormularioLogin.databaseReference.child("Produto").child("Produtos").orderByChild("Usuario").equalTo(FormularioLogin.usuario.getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listProduto.clear();
                for (DataSnapshot objSnapshot:dataSnapshot.getChildren()) {
                    Produto p = objSnapshot.getValue(Produto.class);
                    listProduto.add(p);
                }
                arrayAdapterProduto = new ArrayAdapter<>(ListaProdutos.this,
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
        Publico.Intente(ListaProdutos.this, FormProd.class);
        finish();
    }

}
