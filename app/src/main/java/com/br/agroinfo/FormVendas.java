package com.br.agroinfo;


import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.br.agroinfo.modelo.Vendas;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

public class FormVendas extends AppCompatActivity {

    String[] datas = new String[]
            {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro",
                    "Novembro", "Dezembro"};
    String valorId, valor, dataC, id_categ, catego, quantT;
    Button btnCalcular, btnVenda, btnVerVendas;
    EditText edtQuant, edtAno;
    TextView textQuanti, textPreco, textPrecoFin;
    TextInputLayout textQuant, textAno;
    Spinner spnProd, spnMes;
    private List<Produto> listProduto = new ArrayList<>() ;
    private ArrayAdapter<Produto> arrayAdapterProduto;
    private ArrayAdapter<String> arrayAdapterMes;
    int total, posicaoMes;
    float precoCusto, precoVenda, precoFinalC,precoFinalV;
    private Vibrator vib;
    Animation animBalanc;
    ValueEventListener pop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_vendas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView titulo = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titulo.setText("NOVA VENDA");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        inicFirebase();
        //Chamar Componentes
        btnCalcular = (Button) findViewById(R.id.btnCalcular);
        btnVenda = (Button) findViewById(R.id.btnVenda);
        btnVerVendas = (Button) findViewById(R.id.btnlistarVendas);
        edtAno = (EditText) findViewById(R.id.edtAno);
        edtQuant = (EditText) findViewById(R.id.edtQuantV);
        textQuanti = (TextView) findViewById(R.id.textQuanti);
        textPreco = (TextView) findViewById(R.id.textPrecoVenn);
        textPrecoFin = (TextView) findViewById(R.id.textPrecoVV);
        textAno = (TextInputLayout) findViewById(R.id.textAno);
        textQuant = (TextInputLayout) findViewById(R.id.textQuant);
        spnProd = (Spinner) findViewById(R.id.spnProd);
        animBalanc = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.balancar);
        spnMes = (Spinner) findViewById(R.id.spnMes);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // ARRAY PARA OS MESES
        spinnerMes();

        //FUNÇÕES DOS BOTÕES
        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BigDecimal precoF;
                String quant = edtQuant.getText().toString();
                int quantidade = Integer.valueOf(quant);
                precoFinalV = precoVenda * quantidade;
                precoFinalC = precoCusto * quantidade;
                precoF = Publico.Casas(precoFinalV);
                textPrecoFin.setText("Preço Final: " + String.valueOf(precoF) + " R$");
            }
        });
        btnVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submForm();
                if (checaQuant() && checaAno() && checaCalculo()) {
                    // Modificar na Venda
                    pegaVCategoria();
                }
            }
        });
        //Clicar para Listar
        btnVerVendas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Publico.Intente(FormVendas.this, ListaVendas.class);
                // solicitar para abir
                finish();

            }
        });
        //ItemSelectedListenerMes
        spnMes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // Pegar ID e o Valor do Produto, forçar para que o Spinner pegue
                posicaoMes = spnMes.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // Faz Nada
            }
        });
        //Máscara
        MaskEditTextChangedListener maskAno = new MaskEditTextChangedListener("20##", edtAno);
        edtAno.addTextChangedListener(maskAno);

        //ItemSelectedListenerProduto
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
                preco = Publico.Casas(precoVenda);
                quantT = ("Quantidade Total: " + String.valueOf(total));
                textQuanti.setText(quantT);
                textPreco.setText("Preço: " + String.valueOf(preco) + " R$");
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // Faz Nada
            }
        });
    }

    private void spinnerMes() {
        arrayAdapterMes = new ArrayAdapter<>(FormVendas.this,android.R.layout.simple_spinner_item, datas);
        arrayAdapterMes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMes.setAdapter(arrayAdapterMes);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Publico.Intente(FormVendas.this, MenuP.class);
        finish();
    }

    private void limpaCampos() {
        edtAno.setText("");
        edtQuant.setText("");
    }
    private void inicFirebase() {
        populaLista();
    }

    private void populaLista() {
        pop = FormularioLogin.databaseReference.child("Produto").child("Produtos").orderByChild("Usuario").equalTo(FormularioLogin.usuario.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listProduto.clear();
                        for (DataSnapshot objSnapshot:dataSnapshot.getChildren()) {
                            Produto p = objSnapshot.getValue(Produto.class);
                            listProduto.add(p);
                        }
                        arrayAdapterProduto = new ArrayAdapter<>(FormVendas.this,
                                android.R.layout.simple_list_item_1, listProduto);
                        spnProd.setAdapter(arrayAdapterProduto);
                        FormularioLogin.databaseReference.removeEventListener(pop);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
    // PEGAR A CATEGORIA
    private void pegaVCategoria() {
        FormularioLogin.databaseReference.child("Produto").child("Produtos").child(valorId).child("Categoria")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        id_categ = dataSnapshot.getValue().toString();
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

        String ano = edtAno.getText().toString();
        ve.setId_venda(UUID.randomUUID().toString());
        ve.setMes_ano(String.valueOf(posicaoMes + 1) + "_" + String.valueOf(ano));
        ve.setPreco_venda(precoFinalV);
        ve.setPreco_custo(precoFinalC);
        ve.setMes(posicaoMes + 1);
        ve.setAno(Integer.valueOf(ano));
        ve.setQuant_venda(Integer.valueOf(edtQuant.getText().toString()));
        FormularioLogin.databaseReference.child("Vendas").child(FormularioLogin.usuario.getUid()).child(ve.getId_venda()).setValue(ve);

        p.setId_produto(valorId);
        p.setNomeProduto(valor);
        p.setPrecoCusto(precoCusto);
        p.setPrecoVenda(precoVenda);
        p.setQuantidade(total - ve.getQuant_venda());
        p.setDataCadastro(dataC);
        c.setId_categoria(getCatego());

        FormularioLogin.databaseReference.child("Vendas").child(FormularioLogin.usuario.getUid()).child(ve.getId_venda())
                .child("Id_produto").setValue(p.getId_produto());
        FormularioLogin.databaseReference.child("Produto").child("Produtos")
                .child(p.getId_produto()).setValue(p);
        FormularioLogin.databaseReference.child("Produto").child("Produtos")
                .child(p.getId_produto()).child("Categoria").setValue(c.getId_categoria());
        FormularioLogin.databaseReference.child("Produto").child("Produtos")
                .child(p.getId_produto()).child("Usuario").setValue(FormularioLogin.usuario.getUid());
        Publico.Alerta(FormVendas.this, "Venda feita com Sucesso");
        // Modificar no Campo do Produto
        Publico.Intente(FormVendas.this, MenuP.class);
        limpaCampos();
        finish();
    }

    private void submForm() {
        if (!checaQuant()) {
            edtQuant.setAnimation(animBalanc);
            edtQuant.startAnimation(animBalanc);
            vib.vibrate(120);
            return;
        }
        if (!checaAno()) {
            edtAno.setAnimation(animBalanc);
            edtAno.startAnimation(animBalanc);
            vib.vibrate(120);
            return;
        }
        if (!checaCalculo()) {
            return;
        }
        textQuant.setErrorEnabled(false);
        textAno.setErrorEnabled(false);
    }
    private boolean checaCalculo() {
        if(precoFinalC <= 0  || precoFinalV <= 0){
            Publico.Alerta(FormVendas.this, "É necessário fazer o cálculo antes da venda");
            return false;
        }
        return true;
    }
    private boolean checaQuant(){
        int quant = 0;
        String calc = edtQuant.getText().toString().trim();
        if (!calc.isEmpty()){
            quant = Integer.parseInt(calc);
        }
        if(calc.isEmpty() || quant > total){
            textQuant.setErrorEnabled(true);
            textQuant.setError("Insira um número, sendo ele menor ou igual a Quantidade total");
            edtQuant.setError("Necessita de Entrada Válida");
            return false;
        }
        textQuant.setErrorEnabled(false);
        return true;
    }
    private boolean checaAno(){
        String ano = edtAno.getText().toString().trim();
        int quant = 0;
        if (!ano.trim().isEmpty()){
            quant = Integer.valueOf(ano);
        }
        if(quant < 2017 || ano.trim().isEmpty()){
            textAno.setErrorEnabled(true);
            textAno.setError("Insira um ano, sendo ele maior ou igual a 2017");
            edtAno.setError("Necessita de Entrada Válida");
            return false;
        }
        textAno.setErrorEnabled(false);
        return true;
    }
    public void setCatego(String catego) { this.catego = catego; }

    public String getCatego() { return catego; }
}
