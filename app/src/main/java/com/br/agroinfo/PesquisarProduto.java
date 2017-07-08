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
import android.widget.Toast;

import com.br.agroinfo.modelo.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PesquisarProduto extends AppCompatActivity {
    public static String pesquisar;

    EditText pesquisa;
    Button btnPesquisar;

    ListView lstProd;
    private List<Produto> listProduto = new ArrayList<Produto>() ;
    private ArrayAdapter<Produto> arrayAdapterProduto;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar_produto);
        inicFirebase();
        //Pega o que foi digitado no Edit
        pesquisa = (EditText) findViewById(R.id.pesquisa);
        btnPesquisar= (Button) findViewById(R.id.btnPesquisar);
        lstProd = (ListView) findViewById(R.id.lstProd);

        btnPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pesquisar = pesquisa.getText().toString();
                populaLista();
            }
        });

        lstProd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Produto produtoEnviado = (Produto) arrayAdapterProduto.getItem(position);

                Intent abrirVisualizacao = new Intent(PesquisarProduto.this, DetalhesProduto.class);
                abrirVisualizacao.putExtra("Produto-enviado",produtoEnviado);
                // solicitar para abir
                startActivity(abrirVisualizacao);
            }
        });
    }

    public void populaLista(){
        Query query = databaseReference.child("Produto").child("Produtos").orderByChild("nomeProduto").startAt(pesquisa.getText().toString().toUpperCase());
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listProduto.clear();
                        for (DataSnapshot objSnapshot:dataSnapshot.getChildren()) {
                            Produto p = objSnapshot.getValue(Produto.class);
                            listProduto.add(p);
                        }
                        arrayAdapterProduto = new ArrayAdapter<Produto>(PesquisarProduto.this,
                                android.R.layout.simple_list_item_1, listProduto);
                        lstProd.setAdapter(arrayAdapterProduto);
                        checaPopulacao();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
    }
    private void checaPopulacao() {
        if (listProduto.isEmpty() || listProduto == null){
            Toast sem = Toast.makeText(PesquisarProduto.this, "Não foi encontrado resultados", Toast.LENGTH_SHORT);
            sem.show();
        }
    }

    private void inicFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
}
