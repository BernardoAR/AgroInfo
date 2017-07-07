package com.br.agroinfo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.br.agroinfo.modelo.Categoria;
import com.br.agroinfo.modelo.Produto;
import com.br.agroinfo.modelo.Vendas;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class FormVendas extends AppCompatActivity {

    String valorId, valor, dataC, id_categ, catego;
    Button btnPData, btnCalcular, btnVenda;
    EditText edtQuant, edtData;
    TextView textQuanti, textPreco, textPrecoFin;
    TextInputLayout textQuant, textLData;
    Spinner spnProd;
    private List<Produto> listProduto = new ArrayList<Produto>() ;
    private ArrayAdapter<Produto> arrayAdapterProduto;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    int total;
    float precoCusto, precoVenda, precoFinal;
    private Vibrator vib;
    Animation animBalanc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_vendas);
        inicFirebase();
        //Chamar Componentes
        btnCalcular = (Button) findViewById(R.id.btnCalcular);
        btnVenda = (Button) findViewById(R.id.btnVenda);
        btnPData = (Button) findViewById(R.id.btnData);
        edtData = (EditText) findViewById(R.id.edtData);
        edtQuant = (EditText) findViewById(R.id.edtQuantV);
        textQuanti = (TextView) findViewById(R.id.textQuanti);
        textPreco = (TextView) findViewById(R.id.textPrecoVenn);
        textPrecoFin = (TextView) findViewById(R.id.textPrecoVV);
        textLData = (TextInputLayout) findViewById(R.id.textLData);
        textQuant = (TextInputLayout) findViewById(R.id.textQuant);
        spnProd = (Spinner) findViewById(R.id.spnProd);
        animBalanc = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.balancar);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //FUNÇÕES DOS BOTÕES
        btnPData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String diaString = java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT, Locale.getDefault()).format(new Date());
                diaString = diaString.replace('/', '-');
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
                if (checaCalculo()) {
                    //TODO: FALTA PARÂMETROS, como checaData, falta também no FormProd
                    // Modificar na Venda
                    pegaVCategoria();
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
                valor = item.getNomeProduto();
                valorId = item.getId_produto();
                total = item.getQuantidade();
                precoCusto = item.getPrecoCusto();
                precoVenda = item.getPrecoVenda();
                dataC = item.getDataCadastro();
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

    private void limpaCampos() {
        edtData.setText("");
        edtQuant.setText("");
    }
    private void inicFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
    private void alerta(String mensagem) {
        Toast.makeText(FormVendas.this, mensagem, Toast.LENGTH_SHORT).show();
    }
    private void populaLista() {
        databaseReference.child("Produto").child(MenuP.usuario.getUid()).child("Produtos").orderByChild("Categoria")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listProduto.clear();
                        for (DataSnapshot objSnapshot:dataSnapshot.getChildren()) {
                            Produto p = objSnapshot.getValue(Produto.class);
                            listProduto.add(p);
                        }
                        arrayAdapterProduto = new ArrayAdapter<Produto>(FormVendas.this,
                                android.R.layout.simple_list_item_1, listProduto);
                        spnProd.setAdapter(arrayAdapterProduto);

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
    // PEGAR A CATEGORIA
    private void pegaVCategoria() {
        databaseReference.child("Produto").child(MenuP.usuario.getUid()).child("Produtos")
                .child(valorId).child("Categoria")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        id_categ = dataSnapshot.getValue().toString();
                        Toast.makeText(FormVendas.this,"PegaVCategoria:" + id_categ, Toast.LENGTH_SHORT).show();
                        setCatego(id_categ);
                        salvarCateg();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void salvarCateg() {
        Vendas ve = new Vendas();
        Produto p = new Produto();
        Categoria c = new Categoria();

        ve.setId_venda(UUID.randomUUID().toString());
        ve.setData_venda(edtData.getText().toString());
        ve.setQuant_venda(Integer.valueOf(edtQuant.getText().toString()));
        databaseReference.child("Vendas").child(MenuP.usuario.getUid()).child(ve.getId_venda()).setValue(ve);

        p.setId_produto(valorId);
        p.setNomeProduto(valor);
        p.setPrecoCusto(precoCusto);
        p.setPrecoVenda(precoVenda);
        p.setQuantidade(total - ve.getQuant_venda());
        p.setDataCadastro(dataC);
        c.setId_categoria(getCatego());

        databaseReference.child("Vendas").child(MenuP.usuario.getUid()).child(ve.getId_venda())
                .child("Id_produto").setValue(p.getId_produto());
        databaseReference.child("Produto").child(MenuP.usuario.getUid()).child("Produtos")
                .child(p.getId_produto()).setValue(p);
        databaseReference.child("Produto").child(MenuP.usuario.getUid()).child("Produtos")
                .child(p.getId_produto()).child("Categoria").setValue(c.getId_categoria());
        alerta("Venda feita com Sucesso");
        // Modificar no Campo do Produto
        limpaCampos();
        finish();
    }

    private void submForm() {
        if (!checaCalculo()) {
            edtQuant.setAnimation(animBalanc);
            edtQuant.startAnimation(animBalanc);
            vib.vibrate(120);
            return;
        }
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

    public void setCatego(String catego) { this.catego = catego; }

    public String getCatego() { return catego; }
}
