package com.br.projeto.modelo;

import java.io.Serializable;

/**
 * Created by João Tailor on 05/06/2017.
 */

public class Anotacao implements Serializable{

    private int id_anotacao;
    private String nova_anotacao;

    public int getId_anotacao() {
        return id_anotacao;
    }

    public void setId_anotacao(int id_anotacao) {
        this.id_anotacao = id_anotacao;
    }

    public String getNova_anotacao() {
        return nova_anotacao;
    }

    public void setNova_anotacao(String nova_anotacao) {
        this.nova_anotacao = nova_anotacao;
    }

    @Override
    public String toString() {
        return nova_anotacao.toString();

    }
}
