package com.monteCCer.projetoatitude.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.monteCCer.projetoatitude.DataDestiny.SendPics;
import com.monteCCer.projetoatitude.R;
import com.monteCCer.projetoatitude.model.CameraUtils;
import com.monteCCer.projetoatitude.model.Foto;
import com.monteCCer.projetoatitude.controller.AreaController;
import com.monteCCer.projetoatitude.controller.EsperaDeEnvioController;
import com.monteCCer.projetoatitude.controller.FotoController;
import com.monteCCer.projetoatitude.controller.LocationTrack;
import com.monteCCer.projetoatitude.controller.ObjetosDoUsuario;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {

    // Codigos de requisão da Atividade
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    //private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;

    // Chave para armazenar o caminho da imagem no estado savedInstance
    public static final String KEY_IMAGE_STORAGE_PATH = "image_path";

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    // Tamanho de amostragem de bitmap
    public static final int BITMAP_SAMPLE_SIZE = 8;

    // Nome do diretório da galeria para armazenar as imagens ou vídeos
    // TODO: 02/07/19 Nome da pasta da galeria
    public static final String GALLERY_DIRECTORY_NAME = "Projeto Atitude";

    // Extensões de arquivo de imagem e vídeo
    public static final String IMAGE_EXTENSION = "jpg";
    public static final String VIDEO_EXTENSION = "mp4";

    private static String imageStoragePath;
    /*
        private TextView txtDescription;
        private ImageViewer imgPreview;
        private VideoView videoPreview;
    */
    private Bitmap Capt_Image;

    private String id;
    Location location;
    LocationTrack locationTrack;
    private int modo; ///0 - Foto Normal, 1 - Foto de Area

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationTrack = new LocationTrack(this);
        //setContentView(R.layout.activity_camera);
        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        */
        // Verificando a disponibilidade da câmera
        if (!CameraUtils.isDeviceSupportCamera(getApplicationContext())) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.Nofoipossivelusaracmera),
                    Toast.LENGTH_LONG).show();
            finish();
        }
        ///Verifica a disponibilidade do GPS ou Internet
        location = getLocation();
        if (location == null)
            finish();
        else {
            // Get the Intent that started this activity and extract the string
            Intent intent = getIntent();
            id = intent.getStringExtra(ObjetosDoUsuario.EXTRA_MESSAGE);
            if (id == null)
                modo = 1;
            else
                modo = (id.equals("") ? 1 : 0);
            captureImage();
        }
        /*
        txtDescription = findViewById(R.id.txt_desc);
        imgPreview = findViewById(R.id.imgPreview);
        videoPreview = findViewById(R.id.videoPreview);
        Button btnCapturePicture = findViewById(R.id.btnCapturePicture);
        Button btnRecordVideo = findViewById(R.id.btnRecordVideo);
        */
        /*
         * Captura a imagem ao clicar no botão
         */
        /*
        btnCapturePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });*/
        /*
         * Grava o video ao clicar no botão
         */
        /*
        btnRecordVideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                captureVideo();
            }
        });*/
        // restaurar o caminho da imagem de armazenamento do estado da instância salvo,
        // caso contrário, o caminho será nulo na rotação do dispositivo
        restoreFromBundle(savedInstanceState);
    }

    /**
     * Cria um Objeto Foto contendo a imagem
     * e a Localização
     */
    public Foto Get_Image(Location location) {
        if (location != null) {
            // TODO: 20/09/2019 Aqui é adicionado a Data da Foto
            SimpleDateFormat formataData = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", getResources().getConfiguration().locale);
            Date date = new Date();
            String st = formataData.format(date);
            //foto.setIMG(FotoController.getInstance(this).salvarImg(null, this, Capt_Image));
            return new Foto("0", id.equals("") ? null : id, null, location.getLongitude(), location.getLatitude(), st);
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        locationTrack.stopListener();
        super.onDestroy();
    }

    /**
     * Pega Coordenadas latitude/longitude
     */

    public Location getLocation() {
        final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
        final long MIN_TIME_BW_UPDATES = 0;
        try {
            LocationManager locationManager;
            locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
            if (locationManager != null) {
                // getting GPS status
                boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                // getting network status
                boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (isGPSEnabled || isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Location loca = null;
                        if (isNetworkEnabled) {
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener());
                            for (int i = 0; i < 3; i++) {
                                loca = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                if (loca != null)
                                    break;
                            }
                        }
                        // if GPS Enabled get lat/long using GPS Services
                        if (loca == null) {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener());
                            for (int i = 0; i < 3; i++) {
                                loca = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (loca != null)
                                    break;
                            }
                        }
                        locationManager.removeUpdates(locationListener());
                        return loca;
                    } else {
                        Toast.makeText(CameraActivity.this, getResources().getString(R.string.ConcedaasPermiesnovamente), Toast.LENGTH_LONG).show();
                        locationManager.removeUpdates(locationListener());
                        finish();
                    }
                } else {
                    // no network provider is enabled
                    Log.i("Gps", "No providers");
                    Toast.makeText(CameraActivity.this, getResources().getString(R.string.PorFavorLigueaFunoLocalizao), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    locationManager.removeUpdates(locationListener());
                    finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private LocationListener locationListener() {
        return new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
    }

    /**
     * Restaurando o caminho da imagem da loja a partir do estado da instância salva
     */
    private void restoreFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_IMAGE_STORAGE_PATH)) {
                imageStoragePath = savedInstanceState.getString(KEY_IMAGE_STORAGE_PATH);
                if (!TextUtils.isEmpty(imageStoragePath)) {
                    if (imageStoragePath.substring(imageStoragePath.lastIndexOf(".")).equals("." + IMAGE_EXTENSION)) {
                        previewCapturedImage();
                    }/* else if (imageStoragePath.substring(imageStoragePath.lastIndexOf(".")).equals("." + VIDEO_EXTENSION)) {
                        previewVideo();
                    }*/
                }
            }
        }
    }

    /**
     * Iniciará a captura de imagem solicitada ao aplicativo da câmera
     */
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (file != null) {
            imageStoragePath = file.getAbsolutePath();
        }

        Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // iniciar a captura de imagem (Intent)
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    /**
     * Salvando o caminho da imagem armazenada no estado da instância salva
     */
    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // salvar o url do arquivo no pacote, pois ele será nulo na rotação da tela
        outState.putString(KEY_IMAGE_STORAGE_PATH, imageStoragePath);
    }

    /**
     * Restaurando o caminho da imagem do estado da instância salva
     */
    @Override
    protected void onRestoreInstanceState(@NotNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // obter o URL do arquivo
        imageStoragePath = savedInstanceState.getString(KEY_IMAGE_STORAGE_PATH);
    }

    /*
     * Iniciando o aplicativo da câmera para gravar vídeo
     */
    /*
    private void captureVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_VIDEO);
        if (file != null) {
            imageStoragePath = file.getAbsolutePath();
        }

        Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);

        // Definir qualidade de vídeo
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file

        // iniciar a captura de vídeo (Intent)
        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
    }
    */

    /**
     * O método de onActivityResult será chamado depois de fechar a câmera
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // se for Foto
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Atualizando a galeria
                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);

                // Capturou com sucesso a imagem
                // Exibi-la no modo de exibição de imagem
                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                // O usuário cancelou a Captura de imagem
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.OusuriocancelouaCapturadeimagem), Toast.LENGTH_SHORT)
                        .show();
                finish();
            } else {
                // Falha ao capturar a imagem
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.DesculpeFalhaaocapturaraimagem), Toast.LENGTH_SHORT)
                        .show();
                finish();
            }
        }
        /* Senão se for Video
        else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Atualizando a galeria
                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);

                // Vídeo gravado com sucesso
                // Pré-visualizar o vídeo gravado
                previewVideo();
            } else if (resultCode == RESULT_CANCELED) {
                // Gravação cancelada pelo usuário
                Toast.makeText(getApplicationContext(),
                        "Gravação cancelada pelo usuário", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // Falha ao gravar vídeo
                Toast.makeText(getApplicationContext(),
                        "Desculpe! Falha ao gravar vídeo", Toast.LENGTH_SHORT)
                        .show();
            }
        }*/
    }

    /**
     * Mosrar imagem da galeria
     */
    private void previewCapturedImage() {
        try {
            // Ocultar a pré-visualização do vídeo
            /*txtDescription.setVisibility(View.GONE);
            videoPreview.setVisibility(View.GONE);

            imgPreview.setVisibility(View.VISIBLE);*/

            // TODO: 31/07/2019 Aqui é onde copia a imagem
            Capt_Image = CameraUtils.optimizeBitmap(imageStoragePath);
            //imgPreview.setImageBitmap(bitmap);

            location = getLocation();
            if (location != null) {
                Foto foto = Get_Image(location);
                String FotoId = imageStoragePath.replaceAll("[^0-9]", "");
                //Log.i("IMG ID:",FotoId);
                foto.setID(FotoId);
                /*
                TextView Localization = findViewById(R.id.Localization);
                Formatter fmt_lat = new Formatter();
                fmt_lat.format("%.4f", foto.getLatitude());
                Formatter fmt_lon = new Formatter();
                fmt_lon.format("%.4f", foto.getLongitude());
                String Localizacao = "Latitude: " + fmt_lat + " | Longitude: " + fmt_lon;
                Localization.setText(Localizacao);
                */
                EsperaDeEnvioController esperaDeEnvioController = EsperaDeEnvioController.getInstance(getBaseContext());
                if (modo == 0) {
                    ///Fotos de espécie
                    Intent intent = new Intent("recreate");
                    sendBroadcast(intent);
                    finish();
                    if (ObjetosDoUsuario.getEspecies().size() > 0) {
                        if (ObjetosDoUsuario.addPicToEspecie(id, foto))
                            Log.i("CameraActivity", "Pic Saved");
                        // Aqui adicionamos a foto na lista de fotos e depois na lista de envio
                        FotoController fotoController = FotoController.getInstance(getBaseContext());
                        fotoController.salvar(foto, CameraActivity.this, Capt_Image);
                    }
                } else {
                    ///Fotos de Área
                    AreaController areaController = AreaController.getInstance(getBaseContext());
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.ImagemCapturadacomSucesso), Toast.LENGTH_LONG).show();
                    areaController.salvar(foto, CameraActivity.this, Capt_Image);
                }
                esperaDeEnvioController.salvar(foto);
                new SendPics(CameraActivity.this).execute();
                finish();
            } else {
                Toast.makeText(CameraActivity.this, getResources().getString(R.string.FalhaaopegarsuaLocalizao), Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /*
     * Mostrar video no VideoView
     */
    /*
    private void previewVideo() {
        try {
            // ocultar a pré-visualização da imagem
            txtDescription.setVisibility(View.GONE);
            imgPreview.setVisibility(View.GONE);

            videoPreview.setVisibility(View.VISIBLE);
            videoPreview.setVideoPath(imageStoragePath);
            // Começa reproduzir
            videoPreview.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */
}
