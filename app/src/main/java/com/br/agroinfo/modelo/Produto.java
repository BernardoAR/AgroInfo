package com.br.agroinfo.modelo;

import java.io.Serializable;

/**
 * Created by João Tailor on 19/06/2017.
 */

public class Produto implements Serializable {

    String nomeProduto, dataCadastro, id_produto;
    float precoCusto, precoVenda;
    int quantidade;

    public String getId_produto() {
        return id_produto;
    }

    public void setId_produto(String id_produto) {
        this.id_produto = id_produto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(String dataCadastro) { this.dataCadastro = dataCadastro;}

    public float getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(float precoCusto) { this.precoCusto = precoCusto; }

    public float getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(float precoVenda) {
        this.precoVenda = precoVenda;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }



    @Override
    public String toString() {
        return nomeProduto.toString();
    }

}


