package com.jcd.rdbordado.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcd.rdbordado.R;
import com.jcd.rdbordado.entity.EPlaces;

import java.util.List;

/**
 * Created by Carmanu on 11/03/2017.
 */

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.ContentHolder>{

    List<EPlaces> list;

    public RankingAdapter(List<EPlaces> list) {
        this.list = list;
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
        holder.loadValuesItem(places);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

     static class ContentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

         TextView txtName ;
         TextView txtType ;
         ImageView imgPhoto;

        public ContentHolder(View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.item_places_name);
            txtType = (TextView) itemView.findViewById(R.id.item_places_type);
            imgPhoto = (ImageView) itemView.findViewById(R.id.item_places_photo);

            itemView.setOnClickListener(this);
        }

         @Override
         public void onClick(View v) {

         }

         public void loadValuesItem(EPlaces places){
             txtName.setText(places.getName());
             txtType.setText(places.getShort_description());
             //txtName.setText(places.);
         }


     }
}
