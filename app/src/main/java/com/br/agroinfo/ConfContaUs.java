package com.br.agroinfo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.br.agroinfo.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;


public class ConfContaUs extends AppCompatActivity {

    Animation animBalanc;
    TextInputLayout textNomeUs, textEmailConf, textNovaSenha, textSenhaAntiga, textEndereco, textTelefone;
    EditText edtNome, edtEmail, edtSenha, edtSenhaAntiga, edtEndereco, edtTelefone;
    Button btnSalvarConfiguracoes, btnExcluirUsuario;
    boolean escolha;
    String emails, senhaAtual, nomeUs, nomeUsAnt, email, emailAnt;
    ValueEventListener pegaDado;
    ChildEventListener produto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf_conta_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView titulo = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titulo.setText("CONFIGURAÇÕES");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        emails = Inicial.usuario.getEmail();
        //resgatar os componentes
        edtNome = (EditText) findViewById(R.id.edtNomeUs);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtSenha = (EditText) findViewById(R.id.edtNovaSenha);
        edtSenhaAntiga = (EditText) findViewById(R.id.edtSenhaAntiga);
        edtEndereco = (EditText) findViewById(R.id.edtEndereco);
        edtTelefone = (EditText) findViewById(R.id.edtTelefone);
        btnSalvarConfiguracoes = (Button) findViewById(R.id.btnSalvarConfiguracoes);
        btnExcluirUsuario = (Button) findViewById(R.id.btnExcluirUsuario);

        //TextInputLayouts que terão, animação
        textNomeUs = (TextInputLayout) findViewById(R.id.textNomeUs);
        textEmailConf = (TextInputLayout) findViewById(R.id.textEmailConf);
        textNovaSenha = (TextInputLayout) findViewById(R.id.textNovaSenha);
        textSenhaAntiga = (TextInputLayout) findViewById(R.id.textSenhaAntiga);
        textEndereco = (TextInputLayout) findViewById(R.id.textEndereco);
        textTelefone = (TextInputLayout) findViewById(R.id.textTelefone);
        animBalanc = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.balancar);

        //Máscara
        MaskEditTextChangedListener maskTel = new MaskEditTextChangedListener("(##) #####-####", edtTelefone);
        edtTelefone.addTextChangedListener(maskTel);
        edtEmail.setText(Inicial.usuario.getEmail());
        btnSalvarConfiguracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submForm();
                if (checaNome() && checaEmail() && checaNovaSenha()){
                    senhaAtual = edtSenhaAntiga.getText().toString().trim();
                    if (!senhaAtual.isEmpty()){
                        AuthCredential credencial = EmailAuthProvider.getCredential(emails, senhaAtual);
                        Inicial.usuario.reauthenticate(credencial).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Usuario u = new Usuario();
                                    nomeUs = edtNome.getText().toString();
                                    email = edtEmail.getText().toString();
                                    if (!edtSenha.getText().toString().trim().isEmpty()){
                                        Inicial.usuario.updatePassword(edtSenha.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Publico.Alerta(ConfContaUs.this, "Senha alterada com sucesso");
                                                } else {
                                                    Publico.Alerta(ConfContaUs.this, "Erro ao alterar edtSenha, tente novamente mais tarde");
                                                }
                                            }
                                        });
                                    }
                                    if (!nomeUsAnt.equals(nomeUs)){
                                        UserProfileChangeRequest atualizaPerfil = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(edtNome.getText().toString().trim()).build();
                                        Inicial.usuario.updateProfile(atualizaPerfil).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Publico.Alerta(ConfContaUs.this, "Nome Alterado com Sucesso!");
                                                }
                                            }
                                        });
                                    }
                                    if (!emailAnt.equals(email)){
                                        Inicial.usuario.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Publico.Alerta(ConfContaUs.this, "E-mail Alterado com Sucesso");
                                                } else {
                                                    Publico.Alerta(ConfContaUs.this, "Erro ao alterar e-mail, tente novamente mais tarde");
                                                }
                                            }
                                        });
                                    }
                                    u.setNome(nomeUs);
                                    u.setEndereco(edtEndereco.getText().toString());
                                    u.setTelefone(edtTelefone.getText().toString());
                                    u.setEscolha(escolha);
                                    Inicial.databaseReference.child("Usuario").child(Inicial.usuario.getUid()).setValue(u);
                                    Publico.Intente(ConfContaUs.this, MenuP.class);
                                    finish();
                                } else {
                                    Publico.Alerta(ConfContaUs.this, "Erro ao alterar, tente novamente mais tarde");
                                    Publico.Intente(ConfContaUs.this, MenuP.class);
                                    finish();
                                }
                            }

                        });
                    } else {
                        Publico.Alerta(ConfContaUs.this, "O campo de senha atual está vazio");
                    }
                }
                }
        });
        btnExcluirUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogo = new AlertDialog.Builder(ConfContaUs.this);
                dialogo.setCancelable(false);
                dialogo.setTitle("AgroInfo");
                dialogo.setMessage("Você deseja realmente excluir a conta? Não haverá como desfazer a ação, tudo em sua conta será perdido");
                dialogo.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deletarContaUs();
                        dialog.cancel();
                    }
                });
                dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialogos = dialogo.create();
                dialogos.show();

            }
        });
    }

    private void deletarContaUs() {
        senhaAtual = edtSenhaAntiga.getText().toString().trim();
        if (!senhaAtual.isEmpty()){
            AuthCredential credencial = EmailAuthProvider.getCredential(emails, senhaAtual);
            Inicial.usuario.reauthenticate(credencial).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Inicial.databaseReference.child("Anotacao").child(Inicial.usuario.getUid()).removeValue();
                        Inicial.databaseReference.child("Vendas").child(Inicial.usuario.getUid()).removeValue();
                        Inicial.databaseReference.child("Categoria").child(Inicial.usuario.getUid()).removeValue();
                        produto = Inicial.databaseReference.child("Produto").child("Produtos").orderByChild("Usuario").equalTo(Inicial.usuario.getUid())
                                .addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                if (dataSnapshot.exists()){
                                    for(DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                                        objSnapshot.getRef().setValue(null);
                                    }
                                }
                            }
                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {}
                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        });
                        Inicial.databaseReference.child("Usuario").child(Inicial.usuario.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Inicial.usuario.delete()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Inicial.autent.signOut();
                                                        Publico.Alerta(ConfContaUs.this, "Conta de usuário deletada com Sucesso!");
                                                        Publico.Intente(ConfContaUs.this, Inicial.class);
                                                        finish();
                                                    } else {
                                                        Publico.Alerta(ConfContaUs.this, "Não foi possível excluir, tente novamente mais tarde");
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                    } else {
                        Publico.Alerta(ConfContaUs.this, "Senha atual incorreta!");
                    }
                }
            });
        } else {
            Publico.Alerta(ConfContaUs.this, "O campo de senha atual está vazio");
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Publico.Intente(ConfContaUs.this, MenuP.class);
        finish();
    }
    private void pegaDados() {
        pegaDado = Inicial.databaseReference.child("Usuario").child(Inicial.usuario.getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Usuario u = dataSnapshot.getValue(Usuario.class);
                    edtNome.setText(Inicial.usuario.getDisplayName());
                    edtEndereco.setText(u.getEndereco());
                    edtTelefone.setText(u.getTelefone());
                    escolha = u.getEscolha();
                    nomeUsAnt = Inicial.usuario.getDisplayName();
                    emailAnt = Inicial.usuario.getEmail();
                }
                Inicial.databaseReference.removeEventListener(pegaDado);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        pegaDados();
    }

    private void submForm() {
        if (!checaNome()){
            edtNome.setAnimation(animBalanc);
            edtNome.startAnimation(animBalanc);
            return;
        }
        if (!checaEmail()) {
            edtEmail.setAnimation(animBalanc);
            edtEmail.startAnimation(animBalanc);
            return;
        }
        if (!checaSenha()){
            edtSenhaAntiga.setAnimation(animBalanc);
            edtSenhaAntiga.startAnimation(animBalanc);
            return;
        }
        if (!checaNovaSenha()){
            edtSenha.setAnimation(animBalanc);
            edtSenha.startAnimation(animBalanc);
            return;
        }
        textNomeUs.setErrorEnabled(false);
        textEmailConf.setErrorEnabled(false);
        textSenhaAntiga.setErrorEnabled(false);
        textNovaSenha.setErrorEnabled(false);
    }
    // Checar tudo
    private boolean checaNome(){
        if(edtNome.getText().toString().trim().isEmpty()){
            textNomeUs.setErrorEnabled(true);
            textNomeUs.setError("Entre com um Nome de Usuário");
            edtNome.setError("Necessita de Entrada Válida");
            return false;
        }
        textNomeUs.setErrorEnabled(false);
        return true;
    }
   private boolean checaEmail(){
        String b = edtEmail.getText().toString().trim();
        if((b.isEmpty() || !eValido(b))){
            textEmailConf.setErrorEnabled(true);
            textEmailConf.setError("Entre com um E-mail Válido!");
            edtEmail.setError("Necessita de Entrada Válida");
            return false;
        }
        textEmailConf.setErrorEnabled(false);
        return true;
    }
    private static boolean eValido(String email){
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean checaSenha() {
        String a = edtSenhaAntiga.getText().toString().trim();
        if(a.length() <= 5){
            textSenhaAntiga.setError("Entre com a Senha Antiga Corretamente");
            edtSenhaAntiga.setError("Entrada Obrigatória!");
            return false;
        }
        textSenhaAntiga.setErrorEnabled(false);
        return true;
    }

    private boolean checaNovaSenha() {
        String a = edtSenha.getText().toString().trim();
        if(!a.isEmpty() && a.length() <= 5){
            textNovaSenha.setError("Entre com uma nova Senha de no Mínimo 6 dígitos");
            edtSenha.setError("Entrada Opcional, porém obrigatório 6 dígitos!");
            return false;
        }
        textNovaSenha.setErrorEnabled(false);
        return true;
    }

}
