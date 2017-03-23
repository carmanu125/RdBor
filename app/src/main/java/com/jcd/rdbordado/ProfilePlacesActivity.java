package com.jcd.rdbordado;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcd.rdbordado.adapters.ImageGalleryAdapter;
import com.jcd.rdbordado.async.DownloadImageTask;
import com.jcd.rdbordado.entity.EPlaces;

import java.util.ArrayList;

public class ProfilePlacesActivity extends Activity implements AdapterView.OnItemSelectedListener {

    ImageView imProfile;
    TextView txtDescription;
    TextView txtAddress;
    TextView txtEmail;
    TextView txtName;
    TextView txtShort;

    public static Gallery gallery;

    //EPlaces place;
    public static ArrayList<Bitmap> lisImage;

    public static ImageGalleryAdapter adapterGallery;

    EPlaces place;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_places);

        Log.e("Activity, " , "Profiles");
        place = (EPlaces) getIntent().getSerializableExtra("Place");

        gallery = (Gallery) findViewById(R.id.gallery1);
        imProfile = (ImageView) findViewById(R.id.img_profile_place_photo);
        txtDescription = (TextView) findViewById(R.id.txt_profile_place_description);
        txtAddress = (TextView) findViewById(R.id.txt_profile_place_add_current);
        txtEmail = (TextView) findViewById(R.id.txt_profile_place_email);
        txtName = (TextView) findViewById(R.id.txt_profile_place_name);
        txtShort = (TextView) findViewById(R.id.txt_profile_place_short);

        txtDescription.setText(place.getDescription());
        txtAddress.setText(place.getAddress());
        txtEmail.setText(place.getEmail());
        txtName.setText(place.getName());
        txtShort.setText(place.getShort_description());

        //adapterGallery = new ImageGalleryAdapter(this, );
        gallery.setOnItemSelectedListener(ProfilePlacesActivity.this);
        //gallery.setAdapter(adapterGallery);
        getListImageBitmap();
    }

    private ArrayList<Bitmap> getListImageBitmap() {

        lisImage = new ArrayList<>();

        new DownloadImageTask(adapterGallery, gallery, imProfile, this).execute("http://bordadosdecartago.com/wp-content/uploads/2015/08/descarga.png", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSRCqfSgPrFdgPDxu4XKx1sc7DamVjFO-9Fwva9eZi7xspg356Z8BkigMg", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSNvCSNziQNvbn6KxJ33afBILIQq_xqleE5_TbbLfSWYkir1wvk", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSFShIyhcBwy9dl2Juslotxrzz2sivNZu58J_MnGBMe-a3_E3bUtw");

        return lisImage;
    }

    private void loadValuesPlace() {


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        // display the images selected
        ImageView imageView = (ImageView) findViewById(R.id.img_profile_place_photo);
        Bitmap bitmap = (Bitmap) parent.getItemAtPosition(position);

        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void CallPlace(View view) {
        Intent intent = new Intent(Intent.ACTION_CALL);

        intent.setData(Uri.parse("tel:" + place.getPhone()));

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    1);
            return;
        }
        startActivity(intent);
    }
}
