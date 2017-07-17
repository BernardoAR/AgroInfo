package com.br.agroinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.br.agroinfo.modelo.Anotacao;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListaAnotacoes extends AppCompatActivity {
    ListView listAnotacoes;
    Button btnNovaAn;
    private List<Anotacao> listAnotacao = new ArrayList<>();
    private ArrayAdapter<Anotacao> arrayAdapterAnotacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_anotacoes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView titulo = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titulo.setText("Anotações");
        //Modificar na Nuvem e no Aplicativo
        populaLista();
        // pega o botao
        btnNovaAn = (Button) findViewById(R.id.btnNovaAnotacao);

        //Pega a lista
        listAnotacoes = (ListView) findViewById(R.id.lstAnotacoes);

        // configurar a acao de clique
        btnNovaAn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Publico.Intente(ListaAnotacoes.this, NovaAnotacao.class);
                // solicitar para abir
                finish();

            }
        });

        listAnotacoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Anotacao anotacaoEnviada = arrayAdapterAnotacao.getItem(position);

                Intent abrirEdicao = new Intent(ListaAnotacoes.this, AlterarAnotacao.class);
                abrirEdicao.putExtra("Anotacao-enviada",anotacaoEnviada);
                // solicitar para abir
                startActivity(abrirEdicao);
                finish();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Publico.Intente(ListaAnotacoes.this, MenuP.class);
        finish();
    }
    // Pegar os Valores
    private void populaLista() {
        FormularioLogin.databaseReference.child("Anotacao").child(FormularioLogin.usuario.getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listAnotacao.clear();
                    for (DataSnapshot objSnapshot:dataSnapshot.getChildren()) {
                        Anotacao a = objSnapshot.getValue(Anotacao.class);
                        listAnotacao.add(a);
                    }
                arrayAdapterAnotacao = new ArrayAdapter<>(ListaAnotacoes.this,
                        android.R.layout.simple_list_item_1, listAnotacao);
                listAnotacoes.setAdapter(arrayAdapterAnotacao);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

}
