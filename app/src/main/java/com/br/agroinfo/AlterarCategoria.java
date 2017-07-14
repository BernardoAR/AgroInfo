package com.br.agroinfo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.br.agroinfo.modelo.Categoria;

public class AlterarCategoria extends AppCompatActivity {
    EditText edtAltCat;
    Button btnSalvarCategoria;
    Categoria altcategoria;
    Button btnExcluirCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_categoria);

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
                Categoria c = new Categoria();
                c.setId_categoria(altcategoria.getId_categoria());
                FormularioLogin.databaseReference.child("Categoria").child(FormularioLogin.usuario.getUid()).child(c.getId_categoria()).removeValue();
                Publico.Alerta(AlterarCategoria.this, "Excluído com Sucesso");
                Publico.Intente(AlterarCategoria.this, NovaCategoria.class);
                finish();
                // Limpa Campo
                edtAltCat.setText("");
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Publico.Intente(AlterarCategoria.this, NovaCategoria.class);
        finish();
    }
}
