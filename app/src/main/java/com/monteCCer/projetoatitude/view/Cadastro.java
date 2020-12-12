package com.monteCCer.projetoatitude.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.monteCCer.projetoatitude.DataDestiny.SendCadastro;
import com.monteCCer.projetoatitude.DataSource.HttpService_Cidades;
import com.monteCCer.projetoatitude.DataSource.HttpService_Estados;
import com.monteCCer.projetoatitude.model.Contato;
import com.monteCCer.projetoatitude.model.Resposta_Cidade;
import com.monteCCer.projetoatitude.model.Resposta_Estado;
import com.monteCCer.projetoatitude.R;
import com.monteCCer.projetoatitude.controller.ContatoController;
import com.monteCCer.projetoatitude.controller.ObjetosDoUsuario;
import com.monteCCer.projetoatitude.model.PhoneMask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Cadastro extends AppCompatActivity {

    String estado = "Minas Gerais";
    String cidade = "Monte Carmelo";
    String codigoCidade = "2741";
    String celular = "";

    Spinner estados;
    Spinner cidades;

    ArrayList<String> listaDeEstados;
    ArrayList<String> listaDeCidades;

    EditText nome;
    EditText email;
    EditText senha;
    EditText senha2;
    EditText telefone;

    TextView txt_cad;
    ImageView btn_cad;
    CheckBox accept;
    Thread setSpinners;

    Resposta_Estado[] resposta_estados;
    Resposta_Cidade[] resposta_cidades;

    boolean loaded;
    boolean clicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_new);

        nome = findViewById(R.id.nome);
        email = findViewById(R.id.email);
        senha = findViewById(R.id.senha);
        senha2 = findViewById(R.id.senha2);
        estados = findViewById(R.id.estados);
        cidades = findViewById(R.id.cidades);
        telefone = findViewById(R.id.telefone);
        telefone.addTextChangedListener(new PhoneMask(new WeakReference<>(telefone)));

        btn_cad = findViewById(R.id.btn_cad);
        accept = findViewById(R.id.aceito);
        txt_cad = findViewById(R.id.btn_txt_cad);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView termos = findViewById(R.id.termos);
        SpannableString spannableString = SpannableString.valueOf(termos.getText().toString());
        spannableString.setSpan(new UnderlineSpan(), 0, termos.getText().toString().length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        termos.setText(spannableString);
        termos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(Cadastro.this, R.anim.image_click));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Thread() {
                            @Override
                            public void run() {
                                /*final Dialog dialog = new Dialog(Cadastro.this);
                                dialog.setContentView(R.layout.termos_pop_up);
                                TextView term = dialog.findViewById(R.id.term);
                                term.setMovementMethod(new ScrollingMovementMethod());
                                // Inflate and set the layout for the dialog
                                // Pass null as the parent view because its going in the dialog layout
                                (dialog.findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();*/
                                startActivity(new Intent(Cadastro.this, PoliticaDePrivacidadeActivity.class));
                            }
                        });
                    }
                }, 100);
            }
        });
        clicked = true;
        loaded = false;

        setSpinners = new Thread() {
            @Override
            public void run() {
                try {
                    resposta_estados = new HttpService_Estados().execute().get();
                    if (resposta_estados == null) {
                        runOnUiThread(
                                new Thread() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Cadastro.this, getResources().getString(R.string.Falhaaobaixarlistadeestados), Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                }
                        );
                    } else if (resposta_estados.length > 0) {
                        listaDeEstados = new ArrayList<>();
                        for (Resposta_Estado r : resposta_estados)
                            listaDeEstados.add(r.getNome());
                        estados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                final int i = position;
                                new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            resposta_cidades = new HttpService_Cidades(resposta_estados[i].getCodigo()).execute().get();
                                            if (resposta_cidades == null) {
                                                runOnUiThread(
                                                        new Thread() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(Cadastro.this, getResources().getString(R.string.Falhaaobaixarlistadecidades), Toast.LENGTH_LONG).show();
                                                                finish();
                                                            }
                                                        }
                                                );
                                            } else {
                                                listaDeCidades = new ArrayList<>();
                                                for (Resposta_Cidade r : resposta_cidades)
                                                    listaDeCidades.add(r.getNome());
                                                if (!listaDeCidades.isEmpty()) {
                                                    final ArrayAdapter<String> adapterCidades = new ArrayAdapter<>(Cadastro.this, R.layout.spinner_white_text, listaDeCidades);
                                                    adapterCidades.setDropDownViewResource(R.layout.spinner_white_text);
                                                    runOnUiThread(new Thread() {
                                                        @Override
                                                        public void run() {
                                                            findViewById(R.id.textCity).setVisibility(View.VISIBLE);
                                                            findViewById(R.id.cidades).setVisibility(View.VISIBLE);
                                                            cidades.setAdapter(adapterCidades);
                                                            cidades.setSelection(listaDeCidades.indexOf(cidade));
                                                        }
                                                    });
                                                }
                                            }
                                        } catch (ExecutionException | InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        loaded = true;
                                    }
                                }.start();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                if (listaDeEstados != null && !listaDeEstados.isEmpty()) {
                    final ArrayAdapter<String> adapterEstados = new ArrayAdapter<>(Cadastro.this, R.layout.spinner_white_text, listaDeEstados);
                    adapterEstados.setDropDownViewResource(R.layout.spinner_white_text);
                    runOnUiThread(new Thread() {
                        @Override
                        public void run() {
                            findViewById(R.id.textEst).setVisibility(View.VISIBLE);
                            findViewById(R.id.estados).setVisibility(View.VISIBLE);
                            estados.setAdapter(adapterEstados);
                            estados.setSelection(listaDeEstados.indexOf(estado));
                        }
                    });
                }
            }
        };
        setSpinners.start();

        accept.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    clicked = false;
                    btn_cad.setImageResource(R.drawable.cadastro_btn_green);
                } else {
                    clicked = true;
                    btn_cad.setImageResource(R.drawable.cadastro_btn);
                }
            }
        });

        senha2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    NestedScrollView nestedScrollView = findViewById(R.id.nestedScrollView);
                    nestedScrollView.fullScroll(View.FOCUS_DOWN);
                    return true; // Focus will do whatever you put in the logic.
                }
                return false;  // Focus will change according to the actionId
            }
        });
    }

    public void click_on_register(View v) {
        if (!clicked) {
            clicked = true;
            btn_cad.startAnimation(AnimationUtils.loadAnimation(Cadastro.this, R.anim.image_click));
            txt_cad.startAnimation(AnimationUtils.loadAnimation(Cadastro.this, R.anim.image_click));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Thread() {
                        @Override
                        public void run() {
                            if (loaded) {
                                if (ObjetosDoUsuario.hasInternet(Cadastro.this)) {
                                    new Thread() {
                                        @Override
                                        public void run() {
                                            estado = (String) estados.getSelectedItem();
                                            cidade = (String) cidades.getSelectedItem();
                                            final Thread thread = new Thread() {
                                                @Override
                                                public void run() {
                                                    for (Resposta_Cidade r : resposta_cidades) {
                                                        if (r.getNome().equals(cidade)) {
                                                            codigoCidade = r.getCodigo();
                                                            break;
                                                        }
                                                    }
                                                }
                                            };
                                            thread.start();
                                            final Contato newContato = new Contato();
                                            Thread thread1 = new Thread() {
                                                @Override
                                                public void run() {
                                                    if (!email.getText().toString().isEmpty() && email.getText().toString().contains("@")
                                                            && email.getText().toString().contains(".")) {
                                                        newContato.setEmail(email.getText().toString());
                                                        newContato.setNome(nome.getText().toString());
                                                        celular = telefone.getText().toString();
                                                        if (!(celular.length() < 10)) {
                                                            if (senha.getText().toString().length() >= ObjetosDoUsuario.MinLengthOfPass) {
                                                                if (senha.getText().toString().equals(senha2.getText().toString())) {
                                                                    newContato.setSenha(senha.getText().toString());

                                                                    try {
                                                                        thread.join();
                                                                        final Integer resposta = (new SendCadastro(newContato.getEmail(), newContato.getNome(),
                                                                                codigoCidade, newContato.getSenha(), celular)).execute().get();
                                                                        runOnUiThread(new Thread() {
                                                                            @Override
                                                                            public void run() {
                                                                                if (resposta == 1) {
                                                                                    ContatoController.getInstance(Cadastro.this).salvar(newContato);
                                                                                    startActivity(new Intent(Cadastro.this, SpashCadastro.class));
                                                                                    finish();
                                                                                } else if (resposta == 2) {
                                                                                    Toast.makeText(Cadastro.this, getResources().getString(R.string.FalhaaoCadastrar), Toast.LENGTH_LONG).show();
                                                                                } else if (resposta == 3) {
                                                                                    Toast.makeText(Cadastro.this, getResources().getString(R.string.Preenchatodososcampos), Toast.LENGTH_LONG).show();
                                                                                } else if (resposta == 4) {
                                                                                    Toast.makeText(Cadastro.this, getResources().getString(R.string.Usuriojcadastrado), Toast.LENGTH_LONG).show();
                                                                                }
                                                                            }
                                                                        });
                                                                    } catch (ExecutionException | InterruptedException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                } else {
                                                                    Toast.makeText(Cadastro.this, getResources().getString(R.string.AssenhasnoCorrespondem), Toast.LENGTH_SHORT).show();
                                                                    senha.setText("");
                                                                    senha2.setText("");
                                                                }
                                                            } else {
                                                                String msg = getResources().getString(R.string.SenhaMuitoFraca) + " >= " + ObjetosDoUsuario.MinLengthOfPass;
                                                                Toast.makeText(Cadastro.this, msg, Toast.LENGTH_SHORT).show();
                                                                senha.setText("");
                                                                senha2.setText("");
                                                            }
                                                        } else {
                                                            Toast.makeText(Cadastro.this, getResources().getString(R.string.TelefoneInvlido), Toast.LENGTH_SHORT).show();
                                                            telefone.setText("");
                                                        }
                                                    } else {
                                                        Toast.makeText(Cadastro.this, getResources().getString(R.string.EmailInvlido), Toast.LENGTH_SHORT).show();
                                                        email.setText("");
                                                    }
                                                    accept.setChecked(false);
                                                }
                                            };
                                            runOnUiThread(thread1);
                                        }
                                    }.start();
                                } else {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.PorFavorseconecteInternet), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
            }, 100);
        }
    }
}
