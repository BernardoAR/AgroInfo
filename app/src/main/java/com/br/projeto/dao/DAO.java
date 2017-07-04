package com.br.projeto.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.br.projeto.AlterarProduto;
import com.br.projeto.FormProd;
import com.br.projeto.MenuP;
import com.br.projeto.modelo.Anotacao;
import com.br.projeto.modelo.Categoria;
import com.br.projeto.modelo.Cliente;
import com.br.projeto.modelo.Produto;
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
    private static final String ENDERECO = "endereco";
    private static final String TELEFONE = "telefone";

    private static final String TABELA2 = "anotacoes";

    private static final String ID_ANOTACAO = "id_anotacao";
    private static final String ASSUNTO = "assunto";
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

    private static final String TABELA5 = "vendas";

    private static final String ID_VENDA = "id_venda";
    private static final String QUANTIDADE_VENDIDA = "quantidade_vendida";
    private static final String DATA_VENDA = "data_venda";
    private static final String ID_U = "id_usuario";
    private static final String ID_P = "id_produto";

    private static final String TABELA6 = "rendimento";

    private static final String ID_RENDIMENTO = "id_rendimento";
    private static final String RENDIMENTO = "rendimento";
    private static final String DATA = "data";
    private static final String ID_PROD = "id_produto";
    private static final String ID_USUAR = "id_usuario";
    private static final String ID_CATEG = "id_categoria";
    private static final String ID_VEN = "id_venda";

    private static final String TABELA7 = "cliente";

    private static final String ID_CLIENTE = "id_usuario";
    private static final String NOME_CLIENTE = "nome_usuario";
    private static final String EMAILC = "email";
    private static final String SENHAC = "senha";

    public SQLiteDatabase db;

    // CRIAR AS TABELAS

    private static final String CRIAR_TABELA_LOGIN = "CREATE TABLE " + TABELA1 + " ( " +
            " " + ID_USUARIO + " INTEGER PRIMARY KEY , " +
            " " + NOME_USUARIO + " VARCHAR(50) NOT NULL, " +
            " " + EMAIL + " VARCHAR(100) NOT NULL, " +
            " " + SENHA + " VARCHAR(50) NOT NULL, " +
            " " + ENDERECO + " VARCHAR(50), " +
            " " + TELEFONE + " VARCHAR(20) ); ";

    private static final String CRIAR_TABELA_ANOTACOES = "CREATE TABLE " + TABELA2 + " ( " +
            " " + ID_ANOTACAO + " INTEGER PRIMARY KEY , " +
            " " + ASSUNTO + " TEXT NOT NULL, " +
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
            " " + PRECO_CUSTO + " DECIMAL(6,2) NOT NULL, " +
            " " + PRECO_VENDA + " DECIMAL(6,2) NOT NULL, " +
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

    private static final String CRIAR_TABELA_VENDAS = "CREATE TABLE " + TABELA5 + " ( " +
            " " + ID_VENDA + " INTEGER PRIMARY KEY , " +
            " " + QUANTIDADE_VENDIDA + " INTEGER NOT NULL, " +
            " " + DATA_VENDA + " DATE NOT NULL, " +
            " " + ID_U + " INTEGER, " +
            " " + ID_P + " INTEGER, " +
            "CONSTRAINT fk_tbVenda_tbUsuario FOREIGN KEY ( " +
            " " + ID_U + ") REFERENCES " + TABELA1 + " (" + ID_USUARIO + ")" +
            "  ON DELETE CASCADE ON UPDATE CASCADE," +
            "CONSTRAINT fk_tbProduto_tbUsuario FOREIGN KEY ( " +
            " " + ID_P + ") REFERENCES " + TABELA4 + " (" + ID_PRODUTO + ")" +
            "  ON DELETE CASCADE ON UPDATE CASCADE ); ";

    private static final String CRIAR_TABELA_RENDIMENTO = "CREATE TABLE " + TABELA6 + " ( " +
            " " + ID_RENDIMENTO + " INTEGER PRIMARY KEY , " +
            " " + RENDIMENTO + " DECIMAL(6,2) NOT NULL, " +
            " " + DATA + " DATE NOT NULL, " +
            " " + ID_PROD + " INTEGER, " +
            " " + ID_USUAR + " INTEGER, " +
            " " + ID_CATEG + " INTEGER, " +
            " " + ID_VEN+ " INTEGER, " +
            "CONSTRAINT fk_tbRendimento_tbProduto FOREIGN KEY ( " +
            " " + ID_PROD + ") REFERENCES " + TABELA4 + " (" + ID_PRODUTO + ")" +
            "  ON DELETE CASCADE ON UPDATE CASCADE," +
            "CONSTRAINT fk_tbRendimento_tbUsuario FOREIGN KEY ( " +
            " " + ID_USUAR + ") REFERENCES " + TABELA1 + " (" + ID_USUARIO + ")" +
            "  ON DELETE CASCADE ON UPDATE CASCADE," +
            "CONSTRAINT fk_tbRendimento_tbCategoria FOREIGN KEY ( " +
            " " + ID_CATEG + ") REFERENCES " + TABELA3 + " (" + ID_CATEGORIA + ")" +
            "  ON DELETE CASCADE ON UPDATE CASCADE," +
            "CONSTRAINT fk_tbRendimento_tbVendas FOREIGN KEY ( " +
            " " + ID_VEN + ") REFERENCES " + TABELA5 + " (" + ID_VENDA + ")" +
            "  ON DELETE CASCADE ON UPDATE CASCADE); ";

    private static final String CRIAR_TABELA_CLIENTE = "CREATE TABLE " + TABELA7 + " ( " +
            " " + ID_CLIENTE + " INTEGER PRIMARY KEY , " +
            " " + NOME_CLIENTE + " VARCHAR(50) NOT NULL, " +
            " " + EMAILC + " VARCHAR(100) NOT NULL, " +
            " " + SENHAC + " VARCHAR(50) NOT NULL ); ";


    public DAO(Context context) {

        super(context, NOME_BANCO, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG, "Checando");
        db.execSQL(CRIAR_TABELA_LOGIN);
        db.execSQL(CRIAR_TABELA_ANOTACOES);
        db.execSQL(CRIAR_TABELA_CATEGORIA);
        db.execSQL(CRIAR_TABELA_PRODUTO);
        db.execSQL(CRIAR_TABELA_VENDAS);
        db.execSQL(CRIAR_TABELA_RENDIMENTO);
        db.execSQL(CRIAR_TABELA_CLIENTE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABELA1);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA2);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA3);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA4);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA5);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA6);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA7);

        onCreate(db);

    }
    //USUÁRIO/ADMIN
    public void salvarUsuario(Usuario u) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NOME_USUARIO,u.getNome());
        values.put(EMAIL,u.getEmail());
        values.put(SENHA,u.getSenha());

        db.insert(TABELA1, null, values);
        db.close();
    }

    //ALTERAR CONFIGURAÇÕES DO ADMIN/EMPRESÁRIO

    public void alterarUsuario(Usuario u) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOME_USUARIO,u.getNome());
        values.put(EMAIL, u.getEmail());
        values.put(SENHA, u.getSenha());
        values.put(ENDERECO, u.getEndereco());
        values.put(TELEFONE, u.getTelefone());

        String[] args = {String.valueOf(u.getId_usuario())};

        db.update(TABELA1,values,"id_usuario=?",args);
        db.close();
    }

    //CLIENTE
    public void salvarCliente(Cliente c) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NOME_CLIENTE,c.getNome());
        values.put(EMAILC,c.getEmail());
        values.put(SENHAC,c.getSenha());

        db.insert(TABELA7, null, values);
        db.close();
    }
    //ALTERAR CONFIGURAÇÕES CLIENTE
    public void alterarCliente(Cliente c) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOME_CLIENTE,c.getNome());
        values.put(EMAILC, c.getEmail());
        values.put(SENHAC, c.getSenha());

        String[] args = {String.valueOf(c.getId_cliente())};

        db.update(TABELA7,values,"id_usuario=?",args);
        db.close();
    }
    // Pegar e Colocar nome, email, endereco, telefone, senha
    private static String str, str1, str2, str3, strSenha;
    private static int id;
    //Cliente ou Usuário, pegar

    private static String nomeMod;
    public void setNome(String nomeMod) { this.nomeMod = nomeMod; }
    public static String getNome(){ return nomeMod; }

    private static String emailMod;
    public void setEmail(String emailMod) { this.emailMod = emailMod; }
    public static String getEmail(){ return emailMod; }

    private static String enderecoMod;
    public void setEndereco(String enderecoMod) { this.enderecoMod = enderecoMod; }
    public static String getEndereco(){ return enderecoMod; }

    private static String telefoneMod;
    public void setTelefone(String telefoneMod) { this.telefoneMod = telefoneMod; }
    public static String getTelefone(){ return telefoneMod; }

    //Pegar E-mail
    public String getEmailUs(){
        db = this.getReadableDatabase();
        String query = "select email from " + TABELA1 + " where id_usuario = " + MenuP.ID;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do{
                strSenha = cursor.getString(0);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return strSenha;
    }
    public String getEmailCl(){
        db = this.getReadableDatabase();
        String query2 = "select email from " + TABELA7 + " where id_usuario = " + MenuP.ID;
        Cursor cursor2 = db.rawQuery(query2, null);
        if(cursor2.moveToFirst()){
            do{
                strSenha = cursor2.getString(0);
            }while(cursor2.moveToNext());
        }
        cursor2.close();
        db.close();
        return strSenha;
    }

    //Pegar a senha do usuário
    public String getSenhaUs(){
        db = this.getReadableDatabase();
        String query = "select senha from " + TABELA1 + " where id_usuario = " + MenuP.ID;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do{
                strSenha = cursor.getString(0);
            }while(cursor.moveToNext());
        }
        cursor.close();

        String query2 = "select senha from " + TABELA7 + " where id_usuario = " + MenuP.ID;
        Cursor cursor2 = db.rawQuery(query2, null);
        if(cursor2.moveToFirst()){
            do{
                strSenha = cursor2.getString(0);
            }while(cursor2.moveToNext());
        }
        cursor2.close();
        db.close();
        return strSenha;
    }

    public String getNomeUs() {
        db = this.getReadableDatabase();
        String query = "select nome_usuario, email, endereco, telefone from " + TABELA1 + " where id_usuario = " + MenuP.ID;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do{
                str = cursor.getString(0);
                str1 = cursor.getString(1);
                str2 = cursor.getString(2);
                str3 = cursor.getString(3);
                //Do something Here with values
                setNome(str);
                setEmail(str1);
                setEndereco(str2);
                setTelefone(str3);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return str ;
    }
    public String getNomeCl() {
        db = this.getReadableDatabase();
        String query2 = "select nome_usuario, email, senha from " + TABELA7 + " where id_usuario = " + MenuP.ID;
        Cursor cursor2 = db.rawQuery(query2, null);
        if (cursor2.moveToFirst()) {
            do {
                str = cursor2.getString(0);
                str1 = cursor2.getString(1);
                //Do something Here with values
                setNome(str);
                setEmail(str1);

            } while (cursor2.moveToNext());
        }
        cursor2.close();
        db.close();
        return str ;
    }
    // Pegar e Colocar ID
    public void setIDUs(int id) { this.id = id; }
    public static int getIDUs(){ return id; }
    // PEGAR O NOME
    // Método de ver se o email já está cadastrado
    public boolean checaEmailsUs(String email){
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
    public boolean checaEmailsCli(String email){
        db = this.getReadableDatabase();
        String query = "select email from " + TABELA7;
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
    // Ver qual dos dois ficou(Cliente ou Uuário), True Usuário, False Cliente
    private static boolean CoU;
    public void setBolR(boolean CoU){
        this.CoU = CoU;
    }
    public static boolean getBolR(){
        return CoU;
    }
    // Método de procurar senha
    public String checaLogin(String email){
        db = this.getReadableDatabase();
        //Usuário
        String query = "select email, senha, id_usuario from " + TABELA1;
        Cursor cursor = db.rawQuery(query, null);
        String a, b, c;
        int d;
        b = "Não encontrado";
        // String a para email, b para senha, c para nome do usuário e d para id do usuário
        if(cursor.moveToFirst()) {
            do {
                // 0 valor email, 1 valor senha, 2 usuario, 3 id terminar após
                a = cursor.getString(0);
                if (a.equals(email)) {
                    b = cursor.getString(1);
                    d = cursor.getInt(2);
                    setIDUs(d);
                    setBolR(true);
                    break;
                }
            } while (cursor.moveToNext());
        }
        //Cliente
        String query2 = "select email, senha, id_usuario from " + TABELA7;
        cursor = db.rawQuery(query2, null);
        // String a para email, b para senha, c para nome do usuário e d para id do usuário
        if(cursor.moveToFirst()){
            do{
                // 0 valor email, 1 valor senha, 2 usuario, 3 id terminar após
                a = cursor.getString(0);
                if(a.equals(email)){
                    b = cursor.getString(1);
                    d = cursor.getInt(2);
                    setIDUs(d);
                    setBolR(false);
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

        values.put(ASSUNTO, a.getNovo_assunto());
        values.put(ANOTACAO,a.getNova_anotacao());
        values.put(ID_USER, ID);

        db.insert(TABELA2, null, values);
        db.close();
    }

    public ArrayList<Anotacao> selectAllAnotacao(){

        db = this.getReadableDatabase();
        String[] coluns = {ID_ANOTACAO, ASSUNTO, ANOTACAO, ID_USER};
        Cursor cursor = db.query(TABELA2,coluns,ID_USER + "=" + ID,null,null,null,null,null);

        ArrayList<Anotacao> ListAnotacao = new ArrayList<>();

        while (cursor.moveToNext()){

            Anotacao a = new Anotacao();
            a.setId_anotacao(cursor.getInt(0));
            a.setNovo_assunto(cursor.getString(1));
            a.setNova_anotacao(cursor.getString(2));
            ListAnotacao.add(a);

        }
        cursor.close();
        return ListAnotacao;

    }

    public void alterarAnotacao(Anotacao a) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ANOTACAO,a.getNova_anotacao());
        values.put(ASSUNTO, a.getNovo_assunto());
        values.put(ID_USER, ID);

        String[] args = {String.valueOf(a.getId_anotacao())};

        db.update(TABELA2,values,"id_anotacao=?",args);
        db.close();
    }

    public void excluirAnotacao(Anotacao a) {
        db = this.getWritableDatabase();

        String[] args = {String.valueOf(a.getId_anotacao())};

        db.delete(TABELA2,"id_anotacao=?",args);
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
        Cursor cursor = db.query(TABELA3,coluns,ID_USUA + "=" + ID,null,null,null,"nome_categoria",null);

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

    public void excluirCategoria(Categoria c) {
        db = this.getWritableDatabase();

        String[] args = {String.valueOf(c.getId_categoria())};

        db.delete(TABELA3,"id_categoria=?",args);
        db.close();
    }

    //PRODUTO

    public void salvarProduto(Produto p) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NOME_PRODUTO,p.getNomeProduto());
        values.put(PRECO_CUSTO,p.getPrecoCusto());
        values.put(PRECO_VENDA,p.getPrecoVenda());
        values.put(QUANTIDADE,p.getQuantidade());
        values.put(DATA_CADASTRO,p.getDataCadastro());
        values.put(ID_USU,MenuP.ID);
        values.put(ID_CAT, FormProd.valorId);

        db.insert(TABELA4, null, values);
        db.close();
    }

    //LISTAR PRODUTO

    public ArrayList<Produto> selectAllProduto(){

        db = this.getReadableDatabase();
        String[] coluns = {ID_PRODUTO, NOME_PRODUTO, PRECO_CUSTO, PRECO_VENDA, QUANTIDADE, DATA_CADASTRO, ID_CAT};
        Cursor cursor = db.query(TABELA4,coluns,ID_USU + "=" + ID,null,null,null,null,null);

        ArrayList<Produto> ListProduto = new ArrayList<>();

        while (cursor.moveToNext()){

            Produto p = new Produto();
            Categoria c = new Categoria();

            p.setId_produto(cursor.getInt(0));
            p.setNomeProduto(cursor.getString(1));
            p.setPrecoCusto(cursor.getFloat(2));
            p.setPrecoVenda(cursor.getFloat(3));
            p.setQuantidade(cursor.getInt(4));
            p.setDataCadastro(cursor.getString(5));
            c.setId_categoria(cursor.getInt(6));
            ListProduto.add(p);

        }
        cursor.close();
        return ListProduto;

    }

    //Alterar produto

    public void alterarProduto(Produto p) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOME_PRODUTO,p.getNomeProduto());
        values.put(PRECO_CUSTO,p.getPrecoCusto());
        values.put(PRECO_VENDA,p.getPrecoVenda());
        values.put(QUANTIDADE,p.getQuantidade());
        values.put(ID_USU,MenuP.ID);
        values.put(ID_CAT, AlterarProduto.idCat);

        String[] args = {String.valueOf(p.getId_produto())};

        db.update(TABELA4,values,"id_produto=?",args);
        db.close();
    }

    //EXCLUIR PRODUTO

    public void excluirProduto(Produto p) {
        db = this.getWritableDatabase();

        String[] args = {String.valueOf(p.getId_produto())};

        db.delete(TABELA4,"id_produto=?",args);
        db.close();
    }
    //Pegar ID e Nome da Categoria do Produto
    public int getCategoriaProd(int id){
        db = this.getReadableDatabase();
        String query = "select id_categoria from " + TABELA4 + " where id_produto = " + id;
        Cursor cursor = db.rawQuery(query, null);
        int idz;
        idz = 0;
        if(cursor.moveToFirst()){
            do{
                idz = cursor.getInt(0);
            }while(cursor.moveToNext());
        }
        cursor.close();

        db.close();
        return idz;
    }
    public String getCategoriaProdN(int id){
        db = this.getReadableDatabase();
        String query = "select nome_categoria from " + TABELA3 + " where id_categoria = " + id;
        Cursor cursor = db.rawQuery(query, null);
        String idz;
        idz = "0";
        if(cursor.moveToFirst()){
            do{
                idz = cursor.getString(0);
            }while(cursor.moveToNext());
        }
        cursor.close();

        db.close();
        return idz;
    }
    //SOMA DO PREÇOCUSTO E PREÇOVENDA
    public void somaPCV(){
        db = this.getReadableDatabase();

        String query = "select SUM(preco_custo) AS 'Soma dos precos de custo' FROM " + TABELA4 + " where id_usuario = " + MenuP.ID;
        String query2 = "select SUM(preco_venda) AS 'Soma dos precos de venda' FROM " + TABELA4 + " where id_usuario = " + MenuP.ID ;
        Cursor cursor = db.rawQuery(query, null);
        Cursor cursor2 = db.rawQuery(query2, null);
        float a,b;
        a = b =0;

        if(cursor.moveToFirst()) {
            do {
                a = cursor.getFloat(0);
            } while (cursor.moveToNext());
        }
        if(cursor2.moveToFirst()) {
            do {
                b = cursor2.getFloat(0);
            } while (cursor2.moveToNext());
        }
        setSomaPC(a);
        setSomaPV(b);
        db.close();
    }
    float precoCus, precoVen;
    public void setSomaPC(float precoCus) { this.precoCus = precoCus; }
    public float getSomaPC(){ return precoCus; }
    public void setSomaPV(float precoVen) { this.precoVen = precoVen; }
    public float getSomaPV(){ return precoVen; }
}
