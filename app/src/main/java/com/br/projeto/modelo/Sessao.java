package com.br.projeto.modelo;

import android.content.Context;
import android.content.SharedPreferences;

import com.br.projeto.FormularioLogin;

import static com.br.projeto.FormularioLogin.id;

/**
 * Created by Bernardo on 04/06/2017.
 */

public class Sessao {
    // FAZER A CONTINUAÇÃO DE SESSÃO
    public SharedPreferences prefs;
    public SharedPreferences.Editor editor;
    public Context ctx;


    public Sessao(Context ctx){
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences("manter", Context.MODE_PRIVATE);
        editor = prefs.edit();

    }

    //Ver se está logado ou não
    public void dizID(int idz){
        editor.putInt("id", idz);
        editor.commit();
    }

    public int idzado(){
        return prefs.getInt("id", 0);
    }

    //Ver em qual dos modos está
    public void dizEsc(boolean escolha){
        editor.putBoolean("modoInt", escolha);
        editor.commit();
    }

    public boolean escolhido(){
        return prefs.getBoolean("modoInt", false);
    }

    //Ver se está logado ou não
    public void dizLogado(boolean loggado){
        editor.putBoolean("modoLogado", loggado);
        editor.commit();
    }

    //Ver logado
    public boolean logado(){
        return prefs.getBoolean("modoLogado", false);
    }

}
