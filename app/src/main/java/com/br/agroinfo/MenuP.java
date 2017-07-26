package com.br.agroinfo;

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
import android.webkit.WebView;
import android.widget.TextView;

import com.br.agroinfo.dao.Conexao;
import com.br.agroinfo.modelo.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class MenuP extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView textNomeUsuario;
    WebView textoBV;
    boolean escolha;
    ValueEventListener valores;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_p);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView titulo = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titulo.setText("MENU PRINCIPAL");
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

        //Com o ID já pego, pegar o edtNome que possui nesse id
        textNomeUsuario = (TextView) cabecalho.findViewById(R.id.nome_usuario);
        textoBV = (WebView) findViewById(R.id.textoBV);
    }
    // PEGAR A CATEGORIA
    private void pegaNomeeEsc() {
        valores = FormularioLogin.databaseReference.child("Usuario").child(FormularioLogin.usuario.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            invalidateOptionsMenu();
                            Usuario u = dataSnapshot.getValue(Usuario.class);
                            escolha = u.getEscolha();
                            textNomeUsuario.setText(u.getNome().toUpperCase());
                            if (escolha){
                                String texto = "Seja bem-vindo(a) ao AgroInfo! Esta é sua área administrativa, nela você terá total controle e autonomia para gerenciar seu negócio. Para iniciar sua experiência, clique no menu do canto superior esquerdo da tela e veja as possibilidades feitas para você.";
                                textoBV.loadData("<p style=\"text-align: justify\">" + texto + "</p>", "text/html; charset=utf-8","UTF-8");
                            } else {
                                String texto = "Seja bem-vindo(a) ao AgroInfo! Esta é a sua área do cliente onde você terá a possibilidade de pesquisar por produtos em estabelecimentos com os melhores preços para você. Para iniciar sua experiência, clique no menu do canto superior esquerdo da tela e veja as possibilidades feitas para você.";
                                textoBV.loadData("<p style=\"text-align: justify\">" + texto + "</p>", "text/html; charset=utf-8","UTF-8");
                            }
                        } else {
                            FormularioLogin.databaseReference.removeEventListener(valores);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        verificaUsuario();
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
        if (escolha) {
            pesquisa.setVisible(false);
        } else if (!escolha){
            anotacoes.setVisible(false);
            produtos.setVisible(false);
            rendimentos.setVisible(false);
            vendas.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void verificaUsuario() {
        if (FormularioLogin.autent.getCurrentUser() == null){
            Publico.Intente(MenuP.this, FormularioLogin.class);
            finish();
        } else {
            pegaNomeeEsc();
        }
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Views itens clicados, suas ações
        int id = item.getItemId();

        if (id == R.id.nav_anotacoes) {
            Publico.Intente(MenuP.this,ListaAnotacoes.class);
            // solicitar para abir
            invalidateOptionsMenu();
            finish();
        } else if (id == R.id.nav_prod) {
            Publico.Intente(MenuP.this,FormProd.class);
            invalidateOptionsMenu();
            finish();
        } else  if(id == R.id.nav_rendimentos){
            Publico.Intente(MenuP.this, Rendimentos.class);
            invalidateOptionsMenu();
            finish();
        } else  if(id == R.id.nav_vendas){
            Publico.Intente(MenuP.this, FormVendas.class);
            invalidateOptionsMenu();
            finish();
        }else  if(id == R.id.nav_pesquisa){
            Publico.Intente(MenuP.this, PesquisarProduto.class);
            invalidateOptionsMenu();
            finish();
        } else  if(id == R.id.nav_cont){
            if (escolha){
                Publico.Intente(MenuP.this, ConfContaUs.class);
                invalidateOptionsMenu();
                finish();
            } else {
                Publico.Intente(MenuP.this, ConfConta.class);
                invalidateOptionsMenu();
                finish();
            }
        } else  if(id == R.id.nav_sair){
            //Deslogar
            Conexao.deslogar();
            Publico.Intente(MenuP.this, FormularioLogin.class);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        FormularioLogin.databaseReference.removeEventListener(valores);
    }
}
