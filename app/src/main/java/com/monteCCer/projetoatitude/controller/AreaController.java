package com.monteCCer.projetoatitude.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import com.monteCCer.projetoatitude.DataModel.AreaDataModel;
import com.monteCCer.projetoatitude.DataSource.DataSource;
import com.monteCCer.projetoatitude.model.Foto;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

public class AreaController extends DataSource {
    private static AreaController sInstance;

    public static synchronized AreaController getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new AreaController(context.getApplicationContext());
        }
        return sInstance;
    }

    private AreaController(@Nullable Context context) {
        super(context);
    }

    public boolean salvar(@NotNull Foto obj, Context ctx, Bitmap bitmap) {
        ContentValues dados = new ContentValues();
        dados.put(AreaDataModel.getID(), obj.getID());

        if (bitmap != null)
            dados.put(AreaDataModel.getIMG(), escreveImagens(obj, ctx, bitmap));
        else dados.put(AreaDataModel.getIMG(), obj.getIMG());

        dados.put(AreaDataModel.getLongitude(), obj.getLongitude());
        dados.put(AreaDataModel.getLatitude(), obj.getLatitude());
        dados.put(AreaDataModel.getData(), obj.getData());
        dados.put(AreaDataModel.getCPF(), ObjetosDoUsuario.getCPF());
        return insert(AreaDataModel.getTabela(), dados);
    }

    public boolean deletar(@NotNull Foto obj) {
        return deletar(AreaDataModel.getTabela(), obj.getID());
    }

    public boolean alterar(@NotNull Foto obj, Context ctx) {
        ContentValues dados = new ContentValues();
        dados.put(AreaDataModel.getID(), obj.getID());

        dados.put(AreaDataModel.getIMG(), obj.getIMG());
        //dados.put(AreaDataModel.getIMG(), obj.getBase64());

        dados.put(AreaDataModel.getLongitude(), obj.getLongitude());
        dados.put(AreaDataModel.getLatitude(), obj.getLatitude());
        dados.put(AreaDataModel.getData(), obj.getData());
        dados.put(AreaDataModel.getCPF(), ObjetosDoUsuario.getCPF());
        return alterar(AreaDataModel.getTabela(), dados);
    }

    public void deletarFotosAreas(Context ctx) {
        deletarAreas(ctx);
    }

    public ArrayList<Foto> getPics() {
        return getAllPicAreas();
    }

    private boolean deletarAreas(@NotNull Context ctx) {
        File dir = ctx.getDir("imgs", Context.MODE_PRIVATE);
        if (dir.exists() && dir.isDirectory()) {
            return (1 == db.delete(AreaDataModel.getTabela(), null, null)) && deleteRecursive(dir);
        } else return true;
    }

    private ArrayList<Foto> getAllPicAreas() {
        String sql1 = "SELECT * FROM " + AreaDataModel.getTabela();
        Cursor cursor1 = db.rawQuery(sql1, null);
        ArrayList<Foto> fotos = new ArrayList<>();
        if (cursor1.moveToFirst()) {
            do {
                Foto foto = new Foto(cursor1.getString(cursor1.getColumnIndex(AreaDataModel.getID())),
                        null, null,
                        Double.valueOf(cursor1.getString(cursor1.getColumnIndex(AreaDataModel.getLongitude()))),
                        Double.valueOf(cursor1.getString(cursor1.getColumnIndex(AreaDataModel.getLatitude()))),
                        cursor1.getString(cursor1.getColumnIndex(AreaDataModel.getData())));

                foto.setIMG(cursor1.getString(cursor1.getColumnIndex(AreaDataModel.getIMG())));

                fotos.add(foto);
            } while (cursor1.moveToNext());
        }
        cursor1.close();
        return fotos;
    }

    public ArrayList<Foto> getAllAreas() {
        //Log.i("IMGs", "Buscando fotos");
        boolean flag = true;
        ArrayList<Foto> fotos = new ArrayList<>();
        String sql = "SELECT * FROM " + AreaDataModel.getTabela();
        //Log.i("IMGs: Sql:", sql);
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                Foto foto = new Foto(cursor.getString(cursor.getColumnIndex(AreaDataModel.getID())), null,
                        cursor.getString(cursor.getColumnIndex(AreaDataModel.getIMG())),
                        Double.valueOf(cursor.getString(cursor.getColumnIndex(AreaDataModel.getLongitude()))),
                        Double.valueOf(cursor.getString(cursor.getColumnIndex(AreaDataModel.getLatitude()))),
                        cursor.getString(cursor.getColumnIndex(AreaDataModel.getData())));
                fotos.add(foto);
                //Log.i(">>Foto:", foto.toString());
            } while (cursor.moveToNext());
        } else {
            //Log.i("IMGs", "Select trouxe nada");
            flag = false;
        }
        cursor.close();
        if (!flag)
            return null;
        else
            return fotos;
    }

}
