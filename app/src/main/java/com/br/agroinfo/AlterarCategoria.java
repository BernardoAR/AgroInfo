package com.br.agroinfo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.br.agroinfo.dao.Conexao;
import com.br.agroinfo.modelo.Categoria;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AlterarCategoria extends AppCompatActivity {
    EditText alteracaoCat;
    Button btnSalvarCategoria;
    Categoria altcategoria;
    Button btnExcluirCategoria;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_categoria);
        inicializarFirebase();

        Intent abrirEdicao = getIntent();
        altcategoria = (Categoria) abrirEdicao.getSerializableExtra("Categoria-enviada");
        //resgatar os componentes

        alteracaoCat = (EditText) findViewById(R.id.alteracaoCat);
        btnSalvarCategoria = (Button) findViewById(R.id.btnSalvarCategoria);
        btnExcluirCategoria = (Button) findViewById(R.id.btnExcluirCategoria);

        if (altcategoria != null) {
            alteracaoCat.setText(altcategoria.getNova_categoria());
        }

        btnSalvarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Categoria c = new Categoria();
                c.setId_categoria(altcategoria.getId_categoria());
                c.setNova_categoria(alteracaoCat.getText().toString().toUpperCase());
                databaseReference.child("Categoria").child(usuario.getUid()).child(c.getId_categoria()).setValue(c);
                alerta("Alterado com Sucesso");
                finish();
                // Limpa Campo
                alteracaoCat.setText("");
            }
        });

        btnExcluirCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Categoria c = new Categoria();
                c.setId_categoria(altcategoria.getId_categoria());
                databaseReference.child("Categoria").child(usuario.getUid()).child(c.getId_categoria()).removeValue();
                alerta("Excluído com Sucesso");
                finish();
                // Limpa Campo
                alteracaoCat.setText("");
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AlterarCategoria.this, NovaCategoria.class);
        startActivity(i);
    }
    private void alerta(String mensagem) {
        Toast.makeText(AlterarCategoria.this, mensagem, Toast.LENGTH_SHORT).show();
    }
    private void inicializarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        usuario = Conexao.getFirebaseUser();
    }
}
