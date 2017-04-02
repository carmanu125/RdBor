package com.jcd.rdbordado.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcd.rdbordado.ProfilePlacesActivity;
import com.jcd.rdbordado.R;
import com.jcd.rdbordado.async.DownloadImageTask;
import com.jcd.rdbordado.entity.EPlaces;
import com.jcd.rdbordado.ws.WebServicesRutDB;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Carmanu on 26/03/2017.
 */

public class SellsAdapter extends RecyclerView.Adapter<SellsAdapter.SellsHolder> {

    static List<EPlaces> list;
    static Activity context;

    public SellsAdapter(List<EPlaces> list, Activity context) {

        this.list = list;
        this.context = context;
    }

    @Override
    public SellsAdapter.SellsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_ranking, parent, false);
        SellsAdapter.SellsHolder holder = new SellsAdapter.SellsHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(SellsAdapter.SellsHolder holder, int position) {
        EPlaces places = list.get(position);
        holder.loadValuesItem(places);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class SellsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView txtName ;
        TextView txtType ;
        ImageView imgPhoto;


        @Override
        public void onClick(View v) {

            WebServicesRutDB webServicesRutDB = new WebServicesRutDB(context);
            webServicesRutDB.getPlacesImage(String.valueOf(list.get(getLayoutPosition()).getId()));

        }

        public SellsHolder(View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.item_places_name);
            txtType = (TextView) itemView.findViewById(R.id.item_places_type);
            imgPhoto = (ImageView) itemView.findViewById(R.id.item_places_photo);

            itemView.setOnClickListener(this);
        }

        public void loadValuesItem(EPlaces places) {
            txtName.setText(places.getName());
            txtType.setText(places.getShort_description());
            try {
                Picasso.with(context)
                        .load(places.getUrlImage())
                        //.placeholder(R.mipmap.logo_azul)
                        .error(R.mipmap.logo_cicle)
                        .into(imgPhoto);
            } catch (Exception e) {
                Log.e("Error Picasso: ", e.toString());
            }
        }
    }

}
