package com.monteCCer.projetoatitude.DataModel;

public class EsperaDeEnvioDataModel {
    //Dados para criar as tabelas no banco de dados
    //MOR - Modelo objeto Relacional
    //Tupla ou Registros
    private final static String ID = "ID";
    private final static String Tabela = "ESPERADEENVIO";
    private final static String CPF = "CPF";

    //Criar dinamicamente uma query SQL para criar tabelas no DB

    public static String criarTabelaEsperaDeEnvio() {
        return "CREATE TABLE " + Tabela + " (" + ID + " TEXT PRIMARY KEY NOT NULL, "
                + CPF + " TEXT)";
    }

    public static String getID() {
        return ID;
    }

    public static String getTabela() {
        return Tabela;
    }

    public static String getCPF() {
        return CPF;
    }
}
