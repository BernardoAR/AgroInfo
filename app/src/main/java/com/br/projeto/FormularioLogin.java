package com.br.projeto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FormularioLogin extends Activity {

    //Criando os objetos necessários

    EditText editEmail;
    EditText editSenha;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_login);

        //Vinculando os objetos aos IDs
        editEmail = (EditText) findViewById(R.id.editEmail);
        editSenha = (EditText) findViewById(R.id.editSenha);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        //Teste <
        Button botaoTeste = (Button) findViewById(R.id.btnTeste);

        // configurar a acao de click teste
        botaoTeste.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent abrirActivity = new Intent(FormularioLogin.this, MenuP.class);
                // solicitar para abir
                startActivity(abrirActivity);
                // >
            }
        });
        //Programando o Botão para realizar Login


        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (editEmail.getText().length() == 0 || editSenha.getText().length() == 0) {

                    Toast.makeText(getApplication(),
                            "Os campos E-mail e Senha são obrigatórios!",
                            Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(getApplication(),
                            "Seja bem vindo, " + editEmail.getText().toString() + "!",
                            Toast.LENGTH_LONG).show();

                    //Limpando os dados digitados

                    editEmail.setText("");
                    editSenha.setText("");

                }

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
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
