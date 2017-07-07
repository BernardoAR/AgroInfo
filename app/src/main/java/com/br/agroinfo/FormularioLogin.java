package com.br.agroinfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.br.agroinfo.dao.Conexao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FormularioLogin extends Activity {

    //Criando os objetos necessários

    EditText editEmail;
    EditText editSenha;
    Button btnLogin, btnCadastrar;
    FirebaseAuth autent;
    FirebaseUser usuario;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //Chamado serve para ver se a persistência já foi chamada
    public static boolean chamado = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_login);
        inicializarFirebase();
        //Vinculando os objetos aos IDs
        editEmail = (EditText) findViewById(R.id.editEmail);
        editSenha = (EditText) findViewById(R.id.editSenha);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);

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
                String str = a.getText().toString().trim();
                EditText b = (EditText)findViewById(R.id.editSenha);
                String senha = b.getText().toString().trim();
                if (((str != null) && (!str.isEmpty())) && ((senha != null) && (!senha.isEmpty()))) {
                    login(str, senha);
                } else {
                    alerta("Algum dos campos está vazio");
                }
            }
        });
        // BOTÃO PARA ABRIR ACTIVITY DE CADASTRAR
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent abrirCadastro = new Intent(FormularioLogin.this, FormularioCadastro.class);
                // solicitar para abir
                startActivity(abrirCadastro);

            }
        });
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(FormularioLogin.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        if (!chamado){
            firebaseDatabase.setPersistenceEnabled(true);
            chamado = true;
        }
        databaseReference = firebaseDatabase.getReference();
    }


    private void login(String email, String senha) {
        autent.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(FormularioLogin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent abrirCOP = new Intent(FormularioLogin.this, CadastrosOpcionais.class);
                            editEmail.setText("");
                            editSenha.setText("");
                            startActivity(abrirCOP);
                        } else {
                            alerta("E-mail ou Senha não Correspondem!");
                        }
                    }
                });
    }

    private void alerta(String s) {
        Toast.makeText(FormularioLogin.this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        autent = Conexao.getFirebaseAuth();
        usuario = Conexao.getFirebaseUser();
    }

    //RequestFocus para pegar as Edittext
    private void requestFocus(View view){
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    //
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
