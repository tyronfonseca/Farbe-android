package com.tf.farbe.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tf.farbe.R;
import com.tf.farbe.models.ColorPalette;
import com.tf.farbe.utils.Constantes;

import java.util.ArrayList;

/**
 * Created by TF on 30/10/2017.
 */

public class ColorPaletteAdapter extends RecyclerView.Adapter<ColorPaletteAdapter.ColorPaletteViewHolder> {
    private ArrayList<ColorPalette> colorPalettes;

    /**
     * Custom RecyclerView.Adapter que utiliza un layout personalizado para mostrar la paleta de
     * colores.
     * @param colorPalettesParam Objeto de tipo ColorPalette con el que llenara el RecycleView.
     */
    public ColorPaletteAdapter(ArrayList<ColorPalette> colorPalettesParam) {
        this.colorPalettes = colorPalettesParam;
    }

    @Override
    public ColorPaletteAdapter.ColorPaletteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        View rowView = inflater.inflate(R.layout.row_palette_layout, parent, false);

        ColorPaletteAdapter.ColorPaletteViewHolder viewHolder = new ColorPaletteAdapter.ColorPaletteViewHolder(rowView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ColorPaletteAdapter.ColorPaletteViewHolder holder, int position) {
        ColorPalette colorPalette = colorPalettes.get(position);

        holder.paletteName.setText(colorPalette.getPaletteName());
        holder.color1.setBackgroundColor(Color.parseColor(colorPalette.getColor1()));
        holder.color2.setBackgroundColor(Color.parseColor(colorPalette.getColor2()));
        holder.color3.setBackgroundColor(Color.parseColor(colorPalette.getColor3()));
        holder.color4.setBackgroundColor(Color.parseColor(colorPalette.getColor4()));
        holder.color5.setBackgroundColor(Color.parseColor(colorPalette.getColor5()));

        Log.d(Constantes.TAG,colorPalette.toString());
    }

    @Override
    public int getItemCount() {
        return colorPalettes.size();
    }

    static class ColorPaletteViewHolder extends RecyclerView.ViewHolder {
        TextView paletteName;
        View color1, color2, color3, color4, color5;

        public ColorPaletteViewHolder(View itemView) {
            super(itemView);
            paletteName = (TextView) itemView.findViewById(R.id.paletteName);
            color1 = itemView.findViewById(R.id.color1);
            color2 = itemView.findViewById(R.id.color2);
            color3 = itemView.findViewById(R.id.color3);
            color4 = itemView.findViewById(R.id.color4);
            color5 = itemView.findViewById(R.id.color5);
        }
    }
}
