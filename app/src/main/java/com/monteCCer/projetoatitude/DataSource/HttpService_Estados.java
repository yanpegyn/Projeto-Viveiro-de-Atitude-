package com.monteCCer.projetoatitude.DataSource;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.monteCCer.projetoatitude.model.Resposta_Estado;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class HttpService_Estados extends AsyncTask<Void, Void, Resposta_Estado[]> {

    @Override
    protected Resposta_Estado[] doInBackground(Void... voids) {
        StringBuilder resposta = new StringBuilder();
        try {

            URL url = new URL("https://viveiroapp.monteccer.com.br/estados.php");
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

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (resposta.toString().equals("[]")) {
            return null;
        } else {
            return new Gson().fromJson(resposta.toString(), Resposta_Estado[].class);
        }
    }
}
