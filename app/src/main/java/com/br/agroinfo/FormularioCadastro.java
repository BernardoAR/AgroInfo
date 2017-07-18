package com.br.agroinfo;

import android.content.Context;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.br.agroinfo.dao.Conexao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class FormularioCadastro extends AppCompatActivity {
    private Vibrator vib;
    Animation animBalanc;
    TextInputLayout textEmail_Layout, textSenha_Layout ,textConfirmar_Senha_Layout;
    EditText edtEmail, edtSenha, edtConfSenha;
    Button btnCadastrar;
    RadioButton rbEmpresa, rbCliente;
    String email, senha, confSenha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_cadastro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView titulo = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titulo.setText("CADASTRE-SE");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Resgatar componentes do Layout de texto
        textEmail_Layout = (TextInputLayout) findViewById(R.id.textEmail_Layout);
        textSenha_Layout = (TextInputLayout) findViewById(R.id.textSenha_Layout);
        textConfirmar_Senha_Layout = (TextInputLayout) findViewById(R.id.textConfirmar_Senha_Layout);

        //resgatar os componentes
        edtEmail = (EditText) findViewById(R.id.textEmail);
        edtSenha = (EditText) findViewById(R.id.textSenha);
        edtConfSenha = (EditText) findViewById(R.id.textConfirmar_senha);
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
        rbEmpresa = (RadioButton) findViewById(R.id.rbEmpresa);
        rbCliente = (RadioButton) findViewById(R.id.rbCliente);

        // Chamar os de Animação
        animBalanc = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.balancar);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Verificar se já existe campo de edtEmail existente
                email = edtEmail.getText().toString();
                senha = edtSenha.getText().toString();
                confSenha = edtConfSenha.getText().toString();
                submForm();

                if (checaEmail() && checaSenha() && checaConf_Senha()) {
                    // Inserir os detalhes no BD, dependendo de qual dos Radiobuttons for selecionado
                    criarUsuario(email, senha);
                }
            }
        });

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Publico.Intente(FormularioCadastro.this, FormularioLogin.class);
        finish();
    }

    private void criarUsuario(String email, String senha) {
        FormularioLogin.autent.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(FormularioCadastro.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Publico.Alerta(FormularioCadastro.this, "Usuário Cadastrado com Sucesso");
                            limpaCampos();
                            Conexao.deslogar();
                            finish();
                        } else {
                            Publico.Alerta(FormularioCadastro.this, "Erro de Cadastro, e-mail existente");
                        }
                    }
                });
    }

    private void limpaCampos() {
        edtEmail.setText("");
        edtSenha.setText("");
        edtConfSenha.setText("");
    }


    // Ver se tudo está correto nos preenchimentos
    private void submForm() {
        if (!checaEmail()) {
            edtEmail.setAnimation(animBalanc);
            edtEmail.startAnimation(animBalanc);
            vib.vibrate(120);
            return;
        }
        if (!checaSenha()) {
            edtSenha.setAnimation(animBalanc);
            edtSenha.startAnimation(animBalanc);
            vib.vibrate(120);
            return;
        }
        if (!checaConf_Senha()) {
            edtConfSenha.setAnimation(animBalanc);
            edtConfSenha.startAnimation(animBalanc);
            vib.vibrate(120);
            return;
        }
        textEmail_Layout.setErrorEnabled(false);
        textSenha_Layout.setErrorEnabled(false);
        textConfirmar_Senha_Layout.setErrorEnabled(false);
    }
    private boolean checaEmail(){
        String email = edtEmail.getText().toString().trim();
        if(email.isEmpty() || !eValido(email)){
            textEmail_Layout.setErrorEnabled(true);
            textEmail_Layout.setError("Entre com um E-mail Válido!");
            edtEmail.setError("Necessita de Entrada Válida");
            requestFocus(edtEmail);
            return false;
        }
        textEmail_Layout.setErrorEnabled(false);
        return true;
    }

    //Ver se tem 6 caracteres no mínimo
    private boolean checaSenha(){
        if(senha.isEmpty() || senha.length() <= 5){
            textSenha_Layout.setError("Entre com uma edtSenha, que contenha no mínimo 6 caracteres!");
            requestFocus(edtSenha);
            return false;
        }
        textSenha_Layout.setErrorEnabled(false);
        return true;
    }

    private boolean checaConf_Senha(){
        if(!confSenha.equals(senha)){
            textConfirmar_Senha_Layout.setError("Senhas não equivalentes");
            return false;
        }
        textSenha_Layout.setErrorEnabled(false);
        return true;
    }

    //Checar certas variáveis acima
    public static boolean eValido(String email){
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private void requestFocus(View view){
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}
