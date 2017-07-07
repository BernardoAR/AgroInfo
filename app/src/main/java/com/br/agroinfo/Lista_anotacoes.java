package com.br.agroinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.br.agroinfo.modelo.Anotacao;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Lista_anotacoes extends AppCompatActivity {
    private long a;
    ListView listAnotacoes;
    Anotacao anotacao;
    private List<Anotacao> listAnotacao = new ArrayList<Anotacao>() ;
    private ArrayAdapter<Anotacao> arrayAdapterAnotacao;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    private FirebaseUser usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_anotacoes);
        //Modificar na Nuvem e no Aplicativo
        inicFirebase();
        populaLista();
        // pega o botao
        Button botao = (Button) findViewById(R.id.btnNovaAnotacao);

        //Pega a lista

        listAnotacoes = (ListView) findViewById(R.id.lstAnotacoes);

        // configurar a acao de click
        botao.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent abrirNova_anotacao = new Intent(Lista_anotacoes.this, Nova_anotacao.class);
                // solicitar para abir
                startActivity(abrirNova_anotacao);

            }
        });

        listAnotacoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Anotacao anotacaoEnviada = (Anotacao) arrayAdapterAnotacao.getItem(position);

                Intent abrirEdicao = new Intent(Lista_anotacoes.this, alterar_anotacao.class);
                abrirEdicao.putExtra("Anotacao-enviada",anotacaoEnviada);
                // solicitar para abir
                startActivity(abrirEdicao);
            }
        });
    }

    private void inicFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    // Pegar os Valores
    private void populaLista() {
        DatabaseReference db  = databaseReference.child("Anotacao").child(MenuP.usuario.getUid());
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listAnotacao.clear();
                    for (DataSnapshot objSnapshot:dataSnapshot.getChildren()) {
                        Anotacao a = objSnapshot.getValue(Anotacao.class);
                        listAnotacao.add(a);
                    }
                arrayAdapterAnotacao = new ArrayAdapter<Anotacao>(Lista_anotacoes.this,
                        android.R.layout.simple_list_item_1, listAnotacao);
                listAnotacoes.setAdapter(arrayAdapterAnotacao);

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
