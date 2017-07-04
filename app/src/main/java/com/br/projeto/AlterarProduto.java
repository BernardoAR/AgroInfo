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
import android.widget.TextView;
import android.widget.Toast;

import com.br.projeto.dao.DAO;
import com.br.projeto.modelo.Categoria;
import com.br.projeto.modelo.Produto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class AlterarProduto extends AppCompatActivity {

    public static String categN;
    public static int idCat;
    // Declarar

    public EditText edtNomeProd;
    TextView textCateg;
    Produto produto,altproduto;
    Categoria categoria;
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
        produto = new Produto();
        categoria = new Categoria();
        dao = new DAO(this);

        //resgatar os componentes

        edtNomeProd = (EditText) findViewById(R.id.edtNomeProd);
        editPrecoCusto = (EditText) findViewById(R.id.editPrecoCusto);
        editPrecoVenda = (EditText) findViewById(R.id.editPrecoVenda);
        quantidade = (EditText) findViewById(R.id.quantidade);
        btnSalvarProd = (Button) findViewById(R.id.btnSalvarProd);
        btnExcluirProd = (Button) findViewById(R.id.btnExcluirProd);
        textCateg = (TextView) findViewById(R.id.textCateg);
        // Layout
        textPrecoCus = (TextInputLayout) findViewById(R.id.textPrecoCus);
        textPrecoVen = (TextInputLayout) findViewById(R.id.textPrecoVen);
        if (altproduto != null) {
            edtNomeProd.setText(altproduto.getNomeProduto());
            produto.setId_produto(altproduto.getId_produto());
            idCat = helper.getCategoriaProd(produto.getId_produto());
            categN = helper.getCategoriaProdN(idCat);
            categoria.setId_categoria(idCat);
            categoria.setNova_categoria(categN);
            editPrecoCusto.setText(String.valueOf(altproduto.getPrecoCusto()));
            editPrecoVenda.setText(String.valueOf(altproduto.getPrecoVenda()));
            quantidade.setText(String.valueOf(altproduto.getQuantidade()));
        }

        textCateg.setText("Categoria: " + categN);

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

}
