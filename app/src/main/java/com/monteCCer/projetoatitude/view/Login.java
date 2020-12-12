package com.monteCCer.projetoatitude.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.monteCCer.projetoatitude.DataDestiny.SendNewPass;
import com.monteCCer.projetoatitude.DataDestiny.SendTerms;
import com.monteCCer.projetoatitude.DataSource.HttpService_Areas;
import com.monteCCer.projetoatitude.DataSource.HttpService_EspeciesAndMudas;
import com.monteCCer.projetoatitude.DataSource.HttpService_Login;
import com.monteCCer.projetoatitude.model.Contato;
import com.monteCCer.projetoatitude.R;
import com.monteCCer.projetoatitude.controller.ContatoController;
import com.monteCCer.projetoatitude.controller.EspecieController;
import com.monteCCer.projetoatitude.controller.ObjetosDoUsuario;
import com.monteCCer.projetoatitude.model.Resposta_Login;

import java.util.concurrent.ExecutionException;


public class Login extends AppCompatActivity {

    Button logar;
    EditText email, senha;
    Contato obj;
    ContatoController controller;
    TextView recuperar_key, duvida, duvida2;
    Boolean loading = false;
    ProgressBar progressBar;
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        email = findViewById(R.id.email);
        /*
        //Mascara CPF feita ba CPFMask (kotlin)
        email.addTextChangedListener(CPFMask.Companion.mask("###.###.###-##", email));
        ///Apenas Numeros
        email.setInputType(InputType.TYPE_CLASS_NUMBER);
        */
        senha = findViewById(R.id.senha);
        logar = findViewById(R.id.btn_logar);
        recuperar_key = findViewById(R.id.txt_esq_sen);
        duvida2 = findViewById(R.id.txt_cad2);
        duvida = findViewById(R.id.txt_cad);
        progressBar = findViewById(R.id.progressBar);

        recuperar_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        duvida2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acao_duvida();
            }
        });

        duvida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acao_duvida();
            }
        });

        logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(Login.this, R.anim.image_click));
                Log.i("BaseApp", "Botão Logar");
                if (!loading) {
                    loading = true;
                    pass = senha.getText().toString();
                    Toast.makeText(Login.this, getResources().getString(R.string.CarregandoPorFavorAguarde), Toast.LENGTH_LONG).show();
                    logar.setVisibility(View.GONE);
                    findViewById(R.id.btn_cad).setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    validar();
                }
            }
        });
        if (getIntent().getBooleanExtra("Recharge", false)) {
            Contato contato = ContatoController.getInstance(this).get_UsuarioAtual();
            if (contato != null) {
                this.email.setText(contato.getEmail());
                this.senha.setText(contato.getSenha());
                logar.callOnClick();
            }
        }
    }

    Resposta_Login lg;

    private void validar() {
        // TODO: 15/10/2019 Tentar Amenizar o tempo gasto
        lg = null;
        new Thread() {
            public void run() {
                if (!email.getText().toString().isEmpty()) {
                    final String Email = email.getText().toString();
                    runOnUiThread(new Thread() {
                        @Override
                        public void run() {
                            email.setFocusable(false);
                            senha.setFocusable(false);
                        }
                    });
                    ///Formata o email
                    //String OnlyNumberCPF = email.getText().toString().replace(".", "").replace("-", "");
                    //Busca se existe o Email no Banco SqlLite

                    controller = ContatoController.getInstance(getBaseContext());
                    obj = controller.get_contato(Email, pass);
                    //Se não tiver no banco local solicita Online
                    //if(obj != null)
                    //  Log.i("OBJ", obj.toString());
                    if (ObjetosDoUsuario.hasInternet(Login.this)) {
                        try {
                            lg = new HttpService_Login(Email, pass).execute().get();
                            Contato contato = null;
                            if (lg != null) {
                                contato = new Contato();
                                contato.setCpf(lg.getCpf());
                                contato.setSenha(pass);
                                contato.setNome(lg.getNome());
                                contato.setEmail(Email);
                            }
                            if (obj != null && contato != null) {
                                controller.alterar(contato);
                            } else if (contato != null) {
                                ContatoController.getInstance(Login.this).clearAllTables(Login.this);
                                obj = contato;
                                controller.salvar(obj);
                            } else if (obj != null) {
                                ContatoController.getInstance(Login.this).clearAllTables(Login.this);
                                //controller.deletar(obj);
                                obj = null;
                            }
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //Se encontrar o usuário Faz login senão acusa erro
                if (obj != null && obj.getCpf() != null && !obj.getCpf().isEmpty()) {
                    if (ObjetosDoUsuario.hasInternet(Login.this)) {
                        if (lg != null && (lg.getFlagnovasenha() == null || lg.getFlagnovasenha().equals("1"))) {
                            popUpNovaSenha();
                        } else if (lg != null && lg.getDataaceitetermos() == null) {
                            popUpTermos();
                        } else if (lg != null) {
                            logarComoCliente();
                            baixar();
                        }
                    } else {
                        logarComoCliente();
                        EspecieController especieController = EspecieController.getInstance(Login.this);
                        ObjetosDoUsuario.setEspecies(especieController.getAllEspecies());
                        startActivity(new Intent(Login.this, TelaMain.class));
                        finish();
                    }
                    //finish();
                } else if (obj != null && ObjetosDoUsuario.hasInternet(Login.this)) {
                    Log.i("USER", "Logado como Visitante");
                    obj.setLogado("True");
                    controller.alterar(obj);
                    startActivity(new Intent(Login.this, WebViewer.class));
                    finish();
                } else {
                    fail();
                    runOnUiThread(new Thread() {
                        @Override
                        public void run() {
                            if (!ObjetosDoUsuario.hasInternet(Login.this)) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.PorFavorseconecteInternet), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.EmailSenhaInvlidos), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        }.start();
    }

    private void logarComoCliente() {
        Log.i("USER", "Logado como Cliente");
        obj.setLogado("True");
        controller.alterar(obj);
        ObjetosDoUsuario.setCPF(obj.getCpf());
        ObjetosDoUsuario.setEmail(obj.getEmail());
    }

    public void fail() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                apagarCampos();
                email.setFocusableInTouchMode(true);
                senha.setFocusableInTouchMode(true);
                email.setFocusable(true);
                senha.setFocusable(true);
                loading = false;
                progressBar.setVisibility(View.GONE);
                logar.setVisibility(View.VISIBLE);
                findViewById(R.id.btn_cad).setVisibility(View.VISIBLE);
            }
        });
    }

    private void baixar() {
        new Thread() {
            @Override
            public void run() {
                try {
                    ObjetosDoUsuario.setEspeciesArray(new HttpService_EspeciesAndMudas(ObjetosDoUsuario.getEmail(), pass).execute().get(), Login.this, "URL");
                    ObjetosDoUsuario.setAreasArray(new HttpService_Areas(ObjetosDoUsuario.getEmail()).execute().get(), Login.this, "URL");
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void acao_cadastro(View view) {
        if (ObjetosDoUsuario.hasInternet(Login.this)) {
            view.startAnimation(AnimationUtils.loadAnimation(Login.this, R.anim.image_click));
            //duvida2.startAnimation(AnimationUtils.loadAnimation(Login.this, R.anim.image_click));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Thread() {
                        @Override
                        public void run() {
                            startActivity(new Intent(Login.this, Cadastro.class));
                        }
                    });
                }
            }, 100);
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.PorFavorseconecteInternet), Toast.LENGTH_LONG).show();
        }
    }

    public void acao_duvida() {

    }

    public void apagarCampos() {
        email = findViewById(R.id.email);
        senha = findViewById(R.id.senha);
        email.setText("");
        senha.setText("");
    }

    private void popUpNovaSenha() {
        if (lg != null && (lg.getFlagnovasenha() == null || lg.getFlagnovasenha().equals("1"))) {
            Thread newPass = new Thread() {
                @Override
                public void run() {
                    final Dialog dialog = new Dialog(Login.this);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.novasenha_pop_up);
                    (dialog.findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            fail();
                            runOnUiThread(new Thread() {
                                @Override
                                public void run() {
                                    Toast.makeText(Login.this, getResources().getString(R.string.ParaContinuarAltereSuaSenha), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                    Button ok = dialog.findViewById(R.id.ok_new_senha);
                    final boolean[] btnOkPressed = {false};
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!btnOkPressed[0]) {
                                btnOkPressed[0] = true;
                                new Thread() {
                                    @Override
                                    public void run() {
                                        final boolean[] erro = {false, false};
                                        final TextView[] senha = new TextView[2];
                                        senha[0] = dialog.findViewById(R.id.novaSenha);
                                        senha[1] = dialog.findViewById(R.id.novaSenha2);
                                        runOnUiThread(new Thread() {
                                            @Override
                                            public void run() {
                                                senha[0].setFocusable(false);
                                                senha[1].setFocusable(false);
                                                senha[0] = dialog.findViewById(R.id.novaSenha);
                                                senha[1] = dialog.findViewById(R.id.novaSenha2);
                                                new Thread() {
                                                    @Override
                                                    public void run() {
                                                        if (senha[0].getText().toString().equals(ObjetosDoUsuario.SenhaPadrao)) {
                                                            runOnUiThread(new Thread() {
                                                                @Override
                                                                public void run() {
                                                                    senha[0].setText("");
                                                                    senha[1].setText("");
                                                                    senha[0].setFocusableInTouchMode(true);
                                                                    senha[1].setFocusableInTouchMode(true);
                                                                    senha[0].setFocusable(true);
                                                                    senha[1].setFocusable(true);
                                                                    Toast.makeText(Login.this, getResources().getString(R.string.SenhaPadrao) + " '" + ObjetosDoUsuario.SenhaPadrao + "'", Toast.LENGTH_SHORT).show();
                                                                    btnOkPressed[0] = false;
                                                                }
                                                            });
                                                        } else if (senha[0].getText().toString().length() < ObjetosDoUsuario.MinLengthOfPass) {
                                                            runOnUiThread(new Thread() {
                                                                @Override
                                                                public void run() {
                                                                    senha[0].setText("");
                                                                    senha[1].setText("");
                                                                    senha[0].setFocusableInTouchMode(true);
                                                                    senha[1].setFocusableInTouchMode(true);
                                                                    senha[0].setFocusable(true);
                                                                    senha[1].setFocusable(true);
                                                                    Toast.makeText(Login.this, getResources().getString(R.string.SenhaMuitoFraca) + " >= " + ObjetosDoUsuario.MinLengthOfPass, Toast.LENGTH_SHORT).show();
                                                                    btnOkPressed[0] = false;
                                                                }
                                                            });
                                                        } else if (senha[0].getText().toString().equals(senha[1].getText().toString())) {
                                                            Integer stats;
                                                            try {
                                                                obj.setSenha(senha[0].getText().toString());
                                                                stats = new SendNewPass(obj.getEmail(), obj.getSenha()).execute().get();
                                                                if (stats == 1) {
                                                                    //Depois já pode baixar os dados
                                                                    popUpTermos();
                                                                    dialog.dismiss();
                                                                } else {
                                                                    //Caso de erro
                                                                    erro[0] = true;
                                                                }
                                                            } catch (ExecutionException | InterruptedException e) {
                                                                e.printStackTrace();
                                                                erro[0] = true;
                                                            }
                                                        } else {
                                                            runOnUiThread(new Thread() {
                                                                @Override
                                                                public void run() {
                                                                    senha[0].setText("");
                                                                    senha[1].setText("");
                                                                    senha[0].setFocusableInTouchMode(true);
                                                                    senha[1].setFocusableInTouchMode(true);
                                                                    senha[0].setFocusable(true);
                                                                    senha[1].setFocusable(true);
                                                                    btnOkPressed[0] = false;
                                                                    Toast.makeText(Login.this, getResources().getString(R.string.AssenhasnoCorrespondem), Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                        }
                                                        if (erro[0]) {
                                                            fail();
                                                            runOnUiThread(new Thread() {
                                                                @Override
                                                                public void run() {
                                                                    Toast.makeText(Login.this, getResources().getString(R.string.ErroAoAlterarSenha), Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                            dialog.dismiss();
                                                        }
                                                    }
                                                }.start();
                                            }
                                        });
                                    }
                                }.start();
                            }
                        }
                    });
                    dialog.show();
                }
            };
            runOnUiThread(newPass);
        }
    }

    private void popUpTermos() {
        if (lg != null && lg.getDataaceitetermos() == null) {
            Thread termos = new Thread() {
                @Override
                public void run() {
                    final Dialog dialog = new Dialog(Login.this);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.termos_of_proj);
                    TextView term = dialog.findViewById(R.id.term);
                    term.setMovementMethod(new ScrollingMovementMethod());
                    (dialog.findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            fail();
                            runOnUiThread(new Thread() {
                                @Override
                                public void run() {
                                    Toast.makeText(Login.this, getResources().getString(R.string.Casoqueiracontinuarestejadeacordocomnossostermos), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                    final ImageView prox = dialog.findViewById(R.id.prox);
                    prox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(v.isClickable()) {
                                //Manda o estado de termo aceito aqui
                                new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            //Log.i("Terms", "Esperando stats");
                                            Integer stats = new SendTerms(obj.getEmail(), obj.getSenha()).execute().get();
                                            //if(stats != null) {
                                            if (stats == 1 || stats == 4) {
                                                //Depois já pode baixar os dados
                                                logarComoCliente();
                                                baixar();
                                            } else {
                                                //Caso de erro
                                                fail();
                                                runOnUiThread(new Thread() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(Login.this, getResources().getString(R.string.Erroaosalvarestadodostermos), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        } catch (ExecutionException | InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }.start();
                                dialog.dismiss();
                            }
                        }
                    });
                    prox.setClickable(false);
                    CheckBox ck = dialog.findViewById(R.id.terms);
                    ck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                prox.setImageResource(R.drawable.btn_term_prox_green);
                                prox.setClickable(true);
                            } else {
                                prox.setImageResource(R.drawable.btn_term_prox);
                                prox.setClickable(false);
                            }
                        }
                    });
                    Button btnpolitica = dialog.findViewById(R.id.btnpolitica);
                    btnpolitica.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getBaseContext(), PoliticaDePrivacidadeActivity.class));
                        }
                    });
                    dialog.show();
                }
            };
            runOnUiThread(termos);
        }
    }

}