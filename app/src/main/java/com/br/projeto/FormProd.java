package com.br.projeto;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.br.projeto.dao.DAO;
import com.br.projeto.modelo.Categoria;
import com.br.projeto.modelo.Produto;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class FormProd extends AppCompatActivity {

    public static int valorId;

    // Declarar
    Button btnSelDataC;
    public EditText edtNomeProd;
    Spinner spnCateg;
    public EditText editPrecoCusto;
    public EditText editPrecoVenda;
    public EditText quantidade;
    public EditText DataCadastro;
    public Button btnAdProd;
    public Button btnListProd;
    public Button btnAdCat;
    private TextInputLayout textPrecoC, textPrecoV;
    private Vibrator vib;
    Animation animBalanc;

    DAO dao;
    Categoria helperc = new Categoria();
    DAO helper = new DAO(this);
    ArrayList<Categoria> arrayListCategoria;
    ArrayAdapter<Categoria> arrayAdapterCategoria;
    long retornoDB;
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
        btnSelDataC = (Button) findViewById(R.id.btnSelDataC);

        // Chamar Animações
        animBalanc = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.balancar);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Categoria
        Button botaoCateg = (Button) findViewById(R.id.btnAdCat);
        Button btnAdProd = (Button) findViewById(R.id.btnAdProd);
        //Produto
        Button btnListProd = (Button) findViewById(R.id.btnListProd);
        // Configura a Ação de clique
        botaoCateg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent abrirCateg = new Intent(FormProd.this, NovaCategoria.class);
                // solicitar para abir
                startActivity(abrirCateg);

            }
        });
        //Data pegar
        btnSelDataC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String diaString = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault()).format(new Date());
                DataCadastro.setText(diaString);
            }
        });
        //
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

                    // Pegar Strings
                    EditText edtNomeProd = (EditText)findViewById(R.id.edtNomeProd);
                    EditText editPrecoCusto = (EditText)findViewById(R.id.editPrecoCusto);
                    EditText editPrecoVenda = (EditText)findViewById(R.id.editPrecoVenda);
                    EditText quantidade = (EditText) findViewById(R.id.quantidade);
                    EditText DataCadastro = (EditText) findViewById(R.id.DataCadastro);
                    // Colocar em Strings
                    String nome = edtNomeProd.getText().toString();
                    String PrecoC = editPrecoCusto.getText().toString();
                    String PrecoV = editPrecoVenda.getText().toString();
                    String Quantidade = quantidade.getText().toString();
                    String Data = DataCadastro.getText().toString();


                    // Inserir os detalhes no BD
                    Produto p   = new Produto();
                    p.setNomeProduto(nome);
                    p.setPrecoCusto (Float.parseFloat(PrecoC));
                    p.setPrecoVenda(Float.parseFloat(PrecoV));
                    p.setQuantidade(Integer.parseInt(Quantidade));
                    p.setDataCadastro(Data);

                    dao.salvarProduto(p);

                    Toast tempos = Toast.makeText(FormProd.this,String.valueOf(valorId),Toast.LENGTH_SHORT);
                    tempos.show();

                    if (retornoDB == -1) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(FormProd.this);
                        dialog.setTitle("AgroInfo");
                        dialog.setMessage("Erro ao cadastrar!");
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                FormProd.this.finish();
                            }
                        });


                        dialog.show();

                    } else {

                        AlertDialog.Builder dialog = new AlertDialog.Builder(FormProd.this);
                        dialog.setTitle("AgroInfo");
                        dialog.setMessage("Cadastrado com sucesso!");
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                FormProd.this.finish();
                            }
                        });


                        dialog.show();
                    }

                }

            }
        });

        // Configura a Ação de clique listagem produtos
        btnListProd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent abrirListaProd = new Intent(FormProd.this, Lista_produtos.class);
                // solicitar para abir
                startActivity(abrirListaProd);

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
                Toast tempo2 = Toast.makeText(FormProd.this,valor,Toast.LENGTH_SHORT);
                tempo2.show();
                Toast tempo = Toast.makeText(FormProd.this,String.valueOf(valorId),Toast.LENGTH_SHORT);
                tempo.show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // Faz Nada
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