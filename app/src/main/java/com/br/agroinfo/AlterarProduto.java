package com.br.agroinfo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.br.agroinfo.modelo.Categoria;
import com.br.agroinfo.modelo.Produto;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class AlterarProduto extends AppCompatActivity {

    EditText edtNomeProd, edtPrecoCusto, edtPrecoVenda, edtQuantidade;
    TextView textCateg;
    Produto altproduto;
    Button btnSalvarProd, btnExcluirProd;
    TextInputLayout textPrecoCus, textPrecoVen, textNomeProd, textQuant;
    Vibrator vib;
    Animation animBalanc;
    String id_categ, id_Catego, nome_categ, nome_Catego;
    ChildEventListener vendas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_produto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView titulo = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titulo.setText("ALTERAR PRODUTO");
        Intent abrirEdicao = getIntent();
        altproduto = (Produto) abrirEdicao.getSerializableExtra("Produto-enviado");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //resgatar os componentes
        edtNomeProd = (EditText) findViewById(R.id.edtNomeProd);
        edtPrecoCusto = (EditText) findViewById(R.id.editPrecoCusto);
        edtPrecoVenda = (EditText) findViewById(R.id.editPrecoVenda);
        edtQuantidade = (EditText) findViewById(R.id.quantidade);
        btnSalvarProd = (Button) findViewById(R.id.btnSalvarProd);
        btnExcluirProd = (Button) findViewById(R.id.btnExcluirProd);
        textCateg = (TextView) findViewById(R.id.textCateg);
        animBalanc = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.balancar);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Layout
        textPrecoCus = (TextInputLayout) findViewById(R.id.textPrecoCus);
        textPrecoVen = (TextInputLayout) findViewById(R.id.textPrecoVen);
        textNomeProd = (TextInputLayout) findViewById(R.id.textNomeProd);
        textQuant = (TextInputLayout) findViewById(R.id.textQuant);

        if (altproduto != null) {
            BigDecimal precoC, precoV;
            float pC = altproduto.getPrecoCusto();
            float pV = altproduto.getPrecoVenda();
            precoC = Publico.Casas(pC);
            precoV = Publico.Casas(pV);
            edtNomeProd.setText(altproduto.getNomeProduto());
            edtPrecoCusto.setText(String.valueOf(precoC));
            edtPrecoVenda.setText(String.valueOf(precoV));
            edtQuantidade.setText(String.valueOf(altproduto.getQuantidade()));
        }
        pegaVCategoria();
        btnSalvarProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submForm();
                if (checaNomeProd() && checaNumC() && checaNumV() && checaQuant()) {
                    Produto p = new Produto();
                    Categoria c = new Categoria();
                    p.setId_produto(altproduto.getId_produto());
                    p.setNomeProduto(edtNomeProd.getText().toString().toUpperCase());
                    p.setDataCadastro(altproduto.getDataCadastro());
                    p.setPrecoCusto(Float.valueOf(edtPrecoCusto.getText().toString()));
                    p.setPrecoVenda(Float.valueOf(edtPrecoVenda.getText().toString()));
                    p.setQuantidade(Integer.valueOf(edtQuantidade.getText().toString()));
                    c.setId_categoria(getId_Catego());
                    FormularioLogin.databaseReference.child("Produto").child("Produtos")
                            .child(p.getId_produto()).setValue(p);
                    FormularioLogin.databaseReference.child("Produto").child("Produtos")
                            .child(p.getId_produto()).child("Categoria").setValue(c.getId_categoria());
                    FormularioLogin.databaseReference.child("Produto").child("Produtos")
                            .child(p.getId_produto()).child("Usuario").setValue(FormularioLogin.usuario.getUid());
                    Publico.Alerta(AlterarProduto.this, "Alterado com Sucesso");
                    Publico.Intente(AlterarProduto.this, ListaProdutos.class);
                    limpaCampos();
                    finish();
                }
            }
        });

        btnExcluirProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogo = new AlertDialog.Builder(AlterarProduto.this);
                dialogo.setCancelable(false);
                dialogo.setTitle("AgroInfo");
                dialogo.setMessage("Você deseja realmente excluir o Produto? Além do produto, as vendas desse produto também serão excluídas!");
                dialogo.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deletarProduto();
                        dialog.cancel();
                    }
                });
                dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialogos = dialogo.create();
                dialogos.show();
            }
        });
    }

    private void deletarProduto() {
        vendas = FormularioLogin.databaseReference.child("Vendas").child(FormularioLogin.usuario.getUid())
                .orderByChild("Id_produto").equalTo(altproduto.getId_produto())
                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                        objSnapshot.getRef().setValue(null);
                    }
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        FormularioLogin.databaseReference.child("Produto").child("Produtos")
                .child(altproduto.getId_produto()).removeValue();
        Publico.Alerta(AlterarProduto.this, "Excluído com Sucesso");
        limpaCampos();
        Publico.Intente(AlterarProduto.this, ListaProdutos.class);
        finish();
        FormularioLogin.databaseReference.removeEventListener(vendas);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Publico.Intente(AlterarProduto.this, ListaProdutos.class);
        finish();
    }

    private void limpaCampos() {
        edtNomeProd.setText("");
        edtPrecoCusto.setText("");
        edtPrecoVenda.setText("");
        edtQuantidade.setText("");
    }

    private void pegaVCategoria() {
        FormularioLogin.databaseReference.child("Produto").child("Produtos")
                .child(altproduto.getId_produto()).child("Categoria")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        id_categ = dataSnapshot.getValue().toString();
                        setId_Catego(id_categ);
                        categorias();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void categorias() {
        FormularioLogin.databaseReference.child("Categoria").child(FormularioLogin.usuario.getUid()).child(getId_Catego()).child("nova_categoria")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        nome_categ = dataSnapshot.getValue().toString();
                        setNome_Catego(nome_categ);
                        colocaNomeCateg();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
    private void colocaNomeCateg(){
        String textoCateg = ("Categoria: " + getNome_Catego());
        textCateg.setText(textoCateg);
    }

    //Void para ver todos
    private void submForm() {
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
        textNomeProd.setErrorEnabled(false);
        textPrecoCus.setErrorEnabled(false);
        textPrecoVen.setErrorEnabled(false);
        textQuant.setErrorEnabled(false);
    }

    // FLOAT PRECOCUSTO
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
            textQuant.setError("Indique a edtQuantidade, sendo maior que 0");
            edtQuantidade.setError("Necessita de Entrada Válida");
            return false;
        }
        textQuant.setErrorEnabled(false);
        return true;
    }
    private boolean checaNumC(){
        String num = edtPrecoCusto.getText().toString().trim();
        if(num.isEmpty() || !validNum(num)){
            textPrecoCus.setErrorEnabled(true);
            textPrecoCus.setError("Máximo 6 Números, sendo 2 Decimais");
            edtPrecoCusto.setError("Necessita de Entrada Válida");
            return false;
        }
        textPrecoCus.setErrorEnabled(false);
        return true;
    }
    //FLOAT PRECOVENDA
    private boolean checaNumV(){
        String num = edtPrecoVenda.getText().toString().trim();
        if(num.isEmpty() || !validNum(num)){
            textPrecoVen.setErrorEnabled(true);
            textPrecoVen.setError("Máximo 6 Números, sendo 2 Decimais");
            edtPrecoVenda.setError("Necessita de Entrada Válida");
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

    public void setId_Catego(String id_Catego) { this.id_Catego = id_Catego; }

    public String getId_Catego() { return id_Catego; }

    public void setNome_Catego(String nome_Catego) { this.nome_Catego = nome_Catego; }

    public String getNome_Catego() { return nome_Catego; }
}