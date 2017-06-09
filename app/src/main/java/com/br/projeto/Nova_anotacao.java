package com.br.projeto;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.br.projeto.dao.DAO;
import com.br.projeto.modelo.Anotacao;

import static com.br.projeto.MenuP.ID;

public class Nova_anotacao extends AppCompatActivity {

    EditText nova_anotacao;
    Button btnCadastrar;
    DAO helper = new DAO(this);
    long retornoDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_anotacao);

        Toast tempor = Toast.makeText(this,String.valueOf(ID),Toast.LENGTH_LONG);
        tempor.show();
        //resgatar os componentes

        nova_anotacao = (EditText) findViewById(R.id.nova_anotacao);
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Pegar Strings
                EditText nova_anotacao = (EditText)findViewById(R.id.nova_anotacao);

                // Colocar em Strings
                String anotacaostr = nova_anotacao.getText().toString();



                // Inserir os detalhes no BD
                Anotacao a = new Anotacao();
                a.setNova_anotacao(anotacaostr);

                helper.salvarAnotacao(a);


                if (retornoDB == -1) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Nova_anotacao.this);
                    dialog.setTitle("AgroInfo");
                    dialog.setMessage("Erro ao cadastrar!");
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Nova_anotacao.this.finish();
                        }
                    });


                    dialog.show();

                } else {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(Nova_anotacao.this);
                    dialog.setTitle("AgroInfo");
                    dialog.setMessage("Cadastrado com sucesso!");
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Nova_anotacao.this.finish();
                        }
                    });


                    dialog.show();
                }

            }
        });
    }
}
