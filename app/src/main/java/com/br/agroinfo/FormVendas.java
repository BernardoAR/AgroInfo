package com.br.projeto;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Vibrator;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.br.projeto.dao.DAO;
import com.br.projeto.modelo.Categoria;
import com.br.projeto.modelo.Produto;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FormVendas extends AppCompatActivity {

    int valorId;
    Button btnPData, btnCalcular, btnVenda;
    EditText edtQuant, edtData;
    TextView textQuanti, textPreco, textPrecoFin;
    TextInputLayout textQuant, textNumMes, textNumAno;
    Spinner spnProd;
    DAO dao;
    ArrayList<Produto> arrayListProdutos;
    ArrayAdapter<Produto> arrayAdapterProdutos;
    int total;
    float precoVenda, precoFinal;
    private Vibrator vib;
    Animation animBalanc;
    long retornoDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_vendas);

        //Chamar Componentes
        btnCalcular = (Button) findViewById(R.id.btnCalcular);
        btnVenda = (Button) findViewById(R.id.btnVenda);
        btnPData = (Button) findViewById(R.id.btnData);
        edtData = (EditText) findViewById(R.id.edtData);
        edtQuant = (EditText) findViewById(R.id.edtQuantV);
        textQuanti = (TextView) findViewById(R.id.textQuanti);
        textPreco = (TextView) findViewById(R.id.textPrecoVenn);
        textPrecoFin = (TextView) findViewById(R.id.textPrecoVV);
        textQuant = (TextInputLayout) findViewById(R.id.textQuant);
        spnProd = (Spinner) findViewById(R.id.spnProd);
        animBalanc = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.balancar);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //FUNÇÕES DOS BOTÕES
        btnPData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String diaString = java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT, Locale.getDefault()).format(new Date());
                edtData.setText(diaString);
            }
        });

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BigDecimal precoF;
                String quant = edtQuant.getText().toString();
                int quantidade = Integer.parseInt(quant);
                precoFinal = precoVenda * quantidade;
                precoF = FormProd.casas(precoFinal, 2);
                textPrecoFin.setText("Preço Final: " + String.valueOf(precoF) + " R$");
            }
        });
        btnVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submForm();
                if (checaCalculo()){
                    //TODO: FALTA PARÂMETROS, como checaData, falta também no FormProd



                    if (retornoDB == -1) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(FormVendas.this);
                        dialog.setTitle("AgroInfo");
                        dialog.setMessage("Erro ao cadastrar!");
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                FormVendas.this.finish();
                            }
                        });


                        dialog.show();

                    } else {

                        AlertDialog.Builder dialog = new AlertDialog.Builder(FormVendas.this);
                        dialog.setTitle("AgroInfo");
                        dialog.setMessage("Cadastrado com sucesso!");
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                FormVendas.this.finish();
                            }
                        });


                        dialog.show();
                    }

                }

            }
        });

        //ClickListener do Spinner
        spnProd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // Pegar ID e o Valor do Produto, forçar para que o Spinner pegue
                BigDecimal preco;
                Produto item = (Produto) parent.getItemAtPosition(pos);
                String valor = item.getNomeProduto();
                valorId = item.getId_produto();
                total = item.getQuantidade();
                precoVenda = item.getPrecoVenda();
                preco = FormProd.casas(precoVenda, 2);
                textQuanti.setText("Quantidade Total: " + String.valueOf(total));
                textPreco.setText("Preço: " + String.valueOf(preco) + " R$");
                Toast tempo2 = Toast.makeText(FormVendas.this,valor,Toast.LENGTH_SHORT);
                tempo2.show();
                Toast tempo = Toast.makeText(FormVendas.this,String.valueOf(valorId),Toast.LENGTH_SHORT);
                tempo.show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // Faz Nada
            }
        });
    }

    public void populaLista() {
        dao = new DAO(this);
        arrayListProdutos = dao.selectAllProduto();
        dao.close();

        if (spnProd != null) {
            arrayAdapterProdutos = new ArrayAdapter<Produto>(FormVendas.this,
                    android.R.layout.simple_spinner_dropdown_item, arrayListProdutos);
            spnProd.setAdapter(arrayAdapterProdutos);
        }
    }

    private void submForm() {
        if (!checaCalculo()) {
            edtQuant.setAnimation(animBalanc);
            edtQuant.startAnimation(animBalanc);
            vib.vibrate(120);
            return;
        }
        textNumAno.setErrorEnabled(false);
        textNumMes.setErrorEnabled(false);
        textQuant.setErrorEnabled(false);


    }
    private boolean checaCalculo(){
        String calc = edtQuant.getText().toString().trim();
        int quant = Integer.parseInt(calc);
        if(calc.isEmpty() || quant > total){
            textQuant.setErrorEnabled(true);
            textQuant.setError("Insira um número, sendo ele menor ou igual a quantidade total");
            edtQuant.setError("Necessita de Entrada Válida");
            return false;
        }
        textQuant.setErrorEnabled(false);
        return true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        populaLista();
    }
}
