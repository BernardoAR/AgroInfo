package com.br.projeto;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.br.projeto.dao.DAO;
import com.br.projeto.modelo.Sessao;

public class MenuP extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView nomeusuario;
    //Sessão
    private Sessao sessao;
    String nome_usuario;
    DAO helper = new DAO(this);
    public static int ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Checar se está com a sessão logada, se não estiver volta ao FormularioLogin
        sessao = new Sessao(this);
        if (!sessao.logado()) {
            deslogar();
        }
        //
        setContentView(R.layout.activity_menu_p);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /* Checar se a String procede, Pegar o TextView, Modificar e referenciar ao TextView novamente
            Cuidar ao pegar alguma TextView caso não esteja chamada a activity
        */
        View cabecalho = navigationView.getHeaderView(0);
        //EX Pegar o ID     e Mostrar
        ID = sessao.prefs.getInt("id", 0);
        Toast tempor = Toast.makeText(this, String.valueOf(ID), Toast.LENGTH_LONG);
        tempor.show();


        //Com o ID já pego, pegar o nome que possui nesse id
        if (sessao.escolhido() == true) {
            nome_usuario = helper.getNomeUs().toUpperCase();
        } else {
            nome_usuario = helper.getNomeCl().toUpperCase();
        }
        nomeusuario = (TextView) cabecalho.findViewById(R.id.nome_usuario);
        nomeusuario.setText(nome_usuario);
    }

    //Sessão Deslogar
    private void deslogar(){
        //Deslogar em Booleano
        sessao.dizLogado(false);
        finish();
        // Tirar o ID gravado
        sessao.dizID(0);
        //
        Toast temp = Toast.makeText(MenuP.this, "Deslogado", Toast.LENGTH_SHORT);
        temp.show();
        startActivity(new Intent(MenuP.this, FormularioLogin.class));

    }
    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        menu = navigationView.getMenu();

        // Preparar todos
        MenuItem anotacoes = menu.findItem(R.id.nav_anotacoes);
        MenuItem produtos = menu.findItem(R.id.nav_prod);
        MenuItem rendimentos = menu.findItem(R.id.nav_rendimentos);
        MenuItem vendas = menu.findItem(R.id.nav_vendas);
        MenuItem pesquisa = menu.findItem(R.id.nav_pesquisa);
        MenuItem confconta = menu.findItem(R.id.nav_cont);
        MenuItem sair = menu.findItem(R.id.nav_sair);

        anotacoes.setVisible(true);
        produtos.setVisible(true);
        rendimentos.setVisible(true);
        vendas.setVisible(true);
        pesquisa.setVisible(true);
        confconta.setVisible(true);
        sair.setVisible(true);

        if (sessao.escolhido() && sessao.logado()) {
            pesquisa.setVisible(false);
        } else if (!sessao.escolhido() && sessao.logado()){
            anotacoes.setVisible(false);
            produtos.setVisible(false);
            rendimentos.setVisible(false);
            vendas.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected( MenuItem item) {

        // Views itens clicados, suas ações
        int id = item.getItemId();

        if (id == R.id.nav_anotacoes) {
            Intent abrirAnotacoes = new Intent(MenuP.this,Lista_anotacoes.class);
            // solicitar para abir
            startActivity(abrirAnotacoes);

        } else if (id == R.id.nav_prod) {
            Intent abrirProd = new Intent(MenuP.this,FormProd.class);
            startActivity(abrirProd);

        } else  if(id == R.id.nav_rendimentos){
            Intent abrirRendimentos = new Intent(MenuP.this, Rendimentos.class);
            startActivity(abrirRendimentos);
        } else  if(id == R.id.nav_vendas){
            Intent abrirVendas = new Intent(MenuP.this, FormVendas.class);
            startActivity(abrirVendas);

        }else  if(id == R.id.nav_pesquisa){
            Intent abrirPesquisa = new Intent(MenuP.this, PesquisarProduto.class);
            startActivity(abrirPesquisa);

        } else  if(id == R.id.nav_cont){
            if (sessao.escolhido()){
                Intent abrirCont = new Intent(MenuP.this, ConfContaUs.class);
                startActivity(abrirCont);
            } else {
                Intent abrirCont = new Intent(MenuP.this, ConfConta.class);
                startActivity(abrirCont);
            }
        } else  if(id == R.id.nav_sair){
            //Deslogar
            deslogar();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}