package com.jcd.rdbordado.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcd.rdbordado.ProfilePlacesActivity;
import com.jcd.rdbordado.R;
import com.jcd.rdbordado.entity.EPlaces;

import java.util.List;

/**
 * Created by Argosoft03 on 22/03/2017.
 */
public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.ContentHolderDirectory>{

    static List<EPlaces> listPlaces;
    static Activity context;

    public DirectoryAdapter(List<EPlaces> listPlaces, Activity context) {
        this.listPlaces = listPlaces;
        this.context = context;
    }

    @Override
    public ContentHolderDirectory onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_directory, parent, false);
        DirectoryAdapter.ContentHolderDirectory holder = new ContentHolderDirectory(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ContentHolderDirectory holder, int position) {
        EPlaces places = listPlaces.get(position);
        holder.loadValuesItem(places);
    }

    @Override
    public int getItemCount() {
        return listPlaces.size();
    }

    static class ContentHolderDirectory extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtName;
        TextView txtAddress;
        ImageView imgPhoto;

        public ContentHolderDirectory(View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.item_direct_name);
            txtAddress = (TextView) itemView.findViewById(R.id.item_direct_address);
            imgPhoto = (ImageView) itemView.findViewById(R.id.item_places_photo);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ProfilePlacesActivity.class);
            EPlaces place = listPlaces.get(getLayoutPosition());
            intent.putExtra("Place", place);
            context.startActivity(intent);
        }

        public void loadValuesItem(EPlaces places) {
            txtName.setText(places.getName());
            txtAddress.setText(places.getAddress());
            //txtName.setText(places.);
        }
    }
}
