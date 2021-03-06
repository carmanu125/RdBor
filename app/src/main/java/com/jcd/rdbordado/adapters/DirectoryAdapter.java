package com.jcd.rdbordado.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
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
        holder.loadValuesItem(places, holder);
    }

    @Override
    public int getItemCount() {
        return listPlaces.size();
    }

    static class ContentHolderDirectory extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtName;
        TextView txtAddress;
        ImageView imgPhoto;
        int idImage ;

        public ContentHolderDirectory(View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.item_direct_name);
            txtAddress = (TextView) itemView.findViewById(R.id.item_direct_address);
            imgPhoto = (ImageView) itemView.findViewById(R.id.item_direct_photo);
            idImage = context.getResources().getIdentifier("logo_cicle", "mipmap", context.getPackageName());

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ProfilePlacesActivity.class);
            EPlaces place = listPlaces.get(getLayoutPosition());
            intent.putExtra("Place", place);
            context.startActivityForResult(intent, 2);
        }

        public void loadValuesItem(EPlaces places, ContentHolderDirectory holder) {
            txtName.setText(places.getName());
            txtAddress.setText(places.getAddress());
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
            }else{
                imgPhoto.setImageResource(idImage);
            }
        }
    }
}
