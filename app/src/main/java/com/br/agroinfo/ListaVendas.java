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

import com.br.agroinfo.modelo.Vendas;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListaVendas extends AppCompatActivity {
    Button btnNovaVenda;
    ListView lstVendas;
    ValueEventListener lista;
    private List<Vendas> listVendas = new ArrayList<>() ;
    private ArrayAdapter<Vendas> arrayAdapterVenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_vendas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView titulo = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titulo.setText("VENDAS");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        populaLista();
        // pega o botao
        btnNovaVenda = (Button) findViewById(R.id.btnNovaVenda);

        //Pega a lista

        lstVendas = (ListView) findViewById(R.id.lstVendas);

        // configurar a acao de click
        btnNovaVenda.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Publico.Intente(ListaVendas.this, FormVendas.class);
                finish();

            }
        });

        lstVendas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Vendas vendaEnviada = arrayAdapterVenda.getItem(position);

                Intent abrirEdicao = new Intent(ListaVendas.this, DetalhesVenda.class);
                abrirEdicao.putExtra("Venda-enviada", vendaEnviada);
                // solicitar para abir
                startActivity(abrirEdicao);
                finish();
            }
        });

    }

    private void populaLista() {
        lista = Inicial.databaseReference.child("Vendas").child(Inicial.usuario.getUid()).orderByChild("mes_ano")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listVendas.clear();
                        for (DataSnapshot objSnapshot:dataSnapshot.getChildren()) {
                            Vendas v = objSnapshot.getValue(Vendas.class);
                            listVendas.add(v);
                        }
                        arrayAdapterVenda = new ArrayAdapter<>(ListaVendas.this,
                                android.R.layout.simple_list_item_1, listVendas);
                        lstVendas.setAdapter(arrayAdapterVenda);
                        checaPopulacao();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
    }
    private void checaPopulacao() {
        if (listVendas.isEmpty() || listVendas == null){
            Publico.Alerta(ListaVendas.this, "Nenhuma venda para ser exibida.");
        }
        Inicial.databaseReference.removeEventListener(lista);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Publico.Intente(ListaVendas.this, FormProd.class);
        finish();
    }
}
