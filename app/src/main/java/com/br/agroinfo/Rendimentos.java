package com.br.agroinfo;

import android.graphics.Color;
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

import com.br.agroinfo.modelo.Vendas;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.util.ArrayList;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

public class Rendimentos extends AppCompatActivity {

    TextInputLayout textAno;
    EditText edtAno;
    Spinner spnMes;
    Button btnChecRend;
    PieChart gfcRend;
    Animation animBalanc;
    int posicaoMes;
    float totalCusto, totalVenda;
    private ArrayAdapter<String> arrayAdapterMes;

    String[] parteS =  {"Custo", "Lucro"};
    String[] datas = new String[]
            {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro",
                    "Novembro", "Dezembro"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rendimentos);
        spnMes = (Spinner) findViewById(R.id.spnMes);
        edtAno = (EditText) findViewById(R.id.edtAno);
        btnChecRend = (Button) findViewById(R.id.btnChecRend);
        gfcRend = (PieChart) findViewById(R.id.gfcRend);
        textAno = (TextInputLayout) findViewById(R.id.textAno);
        animBalanc = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.balancar);
        //Cria Spinner Mês e fazer o SelectedListener e máscara
        //Máscara
        MaskEditTextChangedListener maskAno = new MaskEditTextChangedListener("20##", edtAno);
        edtAno.addTextChangedListener(maskAno);

        spinnerMes();
        spnMes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // Pegar ID e o Valor do Produto, forçar para que o Spinner pegue
                posicaoMes = spnMes.getSelectedItemPosition();
                Publico.Alerta(Rendimentos.this, String.valueOf(posicaoMes));
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // Faz Nada
            }
        });
        //Gráfico, listener e sem valor
        gfcRend.setNoDataText("Coloque o mês e ano e clique no botão acima");
        gfcRend.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null) return;
                BigDecimal casa = Publico.Casas(e.getY());
                Publico.Alerta(Rendimentos.this, String.valueOf(casa) + "R$");
            }
            @Override
            public void onNothingSelected() {
            }
        });

        btnChecRend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submForm();
                if (checaAno()){
                    String mes = String.valueOf(posicaoMes + 1);
                    String ano = edtAno.getText().toString();
                    FormularioLogin.databaseReference.child("Vendas").child(FormularioLogin.usuario.getUid()).orderByChild("mes_ano").equalTo(mes + "_" + ano)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot objSnapshot:dataSnapshot.getChildren()) {
                                        Vendas ve = objSnapshot.getValue(Vendas.class);
                                        totalCusto += ve.getPreco_custo();
                                        totalVenda += ve.getPreco_venda();
                                    }
                                    if ((totalCusto != 0) && (totalVenda != 0)){
                                        metodoPieChart();
                                    } else {
                                        Publico.Alerta(Rendimentos.this, "Não possui valores para criar o Gráfico!");
                                    }

                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                }
            }
        });
    }


    private void submForm() {
        if (!checaAno()) {
            edtAno.setAnimation(animBalanc);
            edtAno.startAnimation(animBalanc);
            return;
        }
        textAno.setErrorEnabled(false);
    }
    private void metodoPieChart() {
        float[] yData = {totalCusto, totalVenda};
        Description descricao = new Description();
        descricao.setText("Porcentagem do Rendimento no Mês");
        gfcRend.setDescription(descricao);
        gfcRend.setUsePercentValues(true);
        gfcRend.setRotationEnabled(true);
        gfcRend.setHoleRadius(25f);
        gfcRend.setTransparentCircleAlpha(10);
        gfcRend.setTransparentCircleRadius(12);

        ArrayList<PieEntry> entradas = new ArrayList<>();

        for (int i = 0; i < yData.length; i++){
            entradas.add(new PieEntry(yData[i], parteS[i]));
        }

        //Criar o DataSet
        PieDataSet pieDataSet = new PieDataSet(entradas, "");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        //Adicionar Cores
        ArrayList<Integer> cores = new ArrayList<>();
        cores.add(Color.GREEN);
        cores.add(Color.BLUE);

        pieDataSet.setColors(cores);

        //Adicionar Legenda
        Legend legenda = gfcRend.getLegend();
        legenda.setForm(Legend.LegendForm.SQUARE);
        legenda.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legenda.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legenda.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legenda.setDrawInside(false);

        //Criar objeto
        PieData pieData = new PieData(pieDataSet);
        gfcRend.setData(pieData);
        gfcRend.invalidate();
        totalVenda = 0;
        totalCusto = 0;
    }

    private void spinnerMes() {
        arrayAdapterMes = new ArrayAdapter<>(Rendimentos.this,android.R.layout.simple_spinner_item, datas);
        arrayAdapterMes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMes.setAdapter(arrayAdapterMes);
    }

    private boolean checaAno(){
        String ano = edtAno.getText().toString().trim();
        int quant;
        quant = 0;
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Publico.Intente(Rendimentos.this, MenuP.class);
        finish();
    }
}
