package com.br.projeto;

import android.content.Context;
import android.os.Vibrator;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.AlertDialog;
import android.content.DialogInterface;
import com.br.projeto.dao.DAO;
import com.br.projeto.modelo.Usuario;

public class FormularioCadastro extends Activity {
    private Vibrator vib;
    Animation animBalanc;
    TextInputLayout textNome_Layout, textEmail_Layout, textSenha_Layout ,textConfirmar_Senha_Layout;
    EditText textNome, textEmail, textSenha, textConfirmar_senha;
    Button btnCadastrar;
    Usuario usuario, altusuario;
    DAO DAO;
    long retornoDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_cadastro);

        Intent abrirCadastro = getIntent();
        altusuario = (Usuario) abrirCadastro.getSerializableExtra("usuario-enviado");
        usuario = new Usuario();
        DAO = new DAO(FormularioCadastro.this);

        //Resgatar componentes do Layout de texto
        textNome_Layout = (TextInputLayout) findViewById(R.id.textNome_Layout);
        textEmail_Layout = (TextInputLayout) findViewById(R.id.textEmail_Layout);
        textSenha_Layout = (TextInputLayout) findViewById(R.id.textSenha_Layout);
        textConfirmar_Senha_Layout = (TextInputLayout) findViewById(R.id.textConfirmar_Senha_Layout);

        //resgatar os componentes



        textNome = (EditText) findViewById(R.id.textNome);
        textEmail = (EditText) findViewById(R.id.textEmail);
        textSenha = (EditText) findViewById(R.id.textSenha);
        textConfirmar_senha = (EditText) findViewById(R.id.textConfirmar_senha);
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);



        // Chamar os de Animação
        animBalanc = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.balancar);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Verificar se os campos de senha são iguais */
                submForm();
                if (checaNome() && checaEmail() && checaSenha() && checaConf_Senha()) {
                    usuario.setNome(textNome.getText().toString());
                    usuario.setEmail(textEmail.getText().toString());
                    usuario.setSenha(textSenha.getText().toString());
                    usuario.setConfirmar_senha(textConfirmar_senha.getText().toString());


                    retornoDB = DAO.salvarUsuario(usuario);

                    if (retornoDB == -1) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(FormularioCadastro.this);
                        dialog.setTitle("AgroInfo");
                        dialog.setMessage("Erro ao cadastrar!");
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                FormularioCadastro.this.finish();
                            }
                        });


                        dialog.show();

                    } else {

                        AlertDialog.Builder dialog = new AlertDialog.Builder(FormularioCadastro.this);
                        dialog.setTitle("AgroInfo");
                        dialog.setMessage("Cadastrado com sucesso!");
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                FormularioCadastro.this.finish();
                            }
                        });


                        dialog.show();
                    }
                }
            }
        });

    }
    // Ver se tudo está correto nos preenchimentos
    private void submForm() {
        if (!checaNome()) {
            textNome.setAnimation(animBalanc);
            textNome.startAnimation(animBalanc);
            vib.vibrate(120);
            return;
        }
        if (!checaEmail()) {
            textEmail.setAnimation(animBalanc);
            textEmail.startAnimation(animBalanc);
            vib.vibrate(120);
            return;
        }
        if (!checaSenha()) {
            textSenha.setAnimation(animBalanc);
            textSenha.startAnimation(animBalanc);
            vib.vibrate(120);
            return;
        }
        if (!checaConf_Senha()) {
            textConfirmar_senha.setAnimation(animBalanc);
            textConfirmar_senha.startAnimation(animBalanc);
            vib.vibrate(120);
            return;
        }
        textNome_Layout.setErrorEnabled(false);
        textEmail_Layout.setErrorEnabled(false);
        textSenha_Layout.setErrorEnabled(false);
        textConfirmar_Senha_Layout.setErrorEnabled(false);


    }
    private boolean checaNome(){
        if(textNome.getText().toString().trim().isEmpty()){
            textNome_Layout.setErrorEnabled(true);
            textNome_Layout.setError("Entre com um Nome de Usuário");
            textNome.setError("Necessita de Entrada Válida");
            return false;
        }
        textNome_Layout.setErrorEnabled(false);
        return true;
    }
    private boolean checaEmail(){
        String email = textEmail.getText().toString().trim();
        if(email.isEmpty() || !eValido(email)){
            textEmail_Layout.setErrorEnabled(true);
            textEmail_Layout.setError("Entre com um E-mail Válido!");
            textEmail.setError("Necessita de Entrada Válida");
            requestFocus(textEmail);
            return false;
        }
        textEmail_Layout.setErrorEnabled(false);
        return true;
    }

    private boolean checaSenha(){
        if(textSenha.getText().toString().trim().isEmpty()){

            textSenha_Layout.setError("Entre com uma senha");
            requestFocus(textSenha);
            return false;
        }
        textSenha_Layout.setErrorEnabled(false);
        return true;
    }

    private boolean checaConf_Senha(){
        if(!textConfirmar_senha.getText().toString().equals(textSenha.getText().toString())){
            textConfirmar_Senha_Layout.setError("Senhas não equivalentes");
            return false;
        }
        textSenha_Layout.setErrorEnabled(false);
        return true;
    }
    //Checar certas variáveis acima
    private static boolean eValido(String email){
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private void requestFocus(View view){
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    // Fim

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
