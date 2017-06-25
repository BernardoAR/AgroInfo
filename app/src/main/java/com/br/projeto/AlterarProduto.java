package com.br.projeto;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Adapter;
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
import java.util.ArrayList;
import java.util.regex.Pattern;

public class AlterarProduto extends AppCompatActivity {

    public static int valorIdCateg;
    public static int idCat;
    // Declarar

    public EditText edtNomeProd;
    Spinner spnCateg;
    Produto produto,altproduto;
    Categoria altcategoria,categoria;
    public EditText editPrecoCusto;
    public EditText editPrecoVenda;
    public EditText quantidade;
    public Button btnSalvarProd;
    public Button btnExcluirProd;

    private TextInputLayout textPrecoCus, textPrecoVen;
    private Vibrator vib;
    Animation animBalanc;


    DAO dao;
    DAO helper = new DAO(this);
    long retornoDB;
    ArrayList<Categoria> arrayListCategoria;
    ArrayAdapter<Categoria> arrayAdapterCategoria;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_produto);

        Intent abrirEdicao = getIntent();
        altproduto = (Produto) abrirEdicao.getSerializableExtra("Produto-enviado");
        categoria = (Categoria) abrirEdicao.getSerializableExtra("IDCAT-enviada");
        produto = new Produto();
        categoria = new Categoria();
        dao = new DAO(AlterarProduto.this);

        //resgatar os componentes

        edtNomeProd = (EditText) findViewById(R.id.edtNomeProd);
        spnCateg = (Spinner) findViewById(R.id.spnCateg);
        editPrecoCusto = (EditText) findViewById(R.id.editPrecoCusto);
        editPrecoVenda = (EditText) findViewById(R.id.editPrecoVenda);
        quantidade = (EditText) findViewById(R.id.quantidade);
        btnSalvarProd = (Button) findViewById(R.id.btnSalvarProd);
        btnExcluirProd = (Button) findViewById(R.id.btnExcluirProd);
        // Layout
        textPrecoCus = (TextInputLayout) findViewById(R.id.textPrecoCus);
        textPrecoVen = (TextInputLayout) findViewById(R.id.textPrecoVen);

        if (altproduto != null) {
            edtNomeProd.setText(altproduto.getNomeProduto());
            produto.setId_produto(altproduto.getId_produto());
            idCat = helper.getCategoriaProd(produto.getId_produto());
            categoria.setId_categoria(idCat);
            editPrecoCusto.setText(String.valueOf(altproduto.getPrecoCusto()));
            editPrecoVenda.setText(String.valueOf(altproduto.getPrecoVenda()));
            quantidade.setText(String.valueOf(altproduto.getQuantidade()));
        }

        btnSalvarProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submForm();
                if (checaNumC() && checaNumV()) {
                    // Método para Conversão de Float
                    Float f = Float.parseFloat(editPrecoCusto.getText().toString());
                    Float f2 = Float.parseFloat(editPrecoVenda.getText().toString());
                    BigDecimal resultado, resultado2;
                    resultado = casas(f, 2);
                    resultado2 = casas(f2, 2);
                    //


                    produto.setId_produto(altproduto.getId_produto());
                    produto.setNomeProduto(edtNomeProd.getText().toString());
                    produto.setPrecoCusto(Float.parseFloat(editPrecoCusto.getText().toString()));
                    produto.setPrecoVenda(Float.parseFloat(editPrecoVenda.getText().toString()));
                    produto.setQuantidade(Integer.parseInt(quantidade.getText().toString()));


                    dao.alterarProduto(produto);
                    dao.close();


                    if (retornoDB == -1) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(AlterarProduto.this);
                        dialog.setTitle("AgroInfo");
                        dialog.setMessage("Erro ao alterar!");
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                AlterarProduto.this.finish();
                            }
                        });


                        dialog.show();

                    } else {

                        AlertDialog.Builder dialog = new AlertDialog.Builder(AlterarProduto.this);
                        dialog.setTitle("AgroInfo");
                        dialog.setMessage("Alterado com sucesso!");
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                AlterarProduto.this.finish();
                            }
                        });


                        dialog.show();
                    }
                }
            }
        });

        btnExcluirProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                produto.setId_produto(altproduto.getId_produto());

                dao.excluirProduto(produto);
                dao.close();


                if (retornoDB == -1) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(AlterarProduto.this);
                    dialog.setTitle("AgroInfo");
                    dialog.setMessage("Erro ao excluir o registro!");
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            AlterarProduto.this.finish();
                        }
                    });


                    dialog.show();

                } else {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(AlterarProduto.this);
                    dialog.setTitle("AgroInfo");
                    dialog.setMessage("Registro excluído com sucesso!");
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            AlterarProduto.this.finish();
                        }
                    });


                    dialog.show();
                }

            }
        });

        //ClickListener do Spinner
        spnCateg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // Pegar ID e o Valor da Categoria, forçar para que o Spinner pegue
                Categoria item = (Categoria) parent.getItemAtPosition(pos);
                String valor = item.getNova_categoria();
                valorIdCateg = item.getId_categoria();
                Toast tempo2 = Toast.makeText(AlterarProduto.this,valor,Toast.LENGTH_SHORT);
                tempo2.show();
                Toast tempo = Toast.makeText(AlterarProduto.this,String.valueOf(valorIdCateg),Toast.LENGTH_SHORT);
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
        textPrecoCus.setErrorEnabled(false);
        textPrecoVen.setErrorEnabled(false);


    }
    // FLOAT PRECOCUSTO
    private boolean checaNumC(){
        String num = editPrecoCusto.getText().toString().trim();
        if(num.isEmpty() || !validNum(num)){
            textPrecoCus.setErrorEnabled(true);
            textPrecoCus.setError("Máximo 6 Números, sendo 2 Decimais");
            editPrecoCusto.setError("Necessita de Entrada Válida");
            return false;
        }
        textPrecoCus.setErrorEnabled(false);
        return true;
    }
    //FLOAT PRECOVENDA
    private boolean checaNumV(){
        String num = editPrecoVenda.getText().toString().trim();
        if(num.isEmpty() || !validNum(num)){
            textPrecoVen.setErrorEnabled(true);
            textPrecoVen.setError("Máximo 6 Números, sendo 2 Decimais");
            editPrecoVenda.setError("Necessita de Entrada Válida");
            return false;
        }
        textPrecoVen.setErrorEnabled(false);
        return true;
    }
    //Floates
    private static boolean validNum(String num){
        String validaNum = "^[0-9]{1,4}([.][0-9]{0,2})?$";
        return !TextUtils.isEmpty(num) && Pattern.matches(validaNum, num);
    }

    public void populaLista() {
        dao = new DAO(AlterarProduto.this);
        arrayListCategoria = dao.selectAllCategoria();
        dao.close();

        if (spnCateg != null) {
            arrayAdapterCategoria = new ArrayAdapter<Categoria>(AlterarProduto.this,
                    android.R.layout.simple_spinner_dropdown_item, arrayListCategoria);
            spnCateg.setAdapter(arrayAdapterCategoria);

        }
    }


    @Override
    protected void onResume(){
        super.onResume();
        populaLista();
        //Fazer com que pegue no OnResume o da Categoria Escolhida
        spnCateg.setSelection(idCat - 1);
        //

    }
}
