package com.jcd.rdbordado;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcd.rdbordado.adapters.RankingAdapter;
import com.jcd.rdbordado.entity.EPlaces;

import java.util.ArrayList;
import java.util.List;

public class RankingActivity extends Fragment {

    List<EPlaces> listPlaces;

    RecyclerView rcListaRanking;
    RankingAdapter adapterList;


    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_ranking, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListPlaces();

        rcListaRanking = (RecyclerView) view.findViewById(R.id.rv_rank_lista);
        adapterList = new RankingAdapter(listPlaces, getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
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
