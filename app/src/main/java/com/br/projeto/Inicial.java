package com.br.projeto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;




public class Inicial extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);

        // pega o botao
        Button botao = (Button) findViewById(R.id.btnLogin);

        // configurar a acao de click
        botao.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent abrirLogin = new Intent(Inicial.this,FormularioLogin.class);
                // solicitar para abir
                startActivity(abrirLogin);

            }
        });

        // pega o botao
        Button botaoCadastro = (Button) findViewById(R.id.btnCadastro);

        // configurar a acao de click
        botaoCadastro.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent abrirCadastro = new Intent(Inicial.this,FormularioCadastro.class);
                // solicitar para abir
                startActivity(abrirCadastro);

            }
        });

        // pega o botao
        Button botaoTeste = (Button) findViewById(R.id.btnTeste);

        // configurar a acao de click
        botaoTeste.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent abrirTeste = new Intent(Inicial.this,MenuP.class);
                // solicitar para abir
                startActivity(abrirTeste);

            }
        });




        // abrir a conexao


    }




}
