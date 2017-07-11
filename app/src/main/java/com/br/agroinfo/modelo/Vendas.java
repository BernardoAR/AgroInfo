package com.br.agroinfo.modelo;

/**
 * Created by Bernardo on 06/07/2017.
 */

public class Vendas {

    String id_venda;
    int quant_venda, ano, mes;
    float preco_venda;

    public int getAno() { return ano; }

    public void setAno(int ano) { this.ano = ano; }

    public int getMes() { return mes; }

    public void setMes(int mes) { this.mes = mes; }

    public float getPreco_venda() { return preco_venda; }

    public void setPreco_venda(float preco_venda) { this.preco_venda = preco_venda; }

    public String getId_venda() { return id_venda; }

    public void setId_venda(String id_venda) { this.id_venda = id_venda; }

    public int getQuant_venda() { return quant_venda; }

    public void setQuant_venda(int quant_venda) { this.quant_venda = quant_venda; }
}
