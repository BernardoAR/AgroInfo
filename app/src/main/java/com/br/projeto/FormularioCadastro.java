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
import android.widget.RadioButton;
import android.widget.Toast;

import com.br.projeto.dao.DAO;
import com.br.projeto.modelo.Cliente;
import com.br.projeto.modelo.Usuario;

public class FormularioCadastro extends Activity {
    private Vibrator vib;
    Animation animBalanc;
    TextInputLayout textNome_Layout, textEmail_Layout, textSenha_Layout ,textConfirmar_Senha_Layout;
    EditText textNome, textEmail, textSenha, textConfirmar_senha;
    Button btnCadastrar;
    DAO helper = new DAO(this);
    RadioButton rbEmpresa, rbCliente;
    long retornoDB;
    // Pegar email e testar
    public static boolean checaemails, checaemails2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_cadastro);

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
        rbEmpresa = (RadioButton) findViewById(R.id.rbEmpresa);
        rbCliente = (RadioButton) findViewById(R.id.rbCliente);

        // Chamar os de Animação
        animBalanc = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.balancar);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Verificar se já existe campo de email existente
                EditText email = (EditText)findViewById(R.id.textEmail);
                String emailstr = email.getText().toString();
                checaemails = helper.checaEmailsUs(emailstr);
                checaemails2 = helper.checaEmailsCli(emailstr);

                submForm();
                /* Verificar se os campos de senha são iguais */
                // Pegar Strings
                EditText nome = (EditText)findViewById(R.id.textNome);
                EditText senha = (EditText)findViewById(R.id.textSenha);
                EditText confirmar_senha = (EditText)findViewById(R.id.textConfirmar_senha);
                // Colocar em Strings
                String nomestr = nome.getText().toString();

                String senhastr = senha.getText().toString();
                String confsstr = confirmar_senha.getText().toString();

                if (checaNome() && checaEmail() && checaSenha() && checaConf_Senha() && algSelect()) {
                    // Inserir os detalhes no BD, dependendo de qual dos Radiobuttons for selecionado
                    if (pressionado == 1){
                        Usuario u = new Usuario();
                        u.setNome(nomestr);
                        u.setEmail(emailstr);
                        u.setSenha(senhastr);
                        u.setConfirmar_senha(confsstr);
                        helper.salvarUsuario(u);
                    } else  {
                        Cliente c = new Cliente();
                        c.setNome(nomestr);
                        c.setEmail(emailstr);
                        c.setSenha(senhastr);
                        c.setConfirmar_senha(confsstr);
                        helper.salvarCliente(c);
                    }
                    //Desligar o pressionado
                    pressionado = 0;


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
        if (!algSelect()){
            Toast temp = Toast.makeText(FormularioCadastro.this, "Marque um dos dois campos", Toast.LENGTH_SHORT);
            temp.show();
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
        if(email.isEmpty() || !eValido(email) || checaemails || checaemails2){
            if (checaemails || checaemails2){
                textEmail_Layout.setErrorEnabled(true);
                textEmail_Layout.setError("E-mail já existente!");
                textEmail.setError("Necessita de Entrada Válida");
                requestFocus(textEmail);
                return false;
            }
            textEmail_Layout.setErrorEnabled(true);
            textEmail_Layout.setError("Entre com um E-mail Válido!");
            textEmail.setError("Necessita de Entrada Válida");
            requestFocus(textEmail);
            return false;
        }
        textEmail_Layout.setErrorEnabled(false);
        return true;
    }

    //Ver se tem 6 caracteres no mínimo
    private boolean checaSenha(){
        if(textSenha.getText().toString().trim().isEmpty() || textSenha.getText().toString().trim().length() <= 5){
            textSenha_Layout.setError("Entre com uma senha, que contenha no mínimo 6 caracteres!");
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
    private static int pressionado;
    public boolean algSelect(){
        if (pressionado == 1){
            return true;
        } else
            if (pressionado == 2){
                return true;
            } else {
                return false;
            }
    }

    //SOMENTE PUBLIC E VOID PARA FUNCIONAR
    public void onRadioButtonClicado(View view) {
        // Está checado?
        boolean checado = ((RadioButton) view).isChecked();

        // Checar qual RadioButton foi clicado
        switch(view.getId()) {
            case R.id.rbEmpresa:
                if (checado)
                    pressionado = 1;
                    break;
            case R.id.rbCliente:
                if (checado)
                    pressionado = 2;
                    break;
        }
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
