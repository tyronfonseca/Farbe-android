package com.tf.farbe.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.tf.farbe.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

/**
 * Created by TF on 29/10/2017.
 */

public class ColorHelper {

    private Context context;
    private int[] rgb;
    private JSONObject jsonObject;
    private JSONArray arrayJson;

    /**
     * Clase encargada del manejo de los datos de los colores, ademas de su conversion a distintos
     * formatos.
     * @param contextParam Contexto de la activity donde se llama.
     */
    public ColorHelper(Context contextParam){
        this.context = contextParam;
        this.rgb = new int[3];
        getDataJson();
    }

    /**
     * Conseguir datos del archivo .json en la carpeta de recursos Raw.
     */
    private void getDataJson(){
        InputStream inputStream = context.getResources().openRawResource(R.raw.color_names_v3);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int ctr;
        //Leer el archivo y transformarlo en datos que se puedan manejar.
        try {
            ctr = inputStream.read();
            while (ctr != -1) {
                byteArrayOutputStream.write(ctr);
                ctr = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String data = byteArrayOutputStream.toString();//Datos a String

        try {
            this.jsonObject = new JSONObject(data);//String a JsonObject
            this.arrayJson = jsonObject.getJSONArray("data");//Conseguir Array
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("JSON ERROR", e.toString());
        }
    }

    /**
     * Convertir el color de valores RGB a int Color.
     * @return color convertido en entero.
     */
    public int getColor(){
        return Color.rgb(rgb[0], rgb[1], rgb[2]);
    }

    /**
     * Conseguir el color invertido desde un array.
     * @return color convertido en entero.
     */
    public int getInverseColor(){
        return Color.rgb(255-rgb[0],255-rgb[1],255-rgb[2]);
    }

    /**
     * Convertir Color(int) en String hexadecimal.
     * @return Color en formato Hexadecimal. Ejm: FFFFFF
     */
    public String getColorHexCode(){
        return String.format("%06X", (0xFFFFFF & getColor()));
    }

    /**
     * Buscar nombre del color en un array sacado de un archivo .json.
     * Adaptado del projecto de JavaScript http://chir.ag/projects/ntc/
     * @param colorStr Color hexadecimal a buscar. Ejm: ffffff
     * @return Nombre del color (a veces aproximado).
     */
    public String getColorName(String colorStr){
        int[] color = hex2Rgb(colorStr);
        int r = color[0], g = color[1], b = color[2]; //Valores rgb del color a analizar

        int[] colorHsl = hex2Hsl(colorStr);
        int h = colorHsl[0], s = colorHsl[1], l = colorHsl[2]; //Valores hsl del color a analizar

        int[] rgb, hsl;

        int df=-1, ndf = 0, ndf1 = 0, ndf2 = 0;

        String hexColor,nameColor = "Nombre no encontrado";

        try {
            for(int i=0;i<arrayJson.length();i++){
                //Color del archivo json
                hexColor = arrayJson.getJSONArray(i).getString(0);
                //Color esta en el archivo json.
                if(colorStr.equals(hexColor)){
                    return  arrayJson.getJSONArray(i).getString(1);
                }

                rgb = hex2Rgb(hexColor);//Espacio de color rgb del color en el json
                hsl = hex2Hsl(hexColor);//Espacio de color hsl del color en el json

                //Distancia entre el color analizado y los colores en el archivo .json.
                //Euclidean distance
                ndf1 = (int) Math.sqrt(Math.pow(r - rgb[0],2)+Math.pow(g - rgb[1],2)+Math.pow(b - rgb[2],2));
                ndf2 = (int) Math.sqrt(Math.pow(h - hsl[0],2)+Math.pow(s - hsl[1],2)+Math.pow(l - hsl[2],2));

                ndf = ndf1 + ndf2 * 2;

                //Distancia mas corta
                if(df < 0 || df > ndf)
                {
                    df = ndf;
                    nameColor = "Posible nombre: "+arrayJson.getJSONArray(i).getString(1);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return nameColor;
    }

    /**
     * Convertir color hexadecimal al espacio de color RGB.
     * El espacio RGB tiene tres parametros Rojo(Red), Verde(Green) y Azul(Blue).
     * @param colorStr String en formato hexadecimal a convertir
     * @return Color en formato RGB.
     */
    public int[] hex2Rgb(String colorStr) {
        int[] data = new int[3];
        data[0] = Integer.valueOf(colorStr.substring( 0, 2 ), 16);//Rojo
        data[1] = Integer.valueOf(colorStr.substring( 2, 4 ), 16);//Verde
        data[2] = Integer.valueOf(colorStr.substring( 4, 6 ), 16);//Azul
        return data;
    }

    /**
     * Convertir color hexadecimal al espacio de color HSL.
     * El espacio HSL tiene tres parametros Tono(Hue), Saturacion(Saturation)
     * y Luminosidad(Luminosity). Adaptado de http://chir.ag/projects/ntc/
     *
     * @param colorStr String en formato hexadecimal a convertir.
     * @return Color en formato HSL.
     */
    public int[] hex2Hsl(String colorStr){
        int[]  hsl = new int[3];
        //Conseguir espacio RGB
        double r = (double) Integer.valueOf(colorStr.substring( 0, 2 ), 16 ) / 255;
        double g = (double) Integer.valueOf(colorStr.substring( 2, 4 ), 16 ) / 255;
        double b = (double) Integer.valueOf(colorStr.substring( 4, 6 ), 16 ) / 255;
        double h, s, l;

        double min = Math.min(r, Math.min(g, b));
        double max = Math.max(r, Math.max(g, b));
        double delta = max - min;

        //Luminosidad
        l = (min + max) / 2;
        //Saturacion
        s = 0;
        if(l > 0 && l < 1){
            s = delta / (l < 0.5 ? (2 * l) : (2 - 2 * l));
        }
        //Tono
        h = 0;
        if(delta > 0){
            if(max == r && max != g) h += (g - b) / delta;
            if(max == g && max != b) h += (2 + (b - r) / delta);
            if(max == b && max != r) h += (4 + (r - g) / delta);
            h /= 6;
        }
        hsl[0] = (int) (h * 255); //HUE
        hsl[1] = (int) (s * 255); //SATURATION
        hsl[2] = (int) (l * 255); //LUMINOSITY

        return hsl;
    }

    /**
     * Convertir datos de una image en espacio de color YUV420 a RGB.
     *
     * @param data Datos en formato YUV420.
     * @param count Contador.
     * @param x Posicion en x.
     * @param y Poisicon en y.
     * @param width Largo de la imagen.
     * @param height Ancho de la imagen.
     */
    public void addColorFromYUV420(byte[] data, int count, int x, int y, int width, int height) {
        // The code converting YUV420 to rgb format is highly inspired from this post http://stackoverflow.com/a/10125048
        final int size = width * height;
        final int Y = data[y * width + x] & 0xff;
        final int xby2 = x / 2;
        final int yby2 = y / 2;

        final float V = (float) (data[size + 2 * xby2 + yby2 * width] & 0xff) - 128.0f;
        final float U = (float) (data[size + 2 * xby2 + 1 + yby2 * width] & 0xff) - 128.0f;

        // Do the YUV -> RGB conversion
        float Yf = 1.164f * ((float) Y) - 16.0f;
        int red = (int) (Yf + 1.596f * V);
        int green = (int) (Yf - 0.813f * V - 0.391f * U);
        int blue = (int) (Yf + 2.018f * U);

        // Clip rgb values to [0-255]
        red = red < 0 ? 0 : red > 255 ? 255 : red;
        green = green < 0 ? 0 : green > 255 ? 255 : green;
        blue = blue < 0 ? 0 : blue > 255 ? 255 : blue;

        this.rgb[0] += (red - rgb[0]) / count;
        this.rgb[1] += (green - rgb[1]) / count;
        this.rgb[2] += (blue - rgb[2]) / count;
    }
}
