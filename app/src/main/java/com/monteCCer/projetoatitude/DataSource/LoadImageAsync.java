package com.monteCCer.projetoatitude.DataSource;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.monteCCer.projetoatitude.controller.FotoController;
import com.monteCCer.projetoatitude.model.ListenerAdapterOfImage;

import java.lang.ref.WeakReference;

public class LoadImageAsync extends AsyncTask<Void, Void, Void> {

    private String url;
    private WeakReference<ListenerAdapterOfImage> myView;
    private WeakReference<Context> ctx;
    private int[] sampleSizes;
    private String cod;

    public LoadImageAsync(String url, ListenerAdapterOfImage myView, Context ctx, int[] sampleSizes, String cod) {
        this.url = url;
        this.myView = new WeakReference<>(myView);
        this.ctx = new WeakReference<>(ctx);
        this.sampleSizes = sampleSizes;
        this.cod = cod;
    }

    public LoadImageAsync(String url, ListenerAdapterOfImage myView, Context ctx, String cod) {
        this.url = url;
        this.myView = new WeakReference<>(myView);
        this.ctx = new WeakReference<>(ctx);
        this.cod = cod;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (ctx != null) {
            Bitmap bitmap;
            if (sampleSizes != null)
                bitmap = FotoController.getInstance(ctx.get()).buscarImg(url, sampleSizes[0], sampleSizes[1]);
            else
                bitmap = FotoController.getInstance(ctx.get()).buscarImg(url);
            if (myView != null) {
                if (bitmap != null)
                    myView.get().onImageDownloaded(bitmap, cod);
                else myView.get().onImageDownloadError(cod);
            }
        }
        return null;
    }
}
