package com.br.agroinfo;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.br.agroinfo.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.text.Normalizer;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

public class CadastrosOpcionais extends AppCompatActivity {
    TextInputLayout textEndereco, textNome, textTelefone;
    EditText edtNome, edtEndereco, edtTelefone;
    Button btnCadastrar;
    private Vibrator vib;
    Animation animBalanc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastros_opcionais);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView titulo = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titulo.setText("Outros Cadastros");
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
        if (FormularioLogin.googleL){
            edtNome.setText(FormularioLogin.usuario.getDisplayName());
        }
        //Máscara
        MaskEditTextChangedListener maskTel = new MaskEditTextChangedListener("(##) #####-####", edtTelefone);
        edtTelefone.addTextChangedListener(maskTel);

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
                    FormularioLogin.usuario.updateProfile(atualizaPerfil).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Publico.Alerta(CadastrosOpcionais.this, "Cadastrado completamente com Sucesso!");
                            }
                        }
                    });
                    FormularioLogin.databaseReference.child("Usuario").child(FormularioLogin.usuario.getUid()).setValue(u).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Publico.Intente(CadastrosOpcionais.this, MenuP.class);
                            finish();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        verificaUsuario();
    }

    private void verificaUsuario() {
        if (FormularioLogin.autent.getCurrentUser() == null){
            Publico.Intente(CadastrosOpcionais.this, FormularioLogin.class);
            finish();
        }
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
                FormularioLogin.usuario.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Publico.Alerta(CadastrosOpcionais.this, "Conta deletada com Sucesso!");
                                    Publico.Intente(CadastrosOpcionais.this, FormularioLogin.class);
                                    finish();
                                } else {
                                    Publico.Alerta(CadastrosOpcionais.this, "Não foi possível excluir, tente novamente mais tarde");
                                    finish();
                                }
                            }
                        });
            }
        });
        dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
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
            Publico.Alerta(CadastrosOpcionais.this, "Marque um dos dois campos");
        }
        if (!algSelect2()){
            Publico.Alerta(CadastrosOpcionais.this, "Cliente não possui os campos Endereço e Telefone");
        }
        textNome.setErrorEnabled(false);
    }

    private boolean checaNome(){
        String nome = edtNome.getText().toString();
        if (nome.isEmpty() || nome.length() < 5){
            textNome.setErrorEnabled(true);
            textNome.setError("Entre com um Nome válido!");
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
        return !((pressionado == 2) && ((!Telefone.trim().isEmpty()) || (!Endereco.trim().isEmpty())));
    }
    private static int pressionado;
    public boolean algSelect(){
        if (pressionado == 1){
            return true;
        } else
            return pressionado == 2;
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
