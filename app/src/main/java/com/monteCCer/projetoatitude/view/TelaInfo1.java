package com.monteCCer.projetoatitude.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.monteCCer.projetoatitude.R;

public class TelaInfo1 extends AppCompatActivity {

    private ImageView next;
    private ImageView jump;
    private ImageView ic_1;
    private ImageView ic_2;
    private ImageView ic_3;
    private ImageView btn_gologin;
    private Boolean loading = false;
    int cont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_info1);

        next = findViewById(R.id.next);
        jump = findViewById(R.id.jump);
        ic_1 = findViewById(R.id.ic_1);
        ic_2 = findViewById(R.id.ic_2);
        ic_3 = findViewById(R.id.ic_3);
        btn_gologin = findViewById(R.id.btn_gologin);

        cont = 0;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmento, FragmentoInfo.newInstance(0)).commit();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                view.startAnimation(AnimationUtils.loadAnimation(TelaInfo1.this, R.anim.image_click));
                view.setClickable(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (cont == 2) {
                            startActivity(new Intent(getBaseContext(), Login.class));
                            finish();
                        } else {
                            if (0 == cont) {
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragmento, FragmentoInfo.newInstance(1)).commit();
                                ic_1.setImageResource(R.drawable.ic_ball);
                                ic_2.setImageResource(R.drawable.ic_green_ball);
                                ic_3.setImageResource(R.drawable.ic_ball);
                            } else if (1 == cont) {
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragmento, FragmentoInfo.newInstance(2)).commit();
                                ic_1.setImageResource(R.drawable.ic_ball);
                                ic_2.setImageResource(R.drawable.ic_ball);
                                ic_3.setImageResource(R.drawable.ic_green_ball);
                                next.setVisibility(View.GONE);
                                jump.setVisibility(View.INVISIBLE);
                                btn_gologin.setVisibility(View.VISIBLE);
                            }
                            cont++;
                            view.setClickable(true);
                        }
                    }
                }, 100);
            }
        });

        ic_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ic_1.setImageResource(R.drawable.ic_green_ball);
                ic_2.setImageResource(R.drawable.ic_ball);
                ic_3.setImageResource(R.drawable.ic_ball);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmento, FragmentoInfo.newInstance(0)).commit();
                cont = 0;
                next.setVisibility(View.VISIBLE);
                jump.setVisibility(View.VISIBLE);
                btn_gologin.setVisibility(View.GONE);
            }
        });

        ic_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ic_1.setImageResource(R.drawable.ic_ball);
                ic_2.setImageResource(R.drawable.ic_green_ball);
                ic_3.setImageResource(R.drawable.ic_ball);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmento, FragmentoInfo.newInstance(1)).commit();
                cont = 1;
                next.setVisibility(View.VISIBLE);
                jump.setVisibility(View.VISIBLE);
                btn_gologin.setVisibility(View.GONE);
            }
        });

        ic_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ic_1.setImageResource(R.drawable.ic_ball);
                ic_2.setImageResource(R.drawable.ic_ball);
                ic_3.setImageResource(R.drawable.ic_green_ball);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmento, FragmentoInfo.newInstance(2)).commit();
                cont = 2;
                next.setVisibility(View.GONE);
                jump.setVisibility(View.INVISIBLE);
                btn_gologin.setVisibility(View.VISIBLE);
            }
        });

        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!loading) {
                    loading = true;
                    view.startAnimation(AnimationUtils.loadAnimation(TelaInfo1.this, R.anim.image_click));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(getBaseContext(), Login.class));
                            finish();
                        }
                    }, 100);
                }
            }
        });
        btn_gologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!loading) {
                    loading = true;
                    view.startAnimation(AnimationUtils.loadAnimation(TelaInfo1.this, R.anim.image_click));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(getBaseContext(), Login.class));
                            finish();
                        }
                    }, 100);
                }
            }
        });
    }
}
