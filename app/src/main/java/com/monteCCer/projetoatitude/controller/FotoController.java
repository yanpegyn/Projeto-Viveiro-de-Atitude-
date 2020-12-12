package com.monteCCer.projetoatitude.controller;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import com.monteCCer.projetoatitude.DataModel.FotoDataModel;
import com.monteCCer.projetoatitude.DataSource.DataSource;
import com.monteCCer.projetoatitude.model.Foto;

import org.jetbrains.annotations.NotNull;

public class FotoController extends DataSource {

    private static FotoController sInstance;

    public static synchronized FotoController getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new FotoController(context.getApplicationContext());
        }
        return sInstance;
    }

    private FotoController(@Nullable Context context) {
        super(context);
    }

    public boolean salvar(@NotNull Foto obj, Context ctx, Bitmap bitmap) {
        ContentValues dados = new ContentValues();
        dados.put(FotoDataModel.getID(), obj.getID());
        dados.put(FotoDataModel.getEspecie(), obj.getEspecie());

        if (bitmap != null)
            dados.put(FotoDataModel.getIMG(), escreveImagens(obj, ctx, bitmap));
        else dados.put(FotoDataModel.getIMG(), obj.getIMG());

        dados.put(FotoDataModel.getLongitude(), obj.getLongitude());
        dados.put(FotoDataModel.getLatitude(), obj.getLatitude());
        dados.put(FotoDataModel.getData(), obj.getData());
        dados.put(FotoDataModel.getCPF(), ObjetosDoUsuario.getCPF());
        return insert(FotoDataModel.getTabela(), dados);
    }

    public boolean deletar(@NotNull Foto obj) {
        return deletar(FotoDataModel.getTabela(), obj.getID());
    }

    public boolean alterar(@NotNull Foto obj, Context ctx) {
        ContentValues dados = new ContentValues();
        dados.put(FotoDataModel.getID(), obj.getID());
        dados.put(FotoDataModel.getEspecie(), obj.getEspecie());

        dados.put(FotoDataModel.getIMG(), obj.getIMG());
        //dados.put(FotoDataModel.getIMG(), obj.getBase64());


        dados.put(FotoDataModel.getLongitude(), obj.getLongitude());
        dados.put(FotoDataModel.getLatitude(), obj.getLatitude());
        dados.put(FotoDataModel.getData(), obj.getData());
        dados.put(FotoDataModel.getCPF(), ObjetosDoUsuario.getCPF());
        return alterar(FotoDataModel.getTabela(), dados);
    }

    public String salvarImg(Foto ft, Context ctx, Bitmap bitmap) {
        return escreveImagens(ft, ctx, bitmap);
    }

    public Bitmap buscarImg(String patch) {
        return loadImageFromStorage(patch);
    }

    public Bitmap buscarImg(String patch, int width, int height) {
        Bitmap bitmap = loadImageFromStorage(patch);
        if(bitmap != null)
            return Bitmap.createScaledBitmap(bitmap, width, height, true);
        else return null;
    }
}
