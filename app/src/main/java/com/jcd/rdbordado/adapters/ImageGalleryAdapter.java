package com.jcd.rdbordado.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.jcd.rdbordado.R;

import java.util.ArrayList;

/**
 * Created by Carmanu on 04/03/2017.
 */

public class ImageGalleryAdapter extends BaseAdapter {

    private Context context;
    private int itemBackground;
    ArrayList<Bitmap> listImage;

    public ImageGalleryAdapter(Context context, ArrayList<Bitmap> listImage) {
        this.context = context;
        this.listImage = listImage;

        // sets a grey background; wraps around the images
        TypedArray a =context.obtainStyledAttributes(R.styleable.MyGallery);
        itemBackground = a.getResourceId(R.styleable.MyGallery_android_galleryItemBackground, 0);
        a.recycle();

    }

    @Override
    public int getCount() {
        return listImage.size();
    }

    @Override
    public Object getItem(int position) {
        return listImage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(listImage.get(position));
        imageView.setLayoutParams(new Gallery.LayoutParams(100, 100));
        imageView.setBackgroundResource(itemBackground);
        return imageView;
    }
}
