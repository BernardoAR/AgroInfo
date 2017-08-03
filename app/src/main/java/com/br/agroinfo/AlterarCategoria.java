package com.br.agroinfo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.br.agroinfo.modelo.Categoria;
import com.br.agroinfo.modelo.Produto;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AlterarCategoria extends AppCompatActivity {
    EditText edtAltCat;
    Button btnSalvarCategoria;
    Categoria altcategoria;
    Button btnExcluirCategoria;
    ChildEventListener exclusao;
    String strCat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_categoria);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView titulo = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titulo.setText("ALTERAR CATEGORIA");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent abrirEdicao = getIntent();
        altcategoria = (Categoria) abrirEdicao.getSerializableExtra("Categoria-enviada");
        //resgatar os componentes

        edtAltCat = (EditText) findViewById(R.id.alteracaoCat);
        btnSalvarCategoria = (Button) findViewById(R.id.btnSalvarCategoria);
        btnExcluirCategoria = (Button) findViewById(R.id.btnExcluirCategoria);

        if (altcategoria != null) {
            edtAltCat.setText(altcategoria.getNova_categoria());
        }

        btnSalvarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strCat = edtAltCat.getText().toString();
                if (!strCat.isEmpty()){
                    Inicial.databaseReference.child("Categoria").child(Inicial.usuario.getUid()).orderByChild("nova_categoria").equalTo(strCat.toUpperCase())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        Publico.Alerta(AlterarCategoria.this, "Categoria já existe!");
                                    } else {
                                        Categoria c = new Categoria();
                                        c.setId_categoria(altcategoria.getId_categoria());
                                        c.setNova_categoria(strCat.toUpperCase());
                                        Inicial.databaseReference.child("Categoria").child(Inicial.usuario.getUid()).child(c.getId_categoria()).setValue(c);
                                        Publico.Alerta(AlterarCategoria.this, "Alterado com Sucesso");
                                        Publico.Intente(AlterarCategoria.this, NovaCategoria.class);
                                        finish();
                                        // Limpa Campo
                                        edtAltCat.setText("");
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {}
                            });
                } else {
                    Publico.Alerta(AlterarCategoria.this, "É necessário preencher o campo com o nome de uma Categoria");
                }
            }
        });

        btnExcluirCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogo = new AlertDialog.Builder(AlterarCategoria.this);
                dialogo.setCancelable(false);
                dialogo.setTitle("AgroInfo");
                dialogo.setMessage("Você deseja realmente excluir a Categoria? Além da categoria, as vendas e os produtos incluídos na categoria também serão excluídas!");
                dialogo.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deletarCategoria();
                        dialog.cancel();
                    }
                });
                dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialogos = dialogo.create();
                dialogos.show();
            }
        });

    }

    private void deletarCategoria() {

        exclusao = Inicial.databaseReference.child("Produto").child("Produtos")
                .orderByChild("Categoria").equalTo(altcategoria.getId_categoria())
                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    Produto p = dataSnapshot.getValue(Produto.class);
                    String id_produto = p.getId_produto();
                    for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                        Query vendas = Inicial.databaseReference.child("Vendas").child(Inicial.usuario.getUid())
                                .orderByChild("Id_produto").equalTo(id_produto);
                        vendas.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                if (dataSnapshot.exists()){
                                for (DataSnapshot objSnapshot2 : dataSnapshot.getChildren()) {
                                    objSnapshot2.getRef().setValue(null);
                                }
                                }
                            }
                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {}
                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        });
                        objSnapshot.getRef().setValue(null);
                    }
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}

        });
        Inicial.databaseReference.child("Categoria").child(Inicial.usuario.getUid()).child(altcategoria.getId_categoria()).removeValue();
        Publico.Alerta(AlterarCategoria.this, "Excluído com Sucesso");
        // Limpa Campo
        edtAltCat.setText("");
        Inicial.databaseReference.removeEventListener(exclusao);
        Publico.Intente(AlterarCategoria.this, NovaCategoria.class);
        finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Publico.Intente(AlterarCategoria.this, NovaCategoria.class);
        finish();
    }
}
