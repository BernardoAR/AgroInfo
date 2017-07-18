package com.br.agroinfo;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import com.br.agroinfo.modelo.Categoria;
import com.br.agroinfo.modelo.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

public class FormProd extends AppCompatActivity {
    Button btnSelDataC, btnListProd, btnCateg, btnAdProd;
    Spinner spnCateg;
    EditText edtPrecoCusto, edtPrecoVenda, edtQuantidade, edtDataCadastro, edtNomeProd;
    TextInputLayout textPrecoC, textPrecoV, textDataCLayout, textQuant, textNomeProd;
    Vibrator vib;
    Animation animBalanc;
    String valorId;
    private List<Categoria> listCategoria = new ArrayList<>() ;
    private ArrayAdapter<Categoria> arrayAdapterCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_prod);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView titulo = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titulo.setText("NOVO PRODUTO");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Pega as partes do Layout
        edtNomeProd = (EditText) findViewById(R.id.edtNomeProd);
        spnCateg = (Spinner) findViewById(R.id.spnCateg);
        edtPrecoCusto = (EditText) findViewById(R.id.editPrecoCusto);
        edtPrecoVenda = (EditText) findViewById(R.id.editPrecoVenda);
        edtQuantidade = (EditText) findViewById(R.id.quantidade);
        edtDataCadastro = (EditText) findViewById(R.id.DataCadastro);
        btnCateg = (Button) findViewById(R.id.btnAdCat);
        btnAdProd = (Button) findViewById(R.id.btnAdProd);
        btnListProd = (Button) findViewById(R.id.btnListProd);
        btnSelDataC = (Button) findViewById(R.id.btnSelDataC);
        textPrecoC = (TextInputLayout) findViewById(R.id.textPrecoC);
        textPrecoV = (TextInputLayout) findViewById(R.id.textPrecoV);
        textDataCLayout = (TextInputLayout) findViewById(R.id.textDataCLayout);
        textNomeProd = (TextInputLayout) findViewById(R.id.textNomeProd);
        textQuant = (TextInputLayout) findViewById(R.id.textQuant);

        // Chamar Animações
        animBalanc = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.balancar);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Configura a Ação de clique
        btnCateg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Publico.Intente(FormProd.this, NovaCategoria.class);
                // solicitar para abir
                finish();

            }
        });
        btnSelDataC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String diaString = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault()).format(new Date());
                edtDataCadastro.setText(diaString);
            }
        });
        btnAdProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submForm();
                if ( checaCategoria() && checaNomeProd() && checaNumC() && checaNumV() &&  checaQuant() && checaData()){
                    // Método para Conversão de Float
                    Float precoC = Float.valueOf(edtPrecoCusto.getText().toString());
                    Float precoV = Float.valueOf(edtPrecoVenda.getText().toString());
                    int quant = Integer.valueOf(edtQuantidade.getText().toString());
                    Produto p = new Produto();
                    p.setId_produto(UUID.randomUUID().toString());
                    p.setDataCadastro(edtDataCadastro.getText().toString().replace('/', '-'));
                    p.setNomeProduto(edtNomeProd.getText().toString().toUpperCase());
                    p.setPrecoCusto(precoC);
                    p.setPrecoVenda(precoV);
                    p.setQuantidade(quant);
                    Categoria c = new Categoria();
                    c.setId_categoria(valorId);
                    FormularioLogin.databaseReference.child("Produto").child("Produtos").child(p.getId_produto()).setValue(p);
                    FormularioLogin.databaseReference.child("Produto").child("Produtos")
                            .child(p.getId_produto()).child("Categoria").setValue(c.getId_categoria());
                    FormularioLogin.databaseReference.child("Produto").child("Produtos")
                            .child(p.getId_produto()).child("Usuario").setValue(FormularioLogin.usuario.getUid());
                    Publico.Alerta(FormProd.this, "Cadastrado com Sucesso!");
                    Publico.Intente(FormProd.this, MenuP.class);
                    limpaCampos();
                    finish();
                }
            }
        });
        btnListProd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Publico.Intente(FormProd.this, ListaProdutos.class);
                // solicitar para abir
                finish();

            }
        });
        //ClickListener do Spinner
        spnCateg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // Pegar ID e o Valor da Categoria, forçar para que o Spinner pegue
                Categoria item = (Categoria) parent.getItemAtPosition(pos);
                String valor = item.getNova_categoria();
                valorId = item.getId_categoria();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // Faz Nada
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        populaLista();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Publico.Intente(FormProd.this, MenuP.class);
        finish();
    }
    private void limpaCampos() {
        edtNomeProd.setText("");
        edtPrecoCusto.setText("");
        edtPrecoVenda.setText("");
        edtQuantidade.setText("");
        edtDataCadastro.setText("");
    }

    //Void para ver todos
    private void submForm() {
        if (!checaCategoria()){
            Publico.Alerta(FormProd.this, "Não possui categoria correspondente");
            return;
        }
        if (!checaNomeProd()) {
            edtNomeProd.setAnimation(animBalanc);
            edtNomeProd.startAnimation(animBalanc);
            vib.vibrate(120);
            return;
        }
        if (!checaNumC()) {
            edtPrecoCusto.setAnimation(animBalanc);
            edtPrecoCusto.startAnimation(animBalanc);
            vib.vibrate(120);
            return;
        }
        if (!checaNumV()) {
            edtPrecoVenda.setAnimation(animBalanc);
            edtPrecoVenda.startAnimation(animBalanc);
            vib.vibrate(120);
            return;
        }
        if (!checaQuant()) {
            edtQuantidade.setAnimation(animBalanc);
            edtQuantidade.startAnimation(animBalanc);
            vib.vibrate(120);
            return;
        }
        if (!checaData()) {
            edtDataCadastro.setAnimation(animBalanc);
            edtDataCadastro.startAnimation(animBalanc);
            vib.vibrate(120);
            return;
        }
        textNomeProd.setErrorEnabled(false);
        textPrecoC.setErrorEnabled(false);
        textPrecoV.setErrorEnabled(false);
        textQuant.setErrorEnabled(false);
        textDataCLayout.setErrorEnabled(false);
    }

    private boolean checaCategoria() {
        if (valorId == null){
            return false;
        }
        return true;
    }

    private boolean checaNomeProd(){
        String nomeProd = edtNomeProd.getText().toString().trim();
        if(nomeProd.isEmpty()){
            textNomeProd.setErrorEnabled(true);
            textNomeProd.setError("Indique o nome do produto");
            edtNomeProd.setError("Necessita de Entrada Válida");
            return false;
        }
        textNomeProd.setErrorEnabled(false);
        return true;
    }
    private boolean checaQuant(){
        String quanti = edtQuantidade.getText().toString().trim();
        int quant = 0;
        if (!quanti.isEmpty()){
            quant = Integer.valueOf(quanti);
        }
        if(quant < 1){
            textQuant.setErrorEnabled(true);
            textQuant.setError("Indique a Quantidade, sendo maior que 0");
            edtQuantidade.setError("Necessita de Entrada Válida");
            return false;
        }
        textQuant.setErrorEnabled(false);
        return true;
    }
    private boolean checaData(){
        String data = edtDataCadastro.getText().toString().trim();
        if(data.length() < 6){
            textDataCLayout.setErrorEnabled(true);
            textDataCLayout.setError("Indique uma data");
            edtDataCadastro.setError("Necessita de Entrada Válida");
            return false;
        }
        textDataCLayout.setErrorEnabled(false);
        return true;
    }
    private boolean checaNumC(){
        String num = edtPrecoCusto.getText().toString().trim();
        if(num.isEmpty() || !validNum(num)){
            textPrecoC.setErrorEnabled(true);
            textPrecoC.setError("Máximo 6 Números, sendo 2 Decimais");
            edtPrecoCusto.setError("Necessita de Entrada Válida");
            return false;
        }
        textPrecoC.setErrorEnabled(false);
        return true;
    }
    //FLOAT PRECOVENDA
    private boolean checaNumV(){
        String num = edtPrecoVenda.getText().toString().trim();
        if(num.isEmpty() || !validNum(num)){
            textPrecoV.setErrorEnabled(true);
            textPrecoV.setError("Máximo 6 Números, sendo 2 Decimais");
            edtPrecoVenda.setError("Necessita de Entrada Válida");
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

    // Pegar os Valores
    private void populaLista() {
        FormularioLogin.databaseReference.child("Categoria").child(FormularioLogin.usuario.getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listCategoria.clear();
                for (DataSnapshot objSnapshot:dataSnapshot.getChildren()) {
                    Categoria c = objSnapshot.getValue(Categoria.class);
                    listCategoria.add(c);
                }
                arrayAdapterCategoria = new ArrayAdapter<>(FormProd.this,
                        android.R.layout.simple_list_item_1, listCategoria);
                spnCateg.setAdapter(arrayAdapterCategoria);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}