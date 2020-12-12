package com.monteCCer.projetoatitude.model;

public class Contato {
    private String logado;
    private String cpf;
    private String email; ///primary
    private String senha;
    private String nome;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getLogado() {
        return logado;
    }

    public void setLogado(String logado) {
        this.logado = logado;
    }

    public Contato() {
        this.logado = "False";
    }

    @Override
    public String toString() {
        return "Contato{" +
                "logado='" + logado + '\'' +
                ", cpf='" + cpf + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", nome='" + nome + '\'' +
                '}';
    }
}