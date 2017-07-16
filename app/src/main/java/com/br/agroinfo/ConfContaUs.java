package com.br.agroinfo;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.br.agroinfo.dao.Conexao;
import com.br.agroinfo.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;


public class ConfContaUs extends AppCompatActivity {

    Animation animBalanc;
    TextInputLayout textNomeUs, textEmailConf, textNovaSenha, textSenhaAntiga, textEndereco, textTelefone;
    EditText edtNome, edtEmail, edtSenha, edtSenhaAntiga, edtEndereco, edtTelefone;
    Button btnSalvarConfiguracoes, btnExcluirUsuario;
    boolean escolha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf_conta_us);
        final String emails = FormularioLogin.usuario.getEmail();

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


        edtEmail.setText(FormularioLogin.usuario.getEmail());
        btnSalvarConfiguracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submForm();
                if (checaNome() && checaEmail() && checaNovaSenha()){
                    AuthCredential credencial = EmailAuthProvider.getCredential(emails, edtSenhaAntiga.getText().toString());
                    FormularioLogin.usuario.reauthenticate(credencial).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                if (!edtSenha.getText().toString().trim().isEmpty()){
                                    FormularioLogin.usuario.updatePassword(edtSenha.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                                FormularioLogin.usuario.updateEmail(edtEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Publico.Alerta(ConfContaUs.this, "E-mail Alterado com Sucesso");
                                        } else {
                                            Publico.Alerta(ConfContaUs.this, "Erro ao alterar e-mail, tente novamente mais tarde");
                                        }
                                    }
                                });
                                UserProfileChangeRequest atualizaPerfil = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(edtNome.getText().toString().trim()).build();
                                FormularioLogin.usuario.updateProfile(atualizaPerfil).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Publico.Alerta(ConfContaUs.this, "Nome Alterado com Sucesso!");
                                        }
                                    }
                                });
                                Usuario u = new Usuario();
                                u.setNome(FormularioLogin.usuario.getDisplayName());
                                u.setEndereco(edtEndereco.getText().toString());
                                u.setTelefone(edtTelefone.getText().toString());
                                u.setEscolha(escolha);
                                FormularioLogin.databaseReference.child("Usuario").child(FormularioLogin.usuario.getUid()).setValue(u);
                            } else {
                                Publico.Alerta(ConfContaUs.this, "Erro ao alterar, tente novamente mais tarde");
                            }
                        }

                    });
                    Publico.Intente(ConfContaUs.this, MenuP.class);
                    finish();
                    }
                }
        });
        btnExcluirUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogo = new AlertDialog.Builder(ConfContaUs.this);
                dialogo.setCancelable(false);
                dialogo.setTitle("AgroInfo");
                dialogo.setMessage("Você tem certeza que quer excluir a conta? Não haverá como desfazer a ação, tudo será perdido");
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
        FormularioLogin.databaseReference.child("Anotacao").child(FormularioLogin.usuario.getUid()).removeValue();
        FormularioLogin.databaseReference.child("Vendas").child(FormularioLogin.usuario.getUid()).removeValue();
        FormularioLogin.databaseReference.child("Categoria").child(FormularioLogin.usuario.getUid()).removeValue();
        Query produto = FormularioLogin.databaseReference.child("Produto").child("Produtos").orderByChild("Usuario").equalTo(FormularioLogin.usuario.getUid());
        produto.addChildEventListener(new ChildEventListener() {
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
        FormularioLogin.databaseReference.child("Usuario").child(FormularioLogin.usuario.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FormularioLogin.usuario.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Conexao.deslogar();
                                        Publico.Alerta(ConfContaUs.this, "Conta de usuário deletada com Sucesso!");
                                        Publico.Intente(ConfContaUs.this, FormularioLogin.class);
                                        finish();
                                    } else {
                                        Publico.Alerta(ConfContaUs.this, "Não foi possível excluir, tente novamente mais tarde");
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Publico.Intente(ConfContaUs.this, MenuP.class);
        finish();
    }
    private void pegaDados() {
        FormularioLogin.databaseReference.child("Usuario").child(FormularioLogin.usuario.getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Usuario u = dataSnapshot.getValue(Usuario.class);
                    edtNome.setText(FormularioLogin.usuario.getDisplayName());
                    edtEndereco.setText(u.getEndereco());
                    edtTelefone.setText(u.getTelefone());
                    escolha = u.getEscolha();
                }
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
        if (!checaNovaSenha()){
            edtSenha.setAnimation(animBalanc);
            edtSenha.startAnimation(animBalanc);
        }
        textNomeUs.setErrorEnabled(false);
        textEmailConf.setErrorEnabled(false);
        textSenhaAntiga.setErrorEnabled(false);
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
    private boolean checaNovaSenha() {
        if(!edtSenha.getText().toString().trim().isEmpty() && edtSenhaAntiga.getText().toString().trim().length() <= 5){
            textNovaSenha.setError("Entre com uma nova edtSenha de no Mínimo 6 dígitos");
            edtSenha.setError("Entrada Opcional, porém obrigatório 6 dígitos!");
            return false;
        }
        textSenhaAntiga.setErrorEnabled(false);
        return true;
    }

}
