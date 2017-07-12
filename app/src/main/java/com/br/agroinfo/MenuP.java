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
import android.widget.TextView;
import android.widget.Toast;

import com.br.agroinfo.dao.Conexao;
import com.br.agroinfo.modelo.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuP extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView nomeusuario;
    private FirebaseAuth autent;
    FirebaseUser usuario;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    boolean escolha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        //Com o ID já pego, pegar o nome que possui nesse id
        nomeusuario = (TextView) cabecalho.findViewById(R.id.nome_usuario);
        inicFirebase();

    }
    // PEGAR A CATEGORIA
    private void pegaNomeeEsc() {
        databaseReference.child("Usuario").child(usuario.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        invalidateOptionsMenu();
                        Usuario u = dataSnapshot.getValue(Usuario.class);
                        escolha = u.getEscolha();
                        nomeusuario.setText(u.getNome().toUpperCase());
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void inicFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();
        autent = Conexao.getFirebaseAuth();
        usuario = Conexao.getFirebaseUser();
        verificaUsuario();
        pegaNomeeEsc();
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        menu = navigationView.getMenu();
        alerta(String.valueOf(escolha));
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
    private void alerta(String mensagem){
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }
    private void verificaUsuario() {
        if (usuario == null){
            alerta("Por algum motivo está nulo");
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
    public boolean onNavigationItemSelected( MenuItem item) {

        // Views itens clicados, suas ações
        int id = item.getItemId();

        if (id == R.id.nav_anotacoes) {
            Intent abrirAnotacoes = new Intent(MenuP.this,Lista_anotacoes.class);
            // solicitar para abir
            invalidateOptionsMenu();
            startActivity(abrirAnotacoes);
        } else if (id == R.id.nav_prod) {
            Intent abrirProd = new Intent(MenuP.this,FormProd.class);
            invalidateOptionsMenu();
            startActivity(abrirProd);
        } else  if(id == R.id.nav_rendimentos){
           Intent abrirRendimentos = new Intent(MenuP.this, Rendimentos.class);
            startActivity(abrirRendimentos);
        } else  if(id == R.id.nav_vendas){
            Intent abrirVendas = new Intent(MenuP.this, FormVendas.class);
            invalidateOptionsMenu();
            startActivity(abrirVendas);

        }else  if(id == R.id.nav_pesquisa){
            Intent abrirPesquisa = new Intent(MenuP.this, PesquisarProduto.class);
            invalidateOptionsMenu();
            startActivity(abrirPesquisa);

        } else  if(id == R.id.nav_cont){
            if (escolha){
                Intent abrirCont = new Intent(MenuP.this, ConfContaUs.class);
                invalidateOptionsMenu();
                startActivity(abrirCont);
            } else {
                Intent abrirCont = new Intent(MenuP.this, ConfConta.class);
                invalidateOptionsMenu();
                startActivity(abrirCont);
            }
        } else  if(id == R.id.nav_sair){
            //Deslogar
            Conexao.deslogar();
            Intent abrirLogin = new Intent(MenuP.this, FormularioLogin.class);
            startActivity(abrirLogin);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
