package com.jcd.rdbordado;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jcd.rdbordado.ws.WebServicesRutDB;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btMap(View view) {

        Intent act = new Intent(this, MapsActivity.class);
        startActivity(act);

    }

    public void btRanking(View view) {

        WebServicesRutDB ws = new WebServicesRutDB();
        ws.getPlaces();

    }

    public void btVideo(View view) {
    }
}
