package com.jcd.rdbordado;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcd.rdbordado.adapters.RankingAdapter;
import com.jcd.rdbordado.entity.EPlaces;
import com.jcd.rdbordado.local.RutaDB;

import java.util.ArrayList;
import java.util.List;

public class RankingActivity extends Fragment {

    List<EPlaces> listPlaces;

    RecyclerView rcListaRanking;
    RankingAdapter adapterList;


    RutaDB nDB;
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

        nDB = new RutaDB(getContext());

        getListPlaces();

        rcListaRanking = (RecyclerView) view.findViewById(R.id.rv_rank_lista);
        adapterList = new RankingAdapter(listPlaces, getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rcListaRanking.setLayoutManager(layoutManager);
        rcListaRanking.setAdapter(adapterList);


    }

    private void getListPlaces() {

        try {
            nDB.openDB();

            listPlaces = nDB.listPlacesTop10();
            nDB.closeDB();

        } catch (Exception e) {
            Log.e("Error BD: ", e.getMessage());
        }

    }
}
