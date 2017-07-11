package com.br.agroinfo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.br.agroinfo.dao.Conexao;
import com.br.agroinfo.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CadastrosOpcionais extends AppCompatActivity {
    FirebaseAuth autent;
    FirebaseUser usuario;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TextInputLayout textEndereco, textNome, textTelefone;
    EditText edtNome, edtEndereco, edtTelefone;
    Button btnCadastrar;
    private Vibrator vib;
    Animation animBalanc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastros_opcionais);
        inicFirebase();
        //Resgatar componentes
        textEndereco = (TextInputLayout) findViewById(R.id.textEndereco);
        textNome = (TextInputLayout) findViewById(R.id.textNome);
        textTelefone = (TextInputLayout) findViewById(R.id.textTelefone);
        edtNome = (EditText) findViewById(R.id.edtNome);
        edtEndereco = (EditText) findViewById(R.id.edtEndereco);
        edtTelefone = (EditText) findViewById(R.id.edtTelefone);
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
        animBalanc = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.balancar);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submForm();
                if (checaNome() && algSelect() && algSelect2()){
                    Usuario u = new Usuario();
                    String nome = edtNome.getText().toString().trim();
                    if (pressionado == 1) {
                        u.setNome(nome);
                        u.setTelefone(edtTelefone.getText().toString());
                        u.setEndereco(edtEndereco.getText().toString());
                        u.setEscolha(true);
                    } else {
                        u.setNome(nome);
                        u.setEscolha(false);
                    }
                    UserProfileChangeRequest atualizaPerfil = new UserProfileChangeRequest.Builder()
                            .setDisplayName(nome).build();
                    usuario.updateProfile(atualizaPerfil).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                alerta("Cadastrado completamente com Sucesso!");
                            }
                        }
                    });
                    databaseReference.child("Usuario").child(usuario.getUid()).setValue(u);
                    Intent abrirLogin = new Intent(CadastrosOpcionais.this, MenuP.class);
                    startActivity(abrirLogin);
                }
            }
        });
    }

    private void inicFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();
        autent = Conexao.getFirebaseAuth();
        usuario = Conexao.getFirebaseUser();
    }

    //Excluir o Criado
    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(CadastrosOpcionais.this);
        dialog.setTitle("AgroInfo");
        dialog.setMessage("Atenção! Se voltar tudo feito até agora será excluído. Deseja continuar?");
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                usuario.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    alerta("Conta deletada com Sucesso!");
                                    Intent abrirFormC = new Intent(CadastrosOpcionais.this, FormularioLogin.class);
                                    startActivity(abrirFormC);
                                } else {
                                    alerta("Não foi possível excluir, tente novamente mais tarde");
                                }
                            }
                        });
            }
        });
        dialog.show();
    }


    private void submForm() {
        if (!checaNome()) {
            edtNome.setAnimation(animBalanc);
            edtNome.startAnimation(animBalanc);
            vib.vibrate(120);
            return;
        }
        if (!algSelect()){
            alerta("Marque um dos dois campos");
        }
        if (!algSelect2()){
            alerta("Cliente não possui os campos Endereço e Telefone");
        }
        textNome.setErrorEnabled(false);
    }

    private void alerta(String mensagem) {
        Toast.makeText(CadastrosOpcionais.this, mensagem, Toast.LENGTH_SHORT).show();
    }

    private boolean checaNome(){
        String nome = edtNome.getText().toString();
        if (nome.isEmpty() || nome.length() < 5){
            textNome.setErrorEnabled(true);
            textNome.setError("Entre com um nome válido!");
            edtNome.setError("Necessita de Entrada Válida, de pelo menos 5 caracteres");
            return false;
        }
        textNome.setErrorEnabled(false);
        return true;
    }
    // Ver se o pressionado necessita dos campos
    public boolean algSelect2(){
        String Telefone, Endereco;
        Telefone = edtTelefone.getText().toString();
        Endereco = edtEndereco.getText().toString();
        if (((pressionado == 2) && ((!Telefone.trim().isEmpty()) || (!Endereco.trim().isEmpty())))){
            return false;
        } else {
            return true;
        }
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
}
