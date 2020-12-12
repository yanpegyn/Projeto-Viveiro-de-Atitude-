package com.monteCCer.projetoatitude.DataModel;

public class AreaDataModel {
    //Dados para criar as tabelas no banco de dados
    //MOR - Modelo objeto Relacional
    //Tupla ou Registros
    private final static String ID = "ID";
    private final static String IMG = "IMG";
    private final static String Longitude = "LONGITUDE";
    private final static String Latitude = "LATITUDE";
    private final static String Data = "DATA";
    private final static String Tabela = "AREA";
    private final static String CPF = "CPF";

    //Criar dinamicamente uma query SQL para criar tabelas no DB

    public static String criarTabelaArea() {
        return "CREATE TABLE " + Tabela + " (" + ID + " TEXT PRIMARY KEY NOT NULL, "
                + IMG + " TEXT, " + Longitude + " TEXT, "
                + Latitude + " TEXT, " + Data + " TEXT, "
                + CPF + " TEXT)";
    }

    public static String getID() {
        return ID;
    }

    public static String getIMG() {
        return IMG;
    }

    public static String getLongitude() {
        return Longitude;
    }

    public static String getLatitude() {
        return Latitude;
    }

    public static String getTabela() {
        return Tabela;
    }

    public static String getCPF() {
        return CPF;
    }

    public static String getData() {
        return Data;
    }
}
