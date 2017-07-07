package com.br.agroinfo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Vibrator;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.br.agroinfo.dao.Conexao;
import com.br.agroinfo.modelo.Anotacao;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;


public class Nova_anotacao extends AppCompatActivity {
    private Vibrator vib;
    Animation animBalanc;
    DateFormat formatarData = DateFormat.getDateInstance();
    EditText nova_anotacao, edtDataAn, edtAssunto;
    TextInputLayout textAssunto, textAnotacao, textDataLayout;
    Button btnCadastrar, btnSelData;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    long retornoDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_anotacao);
        inicializarFirebase();


        //resgatar os componentes
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
        btnSelData = (Button) findViewById(R.id.btnSelData);
        nova_anotacao = (EditText) findViewById(R.id.nova_anotacao);
        edtDataAn = (EditText) findViewById(R.id.edtDataAn);
        edtAssunto = (EditText) findViewById(R.id.edtAssunto);

        btnSelData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String diaString = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault()).format(new Date());
                diaString = diaString.replace('/', '-');
                edtDataAn.setText(diaString);
            }
        });

        // Chamar os de Animação
        animBalanc = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.balancar);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TextInputLayouts
                textAssunto = (TextInputLayout) findViewById(R.id.textAssunto);
                textAnotacao = (TextInputLayout) findViewById(R.id.textAnotacao);
                textDataLayout = (TextInputLayout) findViewById(R.id.textDataLayout);

                submForm();
                if(checaAssunto() && checaAnotacao() && checaData()){
                    // Pegar Strings
                    EditText nova_anotacao = (EditText)findViewById(R.id.nova_anotacao);
                    EditText edtDatanAn = (EditText)findViewById(R.id.edtDataAn);
                    EditText assuntoAn = (EditText)findViewById(R.id.edtAssunto);
                    // Colocar em Strings
                    String anotacao = nova_anotacao.getText().toString();
                    String data = edtDatanAn.getText().toString();
                    String assunto = assuntoAn.getText().toString();

                    // Inserir os detalhes no BD
                    Anotacao a = new Anotacao();
                    a.setId_anotacao(UUID.randomUUID().toString());
                    a.setNovo_assunto(data + " - " + assunto);
                    a.setNova_anotacao(anotacao);
                    databaseReference.child("Anotacao").child(MenuP.usuario.getUid()).child(a.getId_anotacao()).setValue(a);
                    alerta("Salvado com Sucesso");
                    limparCampos();
                    finish();
                }
            }
        });

    }
    private void alerta(String mensagem) {
        Toast.makeText(Nova_anotacao.this, mensagem, Toast.LENGTH_SHORT).show();
    }

    private void limparCampos() {
        nova_anotacao.setText("");
        edtDataAn.setText("");
        edtAssunto.setText("");
    }

    private void inicializarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
    private void submForm() {
        if (!checaAssunto()) {
            edtAssunto.setAnimation(animBalanc);
            edtAssunto.startAnimation(animBalanc);
            vib.vibrate(120);
            return;
        }
        if (!checaAnotacao()) {
            nova_anotacao.setAnimation(animBalanc);
            nova_anotacao.startAnimation(animBalanc);
            vib.vibrate(120);
            return;
        }
        if (!checaData()) {
            edtDataAn.setAnimation(animBalanc);
            edtDataAn.startAnimation(animBalanc);
            vib.vibrate(120);
            return;
        }

        textAssunto.setErrorEnabled(false);
        textAnotacao.setErrorEnabled(false);
        textDataLayout.setErrorEnabled(false);
    }
    // Verificar se todos estão preenchidos
    private boolean checaAssunto(){
        if(edtAssunto.getText().toString().trim().isEmpty()){
            textAssunto.setErrorEnabled(true);
            textAssunto.setError("Entre com um Assunto");
            edtAssunto.setError("Campo não pode ser nulo");
            return false;
        }
        textAssunto.setErrorEnabled(false);
        return true;
    }
    private boolean checaAnotacao(){
        if(nova_anotacao.getText().toString().trim().isEmpty()){
            textAnotacao.setErrorEnabled(true);
            textAnotacao.setError("Entre com a Anotação");
            nova_anotacao.setError("Campo não pode ser nulo");
            return false;
        }
        textAnotacao.setErrorEnabled(false);
        return true;
    }
    private boolean checaData(){
        if(edtDataAn.getText().toString().trim().isEmpty()){
            textDataLayout.setErrorEnabled(true);
            textDataLayout.setError("Entre com uma Data");
            edtDataAn.setError("Campo não pode ser nulo");
            return false;
        }
        textDataLayout.setErrorEnabled(false);
        return true;
    }

}
