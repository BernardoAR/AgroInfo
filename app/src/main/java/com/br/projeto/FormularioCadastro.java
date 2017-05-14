package com.br.projeto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.br.projeto.modelo.Usuario;

import com.br.projeto.modelo.DAO;
import com.br.projeto.modelo.Usuario;

public class FormularioCadastro extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_cadastro);
        //resgatar os componentes

        final TextView campoNome =  (TextView) findViewById(R.id.textNome);
        final TextView campoEmail =  (TextView) findViewById(R.id.textEmail);
        final TextView campoSenha = (TextView) findViewById(R.id.textSenha);
        final TextView campoConfirmar_senha =  (TextView) findViewById(R.id.textConfirmar_senha);
        Button btn =  (Button) findViewById(R.id.btnCadastrar);


        //Associar o Click ao botão

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Usuario novo = new Usuario();
                novo.setNome(campoNome.getText().toString());
                novo.setEmail(campoEmail.getText().toString());
                novo.setSenha(campoSenha.getText().toString());
                novo.setConfirmar_senha(campoConfirmar_senha.getText().toString());

                //Cadastrar

                DAO.gravar(novo);

                AlertDialog.Builder dialog = new AlertDialog.Builder(FormularioCadastro.this);
                dialog.setTitle("Projeto");
                dialog.setMessage("Cadastro realizado com sucesso!");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FormularioCadastro.this.finish();

                    }
                });

                dialog.show();

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cadastro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
