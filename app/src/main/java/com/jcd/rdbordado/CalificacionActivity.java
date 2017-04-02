package com.jcd.rdbordado;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jcd.rdbordado.ws.WebServicesRutDB;

public class CalificacionActivity extends AppCompatActivity {

    RatingBar ratingBar;
    TextView txt_discount_value;
    static float ratingCurrent = 0f;
    String IdPlace = "2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificacion);

        IdPlace = getIntent().getStringExtra("IdPlace");

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        txt_discount_value = (TextView) findViewById(R.id.txt_discount_value);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                txt_discount_value.setText("Calificacion: " + String.valueOf(rating));
                ratingCurrent = rating;
            }
        });
    }

    public void sendRating(View view) {

        WebServicesRutDB webServicesRutDB = new WebServicesRutDB(this);
        webServicesRutDB.posRating(IdPlace,String.valueOf(ratingCurrent), this);

    }
}
