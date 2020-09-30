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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListaProdutos extends AppCompatActivity {
    Button btnNovoProd;
    ListView listProdutos;
    private List<Produto> listProduto = new ArrayList<>() ;
    private ArrayAdapter<Produto> arrayAdapterProduto;
    ValueEventListener pop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_produtos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView titulo = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titulo.setText("PRODUTOS");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
        pop = Inicial.databaseReference.child("Produto").child("Produtos").orderByChild("Usuario").equalTo(Inicial.usuario.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listProduto.clear();
                        for (DataSnapshot objSnapshot:dataSnapshot.getChildren()) {
                            Produto p = objSnapshot.getValue(Produto.class);
                            listProduto.add(p);
                        }

                        Collections.sort(listProduto, new Comparator<Produto>() {
                            @Override
                            public int compare(Produto o1, Produto o2) {
                                return o1.getNomeProduto().compareTo(o2.getNomeProduto());
                            }
                        });

                        arrayAdapterProduto = new ArrayAdapter<>(ListaProdutos.this,
                                android.R.layout.simple_list_item_1, listProduto);
                        listProdutos.setAdapter(arrayAdapterProduto);

                        checaPopulacao();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
    }

    private void checaPopulacao() {
        if (listProduto.isEmpty() || listProduto == null){
            Publico.Alerta(ListaProdutos.this, "Nenhum produto para ser exibido.");
        }
        Inicial.databaseReference.removeEventListener(pop);

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Publico.Intente(ListaProdutos.this, FormProd.class);
        finish();
    }

}