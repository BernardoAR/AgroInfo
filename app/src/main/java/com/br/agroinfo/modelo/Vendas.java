package com.br.agroinfo.modelo;

/**
 * Created by Bernardo on 06/07/2017.
 */

public class Vendas {
    String id_venda, data_venda;
    int quant_venda;

    public String getId_venda() { return id_venda; }

    public void setId_venda(String id_venda) { this.id_venda = id_venda; }

    public String getData_venda() { return data_venda; }

    public void setData_venda(String data_venda) { this.data_venda = data_venda; }

    public int getQuant_venda() { return quant_venda; }

    public void setQuant_venda(int quant_venda) { this.quant_venda = quant_venda; }
}
