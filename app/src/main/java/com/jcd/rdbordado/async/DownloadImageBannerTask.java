package com.jcd.rdbordado.async;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.jcd.rdbordado.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Carmanu on 25/03/2017.
 */

public class DownloadImageBannerTask extends AsyncTask<String, Void, Bitmap> {

    ImageView img;
    Context context;

    public DownloadImageBannerTask(ImageView img, Context context) {
        this.img = img;
        this.context = context;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {

            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                URL url = new URL(urldisplay);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                mIcon11 = BitmapFactory.decodeStream(input);

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();

            }

        return mIcon11;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(bitmap != null){
            img.setImageBitmap(bitmap);
            //img.setScaleType(ImageView.ScaleType.FIT_XY);
        }else{

            //Toast.makeText(context, "No hay conexion con el servidor", Toast.LENGTH_SHORT).show();
        }


    }
}
