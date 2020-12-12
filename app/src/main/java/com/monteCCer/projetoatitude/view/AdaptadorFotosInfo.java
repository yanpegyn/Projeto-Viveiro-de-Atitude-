package com.monteCCer.projetoatitude.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.monteCCer.projetoatitude.DataSource.LoadImageAsync;
import com.monteCCer.projetoatitude.model.Foto;
import com.monteCCer.projetoatitude.R;
import com.monteCCer.projetoatitude.controller.ObjetosDoUsuario;
import com.monteCCer.projetoatitude.model.ListenerAdapterOfImage;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdaptadorFotosInfo extends RecyclerView.Adapter<AdaptadorFotosInfo.MyView> {
    private Activity ctx;
    private List<Foto> list;
    private final int[] sampleSizes = {160, 160};
    private final Bitmap plant;

    AdaptadorFotosInfo(@NotNull Activity ctx, List<Foto> list) {
        this.ctx = ctx;
        this.list = list;
        this.plant = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.plant);
    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.list_item_muda, viewGroup, false);
        return new MyView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView myview, int i) {
        myview.codigo = list.get(i).getID();
        /*Bitmap bitmap = FotoController.getInstance(ctx).buscarImg(list.get(i).getIMG(), sampleSizes[0], sampleSizes[1]);
        if (bitmap == null)
            myview.imagem.setImageResource(R.drawable.plant);
        else myview.imagem.setImageBitmap(bitmap);*/
        myview.imagem.setImageBitmap(plant);
        new LoadImageAsync(list.get(i).getIMG(), myview, ctx, sampleSizes, myview.codigo).execute();
        //myview.imagem.setImageResource(R.drawable.img_beta);
        final int ii = i;
        myview.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObjetosDoUsuario.setListagemDasFotos(list, ii);
                ctx.startActivity(new Intent(ctx, ImageViewer.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyView extends RecyclerView.ViewHolder implements ListenerAdapterOfImage {
        String codigo;
        TextView nome;
        ImageView imagem;
        CardView cardView;

        MyView(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.card_text);
            nome.setVisibility(View.GONE);
            imagem = itemView.findViewById(R.id.card_image);
            cardView = itemView.findViewById(R.id.card);
        }

        @Override
        public void onImageDownloaded(@NotNull final Bitmap bitmap, @NotNull String cod) {
            if (cod.equals(codigo)) {
                ctx.runOnUiThread(new Thread() {
                    @Override
                    public void run() {
                        imagem.setImageBitmap(bitmap);
                    }
                });
            }
        }

        @Override
        public void onImageDownloadError(@NotNull String cod) {
            if (cod.equals(codigo)) {
                ctx.runOnUiThread(new Thread() {
                    @Override
                    public void run() {
                        imagem.setImageBitmap(plant);
                    }
                });
            }
        }
    }
}
