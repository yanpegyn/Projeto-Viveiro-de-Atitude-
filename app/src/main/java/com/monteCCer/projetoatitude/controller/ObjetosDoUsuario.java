package com.monteCCer.projetoatitude.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import com.monteCCer.projetoatitude.R;
import com.monteCCer.projetoatitude.model.Area;
import com.monteCCer.projetoatitude.model.Especie;
import com.monteCCer.projetoatitude.model.Foto;
import com.monteCCer.projetoatitude.model.Resposta_Especie;
import com.monteCCer.projetoatitude.model.Resposta_Foto;
import com.monteCCer.projetoatitude.view.Login;
import com.monteCCer.projetoatitude.view.TelaMain;

import org.jetbrains.annotations.Contract;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class ObjetosDoUsuario {
    public static final String EXTRA_MESSAGE = "com.example.projetoatitude.MESSAGE";
    private static String CPF;
    private static String Email;
    private static ArrayList<Especie> Especies;
    public static final Integer MinLengthOfPass = 8;
    public static final String SenhaPadrao = "atitude2019";


    public static class ObjetosTemporarios {
        public static Area AtualArea;

        public static class listagemDasFotos {
            public static ArrayList<Foto> fotos;
            public static Integer index;
        }

    }

    public static void setListagemDasFotos(List<Foto> fotos, Integer index) {
        ObjetosTemporarios.listagemDasFotos.fotos = (ArrayList<Foto>) fotos;
        ObjetosTemporarios.listagemDasFotos.index = index;
    }

    @Contract(pure = true)
    public static ArrayList<Foto> getListagemDasFotosFotos() {
        return ObjetosTemporarios.listagemDasFotos.fotos;
    }

    @Contract(pure = true)
    public static Integer getListagemDasFotosIndex() {
        return ObjetosTemporarios.listagemDasFotos.index;
    }

    public static void setAtualArea(Area area) {
        ObjetosTemporarios.AtualArea = area;
    }

    @Contract(pure = true)
    public static Area getAtualArea() {
        return ObjetosTemporarios.AtualArea;
    }

    public static void clearAll() {
        CPF = null;
        Especies = null;
        Email = null;
    }

    @Contract(pure = true)
    public static boolean anyFail() {
        return !(CPF != null && Especies != null && Email != null);
    }

    public static boolean addPicToEspecie(String id, Foto ft) {
        ArrayList<Foto> fts = null;
        for (Especie i : Especies) {
            if (i.getID().equals(id)) {
                fts = i.getFotos();
                if (fts == null) {
                    fts = new ArrayList<>();
                }
                fts.add(ft);
                i.setFotos(fts);
                break;
            }
        }
        return fts != null;
    }

    public static ArrayList<Especie> getEspecies() {
        if (Especies == null) {
            Especies = new ArrayList<>();
        }
        return Especies;
    }

    public static void setEspecies(ArrayList<Especie> especies) {
        Especies = especies;
    }

    private static int ct = 0;

    private static void iniciarTelaMain2Steps(Activity ctx) {
        ct++;
        if (ct == 2) {
            Log.i("Iniciar Main", "Contador finalizou");
            ct = 0;
            ctx.finish();
            Intent intent = new Intent(ctx, TelaMain.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ctx.startActivity(intent);
        }
    }

    public static void resetCt() {
        ct = 0;
    }

    public static void setEspeciesArray(final Resposta_Especie[] especies, final Activity ctx, final String ModoDeCaptura) {
        ///Modo de Captura deve ser "URL" ou "Base64"
        if (Especies == null) {
            Especies = new ArrayList<>();
        } else {
            Especies.clear();
        }
        final EspecieController especieController = EspecieController.getInstance(ctx);
        final FotoController fotoController = FotoController.getInstance(ctx);
        especieController.DeletarEspeciesAndFotos(ctx);
        if (especies != null) {
            if (especies.length > 0) {
                Log.i("Especie", "entrou");
                // TODO: 20/09/2019 Thread de carregamento
                new Thread() {
                    public void run() {
                        try {
                            for (Resposta_Especie i : especies) {
                                Especie especie = null;
                                if (i.getCodigo() != null) {
                                    if (ModoDeCaptura.equals("URL")) {
                                        final URL url;
                                        Bitmap img;
                                        String imgUrl = null;
                                        if (i.getFotoprincipal() != null && !i.getFotoprincipal().isEmpty()) {
                                            if (!i.getFotoprincipal().contains("/"))
                                                url = new URL("https://viveiroapp.monteccer.com.br/fotos_viveiro/" + i.getCodigo() + "/" + i.getFotoprincipal());
                                            else
                                                url = new URL("https://viveiroapp.monteccer.com.br/" + i.getFotoprincipal());

                                            Log.i("FotoPrincipal", url.toString());
                                            try {
                                                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                                                InputStream inputStream = httpURLConnection.getInputStream();
                                                img = BitmapFactory.decodeStream(inputStream);
                                                if (img != null) {
                                                    imgUrl = fotoController.salvarImg(
                                                            new Foto(
                                                                    "-" + i.getCodigo(),
                                                                    null, null,
                                                                    null, null,
                                                                    null
                                                            ), ctx, img
                                                    );
                                                }
                                            } catch (FileNotFoundException w) {
                                                Log.w("Foto da Espécie", w);
                                                //img = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.plant);
                                            }
                                        } else {
                                            Log.i("FotoPrincipal", "isNull");
                                        }
                                        especie = new Especie(i.getCodigo(), i.getNome_popular(), i.getNome_cientifico(), imgUrl);
                                        especie.setInfoEco(i.getInfo_ecologica());
                                        especie.setOcorrencia(i.getOcorrencia());
                                        especie.setMadeira(i.getMadeira());
                                        especie.setUtilidade(i.getUtilidade());
                                        especie.setFenologia(i.getFenologia());
                                        especie.setOBT_Semente(i.getObt_semente());
                                        especie.setProducao(i.getProducao());
                                        especie.setQuantidade(i.getQuantidade());
                                    }/* else if (ModoDeCaptura.equals("Base64")) {
                                        especie = new Especie(i.getCodigo(), i.getNome_popular(), i.getNome_cientifico(), null);
                                        especie.setInfoEco(i.getInfo_ecologica());
                                        especie.setOcorrencia(i.getOcorrencia());
                                        especie.setMadeira(i.getMadeira());
                                        especie.setUtilidade(i.getUtilidade());
                                        especie.setFenologia(i.getFenologia());
                                        especie.setOBT_Semente(i.getObt_semente());
                                        especie.setProducao(i.getProducao());
                                        especie.setQuantidade(i.getQuantidade());
                                        especie.setIcone(i.getFotoprincipal());
                                    }*/
                                }
                                // TODO: 11/09/19 Buscar foto do banco
                                if (especie != null) {
                                    ArrayList<Foto> fotos = new ArrayList<>();
                                    for (Resposta_Foto j : i.getFotos()) {
                                        Foto ft = null;
                                        Bitmap img = null;
                                        if (ModoDeCaptura.equals("URL")) {
                                            // TODO: 09/10/19 Ver Local onde a imagem fica salva
                                            final URL url;
                                            if (!j.getUrlfoto().contains("/"))
                                                url = new URL("https://viveiroapp.monteccer.com.br/fotos_viveiro/" + i.getCodigo() + "/" + j.getUrlfoto());
                                            else
                                                url = new URL("https://viveiroapp.monteccer.com.br/" + j.getUrlfoto());
                                            //Log.i("URL", url.toString());
                                            try {
                                                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                                                InputStream inputStream = httpURLConnection.getInputStream();
                                                img = BitmapFactory.decodeStream(inputStream);
                                            } catch (FileNotFoundException w) {
                                                Log.w("Fotos Espécie", w);
                                                img = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.plant);
                                            }
                                            ft = new Foto(
                                                    j.getCodigo(),
                                                    j.getCodespecie(), null,
                                                    Double.valueOf(j.getLongitude()),
                                                    Double.valueOf(j.getLatitude()), j.getData());
                                            //Log.i("Base64", ft.getBase64());
                                        }/* else {
                                            ft = new Foto(j.getCodigo(), j.getCodespecie(), null, Double.valueOf(j.getLongitude()), Double.valueOf(j.getLatitude()), j.getData());
                                            // TODO: 30/10/2019 Caso mudar pra base mude aq
                                            //ft.setIMG_base64(j.getUrlfoto());
                                        }*/
                                        fotos.add(ft);
                                        fotoController.salvar(ft, ctx, img);
                                    }
                                    especie.setFotos(fotos);
                                    //Log.i("Size", String.valueOf(especie.getFotos().size()));
                                    Especies.add(especie);
                                    especieController.salvar(especie, ctx);
                                }
                            }
                            if (especieController.getAllEspecies() != null && !especieController.getAllEspecies().isEmpty()) {
                                iniciarTelaMain2Steps(ctx);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }.start();
            } else naoLogar(ctx);
        } else naoLogar(ctx);
    }

    private static void naoLogar(final Activity ctx) {
        if (ObjetosDoUsuario.getEspecies().isEmpty()) {
            ctx.runOnUiThread(new Thread() {
                @Override
                public void run() {
                    Toast.makeText(ctx, ctx.getResources().getString(R.string.nenhumaEspecie), Toast.LENGTH_SHORT).show();
                    ((Login) ctx).fail();
                }
            });
            ContatoController.getInstance(ctx).deletar(ContatoController.getInstance(ctx).get_UsuarioAtual());
            ObjetosDoUsuario.resetCt();
        }
    }

    public static void setAreasArray(final Resposta_Foto[] fts, final Activity ctx, final String ModoDeCaptura) {
        ///Modo de Captura deve ser "URL" ou "Base64"
        final AreaController areaController = AreaController.getInstance(ctx);
        areaController.deletarFotosAreas(ctx);
        if (fts != null) {
            Log.i("FTS", String.valueOf(fts.length));
            if (fts.length > 0) {
                // TODO: 20/09/2019 Thread de carregamento
                new Thread() {
                    public void run() {
                        try {
                            for (Resposta_Foto i : fts) {
                                if (i.getCodigo() != null && i.getUrlfoto() != null) {
                                    if (ModoDeCaptura.equals("URL")) {
                                        final URL url = new URL("https://viveiroapp.monteccer.com.br/" + i.getUrlfoto().replace("\\", ""));
                                        //Log.i("Area url", url.toString());
                                        Bitmap img;
                                        try {
                                            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                                            InputStream inputStream = httpURLConnection.getInputStream();
                                            img = BitmapFactory.decodeStream(inputStream);
                                        } catch (FileNotFoundException w) {
                                            Log.w("Fotos Areas", w);
                                            img = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.plant);
                                        }
                                        Foto foto = new Foto(
                                                i.getCodigo(), null,
                                                null,
                                                Double.valueOf(i.getLongitude()),
                                                Double.valueOf(i.getLatitude()),
                                                i.getData());
                                        areaController.salvar(foto, ctx, img);
                                    }/* else if (ModoDeCaptura.equals("Base64")) {
                                        // TODO: 25/10/2019 Falta o modo Base64
                                    }*/
                                }
                            }
                            //ctx.startActivity(new Intent(ctx, TelaMain.class));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        iniciarTelaMain2Steps(ctx);
                    }
                }.start();
            }
        } else {
            Log.i("Area", "IsNull");
            iniciarTelaMain2Steps(ctx);
        }
    }

    public static String getCPF() {
        return CPF;
    }

    public static void setCPF(String CPF) {
        ObjetosDoUsuario.CPF = CPF;
    }

    public static String getEmail() {
        return Email;
    }

    public static void setEmail(String email) {
        Email = email;
    }

    public static ArrayList<Especie> getEspeciesLikeIdOrName(String id) {
        if (Especies == null) {
            Especies = new ArrayList<>();
        }
        ArrayList<Especie> res = new ArrayList<>();
        for (Especie i : Especies) {
            if (i.getID().contains(id) || i.getNome().toUpperCase().contains(id.toUpperCase()) || i.getNomeCientifico().toUpperCase().contains(id.toUpperCase())) {
                res.add(i);
            }
        }
        return res;
    }

    private static boolean blockNet = false;

    public static boolean hasInternet(Context ctx) {
        if (!blockNet) {
            ConnectivityManager manager = (ConnectivityManager) ctx.getSystemService(CONNECTIVITY_SERVICE);
            if (manager != null) {
                boolean is3g = Objects.requireNonNull(manager.getNetworkInfo(
                        ConnectivityManager.TYPE_MOBILE)).isConnectedOrConnecting();
                boolean isWifi = Objects.requireNonNull(manager.getNetworkInfo(
                        ConnectivityManager.TYPE_WIFI)).isConnectedOrConnecting();
                Log.i("ObjUser", "Via Dados Móveis: " + is3g + ", Via Wifi: " + isWifi);
                return is3g || isWifi;
            }
        }
        return false;
    }

    public static void internetNotOk(final Activity activity) {
        blockNet = true;
        if (activity != null) {
            activity.runOnUiThread(new Thread() {
                @Override
                public void run() {
                    Toast.makeText(activity, activity.getResources().getString(R.string.internetInstavel), Toast.LENGTH_LONG).show();
                }
            });
        }
        Log.i("ObjetosDoUsuário", "Modo Offline Forçado");
    }
}
