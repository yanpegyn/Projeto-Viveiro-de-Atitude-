package com.monteCCer.projetoatitude.model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Especie {
    private Integer ID;
    private String Nome;
    private String NomeCientifico;
    private String InfoEco;
    private String Ocorrencia;
    private String Madeira;
    private String Utilidade;
    private String Fenologia;
    private String OBT_Semente;
    private String Producao;
    private String Quantidade;
    //private String Icone; //Imagem que aparece no programa
    private String Icone;

    private ArrayList<Foto> Fotos;

    public Especie(String ID, String nome, String NomeCientifico, String icone) {
        setID(ID);
        setNome(nome);
        setNomeCientifico(NomeCientifico);
        setIcone(icone);
    }

    @NotNull
    @Override
    public String toString() {
        return "Especie{" +
                "ID=" + ID +
                ", Nome='" + Nome + '\'' +
                ", NomeCientifico='" + NomeCientifico + '\'' +
                ", InfoEco='" + InfoEco + '\'' +
                ", Ocorrencia='" + Ocorrencia + '\'' +
                ", Madeira='" + Madeira + '\'' +
                ", Utilidade='" + Utilidade + '\'' +
                ", Fenologia='" + Fenologia + '\'' +
                ", OBT_Semente='" + OBT_Semente + '\'' +
                ", Producao='" + Producao + '\'' +
                ", Quantidade='" + Quantidade + '\'' +
                ", Icone=" + Icone +
                ", Fotos=" + Fotos +
                '}';
    }

/*private void setIcone(Bitmap bmp) {
        if (bmp != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            Icone = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }
    }*/

    public void setIcone(String bmp) {
        this.Icone = bmp;
    }

    public String getIcone() {
        return this.Icone;
    }

    /*public Bitmap getIconeBMP() {
        if (Icone != null) {
            byte[] decodedString = Base64.decode(Icone.getBytes(), Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }
        return null;
    }*/

    /*public Bitmap getIconeBMP() {
        return Icone;
    }

    public void setIconeBMP(Bitmap bitmap) {
        this.Icone = bitmap;
    }*/

    public ArrayList<Foto> getFotos() {
        return Fotos;
    }

    /*
    public Foto[] getFotosArray() {
        return (Foto[]) Fotos.toArray();
    }

    public void setFotosWithArray(Foto fotos[]) {
        Fotos = new ArrayList<>(Arrays.asList(fotos));
    }
    */
    public void setFotos(ArrayList<Foto> fotos) {
        Fotos = fotos;
    }

    public String getNomeCientifico() {
        return NomeCientifico;
    }

    public void setNomeCientifico(String nomeCientifico) {
        NomeCientifico = nomeCientifico;
    }

    /*public String getIcone() {
        return Icone;
    }*/

    /*public void setIcone(String icone) {
        Icone = icone;
    }*/

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getID() {
        return ID != null ? Integer.toString(ID) : null;
    }

    public void setID(String ID) {
        if (ID != null) {
            this.ID = Integer.parseInt(ID);
        }
    }

    public String getInfoEco() {
        return InfoEco;
    }

    public void setInfoEco(String infoEco) {
        InfoEco = infoEco;
    }

    public String getOcorrencia() {
        return Ocorrencia;
    }

    public void setOcorrencia(String ocorrencia) {
        Ocorrencia = ocorrencia;
    }

    public String getMadeira() {
        return Madeira;
    }

    public void setMadeira(String madeira) {
        Madeira = madeira;
    }

    public String getUtilidade() {
        return Utilidade;
    }

    public void setUtilidade(String utilidade) {
        Utilidade = utilidade;
    }

    public String getFenologia() {
        return Fenologia;
    }

    public void setFenologia(String fenologia) {
        Fenologia = fenologia;
    }

    public String getOBT_Semente() {
        return OBT_Semente;
    }

    public void setOBT_Semente(String OBT_Semente) {
        this.OBT_Semente = OBT_Semente;
    }

    public String getProducao() {
        return Producao;
    }

    public void setProducao(String producao) {
        Producao = producao;
    }

    public String getQuantidade() {
        return Quantidade;
    }

    public void setQuantidade(String quantidade) {
        Quantidade = quantidade;
    }

}
