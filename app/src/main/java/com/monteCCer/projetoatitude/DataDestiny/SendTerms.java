package com.monteCCer.projetoatitude.DataDestiny;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class SendTerms extends AsyncTask<Void, Void, Integer> {

    private String email;
    private String senha;

    public SendTerms(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        int CallBack = 2;
        Uri.Builder builder = new Uri.Builder();
        builder.appendQueryParameter("email", email);
        builder.appendQueryParameter("senha", senha);
        try {
            URL url = new URL("https://viveiroapp.monteccer.com.br/aceite-termos.php");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestProperty("charset", String.valueOf(StandardCharsets.UTF_8));
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.connect();
            String query = builder.build().getEncodedQuery();
            if (query != null) {
                //Log.i("query", query);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                bufferedWriter.write(query);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                StringBuilder resposta = new StringBuilder();
                Scanner scanner = new Scanner(inputStream);
                while (scanner.hasNextLine()) {
                    resposta.append(scanner.nextLine());
                }
                Log.i("Resposta Termos", resposta.toString());
                CallBack = Integer.parseInt(resposta.toString());
            } else {
                Log.e("Terms", "Querry null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return CallBack;
    }
}
