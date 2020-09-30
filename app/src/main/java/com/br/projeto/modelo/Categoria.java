package com.br.projeto.modelo;

import java.io.Serializable;

/**
 * Created by Bernardo on 08/06/2017.
 */

public class Categoria implements Serializable{

    private int id_categoria;
    private String nova_categoria;

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) { this.id_categoria = id_categoria; }

    public String getNova_categoria() {
        return nova_categoria;
    }

    public void setNova_categoria(String nova_categoria) {
        this.nova_categoria= nova_categoria;
    }

    @Override
    public String toString() {
        return nova_categoria.toString();

    }
}
