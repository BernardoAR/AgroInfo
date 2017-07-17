package com.br.agroinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.br.agroinfo.dao.Conexao;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FormularioLogin extends AppCompatActivity {

    //Criando os objetos necessários
    private SignInButton btnLogGoogle;
    EditText edtEmail, edtSenha;
    Button btnLogin, btnCadastrar;
    public static FirebaseAuth autent;
    public static FirebaseUser usuario;
    public static FirebaseDatabase firebaseDatabase;
    public static DatabaseReference databaseReference;
    boolean existe1, existe;
    public static boolean googleL;
    TextView textResetarSenha;
    String email, senha;
    //Chamado serve para ver se a persistência já foi chamada
    public static boolean chamado = false;
    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "Activity_Normal";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView titulo = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titulo.setText("AgroInfo - Entrar");
        //Vinculando os objetos aos IDs
        edtEmail = (EditText) findViewById(R.id.editEmail);
        edtSenha = (EditText) findViewById(R.id.editSenha);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
        textResetarSenha = (TextView) findViewById(R.id.textResetarSenha);
        btnLogGoogle = (SignInButton) findViewById(R.id.btnLogGoogle);
        // Request Focus para os edittexts
        edtEmail.requestFocus();
        edtSenha.requestFocus();

        // SIGNIN GOOGLE

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Publico.Alerta(FormularioLogin.this, "Erro");
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        btnLogGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logar();
            }
        });
        //Programando o Botão para realizar Login
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Colocando em String
                email = edtEmail.getText().toString().trim();
                senha = edtSenha.getText().toString().trim();
                if (((email != null) && (!email.isEmpty())) && ((senha != null) && (!senha.isEmpty()))) {
                    login(email, senha);
                    googleL = false;
                } else {
                    Publico.Alerta(FormularioLogin.this, "Algum dos campos está vazio");
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
        textResetarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resetar = new Intent(FormularioLogin.this, ResetarSenha.class);
                startActivity(resetar);
            }
        });
    }

    //LOGAR GOOGLE
    private void logar() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Se foi com sucesso, logar
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount conta) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + conta.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(conta.getIdToken(), null);
        autent.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Publico.Alerta(FormularioLogin.this, "Autenticação Falhou");
                        } else {
                            usuario = Conexao.getFirebaseUser();
                            googleL = true;
                            TestaDados();
                        }
                        // ...
                    }
                });
    }

    //
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
                            edtEmail.setText("");
                            edtSenha.setText("");
                            usuario = Conexao.getFirebaseUser();
                            TestaDados();
                        } else {
                            Publico.Alerta(FormularioLogin.this, "E-mail ou Senha não Correspondem!");
                        }
                    }
                });
    }

    private void TestaDados() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                existe1 = dataSnapshot.hasChild("Usuario");
                if (existe1){
                    TestaDados2();
                } else {
                    Publico.Intente(FormularioLogin.this, CadastrosOpcionais.class);
                    finish();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void TestaDados2() {
        databaseReference.child("Usuario").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                existe = dataSnapshot.hasChild(usuario.getUid());
                if (existe){
                    Publico.Intente(FormularioLogin.this, MenuP.class);
                    finish();
                } else if (!existe && !googleL){
                    Publico.Intente(FormularioLogin.this, CadastrosOpcionais.class);
                    finish();
                } else if (!existe && googleL) {
                    Publico.Intente(FormularioLogin.this, CadastrosOpcionais.class);
                    finish();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        inicializarFirebase();
        autent = Conexao.getFirebaseAuth();
        if (autent.getCurrentUser() != null){
            usuario = autent.getCurrentUser();
            Publico.Intente(FormularioLogin.this, MenuP.class);
            finish();
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
