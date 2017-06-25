package com.br.projeto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.br.projeto.dao.DAO;
import com.br.projeto.modelo.Sessao;
import com.br.projeto.modelo.Usuario;

public class FormularioLogin extends Activity {

    //Criando os objetos necessários

    EditText editEmail;
    EditText editSenha;
    Button btnLogin;
    private Sessao sessao;
    public static String id;
    // Sessão
    //Criar novo DAO para ser não estático
    DAO helper = new DAO(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_login);
        //Vinculando os objetos aos IDs
        editEmail = (EditText) findViewById(R.id.editEmail);
        editSenha = (EditText) findViewById(R.id.editSenha);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        sessao = new Sessao(this);
        //Teste <
        Button botaoTeste = (Button) findViewById(R.id.btnTeste);
        // Request Focus para os edittexts
        editEmail.requestFocus();
        editSenha.requestFocus();
        //Programando o Botão para realizar Login
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Colocando em String
                EditText a = (EditText)findViewById(R.id.editEmail);
                String str = a.getText().toString();
                EditText b = (EditText)findViewById(R.id.editSenha);
                String senha = b.getText().toString();

                String senhabd = helper.checaLogin(str);
                    if(senha.equals(senhabd)){
                        sessao.dizLogado(true);
                        //Gravar ID nas preferências
                        int idusuario = DAO.getIDUs();
                        boolean escolha = DAO.getBolR();
                        sessao.dizID(idusuario);
                        sessao.dizEsc(escolha);
                        // Ver qual foi a escolha
                        if(sessao.escolhido()) {
                            Toast temp = Toast.makeText(FormularioLogin.this, "Usuario", Toast.LENGTH_SHORT);
                            temp.show();
                            Intent abrirLogin = new Intent(FormularioLogin.this, MenuP.class);
                            startActivity(abrirLogin);
                        } else {
                            Toast temp = Toast.makeText(FormularioLogin.this, "Cliente", Toast.LENGTH_SHORT);
                            temp.show();
                            Intent abrirLogin = new Intent(FormularioLogin.this, MenuP.class);
                            startActivity(abrirLogin);
                        }
                    } else {
                        Toast temp = Toast.makeText(FormularioLogin.this, "E-mail e/ou Senha não correspondentes!", Toast.LENGTH_SHORT);
                        temp.show();
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
