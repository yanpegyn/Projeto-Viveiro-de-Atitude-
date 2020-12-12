package com.monteCCer.projetoatitude.DataSource;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.monteCCer.projetoatitude.model.Resposta_Foto;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class HttpService_Areas extends AsyncTask<Void, Void, Resposta_Foto[]> {

    private final String email;

    public HttpService_Areas(String email) {
        this.email = email;
    }

    @Override
    protected Resposta_Foto[] doInBackground(Void... voids) {
        StringBuilder resposta = new StringBuilder();

        if ((this.email != null && this.email.length() > 5)) {
            try {

                URL url = new URL("https://viveiroapp.monteccer.com.br/busca-areas.php?email=" + this.email);
                //Log.i("SITE: ", url.toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);
                connection.setConnectTimeout(5000);
                connection.connect();

                Scanner scanner = new Scanner(url.openStream());
                while (scanner.hasNextLine()) {
                    resposta.append(scanner.nextLine());
                }
                Log.i("URL get", url.toString());
                Log.i("Area", resposta.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (resposta.toString().equals("[]")) {
            return null;
        } else {
            return new Gson().fromJson(resposta.toString(), Resposta_Foto[].class);
        }
    }
}
