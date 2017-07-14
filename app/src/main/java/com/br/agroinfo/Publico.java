package com.br.agroinfo;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Bernardo on 14/07/2017.
 */

public class Publico {

    public static void Alerta(Context contexto, String s){
        Toast.makeText(contexto, s, Toast.LENGTH_SHORT).show();
    }
    public static void Intente(Context contexto, Class classe){
        Intent i = new Intent(contexto, classe);
        contexto.startActivity(i);
    }
}
