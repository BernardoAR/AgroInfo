package com.br.agroinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.br.agroinfo.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ConfConta extends AppCompatActivity {

    String nome_usuario;
    Animation animBalanc;
    TextInputLayout textNomeUs, textEmailConf, textNovaSenha, textSenhaAntiga;
    EditText nome, email,senha,senha_antiga;
    Button btnSalvarConfiguracoes;
    Button btnExcluirCliente;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser usuarioF;
    boolean escolha;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf_conta);
        inicFirebase();
        final String emails = usuarioF.getEmail();

        //resgatar os componentes
        nome = (EditText) findViewById(R.id.edtNomeUs);
        email = (EditText) findViewById(R.id.edtEmail);
        senha = (EditText) findViewById(R.id.edtNovaSenha);
        senha_antiga = (EditText) findViewById(R.id.edtSenhaAntiga);
        btnSalvarConfiguracoes = (Button) findViewById(R.id.btnSalvarConfiguracoes);
        btnExcluirCliente = (Button) findViewById(R.id.btnExcluirCliente);

        //TextInputLayouts que terão, animação
        textNomeUs = (TextInputLayout) findViewById(R.id.textNomeUs);
        textEmailConf = (TextInputLayout) findViewById(R.id.textEmailConf);
        textNovaSenha = (TextInputLayout) findViewById(R.id.textNovaSenha);
        textSenhaAntiga = (TextInputLayout) findViewById(R.id.textSenhaAntiga);
        animBalanc = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.balancar);

        email.setText(usuarioF.getEmail());

        btnSalvarConfiguracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submForm();
                if (checaNome() && checaEmail() && checaNovaSenha()){

                    AuthCredential credencial = EmailAuthProvider.getCredential(emails, senha_antiga.getText().toString());
                    usuarioF.reauthenticate(credencial).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                if (!senha.getText().toString().trim().isEmpty()){
                                    usuarioF.updatePassword(senha.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                alerta("Senha alterada com sucesso");
                                            } else {
                                                alerta("Erro ao alterar senha, tente novamente mais tarde");
                                            }
                                        }
                                    });
                                }
                                usuarioF.updateEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            alerta("E-mail Alterado com Sucesso");
                                        } else {
                                            alerta("Erro ao alterar e-mail, tente novamente mais tarde");
                                        }

                                    }
                                });
                                UserProfileChangeRequest atualizaPerfil = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(nome.getText().toString().trim()).build();
                                usuarioF.updateProfile(atualizaPerfil).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            alerta("Nome Alterado com Sucesso!");
                                        }
                                    }
                                });
                                Usuario u = new Usuario();
                                u.setNome(usuarioF.getDisplayName());
                                u.setEscolha(escolha);
                                databaseReference.child("Usuario").child(usuarioF.getUid()).setValue(u);
                            } else {
                                alerta("Erro ao alterar, tente novamente mais tarde");
                            }
                        }
                    });
                    finish();
                }
            }
        });

        btnExcluirCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("Usuario").child(usuarioF.getUid()).removeValue();
                usuarioF.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    alerta("Conta de cliente deletada com Sucesso!");
                                    Intent abrirFormC = new Intent(ConfConta.this, FormularioLogin.class);
                                    startActivity(abrirFormC);
                                } else {
                                    alerta("Não foi possível excluir, tente novamente mais tarde");
                                }
                            }
                        });
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ConfConta.this, MenuP.class);
        startActivity(i);
    }
    @Override
    protected void onStart() {
        super.onStart();
        pegaDados();
    }

    private void pegaDados() {
        DatabaseReference db  = databaseReference.child("Usuario").child(usuarioF.getUid());
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario u = dataSnapshot.getValue(Usuario.class);
                nome.setText(usuarioF.getDisplayName());
                escolha = u.getEscolha();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }
    private void alerta(String mensagem) {
        Toast.makeText(ConfConta.this, mensagem, Toast.LENGTH_SHORT).show();
    }

    private void inicFirebase() {
        usuarioF = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
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
        if((b.isEmpty() || !eValido(b))){
            textEmailConf.setErrorEnabled(true);
            textEmailConf.setError("Entre com um E-mail Válido!");
            email.setError("Necessita de Entrada Válida");
            return false;
        }
        textEmailConf.setErrorEnabled(false);
        return true;
    }
    private static boolean eValido(String email){
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    //Pegar a string de e-mail do ID
    private boolean checaSenha() {
        String a = "Arruma";
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
            senha.setError("Entrada Opcional, porém obrigatório 6 dígitos!");
            return false;
        }
        textSenhaAntiga.setErrorEnabled(false);
        return true;
    }

}
