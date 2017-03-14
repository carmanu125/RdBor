package com.jcd.rdbordado;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jcd.rdbordado.adapters.RankingAdapter;
import com.jcd.rdbordado.entity.EPlaces;

import java.util.ArrayList;
import java.util.List;

public class RankingActivity extends AppCompatActivity {

    List<EPlaces> listPlaces;

    RecyclerView rcListaRanking;
    RankingAdapter adapterList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        getListPlaces();

        rcListaRanking = (RecyclerView) findViewById(R.id.rv_rank_lista);
        adapterList = new RankingAdapter(listPlaces, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcListaRanking.setLayoutManager(layoutManager);
        rcListaRanking.setAdapter(adapterList);

    }

    private void getListPlaces() {

        listPlaces = new ArrayList<>();

        EPlaces places = new EPlaces();
        places.setName("Frixio");
        places.setDescription("Frixio almacen de bordados");
        places.setShort_description("Frixio Almacen");
        places.setLatLong("4.750513, -75.902918");
        listPlaces.add(places);

        for(int i = 0; i< 5; i++){
            places = new EPlaces();

            places.setName("Taller " + i);
            places.setDescription("Taller " + i + "almacen de bordados");
            places.setShort_description("Almacen " +i);
            places.setLatLong("");

            listPlaces.add(places);
        }

    }
}
