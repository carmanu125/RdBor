package com.jcd.rdbordado.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jcd.rdbordado.R;
import com.jcd.rdbordado.entity.EPlaces;

import java.util.List;

/**
 * Created by Argosoft03 on 23/03/2017.
 */

public class SpinnerPlacesAdapter extends BaseAdapter {

    Context context;
    List<EPlaces> listPlaces;

    public SpinnerPlacesAdapter(Context context, List<EPlaces> listPlaces) {
        this.context = context;
        this.listPlaces = listPlaces;
    }

    @Override
    public int getCount() {
        return listPlaces.size();
    }

    @Override
    public Object getItem(int position) {
        return listPlaces.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listPlaces.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if(convertView == null){
            view = View.inflate(context,  R.layout.item_spi_places, null);
        }else{
            view = convertView;
        }

        EPlaces places = (EPlaces) getItem(position);

        TextView txtPlace = (TextView) view.findViewById(R.id.text1);
        txtPlace.setText(places.getName());

        return view;
    }
}
