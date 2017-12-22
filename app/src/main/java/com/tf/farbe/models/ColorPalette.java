package com.tf.farbe.models;

/**
 * Created by TF on 30/10/2017.
 */

public class ColorPalette {
    String color1,color2,color3,color4,color5,paletteName;

    /**
     * Objecto que se utilizara en los recycleViews
     * @param color1    Primer color.
     * @param color2    Segundo color.
     * @param color3    Tercer color.
     * @param color4    Cuarto color.
     * @param color5    Quinto color.
     * @param paletteName   Nombre que tendra la paleta de colores.
     */
    public ColorPalette(String paletteName, String color1, String color2, String color3, String color4, String color5) {
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.color4 = color4;
        this.color5 = color5;
        this.paletteName = paletteName;
    }

    public String getColor1() {
        return color1;
    }

    public void setColor1(String color1) {
        this.color1 = color1;
    }

    public String getColor2() {
        return color2;
    }

    public void setColor2(String color2) {
        this.color2 = color2;
    }

    public String getColor3() {
        return color3;
    }

    public void setColor3(String color3) {
        this.color3 = color3;
    }

    public String getColor4() {
        return color4;
    }

    public void setColor4(String color4) {
        this.color4 = color4;
    }

    public String getColor5() {
        return color5;
    }

    public void setColor5(String color5) {
        this.color5 = color5;
    }

    public String getPaletteName() {
        return paletteName;
    }

    public void setPaletteName(String paletteName) {
        this.paletteName = paletteName;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
