package com.monteCCer.projetoatitude.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import androidx.annotation.Nullable;

import com.monteCCer.projetoatitude.DataModel.ContatoDataModel;
import com.monteCCer.projetoatitude.DataSource.DataSource;
import com.monteCCer.projetoatitude.model.Contato;

import java.util.ArrayList;
import java.util.List;

public class ContatoController extends DataSource {

    private static ContatoController sInstance;

    public static synchronized ContatoController getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ContatoController(context.getApplicationContext());
        }
        return sInstance;
    }

    private ContatoController(@Nullable Context context) {
        super(context);
    }

    public boolean salvar(Contato obj) {
        ContentValues dados = new ContentValues();
        dados.put(ContatoDataModel.getCpf(), obj.getCpf());
        dados.put(ContatoDataModel.getEmail(), obj.getEmail());
        dados.put(ContatoDataModel.getSenha(), obj.getSenha());
        dados.put(ContatoDataModel.getLogado(), obj.getLogado());
        dados.put(ContatoDataModel.getNome(), obj.getNome());
        return insert(ContatoDataModel.getTabela(), dados);
    }

    public boolean deletar(Contato obj) {
        return deletar(ContatoDataModel.getTabela(), obj.getEmail());
    }

    public boolean alterar(Contato obj) {
        ContentValues dados = new ContentValues();
        dados.put(ContatoDataModel.getCpf(), obj.getCpf());
        dados.put(ContatoDataModel.getEmail(), obj.getEmail());
        dados.put(ContatoDataModel.getSenha(), obj.getSenha());
        dados.put(ContatoDataModel.getLogado(), obj.getLogado());
        dados.put(ContatoDataModel.getNome(), obj.getNome());
        return alterar(ContatoDataModel.getTabela(), dados);
    }

    public void logout() {
        sair();
    }

    public void logoutVisitante() {
        deletar(get_UsuarioAtual());
    }

    public Contato get_contato(String emailOrCpf, String senha) {
        Contato contato = umContato(emailOrCpf);
        if (contato == null)
            contato = umContatobyCpf(emailOrCpf); ///Caso o parâmetro seja um cpf
        if (contato != null) {
            if (contato.getSenha().equals(senha)) {
                return contato;
            }
        }
        return null;
    }

    public String get_key_of_user(String cpf) {
        return umContatobyCpf(cpf).getSenha();
    }

    public Contato get_UsuarioAtual() {
        return userAtual();
    }

    public List<Contato> listar() {
        return getAllContato();
    }

    protected Contato umContato(String email) {
        boolean flag = true;
        String sql = "SELECT * FROM " + ContatoDataModel.getTabela() + " WHERE EMAIL = '" + email + "'";
        Cursor cursor = db.rawQuery(sql, null);
        Contato user = new Contato();
        if (cursor.moveToFirst()) {
            user.setCpf(cursor.getString(cursor.getColumnIndex(ContatoDataModel.getCpf())));
            user.setEmail(cursor.getString(cursor.getColumnIndex(ContatoDataModel.getEmail())));
            user.setNome(cursor.getString(cursor.getColumnIndex(ContatoDataModel.getNome())));
            user.setSenha(cursor.getString(cursor.getColumnIndex(ContatoDataModel.getSenha())));
            user.setLogado(cursor.getString(cursor.getColumnIndex(ContatoDataModel.getLogado())));
        } else {
            flag = false;
        }
        cursor.close();
        if (!flag)
            return null;
        else
            return user;
    }

    protected Contato umContatobyCpf(String cpf) {
        boolean flag = true;
        String sql = "SELECT * FROM " + ContatoDataModel.getTabela() + " WHERE CPF = '" + cpf + "'";
        Cursor cursor = db.rawQuery(sql, null);
        Contato contato = null;
        if (cursor.moveToFirst()) {
            contato = umContato(cursor.getString(cursor.getColumnIndex(ContatoDataModel.getEmail())));
        } else {
            flag = false;
        }
        cursor.close();
        if (!flag)
            return null;
        else
            return contato;
    }

    protected void sair() {
        Contato saindo = userAtual();
        saindo.setLogado("False");
        ContentValues dados = new ContentValues();
        dados.put(ContatoDataModel.getCpf(), saindo.getCpf());
        dados.put(ContatoDataModel.getNome(), saindo.getNome());
        dados.put(ContatoDataModel.getSenha(), saindo.getSenha());
        dados.put(ContatoDataModel.getLogado(), saindo.getLogado());
        dados.put(ContatoDataModel.getEmail(), saindo.getEmail());
        alterar(ContatoDataModel.getTabela(), dados);
    }

    protected Contato userAtual() {
        boolean flag = true;
        String sql = "SELECT * FROM " + ContatoDataModel.getTabela() + " WHERE LOGADO = 'True'";
        Cursor cursor = db.rawQuery(sql, null);
        Contato user = new Contato();
        if (cursor.moveToFirst()) {
            user.setCpf(cursor.getString(cursor.getColumnIndex(ContatoDataModel.getCpf())));
            user.setEmail(cursor.getString(cursor.getColumnIndex(ContatoDataModel.getEmail())));
            user.setNome(cursor.getString(cursor.getColumnIndex(ContatoDataModel.getNome())));
            user.setSenha(cursor.getString(cursor.getColumnIndex(ContatoDataModel.getSenha())));
            user.setLogado(cursor.getString(cursor.getColumnIndex(ContatoDataModel.getLogado())));
        } else {
            flag = false;
        }
        //Log.i("USER: ", user.getCpf() + "; " + user.getNome() + "; " + user.getSenha());
        cursor.close();
        if (!flag)
            return null;
        else
            return user;
    }

    protected List<Contato> getAllContato() {
        List<Contato> users = new ArrayList<>();
        //Sql de consulta
        String sql = "SELECT * FROM " + ContatoDataModel.getTabela();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                Contato user = new Contato();
                user.setCpf(cursor.getString(cursor.getColumnIndex(ContatoDataModel.getCpf())));
                user.setEmail(cursor.getString(cursor.getColumnIndex(ContatoDataModel.getEmail())));
                user.setNome(cursor.getString(cursor.getColumnIndex(ContatoDataModel.getNome())));
                user.setSenha(cursor.getString(cursor.getColumnIndex(ContatoDataModel.getSenha())));
                user.setLogado(cursor.getString(cursor.getColumnIndex(ContatoDataModel.getLogado())));
                users.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return users;
    }

    protected String emailToCpf(String email) {
        String sql = "SELECT * FROM " + ContatoDataModel.getTabela() + " WHERE EMAIL = '" + email + "'";
        Cursor cursor = db.rawQuery(sql, null);
        String cpf = null;
        if (cursor.moveToFirst()) {
            cpf = cursor.getString(cursor.getColumnIndex(ContatoDataModel.getCpf()));
        }
        cursor.close();
        return cpf;
    }

    public void clearAllTables(Context ctx) {
        db.delete(ContatoDataModel.getTabela(), null, null);
        deletarEspeciesAndFotos(ctx, this);
        AreaController.getInstance(ctx).deletarFotosAreas(ctx);
    }

}