package com.monteCCer.projetoatitude.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.monteCCer.projetoatitude.DataSource.LoadImageAsync;
import com.monteCCer.projetoatitude.controller.FotoController;
import com.monteCCer.projetoatitude.model.Foto;
import com.monteCCer.projetoatitude.R;
import com.monteCCer.projetoatitude.controller.EspecieController;
import com.monteCCer.projetoatitude.controller.ObjetosDoUsuario;
import com.monteCCer.projetoatitude.model.ListenerAdapterOfImage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class ImageViewer extends AppCompatActivity implements ListenerAdapterOfImage {

    ArrayList<Foto> fotos;
    Integer index;
    PhotoView photoView;
    boolean loading = false;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        photoView = findViewById(R.id.photoView);

        progressBar = findViewById(R.id.load);

        (findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(ImageViewer.this, R.anim.image_click));
                ObjetosDoUsuario.ObjetosTemporarios.listagemDasFotos.index = null;
                ObjetosDoUsuario.ObjetosTemporarios.listagemDasFotos.fotos = null;
                finish();
            }
        });
        (findViewById(R.id.close)).setVisibility(View.VISIBLE);

        (findViewById(R.id.previous)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!loading) {
                    if (index != 0) {
                        loading = true;
                        index--;
                        setImage(v);
                    }
                }
            }
        });
        (findViewById(R.id.next)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!loading) {
                    if (index != fotos.size() - 1) {
                        loading = true;
                        index++;
                        setImage(v);
                    }
                }
            }
        });
        (findViewById(R.id.compartilhar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });
    }

    private void setImage(@Nullable final View v) {
        progressBar.setVisibility(View.VISIBLE);
        if (v != null) {
            v.startAnimation(AnimationUtils.loadAnimation(ImageViewer.this, R.anim.image_click));
        }
        photoView.setImageResource(0);
        new LoadImageAsync(fotos.get(index).getIMG(), ImageViewer.this, ImageViewer.this, String.valueOf(index)).execute();
        //Bitmap bitmap = FotoController.getInstance(this).buscarImg(fotos.get(index).getIMG());

        if (index == 0 && index == fotos.size() - 1) {
            (findViewById(R.id.previous)).setVisibility(View.GONE);
            (findViewById(R.id.next)).setVisibility(View.GONE);
        } else if (index == 0) {
            (findViewById(R.id.previous)).setVisibility(View.GONE);
            (findViewById(R.id.next)).setVisibility(View.VISIBLE);
        } else if (index == fotos.size() - 1) {
            (findViewById(R.id.next)).setVisibility(View.GONE);
            (findViewById(R.id.previous)).setVisibility(View.VISIBLE);
        } else if (index > 0 && index < fotos.size() - 1) {
            (findViewById(R.id.previous)).setVisibility(View.VISIBLE);
            (findViewById(R.id.next)).setVisibility(View.VISIBLE);
        }
        if (v != null) {
            v.postOnAnimation(new Thread() {
                @Override
                public void run() {
                    loading = false;
                }
            });
        } else loading = false;
    }

    @Override
    protected void onStart() {
        fotos = ObjetosDoUsuario.getListagemDasFotosFotos();
        index = ObjetosDoUsuario.getListagemDasFotosIndex();
        setImage(null);
        super.onStart();
    }

    /*@Override
    protected void onDestroy() {
        ObjetosDoUsuario.ObjetosTemporarios.listagemDasFotos.index = null;
        ObjetosDoUsuario.ObjetosTemporarios.listagemDasFotos.fotos = null;
        super.onDestroy();
    }*/

    private void share() {
        //Bitmap bitmap = fotos.get(index).getIMG();
        Bitmap bitmap = FotoController.getInstance(this).buscarImg(fotos.get(index).getIMG());
        onClickApp2(bitmap);
    }

    /*public void onClickApp(Bitmap bitmap) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
            Uri imageUri = Uri.parse(path);
            Intent waIntent = new Intent(Intent.ACTION_SEND);
            String text = "#ViveiroDeAtitude\nImagem de ";
            Foto tmp_ft = fotos.get(index);
            if (tmp_ft.getEspecie() != null) {
                text += "Espécie ";
                text += EspecieController.getInstance(this)
                        .get_especie(tmp_ft.getEspecie()).getNome() + ".";
            } else
                text += "Área.";
            text += "\n\nLocal da Foto:\n";
            text += "https://www.google.com/maps?q=" + tmp_ft.getLatitude() + "," + tmp_ft.getLongitude();
            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            waIntent.setType("image/*");
            waIntent.putExtra(android.content.Intent.EXTRA_STREAM, imageUri);
            startActivity(Intent.createChooser(waIntent, "Compartilhar com"));
        } catch (Exception e) {
            Log.e("Error on sharing", e + " ");
        }
    }*/

    public void onClickApp2(@NotNull Bitmap bitmap) {
        try {
            File path = getExternalCacheDir();
            if (path != null && path.exists()) {
                File file = File.createTempFile("Title", ".jpeg", path);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bytes = stream.toByteArray();
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(bytes);
                fos.close();

                Uri imageUri = Uri.parse(file.getAbsolutePath());
                Intent waIntent = new Intent(Intent.ACTION_SEND);
                String text = "#viveirodeatitude\n" + getResources().getString(R.string.imgde) + " ";
                Foto tmp_ft = fotos.get(index);
                if (tmp_ft.getEspecie() != null) {
                    text += getResources().getString(R.string.compespecie) + " ";
                    text += EspecieController.getInstance(this)
                            .get_especie(tmp_ft.getEspecie()).getNome() + ".";
                } else
                    text += getResources().getString(R.string.area);
                text += getResources().getString(R.string.locaFoto);
                text += "https://www.google.com/maps?q=" + tmp_ft.getLatitude() + "," + tmp_ft.getLongitude();
                waIntent.putExtra(Intent.EXTRA_TEXT, text);
                waIntent.setType("image/*");
                waIntent.putExtra(android.content.Intent.EXTRA_STREAM, imageUri);
                startActivity(Intent.createChooser(waIntent, getResources().getString(R.string.Compartilharcom)));
            } else {
                Toast.makeText(this, getResources().getString(R.string.Erroaoacessarcache), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("Error on sharing", e + " ");
        }
    }

    @Override
    public void onImageDownloaded(@NotNull final Bitmap bitmap, @NotNull String url) {
        runOnUiThread(new Thread() {
            @Override
            public void run() {
                photoView.setImageBitmap(bitmap);
                progressBar.setVisibility(View.GONE);
            }
        });
        ObjetosDoUsuario.setListagemDasFotos(fotos, index);
    }

    @Override
    public void onImageDownloadError(@NotNull String cod) {
        runOnUiThread(new Thread() {
            @Override
            public void run() {
                photoView.setImageResource(R.drawable.plant);
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
