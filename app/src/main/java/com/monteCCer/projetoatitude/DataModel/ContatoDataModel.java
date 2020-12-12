package com.monteCCer.projetoatitude.DataModel;

public class ContatoDataModel {
    //Dados para criar as tabelas no banco de dados
    //MOR - Modelo objeto Relacional
    //Tupla ou Registros
    private final static String logado = "LOGADO";
    private final static String cpf = "CPF";
    private final static String email = "EMAIL";
    private final static String senha = "SENHA";
    private final static String nome = "NOME";
    private final static String tabela = "CONTATOS";
    //Criar dinamicamente uma query SQL para criar tabelas no DB

    public static String criarTabelaContatos() {
        return "CREATE TABLE " + tabela + " (" + cpf + " TEXT, "
                + nome + " TEXT NOT NULL, " + email + " TEXT PRIMARY KEY NOT NULL, " + senha + " TEXT NOT NULL, "
                + logado + " TEXT NOT NULL)";
    }

    public static String getNome() {
        return nome;
    }

    public static String getLogado() {
        return logado;
    }

    public static String getCpf() {
        return cpf;
    }

    public static String getSenha() {
        return senha;
    }

    public static String getTabela() {
        return tabela;
    }

    public static String getEmail() {
        return email;
    }
}
