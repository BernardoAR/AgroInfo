package com.br.projeto;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.br.projeto.dao.DAO;
import com.br.projeto.modelo.Usuario;

import static com.br.projeto.dao.DAO.getEmail;
import static com.br.projeto.dao.DAO.getNome;
import static com.br.projeto.dao.DAO.getSenha;


public class ConfConta extends AppCompatActivity {

    TextView nomeusuario;
    TextView emailusuario;
    TextView senhausuario;
    TextView conf_senha_usuario;

    EditText nome, email,senha,conf_senha;
    Button btnSalvarConfiguracoes;
    Usuario conta;
    DAO dao;
    long retornoDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf_conta);

        conta = new Usuario();
        dao = new DAO(ConfConta.this);

        //resgatar os componentes

        nome = (EditText) findViewById(R.id.textNome);
        email = (EditText) findViewById(R.id.textEmail);
        senha = (EditText) findViewById(R.id.textSenha);
        conf_senha = (EditText) findViewById(R.id.textConfirmar_senha);
        btnSalvarConfiguracoes = (Button) findViewById(R.id.btnSalvarConfiguracoes);

        String nome_usuario = dao.getNomeUs();
        nomeusuario = (TextView) findViewById(R.id.textNome);
        emailusuario = (TextView) findViewById(R.id.textEmail);
        senhausuario = (TextView) findViewById(R.id.textSenha);
        conf_senha_usuario = (TextView) findViewById(R.id.textConfirmar_senha);
        nomeusuario.setText(getNome());
        emailusuario.setText(getEmail());
        senhausuario.setText(getSenha());



        btnSalvarConfiguracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conta.setId_usuario(MenuP.ID);
                conta.setNome(nome.getText().toString());
                conta.setEmail(email.getText().toString());
                conta.setSenha(senha.getText().toString());


                dao.alterarUsuario(conta);
                dao.close();


                if (retornoDB == -1) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(ConfConta.this);
                    dialog.setTitle("AgroInfo");
                    dialog.setMessage("Erro ao alterar!");
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ConfConta.this.finish();
                        }
                    });


                    dialog.show();

                } else {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(ConfConta.this);
                    dialog.setTitle("AgroInfo");
                    dialog.setMessage("Alterado com sucesso!");
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ConfConta.this.finish();
                        }
                    });


                    dialog.show();
                }
            }
        });


    }
}
