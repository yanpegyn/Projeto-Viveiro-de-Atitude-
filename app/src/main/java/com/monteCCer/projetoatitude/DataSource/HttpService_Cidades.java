package com.monteCCer.projetoatitude.DataSource;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.monteCCer.projetoatitude.model.Resposta_Cidade;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class HttpService_Cidades extends AsyncTask<Void, Void, Resposta_Cidade[]> {

    private String estado;

    public HttpService_Cidades(String estado) {
        this.estado = estado;
    }

    @Override
    protected Resposta_Cidade[] doInBackground(Void... voids) {
        StringBuilder resposta = new StringBuilder();
        try {

            URL url = new URL("http://viveiroapp.monteccer.com.br/cidades.php?estado=" + estado);
            //Log.i("SITE: ", url.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.connect();

            Scanner scanner = new Scanner(url.openStream());
            if(!scanner.hasNextLine())
                return null;
            while (scanner.hasNextLine()) {
                resposta.append(scanner.nextLine());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (resposta.toString().equals("[]")) {
            return null;
        } else {
            return new Gson().fromJson(resposta.toString(), Resposta_Cidade[].class);
        }
    }
}
