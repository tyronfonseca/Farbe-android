package com.tf.farbe.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tf.farbe.R;
import com.tf.farbe.utils.ColorHelper;
import com.tf.farbe.utils.Constantes;
import com.tf.farbe.utils.ImageSurfaceView;
import com.tf.farbe.utils.TextViewUIHelper;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, Camera.PreviewCallback {

    private ImageSurfaceView imageSurfaceView;
    private static Camera camera;
    TextView textView;
    View backgroundView;
    FloatingActionButton fbGetColor,fbPalette;
    FrameLayout cameraPreviewLayout;
    static SharedPreferences sharedPreferences;

    ColorHelper colorHelper;
    TextViewUIHelper uiHelper;

    int frameHeight, frameWidth;
    protected static final int POINTER_RADIUS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(Constantes.SHAREDPREF, Context.MODE_PRIVATE);

        textView = (TextView)findViewById(R.id.texto);
        backgroundView = findViewById(R.id.background_view);

        fbGetColor = (FloatingActionButton)findViewById(R.id.getColorFab);
        fbGetColor.setOnClickListener(this);

        fbPalette = (FloatingActionButton) findViewById(R.id.paletteFab);
        fbPalette.setOnClickListener(this);

        cameraPreviewLayout = (FrameLayout)findViewById(R.id.content);

        //Inicializar clases
        colorHelper = new ColorHelper(this);

        camera = checkDeviceCamera();
        setupPreviewCamera();

        //Animador del TextView
        uiHelper = new TextViewUIHelper(backgroundView,textView,fbGetColor,colorHelper
                ,MainActivity.this);

        //Dimensiones de la camara.
        frameHeight = camera.getParameters().getPreviewSize().height;
        frameWidth = camera.getParameters().getPreviewSize().width;

        //Verificaciones SharedPreferences
        if(!sharedPreferences.getBoolean(Constantes.DISPLAY_MESSAGE,false)){
              displayMessage();
        }

        String hexName = sharedPreferences.getString(Constantes.LAST_COLOR_HEX,null);
        //Conseguir ultimo color escaneado.
        if(hexName != null){
            uiHelper.changeTextView(hexName,
                    sharedPreferences.getString(Constantes.LAST_COLOR_NAME,null),
                    sharedPreferences.getInt(Constantes.LAST_COLOR_INV,0));
            textView.setBackgroundColor(sharedPreferences.getInt(Constantes.LAST_COLOR,0));
            fbPalette.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.paletteFab:
                startActivity(new Intent(MainActivity.this, ColorPalettesActivity.class));
                break;
            case R.id.getColorFab:
                //Conseguir color
                uiHelper.Animate();
                fbPalette.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Liberar camara.
        releaseCamera();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        //Re-crear camara.
        if(camera == null){
            camera = checkDeviceCamera();
            setupPreviewCamera();
        }
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        int midX = frameWidth/2;
        int midY = frameHeight/2;
        // Conseguir color desde la preview de la camara.
        for (int i = 0; i <= POINTER_RADIUS; i++) {
            for (int j = 0; j <= POINTER_RADIUS; j++) {
                colorHelper.addColorFromYUV420(data, (i * POINTER_RADIUS + j + 1),
                        (midX - POINTER_RADIUS) + i, (midY - POINTER_RADIUS) + j,
                        frameWidth, frameHeight);
            }
        }
    }

    /**
     * Metodo encargado de configurar la camara.
     * @return Retorna el objeto de tipo Camera ya inicializado.
     */
    private static Camera checkDeviceCamera(){
        Camera mCamera = null;
        try{
            mCamera = Camera.open();
            mCamera.setDisplayOrientation(90);
            Camera.Parameters cameraParam = camera.getParameters();
            cameraParam.setPreviewFormat(ImageFormat.NV21);
            cameraParam.setPreviewSize(240,80);
            camera.setParameters(cameraParam);

        }catch(Exception e){
            e.printStackTrace();
        }
        return mCamera;
    }

    /**
     * Metodo encargado de configurar la preview donde se desplegara la imagen
     */
    private void setupPreviewCamera(){
        imageSurfaceView = new ImageSurfaceView(MainActivity.this,camera);
        imageSurfaceView.setVisibility(View.VISIBLE);
        cameraPreviewLayout.addView(imageSurfaceView);
        camera.setPreviewCallback(this);
    }

    /**
     * Metodo encargado de liberar la camara para que otras aplicaciones la utilicen.
     */
    private void releaseCamera() {
        synchronized (this) {
            if (camera != null) {
                camera.stopPreview();
                camera.setPreviewCallback(null);
                imageSurfaceView.getHolder().removeCallback(imageSurfaceView);
                imageSurfaceView.setVisibility(View.GONE);
                camera.release();// liberar camara
                camera = null;
            }
        }
    }

    /**
     * Mostrar mensaje de bienvenida
     */
    private void displayMessage(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(Constantes.DISPLAY_MESSAGE, true);
                        editor.apply();
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.title).setPositiveButton(R.string.positive, dialogClickListener)
                .setMessage(R.string.message)
                .setNegativeButton(R.string.negative, dialogClickListener).show();
    }

}
