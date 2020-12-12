package com.monteCCer.projetoatitude.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.monteCCer.projetoatitude.DataDestiny.SendPics;
import com.monteCCer.projetoatitude.model.Contato;
import com.monteCCer.projetoatitude.R;
import com.monteCCer.projetoatitude.controller.ContatoController;
import com.monteCCer.projetoatitude.controller.EspecieController;
import com.monteCCer.projetoatitude.controller.ObjetosDoUsuario;

import org.jetbrains.annotations.NotNull;

public class Splash extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //apresentarTelaSplash();
        pedirPermissoes();
    }

    private void apresentarTelaSplash() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ContatoController controller = ContatoController.getInstance(getBaseContext());
                //***********************Aq já é importante *********************//

                Contato user = controller.get_UsuarioAtual();
                if (user != null && user.getCpf() != null && !user.getCpf().isEmpty()) {
                    Log.i("CRUD USERS", "Ativo como Cliente");
                    // TODO: 22/10/2019 Verificar mudança no banco de dados 
                    ObjetosDoUsuario.setCPF(user.getCpf());
                    ObjetosDoUsuario.setEmail(user.getEmail());
                    EspecieController especieController = EspecieController.getInstance(Splash.this);
                    ObjetosDoUsuario.setEspecies(especieController.getAllEspecies());
                    new SendPics(Splash.this).execute();
                    startActivity(new Intent(getBaseContext(), TelaMain.class));
                } else if (user != null) {
                    Log.i("CRUD USERS", "Ativo como Visitante");
                    startActivity(new Intent(Splash.this, WebViewer.class));
                    finish();
                } else {
                    Log.i("CRUD USERS", "Não ativo");
                    startActivity(new Intent(getBaseContext(), TelaInfo1.class));
                    //startActivity(new Intent(getBaseContext(), Login.class));
                }
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private void pedirPermissoes() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_NETWORK_STATE}, 1);
        } else
            apresentarTelaSplash();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        boolean ok = true;
        if (requestCode == 1) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, getResources().getString(R.string.PorFavorAceite), Toast.LENGTH_LONG).show();
                        ok = false;
                        break;
                    }
                }
                if (ok) {
                    apresentarTelaSplash();
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, SPLASH_TIME_OUT);
                }
            }
        }
    }
}
