package com.br.agroinfo.modelo;


public class Usuario{

    String nome, endereco, telefone;

    boolean escolha;

    public String getNome() { return nome; }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean getEscolha() { return escolha; }

    public void setEscolha(boolean escolha) { this.escolha = escolha; }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }


}
