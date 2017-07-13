package com.br.agroinfo.modelo;

import java.io.Serializable;

/**
 * Created by Bernardo on 06/07/2017.
 */

public class Vendas implements Serializable {

    String id_venda, mes_ano;
    int quant_venda, ano, mes;
    float preco_venda, preco_custo;

    public String getMes_ano() {
        return mes_ano;
    }

    public void setMes_ano(String mes_ano) {
        this.mes_ano = mes_ano;
    }

    public float getPreco_custo() {
        return preco_custo;
    }

    public void setPreco_custo(float preco_custo) {
        this.preco_custo = preco_custo;
    }

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

    @Override
    public String toString() {
        return (" Mês " + String.valueOf(mes) + " Ano " + String.valueOf(ano) + " Preço " + String.valueOf(preco_venda));
    }
}
