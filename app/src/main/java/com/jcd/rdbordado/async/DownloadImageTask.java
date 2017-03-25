package com.jcd.rdbordado.async;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Gallery;
import android.widget.ImageView;

import com.jcd.rdbordado.ProfilePlacesActivity;
import com.jcd.rdbordado.adapters.ImageGalleryAdapter;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Carmanu on 04/03/2017.
 */

public class DownloadImageTask extends AsyncTask<String, Void, ArrayList<Bitmap>> {

    Gallery gallery;
    ImageView img;
    Context context;
    public DownloadImageTask(ImageGalleryAdapter galleryAdapter, Gallery gallery, ImageView img, Context context) {
        this.gallery = gallery;
        this.img = img;
        this.context = context;
    }

    @Override
    protected ArrayList<Bitmap> doInBackground(String... urls) {

        ArrayList<Bitmap> list = new ArrayList<>();
        for (int i = 0 ; i< urls.length; i++){

            String urldisplay = urls[i];
            Bitmap mIcon11 = null;
            try {
                URL url = new URL(urldisplay);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                mIcon11 = BitmapFactory.decodeStream(input);


                list.add(mIcon11);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
        }

        return list;
    }

    @Override
    protected void onPostExecute(ArrayList<Bitmap> bitmap) {
        ProfilePlacesActivity.lisImage = bitmap;
        img.setImageBitmap(bitmap.get(0));
        ProfilePlacesActivity.adapterGallery = new ImageGalleryAdapter(context, ProfilePlacesActivity.lisImage);
        //ProfilePlacesActivity.gallery.setAdapter(ProfilePlacesActivity.adapterGallery);
    }
}
