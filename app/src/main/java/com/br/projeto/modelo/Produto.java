package com.br.projeto.modelo;

import java.io.Serializable;

/**
 * Created by João Tailor on 19/06/2017.
 */

public class Produto implements Serializable {

    private int id_produto;

    String nomeProduto, DataCadastro;
    float PrecoCusto, PrecoVenda;
    int Quantidade;

    public int getId_produto() {
        return id_produto;
    }

    public void setId_produto(int id_produto) {
        this.id_produto = id_produto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getDataCadastro() {
        return DataCadastro;
    }

    public void setDataCadastro(String dataCadastro) {
        DataCadastro = dataCadastro;
    }

    public float getPrecoCusto() {
        return PrecoCusto;
    }

    public void setPrecoCusto(float precoCusto) {
        PrecoCusto = precoCusto;
    }

    public float getPrecoVenda() {
        return PrecoVenda;
    }

    public void setPrecoVenda(float precoVenda) {
        PrecoVenda = precoVenda;
    }

    public int getQuantidade() {
        return Quantidade;
    }

    public void setQuantidade(int quantidade) {
        Quantidade = quantidade;
    }

    @Override
    public String toString() {
        return nomeProduto.toString();

    }
}


