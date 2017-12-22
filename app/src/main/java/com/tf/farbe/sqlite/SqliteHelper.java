package com.tf.farbe.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tf.farbe.models.ColorPalette;

import java.util.ArrayList;

/**
 * Created by TF on 14/11/2017.
 */

public class SqliteHelper {
    private SqliteOpenHelper openHelper;

    public SqliteHelper(Context context){
        this.openHelper = new SqliteOpenHelper(context);
    }

    public ArrayList<ColorPalette> getPalettes(){
        SQLiteDatabase db = openHelper.getReadableDatabase();
        String[] projection = {
                PaletteContract.PaletteEntry._ID,
                PaletteContract.PaletteEntry.COLUMN_NAME,
                PaletteContract.PaletteEntry.COLUMN_COLOR_1,
                PaletteContract.PaletteEntry.COLUMN_COLOR_2,
                PaletteContract.PaletteEntry.COLUMN_COLOR_3,
                PaletteContract.PaletteEntry.COLUMN_COLOR_4,
                PaletteContract.PaletteEntry.COLUMN_COLOR_5
        };

        String sortOrder = PaletteContract.PaletteEntry._ID + " DESC";
        Cursor cursor = db.query(
                PaletteContract.PaletteEntry.TABLE_NAME,//Nombre de la tabla
                projection,                             //Columnas a retornar
                null,                                   //Columnas utilizadas en WHERE
                null,                                   //Valores para la solicitud WHERE
                null,                                   //Grupo de filas
                null,                                   //filtrar grupo de filas
                sortOrder                               //Orden
        );

        ArrayList<ColorPalette> lista = new ArrayList<>();

        while(cursor.moveToNext()){
            lista.add(new ColorPalette(
                    cursor.getString(cursor.getColumnIndexOrThrow(PaletteContract.PaletteEntry.COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(PaletteContract.PaletteEntry.COLUMN_COLOR_1)),
                    cursor.getString(cursor.getColumnIndexOrThrow(PaletteContract.PaletteEntry.COLUMN_COLOR_2)),
                    cursor.getString(cursor.getColumnIndexOrThrow(PaletteContract.PaletteEntry.COLUMN_COLOR_3)),
                    cursor.getString(cursor.getColumnIndexOrThrow(PaletteContract.PaletteEntry.COLUMN_COLOR_4)),
                    cursor.getString(cursor.getColumnIndexOrThrow(PaletteContract.PaletteEntry.COLUMN_COLOR_5))
                    ));
        }
        cursor.close();

        return lista;
    }
}
