package com.br.agroinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.br.agroinfo.modelo.Vendas;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListaVendas extends AppCompatActivity {
    Button btnNovaVenda;
    ListView lstVendas;
    private List<Vendas> listVendas = new ArrayList<>() ;
    private ArrayAdapter<Vendas> arrayAdapterVenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_vendas);
        populaLista();
        // pega o botao
        btnNovaVenda = (Button) findViewById(R.id.btnNovaVenda);

        //Pega a lista

        lstVendas = (ListView) findViewById(R.id.lstVendas);

        // configurar a acao de click
        btnNovaVenda.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent abrirFormVendas = new Intent(ListaVendas.this, FormVendas.class);
                // solicitar para abir
                startActivity(abrirFormVendas);
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
        FormularioLogin.databaseReference.child("Vendas").child(FormularioLogin.usuario.getUid()).orderByChild("mes_ano")
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

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Publico.Intente(ListaVendas.this, FormProd.class);
        finish();
    }
}
