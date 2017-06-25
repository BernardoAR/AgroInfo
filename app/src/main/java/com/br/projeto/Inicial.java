package com.br.projeto;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.br.projeto.modelo.Sessao;


public class Inicial extends Activity {
    //Sessão
    private Sessao sessao;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);

        //Sessão, e se estiver logado abrir, dependendo de qual escolha abrir activities diferentes
        sessao = new Sessao(this);
        if(sessao.logado() && sessao.escolhido()){
            startActivity(new Intent(Inicial.this, MenuP.class));
            finish();
        } else if (sessao.logado() && !sessao.escolhido()){
            startActivity(new Intent(Inicial.this, MenuP.class));
            finish();
            Toast temp = Toast.makeText(Inicial.this, "Cliente", Toast.LENGTH_SHORT);
            temp.show();
        }
        //


        // pega o botao
        Button botao = (Button) findViewById(R.id.btnLogin);

        // configurar a acao de click
        botao.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent abrirLogin = new Intent(Inicial.this, FormularioLogin.class);
                // solicitar para abir
                startActivity(abrirLogin);

            }
        });

        // pega o botao
        Button botaoCadastro = (Button) findViewById(R.id.btnCadastro);

        // configurar a acao de click
        botaoCadastro.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent abrirCadastro = new Intent(Inicial.this, FormularioCadastro.class);
                // solicitar para abir
                startActivity(abrirCadastro);

            }
        });

        // pega o botao
        Button botaoTeste = (Button) findViewById(R.id.btnTeste);

        // configurar a acao de click
        botaoTeste.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent abrirTeste = new Intent(Inicial.this, MenuP.class);
                // solicitar para abir
                startActivity(abrirTeste);

            }
        });

    }

}
