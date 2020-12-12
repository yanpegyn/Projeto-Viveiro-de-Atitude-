package com.monteCCer.projetoatitude.model;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class Resposta_Foto {
    private String codigo, codespecie, cpfusuario, data, latitude, longitude, urlfoto, padraofoto, fotobase64;

    public String getCodigo() {
        return codigo;
    }

    public String getCodespecie() {
        return codespecie;
    }

    public String getCpfusuario() {
        return cpfusuario;
    }

    public String getData() {
        return data;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getUrlfoto() {
        return urlfoto;
    }

    public String getPadraofoto() {
        return padraofoto;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setCodespecie(String codespecie) {
        this.codespecie = codespecie;
    }

    public void setCpfusuario(String cpfusuario) {
        this.cpfusuario = cpfusuario;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setUrlfoto(String urlfoto) {
        this.urlfoto = urlfoto;
    }

    public void setUrlfotoWitchBase64(Bitmap urlfoto) {
        if (urlfoto != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            urlfoto.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            this.urlfoto = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }
    }

    public void setPadraofoto(String padraofoto) {
        this.padraofoto = padraofoto;
    }

    @Override
    public String toString() {
        return "Resposta_Foto{" +
                "codigo='" + codigo + '\'' +
                ", codespecie='" + codespecie + '\'' +
                ", cpfusuario='" + cpfusuario + '\'' +
                ", data='" + data + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", urlfoto='" + urlfoto + '\'' +
                ", padraofoto='" + padraofoto + '\'' +
                '}';
    }
}
