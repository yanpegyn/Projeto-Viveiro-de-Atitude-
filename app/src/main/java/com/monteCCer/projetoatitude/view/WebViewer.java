package com.monteCCer.projetoatitude.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.monteCCer.projetoatitude.R;
import com.monteCCer.projetoatitude.controller.ContatoController;
import com.monteCCer.projetoatitude.controller.ObjetosDoUsuario;

import java.util.Objects;

public class WebViewer extends AppCompatActivity {

    WebView myWebView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_viewer);
        myWebView = findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new MyWebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
        myWebView.loadUrl("http://www.viveirodeatitude.org/index.php");
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Objects.requireNonNull(Uri.parse(url).getHost()).equals("http://www.viveirodeatitude.org/index.php")) {
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (myWebView.canGoBack()) {
                    myWebView.goBack();
                } else {
                    confirmacaoSair(this);
                }
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    private void confirmacaoSair(final Activity activity) {
        //atributo da classe.
        AlertDialog alerta;
        //Cria o gerador do AlertDialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AlertDialogCustom);
        //define o titulo
        builder.setTitle(null);
        //define a mensagem
        builder.setMessage(getResources().getString(R.string.DesejafazerLogoff));
        //define um botão como Sim
        builder.setPositiveButton(getResources().getString(R.string.Sim), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                ContatoController controller = ContatoController.getInstance(activity);
                ObjetosDoUsuario.clearAll();
                Intent intent = new Intent(activity.getBaseContext(), Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
                activity.finish();
                controller.logoutVisitante();
            }
        });
        //define um botão como não.
        builder.setNegativeButton(getResources().getString(R.string.Nao), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }
}
