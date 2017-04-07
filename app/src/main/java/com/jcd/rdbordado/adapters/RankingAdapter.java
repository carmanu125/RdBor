package com.jcd.rdbordado.adapters;

import android.app.Activity;
import android.content.Context;
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
import com.jcd.rdbordado.entity.EPlaces;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Carmanu on 11/03/2017.
 */

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.ContentHolder>{

    static List<EPlaces> list;
    static Activity context;

    public RankingAdapter(List<EPlaces> list, Activity context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RankingAdapter.ContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_ranking, parent, false);
        ContentHolder holder = new ContentHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RankingAdapter.ContentHolder holder, int position) {
        EPlaces places = list.get(position);
        holder.loadValuesItem(places, holder);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

     static class ContentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

         TextView txtName ;
         TextView txtType ;
         ImageView imgPhoto;
         int idImage ;


         public ContentHolder(View itemView) {
             super(itemView);

             txtName = (TextView) itemView.findViewById(R.id.item_places_name);
             txtType = (TextView) itemView.findViewById(R.id.item_places_type);
             imgPhoto = (ImageView) itemView.findViewById(R.id.item_places_photo);
             idImage = context.getResources().getIdentifier("logo_cicle", "mipmap", context.getPackageName());


             itemView.setOnClickListener(this);
         }

         @Override
         public void onClick(View v) {
             Intent intent= new Intent(context, ProfilePlacesActivity.class);
             EPlaces place = list.get(getLayoutPosition());
             intent.putExtra("Place",  place);
             context.startActivityForResult(intent, 2);
         }

         public void loadValuesItem(EPlaces places, ContentHolder holder) {
             txtName.setText(places.getName());
             txtType.setText(places.getShort_description());
             if(!places.getUrlImage().equals("")) {

                 try {
                     Picasso.with(context)
                             .load(places.getUrlImage())
                             //.placeholder(R.mipmap.logo_azul)
                             .error(R.mipmap.logo_cicle)
                             .into(holder.imgPhoto);
                 } catch (Exception e) {
                     Log.e("Error Picasso: ", e.toString());
                 }
             }else {
                 imgPhoto.setImageResource(idImage);
             }

         }


     }
}
