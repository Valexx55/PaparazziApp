package com.example.paparazziapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TomarFotoDisco extends AppCompatActivity {

    private static final String[] PERMISOS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
    public static final String DIRECTORIO_PUBLICO_PROPIO = Environment.getExternalStorageDirectory().getPath() + "/CFTIC";
    private Uri foto_uri;
    private String ruta_foto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomar_foto_disco);

        ActivityCompat.requestPermissions(this, PERMISOS, 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super was deleted as is not necessary
        this.foto_uri = crearFicheroImagen();
        Intent intent_foto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent_foto.putExtra(MediaStore.EXTRA_OUTPUT,foto_uri);

        startActivityForResult(intent_foto, 300);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch(resultCode) {

            case RESULT_OK:
                //LA FOTO FUE BIEN
                ImageView imageView = findViewById(R.id.foto_tomada);
                imageView.setImageURI(foto_uri);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                actualizarGaleriaTrasCaptura();
                break;
            case RESULT_CANCELED:
                break;
        }
    }

    /*Si se decide guardar la foto capturada, debo crear antes un fichero y pasar la URI (ruta) del mismo.
            Para eso vale esta función: para crear el fichero donde será almacenado la foto y su URI

             @return La URI que identifica al fichero.Null si la cosa fue mal*/

    private Uri crearFicheroImagen() {
        Uri uri_dest = null;
        String momento_actual = null;
        String nombre_fichero = null;
        File f = null;
        String ruta_captura_foto = null;

        momento_actual = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()); //así nos garantizamos emplear un sufijo aleatorio: el nombre del archivo de la imagen incluirá el momento exacto
        nombre_fichero = "CFTIC_" + momento_actual + ".jpg";


        File fdir = new File(DIRECTORIO_PUBLICO_PROPIO);
        if ((fdir.exists()) || (fdir.mkdir())) {

            ruta_foto = DIRECTORIO_PUBLICO_PROPIO + "/"+nombre_fichero;
            Log.d("MIAPP", "RUTA FOTO = " + ruta_foto);
            f = new File(ruta_foto);

            try {
                if (f.createNewFile()) {
                    Log.d("MIAPP", "Fichero creado");
                    uri_dest = FileProvider.getUriForFile(this, "com.example.paparazziapp.fileprovider", f);
                    //uri_dest = Uri.fromFile(f);//métod antiguo, falla aunque se refiera a unra ruta pública
                    Log.d("MIAPP", "URI FOTO = " + uri_dest.toString());

                } else {
                    Log.d("MIAPP", "Fichero NO creado (ya existía)");
                }
            } catch (IOException e) {
                Log.e("MIAPP", "Error creando el fichero", e);
            }


        }



        return uri_dest;
    }

    /**
     * En caso de ser la carpeta pública el destino de nuestra foto, se actuaiza así para que salga la foto tomada
     * este método funciona pq generamos nosotros la URI a partir de una ruta, de forma que es de tipo file:///
     * si te refieres a este método con una uri del tipo content:/// NO ACTUALIZA
     */
    private void actualizarGaleriaTrasCaptura ()
    {
        Log.d("MIAPP", "Ruta captura foto " + ruta_foto);
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(ruta_foto);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);

    }
}
