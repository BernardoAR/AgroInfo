package com.br.agroinfo;

import android.content.Context;
import android.os.Vibrator;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.br.agroinfo.modelo.Anotacao;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;


public class NovaAnotacao extends AppCompatActivity {
    private Vibrator vib;
    Animation animBalanc;
    EditText edtNovaAn, edtDataAn, edtAssunto;
    TextInputLayout textAssunto, textAnotacao, textDataLayout;
    Button btnCadastrar, btnSelData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_anotacao);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView titulo = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titulo.setText("NOVA ANOTAÇÃO");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //resgatar os componentes
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
        btnSelData = (Button) findViewById(R.id.btnSelData);
        edtNovaAn = (EditText) findViewById(R.id.anotacao);
        edtDataAn = (EditText) findViewById(R.id.edtDataAn);
        edtAssunto = (EditText) findViewById(R.id.edtAssunto);
        textAssunto = (TextInputLayout) findViewById(R.id.textAssunto);
        textAnotacao = (TextInputLayout) findViewById(R.id.textAnotacao);
        textDataLayout = (TextInputLayout) findViewById(R.id.textDataLayout);

        btnSelData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String diaString = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault()).format(new Date());
                edtDataAn.setText(diaString);
            }
        });

        // Chamar os de Animação
        animBalanc = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.balancar);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        MaskEditTextChangedListener MaskData = new MaskEditTextChangedListener("##/##/##", edtDataAn);
        edtDataAn.addTextChangedListener(MaskData);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submForm();
                if(checaAssunto() && checaAnotacao() && checaData()){
                    // Colocar em Strings
                    String anotacao = edtNovaAn.getText().toString();
                    String data = edtDataAn.getText().toString();
                    String assunto = edtAssunto.getText().toString();
                    // Inserir os detalhes no BD
                    Anotacao a = new Anotacao();
                    a.setId_anotacao(UUID.randomUUID().toString());
                    a.setData(data.replace('/', '-'));
                    a.setAssunto(assunto);
                    a.setAnotacao(anotacao);
                    Inicial.databaseReference.child("Anotacao").child(Inicial.usuario.getUid()).child(a.getId_anotacao()).setValue(a);
                    Publico.Alerta(NovaAnotacao.this, "Salvado com Sucesso");
                    Publico.Intente(NovaAnotacao.this, ListaAnotacoes.class);
                    limparCampos();
                    finish();
                }
            }
        });

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Publico.Intente(NovaAnotacao.this, ListaAnotacoes.class);
        finish();
    }


    private void limparCampos() {
        edtNovaAn.setText("");
        edtDataAn.setText("");
        edtAssunto.setText("");
    }

    private void submForm() {
        if (!checaAssunto()) {
            edtAssunto.setAnimation(animBalanc);
            edtAssunto.startAnimation(animBalanc);
            vib.vibrate(120);
            return;
        }
        if (!checaAnotacao()) {
            edtNovaAn.setAnimation(animBalanc);
            edtNovaAn.startAnimation(animBalanc);
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
        if(edtNovaAn.getText().toString().trim().isEmpty()){
            textAnotacao.setErrorEnabled(true);
            textAnotacao.setError("Entre com a Anotação");
            edtNovaAn.setError("Campo não pode ser nulo");
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
