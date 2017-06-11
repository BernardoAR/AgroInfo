package com.br.projeto;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.br.projeto.dao.DAO;
import com.br.projeto.modelo.Categoria;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class FormProd extends AppCompatActivity {
    // Declarar
    private EditText edtNomeProd;
    Spinner spnCateg;
    private EditText editPrecoCusto;
    private EditText editPrecoVenda;
    private EditText quantidade;
    private EditText DataCadastro;
    private Button btnAdProd;
    public Button btnListProd;
    private Button btnAdCat;
    private TextInputLayout textPrecoC, textPrecoV;
    private Vibrator vib;
    Animation animBalanc;

    DAO dao;
    DAO helper = new DAO(this);
    ArrayList<Categoria> arrayListCategoria;
    ArrayAdapter<Categoria> arrayAdapterCategoria;

    //Array Adapter para as Listas
    public ArrayAdapter<String> adpCateg;
    public ArrayAdapter<String> adpProd;

    // Deixar como Padrão o decimal com 2 casas


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_prod);
        // Pega as partes do Layout
        edtNomeProd = (EditText) findViewById(R.id.edtNomeProd);
        spnCateg = (Spinner) findViewById(R.id.spnCateg);
        editPrecoCusto = (EditText) findViewById(R.id.editPrecoCusto);
        editPrecoVenda = (EditText) findViewById(R.id.editPrecoVenda);
        quantidade = (EditText) findViewById(R.id.quantidade);
        DataCadastro = (EditText) findViewById(R.id.DataCadastro);
        btnListProd = (Button) findViewById(R.id.btnListProd);
        textPrecoC = (TextInputLayout) findViewById(R.id.textPrecoC);
        textPrecoV = (TextInputLayout) findViewById(R.id.textPrecoV);

        // Chamar Animações
        animBalanc = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.balancar);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Categoria
        Button botaoCateg = (Button) findViewById(R.id.btnAdCat);
        Button btnAdProd = (Button) findViewById(R.id.btnAdProd);

        // Configura a Ação de clique
        botaoCateg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent abrirCateg = new Intent(FormProd.this, NovaCategoria.class);
                // solicitar para abir
                startActivity(abrirCateg);

            }
        });
        btnAdProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submForm();
                if (checaNumC() && checaNumV()){
                    // Método para Conversão de Float
                    Float f = Float.parseFloat(editPrecoCusto.getText().toString());
                    Float f2 = Float.parseFloat(editPrecoVenda.getText().toString());
                    BigDecimal resultado, resultado2;
                    resultado = casas(f,2);
                    resultado2 = casas(f2,2);
                    //

                    Toast tempor = Toast.makeText(FormProd.this,"Tudo certinho, com valores de",Toast.LENGTH_SHORT);
                    tempor.show();
                    Toast tempor2 = Toast.makeText(FormProd.this,String.valueOf(resultado),Toast.LENGTH_SHORT);
                    tempor2.show();
                    Toast tempor3 = Toast.makeText(FormProd.this,String.valueOf(resultado2),Toast.LENGTH_SHORT);
                    tempor3.show();
                }

            }
        });


    }

    // Fazer com que fique com duas decimais FORÇADAMENTE
    public static BigDecimal casas(float d, int casasDec) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(casasDec, BigDecimal.ROUND_HALF_UP);
        return bd;
    }
    //Void para ver todos
    private void submForm() {
        if (!checaNumC()) {
            editPrecoCusto.setAnimation(animBalanc);
            editPrecoCusto.startAnimation(animBalanc);
            vib.vibrate(120);
            return;
        }
        if (!checaNumV()) {
            editPrecoVenda.setAnimation(animBalanc);
            editPrecoVenda.startAnimation(animBalanc);
            vib.vibrate(120);
            return;
        }
        textPrecoC.setErrorEnabled(false);
        textPrecoV.setErrorEnabled(false);


    }
    // FLOAT PRECOCUSTO
    private boolean checaNumC(){
        String num = editPrecoCusto.getText().toString().trim();
        if(num.isEmpty() || !validNum(num)){
            textPrecoC.setErrorEnabled(true);
            textPrecoC.setError("Máximo 6 Números, sendo 2 Decimais");
            editPrecoCusto.setError("Necessita de Entrada Válida");
            return false;
        }
        textPrecoC.setErrorEnabled(false);
        return true;
    }
    //FLOAT PRECOVENDA
    private boolean checaNumV(){
        String num = editPrecoVenda.getText().toString().trim();
        if(num.isEmpty() || !validNum(num)){
            textPrecoV.setErrorEnabled(true);
            textPrecoV.setError("Máximo 6 Números, sendo 2 Decimais");
            editPrecoVenda.setError("Necessita de Entrada Válida");
            return false;
        }
        textPrecoV.setErrorEnabled(false);
        return true;
    }
    //Floates
    private static boolean validNum(String num){
        String validaNum = "^[0-9]{1,4}([.][0-9]{0,2})?$";
        return !TextUtils.isEmpty(num) && Pattern.matches(validaNum, num);
    }

    public void populaLista() {
        dao = new DAO(FormProd.this);
        arrayListCategoria = dao.selectAllCategoria();
        dao.close();

        if (spnCateg != null) {
            arrayAdapterCategoria = new ArrayAdapter<Categoria>(FormProd.this,
                    android.R.layout.simple_spinner_dropdown_item, arrayListCategoria);
            spnCateg.setAdapter(arrayAdapterCategoria);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        populaLista();
    }
}