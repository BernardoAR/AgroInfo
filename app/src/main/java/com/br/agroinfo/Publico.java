package com.br.agroinfo;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

import static com.br.agroinfo.R.id.toolbar;

/**
 * Created by Bernardo on 14/07/2017.
 */

public class Publico {
    public static BigDecimal Casas(float d) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    public static void Alerta(Context contexto, String s){
        Toast.makeText(contexto, s, Toast.LENGTH_SHORT).show();
    }
    public static void Intente(Context contexto, Class classe){
        Intent i = new Intent(contexto, classe);
        contexto.startActivity(i);
    }
}
