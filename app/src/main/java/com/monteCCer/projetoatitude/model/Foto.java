package com.monteCCer.projetoatitude.model;

import org.jetbrains.annotations.NotNull;

public class Foto {
    private Long ID;
    private Integer Especie;
    private String IMG;
    private String Longitude, Latitude;
    private String Data;

    public double getLongitude() {
        return Double.valueOf(Longitude);
    }

    private void setLongitudeToString(double Longitude) {
        this.Longitude = Double.toString(Longitude);
    }

    public double getLatitude() {
        return Double.valueOf(Latitude);
    }

    private void setLatitudeToString(double Latitude) {
        this.Latitude = Double.toString(Latitude);
    }

    /*private void setBitmapToBase64(Bitmap bmp) {
        if (bmp != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            IMG_base64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }
    }*/

    /*public Bitmap getIMG() {
        byte[] decodedString = Base64.decode(IMG_base64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }*/

    public String getIMG() {
        return IMG;
    }

    /*public String getBase64() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IMG.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }*/

    public Foto(String ID, String especie, String bitmap_url, Double longitude, Double latitude, String Data) {
        if (ID != null)
            setID(ID);
        if (especie != null)
            setEspecie(especie);
        if (latitude != null)
            setLatitudeToString(latitude);
        if (longitude != null)
            setLongitudeToString(longitude);
        if (bitmap_url != null)
            setIMG(bitmap_url);
        if (Data != null)
            setData(Data);
    }

    public String getID() {
        return ID != null ? Long.toString(ID) : null;
    }

    public void setID(String ID) {
        if (ID != null) {
            this.ID = Long.valueOf(ID);
        }
    }

    public String getData() {
        return Data;
    }

    private void setData(String data) {
        Data = data;
    }

    public String getEspecie() {
        if (Especie != null)
            return Integer.toString(Especie);
        else return null;
    }

    public void setEspecie(String especie) {
        if (especie != null) {
            Especie = Integer.valueOf(especie);
        }
    }

    /*public void setIMG_base64(String IMG_base64) {
        this.IMG_base64 = IMG_base64;
    }*/

    public void setIMG(String url) {
        this.IMG = url;
    }

    @NotNull
    @Override
    public String toString() {
        return "Foto{" +
                "ID=" + ID +
                ", Especie=" + Especie +
                '}';
    }
}
