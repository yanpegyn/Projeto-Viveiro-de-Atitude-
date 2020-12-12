package com.monteCCer.projetoatitude.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import androidx.annotation.Nullable;

import com.monteCCer.projetoatitude.DataModel.AreaDataModel;
import com.monteCCer.projetoatitude.DataModel.EsperaDeEnvioDataModel;
import com.monteCCer.projetoatitude.DataModel.FotoDataModel;
import com.monteCCer.projetoatitude.DataSource.DataSource;
import com.monteCCer.projetoatitude.model.Foto;
import com.monteCCer.projetoatitude.model.Resposta_Foto;

import org.jetbrains.annotations.NotNull;

public class EsperaDeEnvioController extends DataSource {

    private static EsperaDeEnvioController sInstance;

    public static synchronized EsperaDeEnvioController getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new EsperaDeEnvioController(context.getApplicationContext());
        }
        return sInstance;
    }

    private EsperaDeEnvioController(@Nullable Context context) {
        super(context);
    }

    public boolean salvar(@NotNull Foto obj) {
        ContentValues dados = new ContentValues();
        dados.put(EsperaDeEnvioDataModel.getID(), obj.getID());
        dados.put(EsperaDeEnvioDataModel.getCPF(), ObjetosDoUsuario.getCPF());
        return insert(EsperaDeEnvioDataModel.getTabela(), dados);
    }

    public boolean deletar(@NotNull Foto obj) {
        return deletar(EsperaDeEnvioDataModel.getTabela(), obj.getID());
    }

    public boolean alterar(@NotNull Foto obj) {
        ContentValues dados = new ContentValues();
        dados.put(EsperaDeEnvioDataModel.getID(), obj.getID());
        dados.put(EsperaDeEnvioDataModel.getCPF(), ObjetosDoUsuario.getCPF());
        return alterar(EsperaDeEnvioDataModel.getTabela(), dados);
    }

    public Resposta_Foto proxIMG(int tipo) {
        return getNextFotoToSend(tipo);
    }

    public void postSendRemoveIMG(String id) {
        deletar(EsperaDeEnvioDataModel.getTabela(), id);
    }

    @org.jetbrains.annotations.Nullable
    private Resposta_Foto getNextFotoToSend(int tipo) {
        // TODO: 24/09/2019 Local onde pega fotos pendentes de envio
        String sql = "SELECT * FROM " + EsperaDeEnvioDataModel.getTabela();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            Resposta_Foto foto = new Resposta_Foto();
            foto.setCpfusuario(cursor.getString(cursor.getColumnIndex(EsperaDeEnvioDataModel.getCPF())));
            String id_Ft = cursor.getString(cursor.getColumnIndex(EsperaDeEnvioDataModel.getID()));
            //Log.i("DataSource", "Próxima foto do envio = " + id_Ft);
            cursor.close();
            if (tipo == 1) {
                String sql1 = "SELECT * FROM " + FotoDataModel.getTabela() + " WHERE ID = " + id_Ft;
                Cursor cursor1 = db.rawQuery(sql1, null);
                if (cursor1.moveToFirst()) {
                    foto.setCodigo(cursor1.getString(cursor1.getColumnIndex(FotoDataModel.getID())));
                    foto.setCodespecie(cursor1.getString(cursor1.getColumnIndex(FotoDataModel.getEspecie())));
                    foto.setData(cursor1.getString(cursor1.getColumnIndex(FotoDataModel.getData())));
                    foto.setLatitude(cursor1.getString(cursor1.getColumnIndex(FotoDataModel.getLatitude())));
                    foto.setLongitude(cursor1.getString(cursor1.getColumnIndex(FotoDataModel.getLongitude())));

                    // TODO: 30/10/2019 Mandando em base64 aq
                    foto.setUrlfotoWitchBase64(loadImageFromStorage(cursor1.getString(cursor1.getColumnIndex(FotoDataModel.getIMG()))));

                    cursor1.close();
                    return foto;
                }
                cursor1.close();
            } else if (tipo == 2) {
                String sql1 = "SELECT * FROM " + AreaDataModel.getTabela() + " WHERE ID = " + id_Ft;
                Cursor cursor1 = db.rawQuery(sql1, null);
                if (cursor1.moveToFirst()) {
                    foto.setCodigo(cursor1.getString(cursor1.getColumnIndex(AreaDataModel.getID())));
                    foto.setData(cursor1.getString(cursor1.getColumnIndex(AreaDataModel.getData())));
                    foto.setLatitude(cursor1.getString(cursor1.getColumnIndex(AreaDataModel.getLatitude())));
                    foto.setLongitude(cursor1.getString(cursor1.getColumnIndex(AreaDataModel.getLongitude())));

                    // TODO: 30/10/2019 Mandando em base64 aq
                    foto.setUrlfotoWitchBase64(loadImageFromStorage(cursor1.getString(cursor1.getColumnIndex(AreaDataModel.getIMG()))));

                    cursor1.close();
                    return foto;
                }
                cursor1.close();
            }
            return null;
        }
        return null;
    }
}
