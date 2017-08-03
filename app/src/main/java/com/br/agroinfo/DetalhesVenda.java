package com.br.agroinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.br.agroinfo.modelo.Vendas;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.math.BigDecimal;

public class DetalhesVenda extends AppCompatActivity {

    TextView textProd, textPreco, textQuantV, textMes, textAno;
    Vendas altvenda;
    BigDecimal precoV;
    Button btnExcluirVenda;
    String id_prod, id_Produ, nome_prod, nome_Produ;
    String ano, mes, quantV, prod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_venda);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView titulo = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titulo.setText("DETALHES DA VENDA");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent abrirEdicao = getIntent();
        altvenda = (Vendas) abrirEdicao.getSerializableExtra("Venda-enviada");

        //resgatar os componentes
        btnExcluirVenda = (Button) findViewById(R.id.btnExcluirVenda);
        textProd = (TextView) findViewById(R.id.textProd);
        textPreco = (TextView) findViewById(R.id.textPreco);
        textQuantV = (TextView) findViewById(R.id.textQuantV);
        textMes = (TextView) findViewById(R.id.textMes);
        textAno = (TextView) findViewById(R.id.Ano);

        if (altvenda != null) {
            ano = ("Ano: " + String.valueOf(altvenda.getAno()));
            mes = ("Mes: " + String.valueOf(altvenda.getMes()));
            quantV = ("Quantidade da venda: " + altvenda.getQuant_venda());
            textAno.setText(ano);
            textMes.setText(mes);
            precoV = Publico.Casas(altvenda.getPreco_venda());
            textPreco.setText("Preço Venda: " + String.valueOf(precoV) + "R$");
            textQuantV.setText(quantV);
        }
        pegaVProduto();
        btnExcluirVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Inicial.databaseReference.child("Vendas").child(Inicial.usuario.getUid())
                        .child(altvenda.getId_venda()).removeValue();
                Publico.Alerta(DetalhesVenda.this, "Excluído com Sucesso");
                Publico.Intente(DetalhesVenda.this, ListaVendas.class);
                finish();
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Publico.Intente(DetalhesVenda.this, ListaVendas.class);
        finish();
    }

    private void pegaVProduto() {
        Inicial.databaseReference.child("Vendas").child(Inicial.usuario.getUid()).child(altvenda.getId_venda()).child("Id_produto")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        id_prod = dataSnapshot.getValue().toString();
                        setId_Produ(id_prod);
                        categorias();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void categorias() {
        Inicial.databaseReference.child("Produto").child("Produtos").child(getId_Produ()).child("nomeProduto")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        nome_prod = dataSnapshot.getValue().toString();
                        setNome_Produ(nome_prod);
                        colocaNomeProdu();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
    private void colocaNomeProdu(){
        prod = ("Produto: " + getNome_Produ());
        textProd.setText(prod);
    }

    public void setId_Produ(String id_Produ) { this.id_Produ = id_Produ; }

    public String getId_Produ() { return id_Produ; }

    public void setNome_Produ(String nome_Produ) { this.nome_Produ = nome_Produ; }

    public String getNome_Produ() { return nome_Produ; }
}
