package com.br.projeto.modelo;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;

import android.os.Environment;

public class DAO {

    private static ObjectContainer db;


    public static void conectar(){


        Thread t = new Thread(){

            public void run (){

                String local = Environment.getExternalStorageDirectory()+"/data.projeto";
                db = Db4oEmbedded.openFile(local);
            }

        };

        t.start();

    }

    public static void gravar(Object dados){

        db.store(dados);

        //efitiva a persistencia

        db.commit();

    }

}
