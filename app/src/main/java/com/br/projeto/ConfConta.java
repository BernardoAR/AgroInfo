package com.br.projeto;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.br.projeto.dao.DAO;
import com.br.projeto.modelo.Usuario;

import static com.br.projeto.FormularioCadastro.checaemails;
import static com.br.projeto.FormularioCadastro.checaemails2;
import static com.br.projeto.dao.DAO.getEmail;
import static com.br.projeto.dao.DAO.getNome;


public class ConfConta extends AppCompatActivity {

    TextView nomeusuario;
    TextView emailusuario;
    TextView conf_senha_usuario;
    Animation animBalanc;
    TextInputLayout textNomeUs, textEmailConf, textNovaSenha, textSenhaAntiga;
    EditText nome, email,senha,senha_antiga;
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

        nome = (EditText) findViewById(R.id.edtNomeUs);
        email = (EditText) findViewById(R.id.edtEmail);
        senha = (EditText) findViewById(R.id.edtNovaSenha);
        senha_antiga = (EditText) findViewById(R.id.edtSenhaAntiga);
        btnSalvarConfiguracoes = (Button) findViewById(R.id.btnSalvarConfiguracoes);

        //TextInputLayouts que terão, animação
        textNomeUs = (TextInputLayout) findViewById(R.id.textNomeUs);
        textEmailConf = (TextInputLayout) findViewById(R.id.textEmailConf);
        textNovaSenha = (TextInputLayout) findViewById(R.id.textNovaSenha);
        textSenhaAntiga = (TextInputLayout) findViewById(R.id.textSenhaAntiga);
        animBalanc = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.balancar);

        String nome_usuario = dao.getNomeUs();
        /*nomeusuario = (TextView) findViewById(R.id.textNome);
        emailusuario = (TextView) findViewById(R.id.textEmail);
        conf_senha_usuario = (TextView) findViewById(R.id.textConfirmar_senha);*/
        nome.setText(getNome());
        email.setText(getEmail());




        btnSalvarConfiguracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailstr = email.getText().toString();
                checaemails = dao.checaEmailsUs(emailstr);
                checaemails2 = dao.checaEmailsCli(emailstr);
                submForm();
                if (checaNome() && checaEmail() && checaSenha() && checaNovaSenha()){
                    conta.setId_usuario(MenuP.ID);
                    conta.setNome(nome.getText().toString());
                    conta.setEmail(email.getText().toString());
                    if (senha.getText().toString().trim().isEmpty()) {
                        conta.setSenha(senha_antiga.getText().toString());
                    } else {
                        conta.setSenha(senha.getText().toString());
                    }

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
            }
        });


    }
    private void submForm() {
        if (!checaNome()){
            nome.setAnimation(animBalanc);
            nome.startAnimation(animBalanc);
            return;
        }
        if (!checaEmail()) {
            email.setAnimation(animBalanc);
            email.startAnimation(animBalanc);
            return;
        }
        if (!checaSenha()) {
            senha_antiga.setAnimation(animBalanc);
            senha_antiga.startAnimation(animBalanc);
            return;
        }
        if (!checaNovaSenha()){
            senha.setAnimation(animBalanc);
            senha.startAnimation(animBalanc);
        }
        textNomeUs.setErrorEnabled(false);
        textEmailConf.setErrorEnabled(false);
        textSenhaAntiga.setErrorEnabled(false);
    }
    // Checar tudo
    private boolean checaNome(){
        if(nome.getText().toString().trim().isEmpty()){
            textNomeUs.setErrorEnabled(true);
            textNomeUs.setError("Entre com um Nome de Usuário");
            nome.setError("Necessita de Entrada Válida");
            return false;
        }
        textNomeUs.setErrorEnabled(false);
        return true;
    }
    private boolean checaEmail(){
        String b = email.getText().toString().trim();
        if(b.isEmpty() || checaemails || checaemails2){
            if (checaemails || checaemails2){
                textEmailConf.setErrorEnabled(true);
                textEmailConf.setError("E-mail já existente!");
                email.setError("Necessita de Entrada Válida");
                return false;
            }

            textEmailConf.setErrorEnabled(true);
            textEmailConf.setError("Entre com um E-mail Válido!");
            email.setError("Necessita de Entrada Válida");
            return false;
        }
        textEmailConf.setErrorEnabled(false);
        return true;
    }
    //Pegar a string de e-mail do ID
    private boolean checaSenha() {
        String a = dao.getSenhaUs();
        if(senha_antiga.getText().toString().trim().length() <= 5 || !senha_antiga.getText().toString().trim().equals(a)){
            textSenhaAntiga.setError("Entre com a Senha Antiga Corretamente");
            senha_antiga.setError("Entrada Obrigatória!");
            return false;
        }
        textSenhaAntiga.setErrorEnabled(false);
        return true;
    }
    private boolean checaNovaSenha() {
        if(!senha.getText().toString().trim().isEmpty() && senha_antiga.getText().toString().trim().length() <= 5){
            textNovaSenha.setError("Entre com uma nova senha de no Mínimo 6 dígitos");
            senha.setError("Entrada Opcional, porém obrigado 6 dígitos!");
            return false;
        }
        textSenhaAntiga.setErrorEnabled(false);
        return true;
    }

}
