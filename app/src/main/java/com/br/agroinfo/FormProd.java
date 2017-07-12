package com.br.agroinfo;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.br.agroinfo.dao.Conexao;
import com.br.agroinfo.modelo.Categoria;
import com.br.agroinfo.modelo.Produto;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

public class FormProd extends AppCompatActivity {
    // Declarar
    Button btnSelDataC;
    private EditText edtNomeProd;
    Spinner spnCateg;
    private EditText editPrecoCusto;
    private EditText editPrecoVenda;
    private EditText quantidade;
    private EditText edtDataCadastro;
    private Button btnAdProd;
    public Button btnListProd;
    private Button btnAdCat;
    private TextInputLayout textPrecoC, textPrecoV;
    private Vibrator vib;
    Animation animBalanc;
    String valorId;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser usuario;
    private List<Categoria> listCategoria = new ArrayList<>() ;
    private ArrayAdapter<Categoria> arrayAdapterCategoria;


    // Deixar como Padrão o decimal com 2 casas


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_prod);
        inicFirebase();
        populaLista();
        // Pega as partes do Layout
        edtNomeProd = (EditText) findViewById(R.id.edtNomeProd);
        spnCateg = (Spinner) findViewById(R.id.spnCateg);
        editPrecoCusto = (EditText) findViewById(R.id.editPrecoCusto);
        editPrecoVenda = (EditText) findViewById(R.id.editPrecoVenda);
        quantidade = (EditText) findViewById(R.id.quantidade);
        edtDataCadastro = (EditText) findViewById(R.id.DataCadastro);
        btnListProd = (Button) findViewById(R.id.btnListProd);
        btnSelDataC = (Button) findViewById(R.id.btnSelDataC);
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
                if (checaNumC() && checaNumV()){
                    // Método para Conversão de Float
                    Float precoC = Float.valueOf(editPrecoCusto.getText().toString());
                    Float precoV = Float.valueOf(editPrecoVenda.getText().toString());
                    int quant = Integer.valueOf(quantidade.getText().toString());
                    Produto p = new Produto();
                    p.setId_produto(UUID.randomUUID().toString());
                    p.setDataCadastro(edtDataCadastro.getText().toString().replace('/', '-'));
                    p.setNomeProduto(edtNomeProd.getText().toString().toUpperCase());
                    p.setPrecoCusto(precoC);
                    p.setPrecoVenda(precoV);
                    p.setQuantidade(quant);
                    Categoria c = new Categoria();
                    c.setId_categoria(valorId);
                    databaseReference.child("Produto").child("Produtos").child(p.getId_produto()).setValue(p);
                    databaseReference.child("Produto").child("Produtos")
                            .child(p.getId_produto()).child("Categoria").setValue(c.getId_categoria());
                    databaseReference.child("Produto").child("Produtos")
                            .child(p.getId_produto()).child("Usuario").setValue(usuario.getUid());
                    alerta("Cadastrado com Sucesso!");
                    limpaCampos();
                    finish();
                }
            }
        });
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(FormProd.this, MenuP.class);
        startActivity(i);
    }
    private void limpaCampos() {
        edtNomeProd.setText("");
        editPrecoCusto.setText("");
        editPrecoVenda.setText("");
        quantidade.setText("");
        edtDataCadastro.setText("");
    }

    private void alerta(String mensagem) {
        Toast.makeText(FormProd.this, mensagem, Toast.LENGTH_SHORT).show();
    }
    private void inicFirebase() {
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

    // Pegar os Valores
    private void populaLista() {
        DatabaseReference db  = databaseReference.child("Categoria").child(usuario.getUid());
        db.addValueEventListener(new ValueEventListener() {
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