package com.monteCCer.projetoatitude.DataSource;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.monteCCer.projetoatitude.model.Resposta_Login;
import com.monteCCer.projetoatitude.controller.ObjetosDoUsuario;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class HttpService_Login extends AsyncTask<Void, Void, Resposta_Login> {

    private final String email;
    private final String key;

    public HttpService_Login(String email, String key) {
        this.key = key;
        this.email = email;
    }

    @Override
    protected Resposta_Login doInBackground(Void... voids) {
        StringBuilder resposta = new StringBuilder();

        if ((this.email != null && !this.email.isEmpty()) && (this.email.contains("@") && this.email.contains(".")) && (this.key != null && this.key.length() >= ObjetosDoUsuario.MinLengthOfPass)) {
            try {
                URL url = new URL("https://viveiroapp.monteccer.com.br/logar.php?email=" + this.email + "&pwd=" + this.key);
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
                Log.i("RespostaLogin", resposta.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (resposta.length() == 0 || resposta.toString().equals("-1") || resposta.toString().equals("1")) {
            return null;
        } else {
            //Log.i("Resposta_Login: ", resposta.toString());
            Resposta_Login obj_RespostaLogin = new Gson().fromJson(resposta.toString(), Resposta_Login.class);
            return obj_RespostaLogin;
        }
    }
}

