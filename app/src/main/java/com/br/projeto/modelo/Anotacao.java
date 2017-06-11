package com.br.projeto.modelo;

import java.io.Serializable;

/**
 * Created by João Tailor on 05/06/2017.
 */

public class Anotacao implements Serializable{

    private int id_anotacao;
    private String nova_anotacao, novo_assunto;

    public int getId_anotacao() {
        return id_anotacao;
    }

    public void setId_anotacao(int id_anotacao) { this.id_anotacao = id_anotacao; }

    public String getNovo_assunto() {
        return novo_assunto;
    }

    public void setNovo_assunto(String novo_assunto) { this.novo_assunto = novo_assunto; }

    public String getNova_anotacao() {
        return nova_anotacao;
    }

    public void setNova_anotacao(String nova_anotacao) {
        this.nova_anotacao = nova_anotacao;
    }

    @Override
    public String toString() {
        return novo_assunto.toString();

    }
}
