package com.monteCCer.projetoatitude.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.monteCCer.projetoatitude.model.Area;
import com.monteCCer.projetoatitude.model.Foto;
import com.monteCCer.projetoatitude.R;
import com.monteCCer.projetoatitude.controller.AreaController;
import com.monteCCer.projetoatitude.controller.ObjetosDoUsuario;

import java.util.ArrayList;

public class FotosAreas extends AppCompatActivity {

    RecyclerView gradeDeItens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotos_areas);

        (findViewById(R.id.btn_voltar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjetosDoUsuario.ObjetosTemporarios.AtualArea = null;
                finish();
            }
        });

        gradeDeItens = findViewById(R.id.recicler_img);
        Configuration configuration = getResources().getConfiguration();

        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gradeDeItens.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false));
        } else {
            gradeDeItens.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        }
    }

    @Override
    protected void onStart() {
        Area area = ObjetosDoUsuario.getAtualArea();
        ArrayList<Foto> fotos;
        if (area != null) {
            fotos = area.getFotos();
            String txt = "Imagens da Área";
            ((TextView) findViewById(R.id.titulo)).setText(txt);
        } else
            fotos = (AreaController.getInstance(getBaseContext())).getPics();
        if (fotos == null)
            fotos = new ArrayList<>();
        AdaptadorFotosAreas adaptadorFotosAreas = new AdaptadorFotosAreas(this, fotos);
        gradeDeItens.setAdapter(adaptadorFotosAreas);
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        ObjetosDoUsuario.ObjetosTemporarios.AtualArea = null;
        super.onBackPressed();
    }
}
