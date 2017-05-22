package com.br.projeto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.*;

public class FormProd extends AppCompatActivity implements View.OnClickListener{
    // Declarar
    private EditText edtProd;
    private EditText edtPreco;
    public Spinner spnCateg;
    private Button btnAdProd;
    private Button btnExProd;
    public ListView lstProd;

    //Array Adapter para as Listas
    public ArrayAdapter<String> adpCateg;
    public ArrayAdapter<String> adpProd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_prod);
        // Pega as partes do Layout
        edtProd = (EditText)findViewById(R.id.edtProd);
        edtPreco = (EditText)findViewById(R.id.edtPreco);
        spnCateg = (Spinner)findViewById(R.id.spnCateg);
        btnAdProd = (Button)findViewById(R.id.btnAdProd);
        btnExProd = (Button)findViewById(R.id.btnExProd);
        lstProd = (ListView)findViewById(R.id.lstProd);

        //Chamar o setOnClickListener
        btnAdProd.setOnClickListener(this);
        btnExProd.setOnClickListener(this);

        /* Categoria
        Button botaoCateg = (Button) findViewById(R.id.btnAdCat);

        // Configura a Ação de clique
        botaoCateg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent abrirCateg = new Intent(FormProd.this,Categ.class);
                // solicitar para abir
                startActivity(abrirCateg);

            }
        });
        */

        //Chamar o Array Adapter e fazer escorregar, mostrando as listas, e vincular ao Spinner

        adpCateg = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adpCateg.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCateg.setAdapter(adpCateg);

        adpCateg.add("Opção 1");
        //
        adpProd = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        lstProd.setAdapter(adpProd);
    }



    @Override
    public void onClick(View v) {
        //Ver qual objeto está chamando o Evento
        if (v == btnAdProd){
            // Pegar os textos colocados
            String item = edtProd.getText().toString();
            String item2 = edtPreco.getText().toString();

            // Receber a Categoria Escolhida
            item += " - "+ spnCateg.getSelectedItem() + " - "  + item2;

            //Adicionar
            adpProd.add(item);

        } else
            if (v == btnExProd){
                //Código de Exclusão, pegar um Item, se for, claro, maior que 0
                if (adpProd.getCount() > 0)
                {
                    // Pegar a posição do Item
                     String item = adpProd.getItem(0);
                    //Remover
                     adpProd.remove(item);
                }
            }

    }
}
