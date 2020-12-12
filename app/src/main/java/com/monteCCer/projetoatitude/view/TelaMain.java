package com.monteCCer.projetoatitude.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;

import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.monteCCer.projetoatitude.DataDestiny.SendPics;
import com.monteCCer.projetoatitude.model.Area;
import com.monteCCer.projetoatitude.model.Contato;
import com.monteCCer.projetoatitude.model.Especie;
import com.monteCCer.projetoatitude.R;
import com.monteCCer.projetoatitude.controller.ContatoController;
import com.monteCCer.projetoatitude.controller.ObjetosDoUsuario;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TelaMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    boolean Searching;

    private RecyclerView gradeDeItens;
    private AdaptadorTela1 searchAdapter;
    private EditText search;

    private TelaMainFunctions telaMainFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContatoController.getInstance(this).get_UsuarioAtual() == null)
            finish();
        else {
            registerLollipopNetworkReceiver(this);
            telaMainFunctions = new TelaMainFunctions(this);
            telaMainFunctions.visibilidadeItensArea(false);

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }

            ContatoController controller = ContatoController.getInstance(getBaseContext());
            Contato user = controller.get_UsuarioAtual();

            TextView v = findViewById(R.id.Nome_tela1);

            String HelloUser = user.getNome();
            if (HelloUser.length() > 20)
                HelloUser = HelloUser.split(" ")[0] + " " + HelloUser.split(" ")[1];
            HelloUser = HelloUser + "!";
            v.setText(HelloUser);

            gradeDeItens = findViewById(R.id.recicler);

            search = findViewById(R.id.Pesquisar);
            search.setOnEditorActionListener(
                    new EditText.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                    actionId == EditorInfo.IME_ACTION_DONE ||
                                    event != null &&
                                            event.getAction() == KeyEvent.ACTION_DOWN &&
                                            event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                                if (event == null || !event.isShiftPressed()) {
                                    // the user is done typing.
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    if (imm != null) {
                                        imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
                                    }
                                    gradeDeItens.setAdapter(new AdaptadorTela1(
                                            TelaMain.this, ObjetosDoUsuario
                                            .getEspeciesLikeIdOrName(search.getText().toString())
                                    ));
                                    Searching = true;
                                    return true; // consume.
                                }
                            }
                            return false; // pass on to other listeners.
                        }
                    }
            );
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("FailFinder", Context.MODE_PRIVATE);
        boolean result = sharedPreferences.getBoolean("jaTentouRestaurar", false);
        if (ObjetosDoUsuario.anyFail()) {
            Log.i("StartMain", "Fail");
            if (result) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("jaTentouRestaurar", true);
                editor.apply();
                Intent i = getBaseContext().getPackageManager().
                        getLaunchIntentForPackage(getBaseContext().getPackageName());
                if (i != null) {
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
                finish();
            } else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("jaTentouRestaurar", false);
                editor.apply();
                if (!ObjetosDoUsuario.hasInternet(this)) {
                    Toast.makeText(this, getResources().getString(R.string.DadosInconsistentes), Toast.LENGTH_SHORT).show();
                    ContatoController.getInstance(this).logout();
                    finish();
                } else {
                    telaMainFunctions.fun_reload();
                }
            }
        } else {
            ArrayList<Especie> itens = ObjetosDoUsuario.getEspecies();
            searchAdapter = new AdaptadorTela1(this, itens);
            gradeDeItens.setLayoutManager(new GridLayoutManager(this, 2));
            gradeDeItens.setAdapter(searchAdapter);
        }
    }

    @Override
    public void onBackPressed() {
        if (Searching) {
            Searching = false;
            gradeDeItens.setAdapter(searchAdapter);
            search.setText("");
            search.clearFocus();
        } else if (telaMainFunctions.isContentViewTrigger()) {
            telaMainFunctions.fun_home();
        } else {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        telaMainFunctions.setGoogleMap(googleMap);
        telaMainFunctions.pointerAreas();
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                //int position = (int)(marker.getTag());
                LatLng coord = marker.getPosition();
                //Log.i("GPS", "ClickOnPointer");
                for (Area a : telaMainFunctions.getAreas()) {
                    if (a.getCoordenadas().equals(coord)) {
                        ObjetosDoUsuario.setAtualArea(a);
                        startActivity(new Intent(getBaseContext(), FotosAreas.class));
                    }
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (telaMainFunctions.isCameraIsOpen()) {
            telaMainFunctions.fun_map();
            telaMainFunctions.setCameraIsOpen(false);
        }
    }

    public static void registerLollipopNetworkReceiver(final Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                cm.registerNetworkCallback(
                        new NetworkRequest.Builder()
                                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                                .build(),
                        new ConnectivityManager.NetworkCallback() {
                            @Override
                            public void onAvailable(@NotNull Network network) {
                                boolean connected;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    connected = cm.bindProcessToNetwork(network);
                                } else {
                                    connected = ConnectivityManager.setProcessDefaultNetwork(network);
                                }
                                if (connected) {
                                    new SendPics(context).execute();
                                }
                            }
                        });
            }
        }
    }
}