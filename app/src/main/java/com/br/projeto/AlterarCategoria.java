package com.br.projeto;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.br.projeto.dao.DAO;
import com.br.projeto.modelo.Categoria;

public class AlterarCategoria extends AppCompatActivity {
    EditText alteracaoCat;
    Button btnSalvarCategoria;
    Categoria categoria, altcategoria;
    Button btnExcluirCategoria;
    DAO dao;
    long retornoDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_categoria);

        Intent abrirEdicao = getIntent();
        altcategoria = (Categoria) abrirEdicao.getSerializableExtra("Categoria-enviada");
        categoria = new Categoria();
        dao = new DAO(AlterarCategoria.this);

        //resgatar os componentes

        alteracaoCat = (EditText) findViewById(R.id.alteracaoCat);
        btnSalvarCategoria = (Button) findViewById(R.id.btnSalvarCategoria);
        btnExcluirCategoria = (Button) findViewById(R.id.btnExcluirCategoria);

        if (altcategoria != null) {
            alteracaoCat.setText(altcategoria.getNova_categoria());
            categoria.setId_categoria(altcategoria.getId_categoria());
        }

        btnSalvarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoria.setId_categoria(altcategoria.getId_categoria());
                categoria.setNova_categoria(alteracaoCat.getText().toString());

                dao.alterarCategoria(categoria);
                dao.close();


                if (retornoDB == -1) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(AlterarCategoria.this);
                    dialog.setTitle("AgroInfo");
                    dialog.setMessage("Erro ao alterar!");
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            AlterarCategoria.this.finish();
                        }
                    });


                    dialog.show();

                } else {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(AlterarCategoria.this);
                    dialog.setTitle("AgroInfo");
                    dialog.setMessage("Alterado com sucesso!");
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            AlterarCategoria.this.finish();
                        }
                    });


                    dialog.show();
                }
            }
        });


        btnExcluirCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                categoria.setId_categoria(altcategoria.getId_categoria());

                dao.excluirCategoria(categoria);
                dao.close();


                if (retornoDB == -1) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(AlterarCategoria.this);
                    dialog.setTitle("AgroInfo");
                    dialog.setMessage("Erro ao excluir o registro!");
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            AlterarCategoria.this.finish();
                        }
                    });


                    dialog.show();

                } else {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(AlterarCategoria.this);
                    dialog.setTitle("AgroInfo");
                    dialog.setMessage("Registro excluído com sucesso!");
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            AlterarCategoria.this.finish();
                        }
                    });


                    dialog.show();
                }

            }
        });

    }
}
