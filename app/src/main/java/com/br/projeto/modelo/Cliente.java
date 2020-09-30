package com.br.projeto.modelo;

/**
 * Created by Bernardo on 10/06/2017.
 */

public class Cliente {
    private int id_cliente;

    String nome, email, senha, confirmar_senha;

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) { this.id_cliente = id_cliente; }

    public String getNome() { return nome; }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getConfirmar_senha() {
        return confirmar_senha;
    }

    public void setConfirmar_senha(String confirmar_senha) {
        this.confirmar_senha = confirmar_senha;
    }
}
