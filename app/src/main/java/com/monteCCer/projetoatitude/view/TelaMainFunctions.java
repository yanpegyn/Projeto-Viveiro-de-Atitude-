package com.monteCCer.projetoatitude.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.monteCCer.projetoatitude.DataDestiny.SendPics;
import com.monteCCer.projetoatitude.model.Area;
import com.monteCCer.projetoatitude.model.Contato;
import com.monteCCer.projetoatitude.model.DroidUtils;
import com.monteCCer.projetoatitude.model.Foto;
import com.monteCCer.projetoatitude.R;
import com.monteCCer.projetoatitude.controller.AreaController;
import com.monteCCer.projetoatitude.controller.ContatoController;
import com.monteCCer.projetoatitude.controller.ObjetosDoUsuario;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class TelaMainFunctions {

    private Menu menu;

    private Activity activity;
    private DrawerLayout drawer;
    private Button btn_sair;

    private boolean ContentViewTrigger;
    private boolean cameraIsOpen;

    private ImageButton btn_home;
    private ImageButton btn_pdf;
    private ImageButton btn_map;

    private GoogleMap googleMap;
    private ArrayList<Area> areas;

    TelaMainFunctions(final Activity activity) {
        this.activity = activity;

        ContentViewTrigger = cameraIsOpen = false;
        ImageView close = activity.findViewById(R.id.close);
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        drawer = activity.findViewById(R.id.drawer_layout);
        NavigationView externalNavigationView = activity.findViewById(R.id.nav_view);
        NavigationView navigationView = externalNavigationView.findViewById(R.id.navigation);
        menu = navigationView.getMenu();
        View headerView = navigationView.getHeaderView(0);
        TextView v2 = headerView.findViewById(R.id.textView_cpf);
        TextView v3 = headerView.findViewById(R.id.textview_nome);
        ContatoController controller = ContatoController.getInstance(activity);
        Contato user = controller.get_UsuarioAtual();
        if (user == null)
            activity.finish();
        else {
            String strrr = user.getCpf();
            strrr = "CPF: " + strrr.substring(0, 3) + "." + strrr.substring(3, 6) + "." + strrr.substring(6, 9) + "-" + strrr.substring(9, 11);
            v2.setText(strrr);
            String username = user.getNome();
            if (username.length() > 20)
                username = username.split(" ")[0] + " " + username.split(" ")[1];
            v3.setText(username);

            btn_sair = activity.findViewById(R.id.nav_sair);
            btn_sair.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    confirmacaoSair();
                }
            });
            ViewTreeObserver vto = btn_sair.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    btn_sair.getViewTreeObserver().removeOnPreDrawListener(this);
                    //scale to 90% of button height
                    DroidUtils.scaleButtonDrawables(btn_sair, 0.5);
                    return true;
                }
            });
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    activity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            toolbar.setNavigationIcon(R.drawable.ic_menu);

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NotNull MenuItem item) {
                    // Handle navigation view item clicks here.
                    int id = item.getItemId();
                    if (id == R.id.nav_list_especies) {
                        fun_home();
                    } else if (id == R.id.nav_cartilha) {
                        fun_pdf();
                    } else if (id == R.id.nav_pic_area) {
                        activity.startActivity(new Intent(activity.getBaseContext(), CameraActivity.class).putExtra(ObjetosDoUsuario.EXTRA_MESSAGE, "").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        fun_home();
                    } else if (id == R.id.nav_map) {
                        fun_map();
                    } else if (id == R.id.nav_site) {
                        fun_site();
                    } else if (id == R.id.recharge) {
                        fun_reload();
                    } else if (id == R.id.nav_terms) {
                        fun_terms();
                    }
                    DrawerLayout drawer = activity.findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                }
            });

            activity.findViewById(R.id.btn_cam_map).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startActivity(new Intent(activity.getBaseContext(), CameraActivity.class).putExtra(ObjetosDoUsuario.EXTRA_MESSAGE, "").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    setCameraIsOpen(true);
                }
            });

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawer.closeDrawer(GravityCompat.START);
                }
            });

            btn_home = activity.findViewById(R.id.btn_home);
            btn_pdf = activity.findViewById(R.id.btn_here);
            btn_map = activity.findViewById(R.id.btn_map);

            btn_home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fun_home();
                }
            });

            btn_pdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fun_pdf();
                }
            });

            btn_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fun_map();
                }
            });

            activity.findViewById(R.id.ic_picture).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startActivity(new Intent(activity, FotosAreas.class));
                }
            });
            activity.findViewById(R.id.txt_pic).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startActivity(new Intent(activity, FotosAreas.class));
                }
            });
        }
    }

    private void confirmacaoSair() {
        //atributo da classe.
        AlertDialog alerta;
        //Cria o gerador do AlertDialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AlertDialogCustom);
        //define o titulo
        builder.setTitle(null);
        //define a mensagem
        builder.setMessage(activity.getResources().getString(R.string.Desejarealmentesair));
        //define um botão como Sim
        builder.setPositiveButton(activity.getResources().getString(R.string.Sim), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                ContatoController controller = ContatoController.getInstance(activity);
                ObjetosDoUsuario.clearAll();
                Intent intent = new Intent(activity.getBaseContext(), Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
                activity.finish();
                controller.logout();
            }
        });
        //define um botão como não.
        builder.setNegativeButton(activity.getResources().getString(R.string.Nao), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }

    private void fun_terms() {
        activity.startActivity(new Intent(activity, PoliticaDePrivacidadeActivity.class));
    }

    void fun_reload() {
        if (ObjetosDoUsuario.hasInternet(activity)) {
            new SendPics(activity).execute();
            Intent intent = new Intent(activity, Login.class);
            intent.putExtra("Recharge", true);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(intent);
            activity.finish();
        } else {
            Toast.makeText(activity, activity.getResources().getString(R.string.NohInternetparareload), Toast.LENGTH_SHORT).show();
        }
    }

    private void fun_site() {
        if (ObjetosDoUsuario.hasInternet(activity)) {
            String url = "http://www.viveirodeatitude.org/index.php";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            activity.startActivity(i);
        } else {
            Toast.makeText(activity, activity.getResources().getString(R.string.NohInternetparacarregarosite), Toast.LENGTH_SHORT).show();
        }
    }

    void fun_map() {
        visibilidadeItensArea(true);
        if (googleMap != null) {
            if (!pointerAreas()) {
                Toast toast = Toast.makeText(activity, activity.getResources().getString(R.string.NohAreasaExibir), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            }
            menu.findItem(R.id.nav_map).setChecked(true);
            activity.findViewById(R.id.include).setVisibility(View.GONE);
            activity.findViewById(R.id.include2).setVisibility(View.GONE);
            activity.findViewById(R.id.includeMaps).setVisibility(View.VISIBLE);
            ContentViewTrigger = true;
            btn_home.setImageResource(R.drawable.ic_inicio);
            btn_pdf.setImageResource(R.drawable.ic_book);
            btn_map.setImageResource(R.drawable.ic_map_green);
        } else {
            Toast.makeText(activity, activity.getResources().getString(R.string.Carregando), Toast.LENGTH_LONG).show();
        }
    }

    void fun_home() {
        visibilidadeItensArea(false);
        menu.findItem(R.id.nav_list_especies).setChecked(true);
        activity.findViewById(R.id.include2).setVisibility(View.GONE);
        activity.findViewById(R.id.includeMaps).setVisibility(View.GONE);
        activity.findViewById(R.id.include).setVisibility(View.VISIBLE);
        ContentViewTrigger = false;
        btn_home.setImageResource(R.drawable.ic_inicio_green);
        btn_pdf.setImageResource(R.drawable.ic_book);
        btn_map.setImageResource(R.drawable.ic_map);
    }

    private void fun_pdf() {
        visibilidadeItensArea(false);
        menu.findItem(R.id.nav_cartilha).setChecked(true);
        activity.findViewById(R.id.include).setVisibility(View.GONE);
        activity.findViewById(R.id.includeMaps).setVisibility(View.GONE);
        activity.findViewById(R.id.include2).setVisibility(View.VISIBLE);
        PDFView pdfView = activity.findViewById(R.id.pdfView);
        pdfView.fromAsset("cartilha.pdf").load();
        ContentViewTrigger = true;
        btn_home.setImageResource(R.drawable.ic_inicio);
        btn_pdf.setImageResource(R.drawable.ic_book_green);
        btn_map.setImageResource(R.drawable.ic_map);
    }

    void visibilidadeItensArea(boolean visivel) {
        int visibility;
        if (visivel) {
            visibility = View.VISIBLE;
        } else {
            visibility = View.GONE;
        }
        activity.findViewById(R.id.text_area).setVisibility(visibility);
        activity.findViewById(R.id.ic_picture).setVisibility(visibility);
        activity.findViewById(R.id.txt_pic).setVisibility(visibility);
        activity.findViewById(R.id.btn_cam_map).setVisibility(visibility);
    }

    boolean pointerAreas() {
        if (googleMap != null) {

            areas = new ArrayList<>();
            double metros;
            ArrayList<Foto> fotosAreas = (AreaController.getInstance(activity)).getAllAreas();
            if (fotosAreas != null && !fotosAreas.isEmpty()) {
                boolean flag;
                do {
                    Foto a = fotosAreas.get(0);
                    Area area = new Area(new LatLng(a.getLatitude(), a.getLongitude()));
                    area.addFoto(a);
                    fotosAreas.remove(0);
                    do {
                        flag = false;
                        for (Foto b : fotosAreas) {
                            metros = getDistancia(a.getLatitude(), a.getLongitude(), b.getLatitude(), b.getLongitude());
                            if (metros <= 50) {
                                area.addFoto(b);
                                fotosAreas.remove(b);
                                flag = true;
                                break;
                            }
                        }
                    } while (flag);
                    areas.add(area);

                } while (!fotosAreas.isEmpty());

                if (!areas.isEmpty()) {
                    LatLng point;
                    MarkerOptions marker;
                    for (Area a : areas) {
                        Foto b = a.getFotos().get(0);
                        point = new LatLng(b.getLatitude(), b.getLongitude());
                        marker = new MarkerOptions().position(point)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer_map))
                                .title(activity.getResources().getString(R.string.VerFotos));
                        googleMap.addMarker(marker);
                        moveToCurrentLocation(point);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private void moveToCurrentLocation(LatLng currentLocation) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }

    void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    boolean isContentViewTrigger() {
        return ContentViewTrigger;
    }

    private double getDistancia(double latitudePto1, double longitudePto1, double latitudePto2, double longitudePto2) {
        double dlon, dlat, a, distancia;
        dlon = Math.toRadians(longitudePto2) - Math.toRadians(longitudePto1);
        dlat = Math.toRadians(latitudePto2) - Math.toRadians(latitudePto1);
        a = Math.pow(
                Math.sin(dlat / 2), 2)
                + Math.cos(Math.toRadians(latitudePto1))
                * Math.cos(Math.toRadians(latitudePto2))
                * Math.pow(Math.sin(dlon / 2)
                , 2);
        distancia = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 6378140 * distancia; /* 6378140 is the radius of the Earth in meters*/
    }

    ArrayList<Area> getAreas() {
        return areas;
    }

    boolean isCameraIsOpen() {
        return cameraIsOpen;
    }

    void setCameraIsOpen(boolean cameraIsOpen) {
        this.cameraIsOpen = cameraIsOpen;
    }
}
