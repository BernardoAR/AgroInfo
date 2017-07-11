package com.br.agroinfo;

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

import com.br.agroinfo.dao.Conexao;
import com.br.agroinfo.modelo.Categoria;
import com.br.agroinfo.modelo.Produto;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AlterarProduto extends AppCompatActivity {
    // Declarar

    public EditText edtNomeProd;
    TextView textCateg;
    Produto altproduto;
    Categoria categoria;
    public EditText editPrecoCusto;
    public EditText editPrecoVenda;
    public EditText quantidade;
    public Button btnSalvarProd;
    public Button btnExcluirProd;
    FirebaseUser usuario;

    private TextInputLayout textPrecoCus, textPrecoVen;
    private Vibrator vib;
    Animation animBalanc;


    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    public static String id_categ, id_Catego, nome_categ, nome_Catego;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_produto);
        inicializarFirebase();
        Intent abrirEdicao = getIntent();
        altproduto = (Produto) abrirEdicao.getSerializableExtra("Produto-enviado");
        categoria = new Categoria();

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
            BigDecimal precoC, precoV;
            float pC = altproduto.getPrecoCusto();
            float pV = altproduto.getPrecoVenda();
            precoC = casas(pC, 2);
            precoV = casas(pV, 2);
            edtNomeProd.setText(altproduto.getNomeProduto());
            editPrecoCusto.setText(String.valueOf(precoC));
            editPrecoVenda.setText(String.valueOf(precoV));
            quantidade.setText(String.valueOf(altproduto.getQuantidade()));
        }
        pegaVCategoria();
        btnSalvarProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submForm();
                if (checaNumC() && checaNumV()) {
                    Produto p = new Produto();
                    Categoria c = new Categoria();
                    p.setId_produto(altproduto.getId_produto());
                    p.setNomeProduto(edtNomeProd.getText().toString().toUpperCase());
                    p.setDataCadastro(altproduto.getDataCadastro());
                    p.setPrecoCusto(Float.valueOf(editPrecoCusto.getText().toString()));
                    p.setPrecoVenda(Float.valueOf(editPrecoVenda.getText().toString()));
                    p.setQuantidade(Integer.valueOf(quantidade.getText().toString()));
                    c.setId_categoria(getId_Catego());
                    databaseReference.child("Produto").child("Produtos")
                            .child(p.getId_produto()).setValue(p);
                    databaseReference.child("Produto").child("Produtos")
                            .child(p.getId_produto()).child("Categoria").setValue(c.getId_categoria());
                    databaseReference.child("Produto").child("Produtos")
                            .child(p.getId_produto()).child("Usuario").setValue(usuario.getUid());
                    alerta("Alterado com Sucesso");
                    limpaCampos();
                    finish();
                }
            }
        });

        btnExcluirProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("Produto").child("Produtos")
                        .child(altproduto.getId_produto()).removeValue();
                alerta("Excluído com Sucesso");
                limpaCampos();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AlterarProduto.this, Lista_produtos.class);
        startActivity(i);
    }

    private void limpaCampos() {
        edtNomeProd.setText("");
        editPrecoCusto.setText("");
        editPrecoVenda.setText("");
        quantidade.setText("");
    }

    private void alerta(String mensagem) {
        Toast.makeText(AlterarProduto.this, mensagem, Toast.LENGTH_SHORT).show();
    }

    private void pegaVCategoria() {
        databaseReference.child("Produto").child("Produtos")
                .child(altproduto.getId_produto()).child("Categoria")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                id_categ = dataSnapshot.getValue().toString();
                setId_Catego(id_categ);
                Toast.makeText(AlterarProduto.this,"PegaVCategoria:" + id_categ, Toast.LENGTH_SHORT).show();
                categorias();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void categorias() {
        Toast.makeText(AlterarProduto.this,"PegaCCategoria:" + getId_Catego(), Toast.LENGTH_SHORT).show();
        databaseReference.child("Categoria").child(usuario.getUid()).child(getId_Catego()).child("nova_categoria")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nome_categ = dataSnapshot.getValue().toString();
                setNome_Catego(nome_categ);
                colocaNomeCateg();
                Toast.makeText(AlterarProduto.this,nome_categ, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void colocaNomeCateg(){
        textCateg.setText("Categoria: " + getNome_Catego());
    }
    private void inicializarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        usuario = Conexao.getFirebaseUser();
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

    public void setId_Catego(String id_Catego) { this.id_Catego = id_Catego; }

    public String getId_Catego() { return id_Catego; }

    public void setNome_Catego(String nome_Catego) { this.nome_Catego = nome_Catego; }

    public String getNome_Catego() { return nome_Catego; }
}
