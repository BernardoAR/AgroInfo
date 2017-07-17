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

public class AlterarCategoria extends AppCompatActivity {
    EditText edtAltCat;
    Button btnSalvarCategoria;
    Categoria altcategoria;
    Button btnExcluirCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_categoria);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView titulo = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titulo.setText("Alterar Categoria");

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
                Categoria c = new Categoria();
                c.setId_categoria(altcategoria.getId_categoria());
                c.setNova_categoria(edtAltCat.getText().toString().toUpperCase());
                FormularioLogin.databaseReference.child("Categoria").child(FormularioLogin.usuario.getUid()).child(c.getId_categoria()).setValue(c);
                Publico.Alerta(AlterarCategoria.this, "Alterado com Sucesso");
                Publico.Intente(AlterarCategoria.this, NovaCategoria.class);
                finish();
                // Limpa Campo
                edtAltCat.setText("");
            }
        });

        btnExcluirCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogo = new AlertDialog.Builder(AlterarCategoria.this);
                dialogo.setCancelable(false);
                dialogo.setTitle("AgroInfo");
                dialogo.setMessage("Você tem certeza que quer excluir a Categoria? Além da categoria, as vendas e os produtos incluídos na categoria também serão excluídas!");
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
        Query produtos = FormularioLogin.databaseReference.child("Produto").child("Produtos")
                .orderByChild("Categoria").equalTo(altcategoria.getId_categoria());
        produtos.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    Produto p = dataSnapshot.getValue(Produto.class);
                    String id_produto = p.getId_produto();
                    for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                        Query vendas = FormularioLogin.databaseReference.child("Vendas").child(FormularioLogin.usuario.getUid())
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
        FormularioLogin.databaseReference.child("Categoria").child(FormularioLogin.usuario.getUid()).child(altcategoria.getId_categoria()).removeValue();
        Publico.Alerta(AlterarCategoria.this, "Excluído com Sucesso");
        // Limpa Campo
        edtAltCat.setText("");
        Publico.Intente(AlterarCategoria.this, NovaCategoria.class);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Publico.Intente(AlterarCategoria.this, NovaCategoria.class);
        finish();
    }
}
