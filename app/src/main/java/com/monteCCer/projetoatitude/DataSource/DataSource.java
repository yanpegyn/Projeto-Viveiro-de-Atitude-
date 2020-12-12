package com.monteCCer.projetoatitude.DataSource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.monteCCer.projetoatitude.DataModel.AreaDataModel;
import com.monteCCer.projetoatitude.DataModel.ContatoDataModel;
import com.monteCCer.projetoatitude.DataModel.EspecieDataModel;
import com.monteCCer.projetoatitude.DataModel.EsperaDeEnvioDataModel;
import com.monteCCer.projetoatitude.DataModel.FotoDataModel;
import com.monteCCer.projetoatitude.controller.ContatoController;
import com.monteCCer.projetoatitude.controller.EspecieController;
import com.monteCCer.projetoatitude.controller.FotoController;
import com.monteCCer.projetoatitude.model.Foto;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class DataSource extends SQLiteOpenHelper {

    private static final String DB_name = "ProjetoAtitude.sqlite";
    private static final int DB_version = 1;

    protected SQLiteDatabase db;

    protected DataSource(@Nullable Context context) {
        super(context, DB_name, null, DB_version);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            // TODO: 09/08/2019 Aq que se cria tabelas
            db.execSQL(ContatoDataModel.criarTabelaContatos());
            db.execSQL(EspecieDataModel.criarTabelaEspecie());
            db.execSQL(FotoDataModel.criarTabelaFoto());
            db.execSQL(EsperaDeEnvioDataModel.criarTabelaEsperaDeEnvio());
            db.execSQL(AreaDataModel.criarTabelaArea());
        } catch (Exception e) {
            Log.e("Model", "DB---> ERRO: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int NewVersion) {

    }

    protected boolean insert(String tabela, ContentValues dados) {
        boolean salvo;
        try {
            salvo = db.insert(tabela, null, dados) > 0;
        } catch (Exception e) {
            salvo = false;
        }
        return salvo;
    }

    protected boolean deletar(String tabela, String id) {
        return db.delete(tabela, ((tabela.equals("CONTATOS") ? "EMAIL" : "ID") + "=?"), new String[]{id}) > 0;
    }

    protected boolean alterar(@NotNull String tabela, ContentValues dados) {
        String chave = (tabela.equals("CONTATOS") ? "EMAIL" : "ID");
        return db.update(tabela, dados, (chave + "=?"), new String[]{dados.getAsString(chave)}) > 0;
    }

    protected void deletarEspeciesAndFotos(Context ctx, @NotNull Object tClass) {
        if (tClass.getClass().equals(EspecieController.class) || tClass.getClass().equals(FotoController.class) || tClass.getClass().equals(ContatoController.class)) {
            db.delete(EspecieDataModel.getTabela(), null, null);
            db.delete(FotoDataModel.getTabela(), null, null);
            deleteFromDirectory(ctx);
        }
    }

    private void deleteFromDirectory(@NotNull Context ctx) {
        File dir = ctx.getDir("imgs", Context.MODE_PRIVATE);
        if (dir.exists() && dir.isDirectory()) {
            boolean delete = deleteRecursive(dir);
            Log.i("Deleted Especies/Fotos", String.valueOf(delete));
        }
    }

    protected boolean deleteRecursive(@NotNull File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            File[] childrens = fileOrDirectory.listFiles();
            if (childrens != null)
                for (File child : childrens)
                    deleteRecursive(child);
        }
        return fileOrDirectory.delete();
    }

    protected static String escreveImagens(Foto ft, Context ctx, Bitmap bitmap) {
        String nomeArquivo = null;
        try {
            File dir = ctx.getDir("imgs", Context.MODE_PRIVATE);
            boolean isDirectoryCreated;
            if (!dir.exists())
                isDirectoryCreated = dir.mkdirs();
            else isDirectoryCreated = true;
            if (isDirectoryCreated) {
                if (bitmap != null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                    byte[] bytes = stream.toByteArray();
                    nomeArquivo = dir + "/" + ft.getID() + ".jpeg";
                    //Log.i("NomeArquivo", nomeArquivo);
                    FileOutputStream fos = new FileOutputStream(nomeArquivo);
                    fos.write(bytes);
                    fos.close();
                    ft.setIMG(nomeArquivo);
                    FotoController.getInstance(ctx).alterar(ft, ctx);
                }
            } else
                Log.e("MKDIR", "Não Criou a pasta IMGs");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nomeArquivo;
    }

    protected Bitmap loadImageFromStorage(String path) {
        if (path != null && !path.isEmpty() && path.contains(".jpeg")) {
            Bitmap b = null;
            try {
                File f = new File(path);
                b = BitmapFactory.decodeStream(new FileInputStream(f));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return b;
        } else return null;
    }

    protected ArrayList<Foto> AllFotosByEspecie(String id) {
        //Log.i("IMGs", "Buscando fotos");
        boolean flag = true;
        ArrayList<Foto> fotos = new ArrayList<>();
        String sql = "SELECT * FROM " + FotoDataModel.getTabela() + " WHERE " + FotoDataModel.getEspecie() + " = '" + id + "'";
        //Log.i("IMGs: Sql:", sql);
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                Foto foto = new Foto(cursor.getString(cursor.getColumnIndex(FotoDataModel.getID())),
                        cursor.getString(cursor.getColumnIndex(FotoDataModel.getEspecie())),
                        cursor.getString(cursor.getColumnIndex(FotoDataModel.getIMG())),
                        Double.valueOf(cursor.getString(cursor.getColumnIndex(FotoDataModel.getLongitude()))),
                        Double.valueOf(cursor.getString(cursor.getColumnIndex(FotoDataModel.getLatitude()))),
                        cursor.getString(cursor.getColumnIndex(FotoDataModel.getData())));
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

    /*private Bitmap Base64ToBitmap(String base64) {
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }*/
}
