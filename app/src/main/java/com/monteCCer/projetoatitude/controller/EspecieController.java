package com.monteCCer.projetoatitude.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import androidx.annotation.Nullable;

import com.monteCCer.projetoatitude.DataModel.EspecieDataModel;
import com.monteCCer.projetoatitude.DataSource.DataSource;
import com.monteCCer.projetoatitude.model.Especie;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EspecieController extends DataSource {

    private static EspecieController sInstance;

    public static synchronized EspecieController getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new EspecieController(context.getApplicationContext());
        }
        return sInstance;
    }

    private EspecieController(@Nullable Context context) {
        super(context);
    }

    private ContentValues EspecieToValues(@NotNull Especie obj, Context ctx) {
        ContentValues dados = new ContentValues();

        dados.put(EspecieDataModel.getID(), obj.getID());
        dados.put(EspecieDataModel.getNome(), obj.getNome());
        dados.put(EspecieDataModel.getNomeCientifico(), obj.getNomeCientifico());
        dados.put(EspecieDataModel.getInfoEco(), obj.getInfoEco());
        dados.put(EspecieDataModel.getOcorrencia(), obj.getOcorrencia());
        dados.put(EspecieDataModel.getMadeira(), obj.getMadeira());
        dados.put(EspecieDataModel.getUtilidade(), obj.getUtilidade());
        dados.put(EspecieDataModel.getFenologia(), obj.getFenologia());
        dados.put(EspecieDataModel.getOBT_Semente(), obj.getOBT_Semente());
        dados.put(EspecieDataModel.getProducao(), obj.getProducao());
        dados.put(EspecieDataModel.getQuantidade(), obj.getQuantidade());

        dados.put(EspecieDataModel.getIcone(), obj.getIcone());

        dados.put(EspecieDataModel.getCPF(), ObjetosDoUsuario.getCPF());
        return dados;
    }

    public boolean salvar(Especie obj, Context ctx) {
        return insert(EspecieDataModel.getTabela(), EspecieToValues(obj, ctx));
    }

    public boolean deletar(@NotNull Especie obj) {
        return deletar(EspecieDataModel.getTabela(), obj.getID());
    }

    void DeletarEspeciesAndFotos(Context ctx) {
        deletarEspeciesAndFotos(ctx, this);
    }

    public boolean alterar(Especie obj, Context ctx) {
        return alterar(EspecieDataModel.getTabela(), EspecieToValues(obj, ctx));
    }

    public ArrayList<Especie> getAllEspecies() {
        return AllEspecies();
    }

    public Especie get_especie(String ID) {
        return umaEspecie(ID);
    }

    @org.jetbrains.annotations.Nullable
    private Especie umaEspecie(String ID) {
        boolean flag = true;
        String sql = "SELECT * FROM " + EspecieDataModel.getTabela() + " WHERE ID = '" + ID + "'" + " AND CPF = '" + ObjetosDoUsuario.getCPF() + "'";
        Cursor cursor = db.rawQuery(sql, null);
        Especie especie = new Especie(null, null, null, null);
        if (cursor.moveToFirst()) {
            especie.setID(cursor.getString(cursor.getColumnIndex(EspecieDataModel.getID())));
            especie.setNome(cursor.getString(cursor.getColumnIndex(EspecieDataModel.getNome())));
            especie.setNomeCientifico(cursor.getString(cursor.getColumnIndex(EspecieDataModel.getNomeCientifico())));
            especie.setInfoEco(cursor.getString(cursor.getColumnIndex(EspecieDataModel.getInfoEco())));
            especie.setOcorrencia(cursor.getString(cursor.getColumnIndex(EspecieDataModel.getOcorrencia())));
            especie.setMadeira(cursor.getString(cursor.getColumnIndex(EspecieDataModel.getMadeira())));
            especie.setUtilidade(cursor.getString(cursor.getColumnIndex(EspecieDataModel.getUtilidade())));
            especie.setFenologia(cursor.getString(cursor.getColumnIndex(EspecieDataModel.getFenologia())));
            especie.setOBT_Semente(cursor.getString(cursor.getColumnIndex(EspecieDataModel.getOBT_Semente())));
            especie.setProducao(cursor.getString(cursor.getColumnIndex(EspecieDataModel.getProducao())));
            especie.setQuantidade(cursor.getString(cursor.getColumnIndex(EspecieDataModel.getQuantidade())));
            especie.setIcone(cursor.getString(cursor.getColumnIndex(EspecieDataModel.getIcone())));
            especie.setFotos(AllFotosByEspecie(ID));
            //Log.i(">>Especie:",especie.toString());
        } else {
            flag = false;
        }
        cursor.close();
        if (!flag)
            return null;
        else
            return especie;
    }

    @org.jetbrains.annotations.Nullable
    private ArrayList<Especie> AllEspecies() {
        boolean flag = true;
        ArrayList<Especie> especies = new ArrayList<>();
        String sql = "SELECT * FROM " + EspecieDataModel.getTabela() + " WHERE CPF = '" + ObjetosDoUsuario.getCPF() + "'";
        Cursor cursor = db.rawQuery(sql, null);
        String especieID;
        if (cursor.moveToFirst()) {
            do {
                especieID = cursor.getString(cursor.getColumnIndex(EspecieDataModel.getID()));
                Especie especie = new Especie(null, null, null, null);
                especie.setID(cursor.getString(cursor.getColumnIndex(EspecieDataModel.getID())));
                especie.setNome(cursor.getString(cursor.getColumnIndex(EspecieDataModel.getNome())));
                especie.setNomeCientifico(cursor.getString(cursor.getColumnIndex(EspecieDataModel.getNomeCientifico())));
                especie.setInfoEco(cursor.getString(cursor.getColumnIndex(EspecieDataModel.getInfoEco())));
                especie.setOcorrencia(cursor.getString(cursor.getColumnIndex(EspecieDataModel.getOcorrencia())));
                especie.setMadeira(cursor.getString(cursor.getColumnIndex(EspecieDataModel.getMadeira())));
                especie.setUtilidade(cursor.getString(cursor.getColumnIndex(EspecieDataModel.getUtilidade())));
                especie.setFenologia(cursor.getString(cursor.getColumnIndex(EspecieDataModel.getFenologia())));
                especie.setOBT_Semente(cursor.getString(cursor.getColumnIndex(EspecieDataModel.getOBT_Semente())));
                especie.setProducao(cursor.getString(cursor.getColumnIndex(EspecieDataModel.getProducao())));
                especie.setQuantidade(cursor.getString(cursor.getColumnIndex(EspecieDataModel.getQuantidade())));
                especie.setIcone(cursor.getString(cursor.getColumnIndex(EspecieDataModel.getIcone())));
                especie.setFotos(AllFotosByEspecie(especieID));
                //Log.i(">>Especie:", especie.toString());
                especies.add(especie);
            } while (cursor.moveToNext());
        } else {
            flag = false;
        }
        cursor.close();
        if (!flag)
            return null;
        else
            return especies;
    }
}
