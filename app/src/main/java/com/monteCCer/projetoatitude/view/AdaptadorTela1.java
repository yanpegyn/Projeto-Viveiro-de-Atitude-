package com.monteCCer.projetoatitude.view;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.monteCCer.projetoatitude.DataSource.LoadImageAsync;
import com.monteCCer.projetoatitude.model.Especie;
import com.monteCCer.projetoatitude.model.Foto;
import com.monteCCer.projetoatitude.R;
import com.monteCCer.projetoatitude.controller.ObjetosDoUsuario;
import com.monteCCer.projetoatitude.model.ListenerAdapterOfImage;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/// AdaptadorTela1 é o adaptador da recicler view
public class AdaptadorTela1 extends RecyclerView.Adapter<AdaptadorTela1.MyView> {

    private Activity ctx;
    private List<Especie> list;
    private final int[] sampleSizes = {160, 160};
    private final Bitmap plant;

    AdaptadorTela1(@NotNull Activity ctx, List<Especie> list) {
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
        myview.nome.setText(list.get(i).getNome());
        myview.codigo = list.get(i).getID();
        myview.imagem.setImageBitmap(plant);

        new LoadImageAsync(list.get(i).getIcone(), myview, ctx, sampleSizes, myview.codigo).execute();

        final int ii = i;
        myview.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (list.get(ii).getIcone() != null) {
                    ArrayList<Foto> fotos = new ArrayList<>();
                    fotos.add(new Foto(null, null, list.get(ii).getIcone(), null, null, null));
                    ObjetosDoUsuario.setListagemDasFotos(fotos, 0);
                    ctx.startActivity(new Intent(ctx, ImageViewer.class));
                }
                return false;
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
            imagem = itemView.findViewById(R.id.card_image);
            cardView = itemView.findViewById(R.id.card);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Log.i("Card", "Card Clicado");
                    ctx.startActivity(new Intent(ctx, InformacoesActivity.class).putExtra(ObjetosDoUsuario.EXTRA_MESSAGE, codigo).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });
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
