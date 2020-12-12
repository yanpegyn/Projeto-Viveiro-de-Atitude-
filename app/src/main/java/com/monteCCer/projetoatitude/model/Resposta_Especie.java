package com.monteCCer.projetoatitude.model;

public class Resposta_Especie {
    private String codigo, nome_cientifico, nome_popular, info_ecologica, ocorrencia, madeira,
            utilidade, fenologia, obt_semente, producao, fotoprincipal, quantidade;
    private Resposta_Foto fotos[];

    public String getCodigo() {
        return codigo;
    }

    public String getNome_cientifico() {
        return nome_cientifico;
    }

    public String getNome_popular() {
        return nome_popular;
    }

    public String getInfo_ecologica() {
        return info_ecologica;
    }

    public String getOcorrencia() {
        return ocorrencia;
    }

    public String getMadeira() {
        return madeira;
    }

    public String getUtilidade() {
        return utilidade;
    }

    public String getFenologia() {
        return fenologia;
    }

    public String getObt_semente() {
        return obt_semente;
    }

    public String getProducao() {
        return producao;
    }

    public String getFotoprincipal() {
        return fotoprincipal;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public Resposta_Foto[] getFotos() {
        return fotos;
    }
}
