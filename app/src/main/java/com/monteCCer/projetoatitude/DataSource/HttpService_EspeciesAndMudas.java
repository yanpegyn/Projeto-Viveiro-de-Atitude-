package com.monteCCer.projetoatitude.DataSource;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.monteCCer.projetoatitude.model.Resposta_Especie;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class HttpService_EspeciesAndMudas extends AsyncTask<Void, Void, Resposta_Especie[]> {

    private final String email;
    private final String key;

    public HttpService_EspeciesAndMudas(String email, String key) {
        this.email = email;
        this.key = key;
    }

    @Override
    protected Resposta_Especie[] doInBackground(Void... voids) {
        StringBuilder resposta = new StringBuilder();

        if ((this.email != null && this.email.length() > 5) && (this.key != null && this.key.length() >= 8)) {
            try {

                URL url = new URL("https://viveiroapp.monteccer.com.br/retorna-mudas.php?email=" + this.email);
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
                Log.i("Especies/Mudas", resposta.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (resposta.toString().equals("[]") || resposta.toString().isEmpty()) {
            return null;
        } else {
            return new Gson().fromJson(resposta.toString(), Resposta_Especie[].class);
        }
    }
}

