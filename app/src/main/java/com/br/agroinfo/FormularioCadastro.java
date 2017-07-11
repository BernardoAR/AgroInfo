package com.br.agroinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
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
import android.widget.Toast;

import com.br.agroinfo.dao.Conexao;
import com.br.agroinfo.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FormularioCadastro extends Activity {
    private Vibrator vib;
    Animation animBalanc;
    TextInputLayout textEmail_Layout, textSenha_Layout ,textConfirmar_Senha_Layout;
    EditText textEmail, textSenha, textConfirmar_senha;
    Button btnCadastrar;
    RadioButton rbEmpresa, rbCliente;
    // Pegar email e testar
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_cadastro);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        //Resgatar componentes do Layout de texto
        textEmail_Layout = (TextInputLayout) findViewById(R.id.textEmail_Layout);
        textSenha_Layout = (TextInputLayout) findViewById(R.id.textSenha_Layout);
        textConfirmar_Senha_Layout = (TextInputLayout) findViewById(R.id.textConfirmar_Senha_Layout);

        //resgatar os componentes
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

                submForm();
                /* Verificar se os campos de senha são iguais */
                // Pegar Strings
                EditText senha = (EditText)findViewById(R.id.textSenha);
                EditText confirmar_senha = (EditText)findViewById(R.id.textConfirmar_senha);

                String senhastr = senha.getText().toString();
                String confsstr = confirmar_senha.getText().toString();

                if (checaEmail() && checaSenha() && checaConf_Senha()) {
                    // Inserir os detalhes no BD, dependendo de qual dos Radiobuttons for selecionado
                    criarUsuario(emailstr, senhastr);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(FormularioCadastro.this, FormularioLogin.class);
        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
    }
    private void criarUsuario(String email, String senha) {
        auth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(FormularioCadastro.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            alerta("Usuário Cadastrado com Sucesso");
                            limpaCampos();
                            Conexao.deslogar();
                            finish();
                        } else {
                            alerta("Erro de Cadastro, e-mail existente");
                        }
                    }
                });
    }

    private void limpaCampos() {
        textEmail.setText("");
        textSenha.setText("");
        textConfirmar_senha.setText("");
    }

    // ALERTA
    private void alerta(String mensagem) {
        Toast.makeText(FormularioCadastro.this, mensagem, Toast.LENGTH_SHORT).show();
    }

    // Ver se tudo está correto nos preenchimentos
    private void submForm() {
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
        textEmail_Layout.setErrorEnabled(false);
        textSenha_Layout.setErrorEnabled(false);
        textConfirmar_Senha_Layout.setErrorEnabled(false);
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
