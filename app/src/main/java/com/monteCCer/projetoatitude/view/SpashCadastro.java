package com.monteCCer.projetoatitude.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;

import com.monteCCer.projetoatitude.R;

public class SpashCadastro extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_cadastro);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        apresentarTelaSplash();
    }

    private void apresentarTelaSplash() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Thread() {
                    @Override
                    public void run() {
                        findViewById(R.id.imageView).setVisibility(View.VISIBLE);
                        findViewById(R.id.imageView).setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate));
                        findViewById(R.id.imageView).postOnAnimationDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 2500);
                    }
                });
            }
        }, SPLASH_TIME_OUT);
    }
}
