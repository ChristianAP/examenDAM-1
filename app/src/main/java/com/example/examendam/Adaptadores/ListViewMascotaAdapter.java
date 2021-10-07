package com.example.examendam.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.examendam.Modelo.Mascota;
import com.example.examendam.R;

import java.util.ArrayList;

public class ListViewMascotaAdapter extends BaseAdapter {
    Context context;
    ArrayList<Mascota> mascotaData;
    LayoutInflater layoutInflater;
    Mascota mascotaModel;

    public ListViewMascotaAdapter(Context context, ArrayList<Mascota> mascotaData) {
        this.context = context;
        this.mascotaData = mascotaData;
        layoutInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
        );
    }

    @Override
    public int getCount() {
        return mascotaData.size();
    }

    @Override
    public Object getItem(int i) {
        return mascotaData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        if (rowView==null){
            rowView = layoutInflater.inflate(R.layout.lista_mascotas,
                    null,
                    true);
        }
        //Enlazamos vista
        TextView mascota = rowView.findViewById(R.id.lblMascotaList);
        TextView dueño = rowView.findViewById(R.id.lblDueñoList);

        mascotaModel = mascotaData.get(position);
        mascota.setText(mascotaModel.getMascota());
        dueño.setText(mascotaModel.getDueño());
        return rowView;
    }
}
