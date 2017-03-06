package com.jcd.rdbordado;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jcd.rdbordado.adapters.ImageGalleryAdapter;
import com.jcd.rdbordado.async.DownloadImageTask;
import com.jcd.rdbordado.entity.EPlaces;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ProfilePlacesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ImageView imProfile;
    TextView txtDescription;
    public static Gallery gallery;

    EPlaces place;
    public static ArrayList<Bitmap> lisImage;

    public static ImageGalleryAdapter adapterGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_places);

        place = (EPlaces) getIntent().getSerializableExtra("Place");

        gallery = (Gallery) findViewById(R.id.gallery1);
        imProfile = (ImageView) findViewById(R.id.img_profile_place_photo);
        txtDescription = (TextView) findViewById(R.id.txt_profile_place_description);

        txtDescription.setText("Frixio es una empresa dedicada al diseño y confeccion de prendas de vestir bordadas a mano, estamos comprometidos con diseñar prendas exclusivas, innovadoras y de alta calidad, ofrecer una excelente atención a nuestros clientes y realizar los despachos de manera oportuna.\n" +
                "Mision: Ser la empresa líder en la fabricación y comercialización de prendas de vestir bordadas a mano, comprometida con la calidad del producto, innovación y diseños ajustados a la petición de nuestros clientes. actualizando los requerimiento de tecnología y mercadeo adecuado donde cada proceso cumpla con los estandares de la más alta calidad sin perder la escencia del arte manual de los bordados de Cartago.\n" +
                "Visión: Continuar siendo la empresa líder a nivel nacional de los bordados hechos a mano y consolidar nuestro mercado internacional con la dinámica y compromiso social que nos ha caracterizado.");

        //adapterGallery = new ImageGalleryAdapter(this, );
        gallery.setOnItemSelectedListener(this);
        //gallery.setAdapter(adapterGallery);
        getListImageBitmap();


    }

    private ArrayList<Bitmap> getListImageBitmap() {

        lisImage = new ArrayList<>();

        new DownloadImageTask(adapterGallery, gallery, imProfile, this).execute("http://bordadosdecartago.com/wp-content/uploads/2015/08/descarga.png","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSRCqfSgPrFdgPDxu4XKx1sc7DamVjFO-9Fwva9eZi7xspg356Z8BkigMg", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSNvCSNziQNvbn6KxJ33afBILIQq_xqleE5_TbbLfSWYkir1wvk", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSFShIyhcBwy9dl2Juslotxrzz2sivNZu58J_MnGBMe-a3_E3bUtw");

        return lisImage;
    }

    private void loadValuesPlace() {



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        // display the images selected
        ImageView imageView = (ImageView) findViewById(R.id.img_profile_place_photo);
        Bitmap bitmap = (Bitmap) parent.getItemAtPosition(position);;
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
