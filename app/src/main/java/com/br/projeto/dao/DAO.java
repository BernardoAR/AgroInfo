package com.br.projeto.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.br.projeto.MenuP;
import com.br.projeto.modelo.Anotacao;
import com.br.projeto.modelo.Categoria;
import com.br.projeto.modelo.Usuario;

import java.util.ArrayList;

import static com.br.projeto.MenuP.ID;


/**
 * Created by João Tailor on 27/05/2017 and modified by Bernardo.
 */

public class DAO extends SQLiteOpenHelper {

    private static final String LOG = "Helper";

    private static final String NOME_BANCO = "projeto.db";
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
    public SQLiteDatabase db;

    // CRIAR AS TABELAS

    private static final String CRIAR_TABELA_LOGIN = "CREATE TABLE " + TABELA1 + " ( " +
            " " + ID_USUARIO + " INTEGER PRIMARY KEY , " +
            " " + NOME_USUARIO + " VARCHAR(50) NOT NULL, " +
            " " + EMAIL + " VARCHAR(100) NOT NULL, " +
            " " + SENHA + " VARCHAR(50) NOT NULL ); ";

    private static final String CRIAR_TABELA_ANOTACOES = "CREATE TABLE " + TABELA2 + " ( " +
            " " + ID_ANOTACAO + " INTEGER PRIMARY KEY , " +
            " " + ANOTACAO + " TEXT NOT NULL, " +
            " " + ID_USER + " INTEGER, " +
            "CONSTRAINT fk_tbAnotacoes_tbUsuario FOREIGN KEY ( " +
            " " + ID_USER + ") REFERENCES " + TABELA1 + " (" + ID_USUARIO + ")" +
            "  ON DELETE CASCADE ON UPDATE CASCADE ); ";

    private static final String CRIAR_TABELA_CATEGORIA = "CREATE TABLE " + TABELA3 + " ( " +
            " " + ID_CATEGORIA + " INTEGER PRIMARY KEY , " +
            " " + NOME_CATEGORIA + " VARCHAR(45) NOT NULL, " +
            " " + ID_USUA + " INTEGER," +
            "CONSTRAINT fk_tbCategoria_tbUsuario FOREIGN KEY ( " +
            " " + ID_USUA + ") REFERENCES " + TABELA1 + " (" + ID_USUARIO + ")" +
            "  ON DELETE CASCADE ON UPDATE CASCADE ); ";

    private static final String CRIAR_TABELA_PRODUTO = "CREATE TABLE " + TABELA4 + " ( " +
            " " + ID_PRODUTO + " INTEGER PRIMARY KEY , " +
            " " + NOME_PRODUTO + " VARCHAR(45) NOT NULL, " +
            " " + PRECO_CUSTO + " DECIMAL(3,2) NOT NULL, " +
            " " + PRECO_VENDA + " DECIMAL(3,2) NOT NULL, " +
            " " + QUANTIDADE + " INTEGER NOT NULL, " +
            " " + DATA_CADASTRO + " DATE NOT NULL, " +
            " " + ID_USU + " INTEGER, " +
            " " + ID_CAT + " INTEGER, " +
            "CONSTRAINT fk_tbProduto_tbUsuario FOREIGN KEY ( " +
            " " + ID_USU + ") REFERENCES " + TABELA1 + " (" + ID_USUARIO + ")" +
            "  ON DELETE CASCADE ON UPDATE CASCADE," +
            "CONSTRAINT fk_tbProduto_tbUsuario FOREIGN KEY ( " +
            " " + ID_CAT + ") REFERENCES " + TABELA3 + " (" + ID_CATEGORIA + ")" +
            "  ON DELETE CASCADE ON UPDATE CASCADE ); ";

    private static final String CRIAR_TABELA_RENDIMENTO = "CREATE TABLE " + TABELA5 + " ( " +
            " " + ID_RENDIMENTO + " INTEGER PRIMARY KEY , " +
            " " + RENDIMENTO + " DECIMAL(6,2) NOT NULL, " +
            " " + DATA + " DATE NOT NULL, " +
            " " + ID_PROD + " INTEGER, " +
            " " + ID_USUAR + " INTEGER, " +
            " " + ID_CATEG + " INTEGER, " +
            "CONSTRAINT fk_tbRendimento_tbProduto FOREIGN KEY ( " +
            " " + ID_PROD + ") REFERENCES " + TABELA4 + " (" + ID_PRODUTO + ")" +
            "  ON DELETE CASCADE ON UPDATE CASCADE," +
            "CONSTRAINT fk_tbRendimento_tbUsuario FOREIGN KEY ( " +
            " " + ID_USUAR + ") REFERENCES " + TABELA1 + " (" + ID_USUARIO + ")" +
            "  ON DELETE CASCADE ON UPDATE CASCADE," +
            "CONSTRAINT fk_tbRendimento_tbCategoria FOREIGN KEY ( " +
            " " + ID_CATEG + ") REFERENCES " + TABELA3 + " (" + ID_CATEGORIA + ")" +
            "  ON DELETE CASCADE ON UPDATE CASCADE ); ";

    public DAO(Context context) {

        super(context, NOME_BANCO, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CRIAR_TABELA_LOGIN);
        db.execSQL(CRIAR_TABELA_ANOTACOES);
        db.execSQL(CRIAR_TABELA_CATEGORIA);
        db.execSQL(CRIAR_TABELA_PRODUTO);
        db.execSQL(CRIAR_TABELA_RENDIMENTO);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABELA1);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA2);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA3);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA4);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA5);

        onCreate(db);

    }

    public void salvarUsuario(Usuario u) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NOME_USUARIO,u.getNome());
        values.put(EMAIL,u.getEmail());
        values.put(SENHA,u.getSenha());

        db.insert(TABELA1, null, values);
        db.close();
    }
    // Pegar e Colocar a Senha
    private static String str;
    private static int id;

    public String getNomeUs() {

        db = this.getReadableDatabase();
        String query = "select nome_usuario from " + TABELA1 + " where id_usuario = " + MenuP.ID;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do{
                str = cursor.getString(0);
                //Do something Here with values

            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return str;
    }
    // Pegar e Colocar ID
    public void setIDUs(int id) { this.id = id; }
    public static int getIDUs(){ return id; }
    // PEGAR O NOME
    // Método de ver se o email já está cadastrado
    public boolean checaEmails(String email){
        db = this.getReadableDatabase();
        String query = "select email from " + TABELA1;
        Cursor cursor = db.rawQuery(query, null);
        String a;
        if (cursor.moveToFirst()){
            do{
                a = cursor.getString(0);
                if(a.equals(email)){
                    return true;
                }
            }while (cursor.moveToNext());
        }
        return false;
    }
    // Método de procurar senha
    public String checaLogin(String email){
        db = this.getReadableDatabase();
        String query = "select email, senha, id_usuario from " + TABELA1;
        Cursor cursor = db.rawQuery(query, null);
        String a, b, c;
        int d;
        b = "Não encontrado";
        // String a para email, b para senha, c para nome do usuário e d para id do usuário
        if(cursor.moveToFirst()){
            do{
                // 0 valor email, 1 valor senha, 2 usuario, 3 id terminar após
                a = cursor.getString(0);
                if(a.equals(email)){
                    b = cursor.getString(1);
                    d = cursor.getInt(2);
                    setIDUs(d);
                    break;
                }
            }while(cursor.moveToNext());
        }
        db.close();
        return b;
    }

    //ANOTAÇÃO
    public void salvarAnotacao(Anotacao a) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ANOTACAO,a.getNova_anotacao());
        values.put(ID_USER, ID);

        db.insert(TABELA2, null, values);
        db.close();
    }

    public ArrayList<Anotacao> selectAllAnotacao(){

        db = this.getReadableDatabase();
        String[] coluns = {ID_ANOTACAO, ANOTACAO, ID_USER};
        Cursor cursor = db.query(TABELA2,coluns,ID_USER + "=" + ID,null,null,null,null,null);

        ArrayList<Anotacao> ListAnotacao = new ArrayList<>();

        while (cursor.moveToNext()){

            Anotacao a = new Anotacao();
            a.setId_anotacao(cursor.getInt(0));
            a.setNova_anotacao(cursor.getString(1));

            ListAnotacao.add(a);

        }
        cursor.close();
        return ListAnotacao;

    }

    public void alterarAnotacao(Anotacao a) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ANOTACAO,a.getNova_anotacao());
        values.put(ID_USER, ID);

        String[] args = {String.valueOf(a.getId_anotacao())};

        db.update(TABELA2,values,"id_anotacao=?",args);
        db.close();
    }

    // CATEGORIA
    public void salvarCategoria(Categoria c) {

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NOME_CATEGORIA,c.getNova_categoria());
        values.put(ID_USUA, ID);

        db.insert(TABELA3, null, values);
        db.close();
    }

    public ArrayList<Categoria> selectAllCategoria(){

        db = this.getReadableDatabase();
        String[] coluns = {ID_CATEGORIA, NOME_CATEGORIA, ID_USUA};
        Cursor cursor = db.query(TABELA3,coluns,ID_USUA + "=" + ID,null,null,null,null,null);

        ArrayList<Categoria> ListCategoria = new ArrayList<>();

        while (cursor.moveToNext()){

            Categoria c = new Categoria();
            c.setId_categoria(cursor.getInt(0));
            c.setNova_categoria(cursor.getString(1));

            ListCategoria.add(c);

        }
        cursor.close();
        return ListCategoria;

    }
    public void alterarCategoria(Categoria c) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOME_CATEGORIA,c.getNova_categoria());
        values.put(ID_USUA, ID);

        String[] args = {String.valueOf(c.getId_categoria())};

        db.update(TABELA3,values,"id_categoria=?",args);
        db.close();
    }
}
