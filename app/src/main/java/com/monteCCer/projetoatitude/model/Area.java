package com.monteCCer.projetoatitude.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Area {
    private ArrayList<Foto> fotos = new ArrayList<>();
    private LatLng coordenadas;

    public Area(LatLng coordenadas) {
        this.coordenadas = coordenadas;
    }

    public void addFoto(Foto ft) {
        fotos.add(ft);
    }

    public ArrayList<Foto> getFotos() {
        return fotos;
    }

    public LatLng getCoordenadas() {
        return coordenadas;
    }
}
