package com.monteCCer.projetoatitude.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.github.barteksc.pdfviewer.PDFView;
import com.monteCCer.projetoatitude.R;

public class PoliticaDePrivacidadeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_politica_de_privacidade);

        (findViewById(R.id.btn_voltar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        PDFView pdfView = findViewById(R.id.pdfView);
        pdfView.fromAsset("politica_de_privacidade.pdf").load();
    }
}
