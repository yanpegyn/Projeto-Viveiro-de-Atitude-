package com.monteCCer.projetoatitude.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.monteCCer.projetoatitude.controller.FotoController;
import com.monteCCer.projetoatitude.model.Especie;
import com.monteCCer.projetoatitude.model.Foto;
import com.monteCCer.projetoatitude.R;
import com.monteCCer.projetoatitude.controller.EspecieController;
import com.monteCCer.projetoatitude.controller.ObjetosDoUsuario;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class InformacoesActivity extends AppCompatActivity {

    BroadcastReceiver broadcast_reciever;
    RecyclerView gradeDeItens;
    ArrayList<Foto> fotos;
    String id;
    boolean[] extendables = {false, false, false, false, false, false, false, false};
    Especie especie;

    // TODO: 05/11/2019 Tentar fazer essa classe voltar normal após reiniciar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacoes);
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        id = intent.getStringExtra(ObjetosDoUsuario.EXTRA_MESSAGE);

        findViewById(R.id.info_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        EspecieController especieController = EspecieController.getInstance(this);

        especie = especieController.get_especie(id);

        final ImageView Foto_Principal_Especie = findViewById(R.id.Foto_Principal_Especie);
        final TextView nome_especie = findViewById(R.id.nome_especie);
        final TextView NomeCientifico = findViewById(R.id.NomeCientifico);
        final TextView t1 = findViewById(R.id.t1);
        final ImageView p1 = findViewById(R.id.p1);
        final TextView InfoEco = findViewById(R.id.InfoEco);
        final TextView t2 = findViewById(R.id.t2);
        final ImageView p2 = findViewById(R.id.p2);
        final TextView Ocorrencia = findViewById(R.id.ocorrencia);
        final TextView t3 = findViewById(R.id.t3);
        final ImageView p3 = findViewById(R.id.p3);
        final TextView Madeira = findViewById(R.id.madeira);
        final TextView t4 = findViewById(R.id.t4);
        final ImageView p4 = findViewById(R.id.p4);
        final TextView Utilidade = findViewById(R.id.utilidade);
        final TextView t5 = findViewById(R.id.t5);
        final ImageView p5 = findViewById(R.id.p5);
        final TextView Fenologia = findViewById(R.id.fenologia);
        final TextView t6 = findViewById(R.id.t6);
        final ImageView p6 = findViewById(R.id.p6);
        final TextView Obt_Semente = findViewById(R.id.obt_semente);
        final TextView t7 = findViewById(R.id.t7);
        final ImageView p7 = findViewById(R.id.p7);
        final TextView Producao = findViewById(R.id.producao);
        final TextView t8 = findViewById(R.id.t8);
        final ImageView p8 = findViewById(R.id.p8);
        final TextView Quantidade = findViewById(R.id.quantidade);

        final ConstraintLayout l1 = findViewById(R.id.BoxEco);
        final ConstraintLayout l2 = findViewById(R.id.BoxOco);
        final ConstraintLayout l3 = findViewById(R.id.BoxMadeira);
        final ConstraintLayout l4 = findViewById(R.id.BoxUtilidade);
        final ConstraintLayout l5 = findViewById(R.id.BoxFenologia);
        final ConstraintLayout l6 = findViewById(R.id.BoxObt);
        final ConstraintLayout l7 = findViewById(R.id.BoxProd);
        final ConstraintLayout l8 = findViewById(R.id.BoxQuantidade);


        ImageButton btn_camera = findViewById(R.id.btn_camera);
        //ImageButton btn_home = findViewById(R.id.btn_home);
        //ImageButton btn_map = findViewById(R.id.btn_map);
        new Thread() {
            @Override
            public void run() {
                if (especie != null) {
                    runOnUiThread(new Thread() {
                        @Override
                        public void run() {
                            if (especie.getIcone() != null)
                                Foto_Principal_Especie.setImageBitmap(FotoController.getInstance(InformacoesActivity.this).buscarImg(especie.getIcone(), 400, 400));
                            nome_especie.setText(especie.getNome());
                            NomeCientifico.setText(especie.getNomeCientifico());
                            InfoEco.setText(especie.getInfoEco());
                            Ocorrencia.setText(especie.getOcorrencia());
                            Madeira.setText(especie.getMadeira());
                            Utilidade.setText(especie.getUtilidade());
                            Fenologia.setText(especie.getFenologia());
                            Obt_Semente.setText(especie.getOBT_Semente());
                            Producao.setText(especie.getProducao());
                            Quantidade.setText(especie.getQuantidade());
                        }
                    });

                    runOnUiThread(new Thread() {
                        @Override
                        public void run() {
                            if (InfoEco.getText().toString().equals("")) {
                                p1.setVisibility(View.GONE);
                                t1.setVisibility(View.GONE);
                                l1.setVisibility(View.GONE);
                                InfoEco.setVisibility(View.GONE);
                            }
                            if (Ocorrencia.getText().toString().equals("")) {
                                p2.setVisibility(View.GONE);
                                t2.setVisibility(View.GONE);
                                l2.setVisibility(View.GONE);
                                Ocorrencia.setVisibility(View.GONE);
                            }
                            if (Madeira.getText().toString().equals("")) {
                                p3.setVisibility(View.GONE);
                                t3.setVisibility(View.GONE);
                                l3.setVisibility(View.GONE);
                                Madeira.setVisibility(View.GONE);
                            }
                            if (Utilidade.getText().toString().equals("")) {
                                p4.setVisibility(View.GONE);
                                t4.setVisibility(View.GONE);
                                l4.setVisibility(View.GONE);
                                Utilidade.setVisibility(View.GONE);
                            }
                            if (Fenologia.getText().toString().equals("")) {
                                p5.setVisibility(View.GONE);
                                t5.setVisibility(View.GONE);
                                l5.setVisibility(View.GONE);
                                Fenologia.setVisibility(View.GONE);
                            }
                            if (Obt_Semente.getText().toString().equals("")) {
                                p6.setVisibility(View.GONE);
                                t6.setVisibility(View.GONE);
                                l6.setVisibility(View.GONE);
                                Obt_Semente.setVisibility(View.GONE);
                            }
                            if (Producao.getText().toString().equals("")) {
                                p7.setVisibility(View.GONE);
                                t7.setVisibility(View.GONE);
                                l7.setVisibility(View.GONE);
                                Producao.setVisibility(View.GONE);
                            }
                            if (Quantidade.getText().toString().equals("")) {
                                p8.setVisibility(View.GONE);
                                t8.setVisibility(View.GONE);
                                l8.setVisibility(View.GONE);
                                Quantidade.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        }.start();

        gradeDeItens = findViewById(R.id.recicler_img);

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), CameraActivity.class).putExtra(ObjetosDoUsuario.EXTRA_MESSAGE, id).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        broadcast_reciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action != null && action.equals("recreate")) {
                    recreate();
                }
            }
        };
        registerReceiver(broadcast_reciever, new IntentFilter("recreate"));
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread() {
            @Override
            public void run() {
                if (especie != null)
                    fotos = especie.getFotos();
                if (fotos == null) {
                    //Log.i("ListaIMGs", "NULL");
                    fotos = new ArrayList<>();
                }
                runOnUiThread(new Thread() {
                    @Override
                    public void run() {
                        gradeDeItens.setLayoutManager(
                                new GridLayoutManager(
                                        InformacoesActivity.this,
                                        2,
                                        GridLayoutManager.HORIZONTAL,
                                        false
                                )
                        );
                        AdaptadorFotosInfo adaptadorFotosInfo = new AdaptadorFotosInfo(InformacoesActivity.this, fotos);
                        gradeDeItens.setAdapter(adaptadorFotosInfo);
                        super.run();
                    }
                });
                super.run();
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcast_reciever);
    }

    public void extend(@NotNull View view) {
        int flag = -1;
        int txtID = -1;
        int ImgId = -1;
        switch (view.getId()) {
            case R.id.btn1:
                flag = 0;
                txtID = R.id.InfoEco;
                ImgId = R.id.p1;
                break;
            case R.id.btn2:
                flag = 1;
                txtID = R.id.ocorrencia;
                ImgId = R.id.p2;
                break;
            case R.id.btn3:
                flag = 2;
                txtID = R.id.madeira;
                ImgId = R.id.p3;
                break;
            case R.id.btn4:
                flag = 3;
                txtID = R.id.utilidade;
                ImgId = R.id.p4;
                break;
            case R.id.btn5:
                flag = 4;
                txtID = R.id.fenologia;
                ImgId = R.id.p5;
                break;
            case R.id.btn6:
                flag = 5;
                txtID = R.id.obt_semente;
                ImgId = R.id.p6;
                break;
            case R.id.btn7:
                flag = 6;
                txtID = R.id.producao;
                ImgId = R.id.p7;
                break;
            case R.id.btn8:
                flag = 7;
                txtID = R.id.quantidade;
                ImgId = R.id.p8;
                break;
        }
        if (flag != -1) {
            if (!extendables[flag]) {
                findViewById(ImgId).setBackgroundResource(R.drawable.minus);
                findViewById(txtID).setVisibility(View.VISIBLE);
                extendables[flag] = true;
            } else {
                findViewById(ImgId).setBackgroundResource(R.drawable.plus);
                findViewById(txtID).setVisibility(View.GONE);
                extendables[flag] = false;
            }
        }
    }
}
