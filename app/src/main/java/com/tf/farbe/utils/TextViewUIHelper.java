package com.tf.farbe.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import io.codetail.animation.ViewAnimationUtils;

/**
 * Created by TF on 30/10/2017.
 */

public class TextViewUIHelper {
    private View backgroundView;
    private TextView textView;
    FloatingActionButton floatButton;
    ColorHelper colorHelper;
    int color;
    Context context;
    SharedPreferences preferences;


    /**
     * Clase encargada de la animacion y el cambio de colores del textView.
     * @param viewParam View en el background.
     * @param textViewParam TextView a cambiar.
     * @param buttonParam Floating button que activa la animacion.
     * @param colorHelperParam ColorHelper ya inicializado.
     * @param contextParam Contexto desde donde se llama la clase.
     */
    public TextViewUIHelper(View viewParam, TextView textViewParam ,FloatingActionButton buttonParam,
                           ColorHelper colorHelperParam, Context contextParam){
        this.backgroundView = viewParam;
        this.textView = textViewParam;
        this.floatButton = buttonParam;
        this.colorHelper = colorHelperParam;
        this.context = contextParam;
        this.preferences = context.getSharedPreferences(Constantes.SHAREDPREF,Context.MODE_PRIVATE);
    }

    /**
     * Cambiar los colores del TextView y el animar el Background cuando cambia de color.
     */
    public void Animate(){
        //Color escaneado
        this.color = colorHelper.getColor();

        //Color invertido
        int inverseColor = colorHelper.getInverseColor();

        //Convertir de RGB a HEX
        String hexColor = colorHelper.getColorHexCode();
        String colorName = colorHelper.getColorName(hexColor);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constantes.LAST_COLOR_NAME,colorName);
        editor.putString(Constantes.LAST_COLOR_HEX,hexColor);
        editor.putInt(Constantes.LAST_COLOR,color);
        editor.putInt(Constantes.LAST_COLOR_INV,inverseColor);
        editor.apply();

        changeTextView(hexColor,colorName,inverseColor);

        //Consiguiendo color anterior del textView
        Drawable drawable = textView.getBackground();
        if (drawable instanceof ColorDrawable) {
            ColorDrawable colorDrawable = (ColorDrawable) drawable;
            backgroundView.setBackgroundColor(colorDrawable.getColor());
        }

        // centro del actionbutton en x
        int cx = (floatButton.getLeft() + floatButton.getRight()) / 2;
        // centro del actionbutton en y
        //TODO remplazar si se puede, cy siempre da 1.
        int cy = (int) ((int) floatButton.getY() / floatButton.getX());

        // Conseguir radio final del circulo
        int dx = Math.max(cx, textView.getWidth() - cx);
        int dy = Math.max(cy, textView.getHeight() - cy);
        float finalRadius = (float) Math.hypot(dx, dy);

        //Inicializando animacion
        Animator animator =
                ViewAnimationUtils.createCircularReveal(textView, cx, cy, 0, finalRadius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                //Cambiando background del textView.
                textView.setBackgroundColor(color);
                //Evitar click mientras la animacion esta activa.
                floatButton.setClickable(false);
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                //Permitir clicks
                floatButton.setClickable(true);
            }
        });
        animator.setDuration(1000);
        animator.start();
    }

    /**
     * Cambiar texto y color del textView.
     *
     * @param hexColorText Texto que se usara.
     * @param inverseColor Color que tendra el texto.
     */
    public void changeTextView(String hexColorText, String colorName, int inverseColor){
        //Cambiar el texto y el color.
        textView.setText("Color: #"+hexColorText+"\n"+colorName);
        textView.setTextColor(inverseColor);
    }
}