package com.tf.farbe.sqlite;

import android.provider.BaseColumns;

/**
 * Created by TF on 14/11/2017.
 */

public class PaletteContract {
    private PaletteContract(){}
    /* Esquema de la base de datos para los productos*/
    public static class PaletteEntry implements BaseColumns {
        public static final String TABLE_NAME = "paletas";
        public static final String COLUMN_NAME = "nombre_color";
        public static final String COLUMN_COLOR_1 = "color_1";
        public static final String COLUMN_COLOR_2 = "color_2";
        public static final String COLUMN_COLOR_3 = "color_3";
        public static final String COLUMN_COLOR_4 = "color_4";
        public static final String COLUMN_COLOR_5 = "color_5";
    }
}
