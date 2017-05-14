package com.br.projeto.modelo;

import java.io.Serializable;

public class Usuario implements Serializable{

    private String nome;
    private String email;
    private String senha;
    private String confirmar_senha;
    public String getNome() {
        return nome;
    }
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
