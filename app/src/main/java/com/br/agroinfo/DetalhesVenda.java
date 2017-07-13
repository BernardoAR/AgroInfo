package com.br.agroinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.br.agroinfo.modelo.Produto;
import com.br.agroinfo.modelo.Vendas;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.math.BigDecimal;

public class DetalhesVenda extends AppCompatActivity {

    TextView textProd, textPreco, textQuantV, textMes, textAno;
    Vendas altvenda;
    Produto produto;
    BigDecimal precoV;
    Button btnExcluirVenda;
    String id_prod, id_Produ, nome_prod, nome_Produ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_venda);
        Intent abrirEdicao = getIntent();
        altvenda = (Vendas) abrirEdicao.getSerializableExtra("Venda-enviada");
        produto = new Produto();

        //resgatar os componentes
        btnExcluirVenda = (Button) findViewById(R.id.btnExcluirVenda);
        textProd = (TextView) findViewById(R.id.textProd);
        textPreco = (TextView) findViewById(R.id.textPreco);
        textQuantV = (TextView) findViewById(R.id.textQuantV);
        textMes = (TextView) findViewById(R.id.textMes);
        textAno = (TextView) findViewById(R.id.Ano);

        if (altvenda != null) {
            textAno.setText("Ano: " + String.valueOf(altvenda.getAno()));
            textMes.setText("Mes: " + String.valueOf(altvenda.getMes()));
            precoV = FormProd.casas(altvenda.getPreco_venda(), 2);
            textPreco.setText("Preço Venda: " + String.valueOf(precoV) + "R$");
            textQuantV.setText("Quantidade da venda: " + altvenda.getQuant_venda());
        }
        pegaVProduto();
        btnExcluirVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FormularioLogin.databaseReference.child("Vendas").child(FormularioLogin.usuario.getUid())
                        .child(altvenda.getId_venda()).removeValue();
                alerta("Excluído com Sucesso");
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(DetalhesVenda.this, ListaVendas.class);
        startActivity(i);
    }

    private void alerta(String mensagem) {
        Toast.makeText(DetalhesVenda.this, mensagem, Toast.LENGTH_SHORT).show();
    }

    private void pegaVProduto() {
        FormularioLogin.databaseReference.child("Vendas").child(FormularioLogin.usuario.getUid()).child(altvenda.getId_venda()).child("Id_produto")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        id_prod = dataSnapshot.getValue().toString();
                        setId_Produ(id_prod);
                        Toast.makeText(DetalhesVenda.this,"PegaVProduto:" + id_prod, Toast.LENGTH_SHORT).show();
                        categorias();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void categorias() {
        Toast.makeText(DetalhesVenda.this,"PegaPProduro:" + getId_Produ(), Toast.LENGTH_SHORT).show();
        FormularioLogin.databaseReference.child("Produto").child("Produtos").child(getId_Produ()).child("nomeProduto")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        nome_prod = dataSnapshot.getValue().toString();
                        setNome_Produ(nome_prod);
                        colocaNomeProdu();
                        Toast.makeText(DetalhesVenda.this,nome_prod, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
    private void colocaNomeProdu(){
        textProd.setText("Produto: " + getNome_Produ());
    }

    // Fazer com que fique com duas decimais FORÇADAMENTE
    public static BigDecimal casas(float d, int casasDec) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(casasDec, BigDecimal.ROUND_HALF_UP);
        return bd;
    }


    public void setId_Produ(String id_Produ) { this.id_Produ = id_Produ; }

    public String getId_Produ() { return id_Produ; }

    public void setNome_Produ(String nome_Produ) { this.nome_Produ = nome_Produ; }

    public String getNome_Produ() { return nome_Produ; }
}
