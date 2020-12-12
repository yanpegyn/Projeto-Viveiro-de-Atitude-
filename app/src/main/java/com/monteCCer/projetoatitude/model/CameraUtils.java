package com.monteCCer.projetoatitude.model;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import androidx.core.content.FileProvider;
import android.util.Log;

import com.monteCCer.projetoatitude.view.CameraActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraUtils {

    /**
     * Atualiza a galeria ao adicionar nova imagem / vídeo.
     * A Galeria não será atualizada em dispositivos mais antigos
     * até que o dispositivo seja reinicializado.
     */
    public static void refreshGallery(Context context, String filePath) {
        // Verifica o arquivo para que ele seja exibido na Galeria
        MediaScannerConnection.scanFile(context,
                new String[]{filePath}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
    }

    /**
     * Evitar OutOfMemory Exception
     */
    public static Bitmap optimizeBitmap(String filePath) {
        // Fabrica a bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        // Diminui a imagem para evitar OutOfMemory Exception de imagens grandes
        options.inSampleSize = CameraActivity.BITMAP_SAMPLE_SIZE;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * Verifica se o dispositivo tem camera. Desnecessario se
     * android:required="true" é usado no arquivo de manifest
     */
    public static boolean isDeviceSupportCamera(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public static Uri getOutputMediaFileUri(Context context, File file) {
        return FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
    }

    /**
     * Cria e retorna o arquivo de imagem ou de vídeo antes de abrir a câmera
     */
    public static File getOutputMediaFile(int type) {

        // Localização de sdcard externo
        File mediaStorageDir = new File(
                // TODO: 31/07/2019 Problema de Depreciação
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                CameraActivity.GALLERY_DIRECTORY_NAME);

        // Cria o diretório de armazenamento, se ele não existir
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e(CameraActivity.GALLERY_DIRECTORY_NAME, "Oops! Falha ao criar "
                        + CameraActivity.GALLERY_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Preparar a convenção de nomenclatura de arquivos
        // de mídia adiciona registro de data e hora
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == CameraActivity.MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + "" + CameraActivity.IMAGE_EXTENSION);
        } else if (type == CameraActivity.MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + "" + CameraActivity.VIDEO_EXTENSION);
        } else {
            return null;
        }

        return mediaFile;
    }

}
