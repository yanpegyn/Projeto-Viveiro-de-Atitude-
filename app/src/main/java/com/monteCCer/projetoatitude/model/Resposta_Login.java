package com.monteCCer.projetoatitude.model;

public class Resposta_Login {
    private String email, cpf, nome, endereco, telefone1, telefone2, datanascimento, permissao, dataaceitetermos, flagnovasenha;

    public String getNome() {
        return nome;
    }

    public String getPermissao() {
        return permissao;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getTelefone1() {
        return telefone1;
    }

    public String getTelefone2() {
        return telefone2;
    }

    public String getDatanascimento() {
        return datanascimento;
    }

    public String getDataaceitetermos() {
        return dataaceitetermos;
    }

    public String getFlagnovasenha() {
        return flagnovasenha;
    }
}
