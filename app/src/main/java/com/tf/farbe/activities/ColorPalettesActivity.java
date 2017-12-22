package com.tf.farbe.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.tf.farbe.R;
import com.tf.farbe.adapters.ColorPaletteAdapter;
import com.tf.farbe.models.ColorPalette;
import com.tf.farbe.utils.Constantes;
import com.tf.farbe.utils.RecyclerTouchListener;

import java.util.ArrayList;

public class ColorPalettesActivity extends AppCompatActivity {
    private ColorPaletteAdapter adaptador;
    LinearLayoutManager llm;
    ArrayList<ColorPalette> colorPalettes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_palettes);

        SharedPreferences preferences = getSharedPreferences(Constantes.SHAREDPREF, Context.MODE_PRIVATE);

        String s = preferences.getString(Constantes.LAST_COLOR_HEX,null);
//TODO borrar al implementar SQLite
        colorPalettes.add(
                new ColorPalette("#"+s,"#c1c1c1","#56555b","#636824","#a14a23","#"+s));
        colorPalettes.add(
                new ColorPalette("Prueba","#"+s,"#56555b","#636824","#a14a23","#"+s));

        //Inicializando RecyclerView
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv_colorPalettes);
        rv.setHasFixedSize(true);

        //Configurando el adaptor con la lista
        adaptador = new ColorPaletteAdapter(colorPalettes);
        llm = new LinearLayoutManager(this);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        rv.setLayoutManager(llm);
        rv.setAdapter(adaptador);

        rv.addOnItemTouchListener(new RecyclerTouchListener(this, rv, new RecyclerTouchListener.OnTouchActionListener() {
            @Override
            public void onLeftSwipe(View view, int position) {
            }

            @Override
            public void onRightSwipe(View view, int position) {
            }

            @Override
            public void onClick(View view, int position) {
                //Mostrar dialog para guardar paleta de colores.
                Toast.makeText(ColorPalettesActivity.this,
                        "Item "+colorPalettes.get(position).getPaletteName(),
                        Toast.LENGTH_SHORT).show();
            }
        }));

    }
}
