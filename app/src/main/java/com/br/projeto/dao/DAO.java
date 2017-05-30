package com.br.projeto.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.br.projeto.modelo.Usuario;

/**
 * Created by João Tailor on 27/05/2017.
 */

public class DAO extends SQLiteOpenHelper{

    private static final String NOME_BANCO ="projeto.db";
    private static final int VERSION = 1;
    private static final String TABELA1 = "usuario";

    private static final String ID_USUARIO = "id_usuario";
    private static final String NOME_USUARIO = "nome_usuario";
    private static final String EMAIL = "email";
    private static final String SENHA = "senha";

    private static final String TABELA2 = "anotacoes";

    private static final String ID_ANOTACAO = "id_anotacao";
    private static final String ANOTACAO = "anotacao";
    private static final String ID_USER = "id_usuario";

    private static final String TABELA3 = "categoria";

    private static final String ID_CATEGORIA = "id_categoria";
    private static final String NOME_CATEGORIA = "nome_categoria";
    private static final String ID_USUA = "id_usuario";

    private static final String TABELA4 = "produto";

    private static final String ID_PRODUTO = "id_produto";
    private static final String NOME_PRODUTO = "anotacao";
    private static final String PRECO_CUSTO = "preco_custo";
    private static final String PRECO_VENDA = "preco_venda";
    private static final String QUANTIDADE = "quantidade";
    private static final String DATA_CADASTRO = "data_cadastro";
    private static final String ID_USU = "id_usuario";
    private static final String ID_CAT = "id_categoria";

    private static final String TABELA5 = "rendimento";

    private static final String ID_RENDIMENTO = "id_rendimento";
    private static final String RENDIMENTO = "rendimento";
    private static final String DATA = "data";
    private static final String ID_PROD = "id_produto";
    private static final String ID_USUAR = "id_usuario";
    private static final String ID_CATEG = "id_categoria";

    public DAO(Context context) {

        super(context, NOME_BANCO, null, VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE "+TABELA1+" ( " +
                " "+ID_USUARIO+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " "+NOME_USUARIO+" VARCHAR(50) NOT NULL, " +
                " "+EMAIL+" VARCHAR(100) NOT NULL, " +
                " "+SENHA+" VARCHAR(50) NOT NULL ); " +

                "CREATE TABLE "+TABELA2+" ( " +
                " "+ID_ANOTACAO+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " "+ANOTACAO+" TEXT NOT NULL, " +
                " "+ID_USER+" INTEGER, " +
                "CONSTRAINT fk_tbAnotacoes_tbUsuario FOREIGN KEY ( "+
                " "+ID_USER+") REFERENCES " +TABELA1+" ("+ID_USUARIO+")" +
                "  ON DELETE CASCADE ON UPDATE CASCADE ); " +

                "CREATE TABLE "+TABELA3+" ( " +
                " "+ID_CATEGORIA+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " "+NOME_CATEGORIA+" VARCHAR(45) NOT NULL, " +
                " "+ID_USUA+" INTEGER," +
                "CONSTRAINT fk_tbCategoria_tbUsuario FOREIGN KEY ( "+
                " "+ID_USUA+") REFERENCES " +TABELA1+" ("+ID_USUARIO+")" +
                "  ON DELETE CASCADE ON UPDATE CASCADE ); " +


                "CREATE TABLE "+TABELA4+" ( " +
                " "+ID_PRODUTO+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " "+NOME_PRODUTO+" VARCHAR(45) NOT NULL, " +
                " "+PRECO_CUSTO+" DECIMAL(3,2) NOT NULL, " +
                " "+PRECO_VENDA+" DECIMAL(3,2) NOT NULL, " +
                " "+QUANTIDADE+" INTEGER NOT NULL, " +
                " "+DATA_CADASTRO+" DATE NOT NULL, " +
                " "+ID_USU+" INTEGER, " +
                " "+ID_CAT+" INTEGER, " +
                "CONSTRAINT fk_tbProduto_tbUsuario FOREIGN KEY ( "+
                " "+ID_USU+") REFERENCES " +TABELA1+" ("+ID_USUARIO+")" +
                "  ON DELETE CASCADE ON UPDATE CASCADE," +
                "CONSTRAINT fk_tbProduto_tbUsuario FOREIGN KEY ( "+
                " "+ID_CAT+") REFERENCES " +TABELA3+" ("+ID_CATEGORIA+")" +
                "  ON DELETE CASCADE ON UPDATE CASCADE ); " +

                "CREATE TABLE "+TABELA5+" ( " +
                " "+ID_RENDIMENTO+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " "+RENDIMENTO+" DECIMAL(6,2) NOT NULL, " +
                " "+DATA+" DATE NOT NULL, " +
                " "+ID_PROD+" INTEGER, " +
                " "+ID_USUAR+" INTEGER, " +
                " "+ID_CATEG+" INTEGER, " +
                "CONSTRAINT fk_tbRendimento_tbProduto FOREIGN KEY ( "+
                " "+ID_PROD+") REFERENCES " +TABELA4+" ("+ID_PRODUTO+")" +
                "  ON DELETE CASCADE ON UPDATE CASCADE," +
                "CONSTRAINT fk_tbRendimento_tbUsuario FOREIGN KEY ( "+
                " "+ID_USUAR+") REFERENCES " +TABELA1+" ("+ID_USUARIO+")" +
                "  ON DELETE CASCADE ON UPDATE CASCADE," +
                "CONSTRAINT fk_tbRendimento_tbCategoria FOREIGN KEY ( "+
                " "+ID_CATEG+") REFERENCES " +TABELA3+" ("+ID_CATEGORIA+")" +
                "  ON DELETE CASCADE ON UPDATE CASCADE ); ";

        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql1 = "DROP TABLE IS EXISTS"+TABELA1;
        String sql2 = "DROP TABLE IS EXISTS"+TABELA2;
        String sql3 = "DROP TABLE IS EXISTS"+TABELA3;
        String sql4 = "DROP TABLE IS EXISTS"+TABELA4;
        String sql5 = "DROP TABLE IS EXISTS"+TABELA5;

        db.execSQL(sql1);
        db.execSQL(sql2);
        db.execSQL(sql3);
        db.execSQL(sql4);
        db.execSQL(sql5);

        onCreate(db);

    }

    public long salvarUsuario(Usuario u){

        ContentValues values = new ContentValues();
        long retornoDB;

        values.put(NOME_USUARIO,u.getNome());
        values.put(EMAIL,u.getEmail());
        values.put(SENHA,u.getSenha());

        retornoDB = getWritableDatabase().insert(TABELA1, null, values);

        return retornoDB;
    }
}
