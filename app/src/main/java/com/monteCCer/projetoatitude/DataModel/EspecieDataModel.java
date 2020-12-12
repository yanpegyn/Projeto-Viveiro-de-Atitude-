package com.monteCCer.projetoatitude.DataModel;

public class EspecieDataModel {
    //Dados para criar as tabelas no banco de dados
    //MOR - Modelo objeto Relacional
    //Tupla ou Registros
    private final static String ID = "ID";
    private final static String Nome = "NOME";
    private final static String NomeCientifico = "NOMECIENTIFICO";
    private final static String InfoEco = "INFOECO";
    private final static String Ocorrencia = "OCORRENCIA";
    private final static String Madeira = "MADEIRA";
    private final static String Utilidade = "UTILIDADE";
    private final static String Fenologia = "FENOLOGIA";
    private final static String OBT_Semente = "OBTSEMENTE";
    private final static String Producao = "PRODUCAO";
    private final static String Quantidade = "QUANTIDADE";
    private final static String Icone = "ICONE";
    private final static String Tabela = "ESPECIE";
    private final static String CPF = "CPF";

    //Criar dinamicamente uma query SQL para criar tabelas no DB

    public static String criarTabelaEspecie() {
        return "CREATE TABLE " + Tabela + " (" + ID + " TEXT PRIMARY KEY NOT NULL, "
                + Nome + " TEXT, " + NomeCientifico + " TEXT, " + InfoEco + " TEXT, " + Ocorrencia + " TEXT, "
                + Madeira + " TEXT, " + Utilidade + " TEXT, " + Fenologia + " TEXT, "
                + OBT_Semente + " TEXT, " + Producao + " TEXT, " + Quantidade + " TEXT, "
                + Icone + " TEXT, " + CPF + " TEXT)";
    }

    public static String getID() {
        return ID;
    }

    public static String getNome() {
        return Nome;
    }

    public static String getInfoEco() {
        return InfoEco;
    }

    public static String getOcorrencia() {
        return Ocorrencia;
    }

    public static String getMadeira() {
        return Madeira;
    }

    public static String getUtilidade() {
        return Utilidade;
    }

    public static String getFenologia() {
        return Fenologia;
    }

    public static String getOBT_Semente() {
        return OBT_Semente;
    }

    public static String getProducao() {
        return Producao;
    }

    public static String getQuantidade() {
        return Quantidade;
    }

    public static String getIcone() {
        return Icone;
    }

    public static String getTabela() {
        return Tabela;
    }

    public static String getNomeCientifico() {
        return NomeCientifico;
    }

    public static String getCPF() {
        return CPF;
    }
}
