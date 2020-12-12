package com.monteCCer.projetoatitude.DataDestiny;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.monteCCer.projetoatitude.model.Resposta_Foto;
import com.monteCCer.projetoatitude.controller.ContatoController;
import com.monteCCer.projetoatitude.controller.EsperaDeEnvioController;
import com.monteCCer.projetoatitude.controller.ObjetosDoUsuario;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class SendPics extends AsyncTask<Void, Void, Void> {

    private WeakReference<Activity> ctx;
    private boolean terminate;

    public SendPics(Activity ctx) {
        // TODO: 24/09/2019 Envio de Imagens
        this.ctx = new WeakReference<>(ctx);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        while (!terminate && ObjetosDoUsuario.hasInternet(this.ctx.get())) {
            Log.i("SendPics", "Enviando Fotos");
            Resposta_Foto fotoA = (EsperaDeEnvioController.getInstance(this.ctx.get())).proxIMG(1);
            if (fotoA == null)
                fotoA = (EsperaDeEnvioController.getInstance(this.ctx.get())).proxIMG(2);
            if (fotoA != null) {
                final Resposta_Foto foto = fotoA;
                final Runnable stuffToDo = new Thread() {
                    @Override
                    public void run() {
                        //Log.i("Enviando", foto.toString());
                        Uri.Builder builder = new Uri.Builder();
                        //builder.appendQueryParameter("cpf", foto.getCpfusuario());
                        String key = ContatoController.getInstance((ctx.get())).get_key_of_user(foto.getCpfusuario());
                        builder.appendQueryParameter("email", ContatoController.getInstance(ctx.get()).get_contato(foto.getCpfusuario(), key).getEmail());
                        builder.appendQueryParameter("senha", key);
                        builder.appendQueryParameter("especie", foto.getCodespecie());
                        builder.appendQueryParameter("latitude", foto.getLatitude());
                        builder.appendQueryParameter("longitude", foto.getLongitude());
                        builder.appendQueryParameter("data", foto.getData());
                        builder.appendQueryParameter("foto", "\"data:image/jpeg;base64,\"" + foto.getUrlfoto());
                        builder.appendQueryParameter("tipofoto", foto.getCodespecie() == null ? "2" : "1");
                        try {
                            URL url = new URL("https://viveiroapp.monteccer.com.br/salvar-infos.php");
                            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                            httpURLConnection.setRequestMethod("POST");
                            httpURLConnection.setConnectTimeout(5000);
                            httpURLConnection.setReadTimeout(5000);
                            //httpURLConnection.setRequestProperty("charset", "UTF8");
                            httpURLConnection.setRequestProperty("charset", String.valueOf(StandardCharsets.UTF_8));
                            //httpURLConnection.setRequestProperty("Content-type", "application/json");
                            //httpURLConnection.setRequestProperty("Accept", "application/json");
                            httpURLConnection.setDoInput(true);
                            httpURLConnection.setDoOutput(true);
                            httpURLConnection.connect();
                            String query = builder.build().getEncodedQuery();
                            Log.i("SendPics", "Conectou");
                            if (query != null) {
                                OutputStream outputStream = httpURLConnection.getOutputStream();
                                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                                bufferedWriter.write(query);
                                bufferedWriter.flush();
                                bufferedWriter.close();
                                outputStream.close();
                                httpURLConnection.connect();
                                Log.i("SendPics", "Conectou2");
                                InputStream inputStream = httpURLConnection.getInputStream();
                                final StringBuilder resposta = new StringBuilder();
                                final Scanner scanner = new Scanner(inputStream);
                                while (scanner.hasNextLine()) {
                                    resposta.append(scanner.nextLine());
                                }
                                Log.i("Resposta Envio", resposta.toString());
                                ///Remove a foto da fila
                                (EsperaDeEnvioController.getInstance(ctx.get())).postSendRemoveIMG(foto.getCodigo());
                            } else {
                                Log.e("Send", "Query null");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            ObjetosDoUsuario.internetNotOk(ctx.get());
                        }
                    }
                };

                final ExecutorService executor = Executors.newSingleThreadExecutor();
                final Future future = executor.submit(stuffToDo);
                executor.shutdown(); // This does not cancel the already-scheduled task.

                try {
                    future.get(15, TimeUnit.SECONDS);
                } catch (InterruptedException | ExecutionException ie) {
                    /* Handle the interruption. Or ignore it. */
                } catch (TimeoutException te) {
                    /* Handle the timeout. Or ignore it. */
                    ObjetosDoUsuario.internetNotOk(ctx.get());
                }
                if (!executor.isTerminated())
                    executor.shutdownNow(); // If you want to stop the code that hasn't finished.
            } else {
                Log.i("SendPics", "Fim da Lista de Envios");
                this.terminate = true;
            }
        }
        return null;
    }

}
