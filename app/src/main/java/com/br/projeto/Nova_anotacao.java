package com.br.projeto;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.br.projeto.dao.DAO;
import com.br.projeto.modelo.Anotacao;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.br.projeto.MenuP.ID;

public class Nova_anotacao extends AppCompatActivity {
    private Vibrator vib;
    Animation animBalanc;
    DateFormat formatarData = DateFormat.getDateInstance();
    EditText nova_anotacao, edtDataAn, edtAssunto;
    TextInputLayout textAssunto, textAnotacao, textDataLayout;
    Button btnCadastrar, btnSelData;
    DAO helper = new DAO(this);
    long retornoDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_anotacao);

        Toast tempor = Toast.makeText(this,String.valueOf(ID),Toast.LENGTH_LONG);
        tempor.show();

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
                    a.setNovo_assunto(data + " - " + assunto);
                    a.setNova_anotacao(anotacao);

                    helper.salvarAnotacao(a);


                    if (retornoDB == -1) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Nova_anotacao.this);
                        dialog.setTitle("AgroInfo");
                        dialog.setMessage("Erro ao cadastrar!");
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Nova_anotacao.this.finish();
                            }
                        });


                        dialog.show();

                    } else {

                        AlertDialog.Builder dialog = new AlertDialog.Builder(Nova_anotacao.this);
                        dialog.setTitle("AgroInfo");
                        dialog.setMessage("Cadastrado com sucesso!");
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Nova_anotacao.this.finish();
                            }
                        });


                        dialog.show();
                    }

                }
            }
        });

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