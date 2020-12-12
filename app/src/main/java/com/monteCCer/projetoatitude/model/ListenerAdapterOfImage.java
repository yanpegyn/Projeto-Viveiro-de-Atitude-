package com.monteCCer.projetoatitude.model;

import android.graphics.Bitmap;

import org.jetbrains.annotations.NotNull;

public interface ListenerAdapterOfImage {

    void onImageDownloaded(@NotNull final Bitmap bitmap, @NotNull String url);

    void onImageDownloadError(@NotNull String cod);
}
