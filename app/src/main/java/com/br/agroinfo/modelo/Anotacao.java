package com.br.agroinfo.modelo;

import java.io.Serializable;

/**
 * Created by João Tailor on 05/06/2017.
 */

public class Anotacao implements Serializable{

    public String getData() {return data;}

    public void setData(String data) {this.data = data;}

    private String data, anotacao, assunto, id_anotacao;

    public String getId_anotacao() { return id_anotacao; }

    public void setId_anotacao(String id_anotacao) { this.id_anotacao = id_anotacao; }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) { this.assunto = assunto; }

    public String getAnotacao() {
        return anotacao;
    }

    public void setAnotacao(String anotacao) {
        this.anotacao = anotacao;
    }

    @Override
    public String toString() {
        return data.replace('-', '/') + " - " + assunto;
    }
}
