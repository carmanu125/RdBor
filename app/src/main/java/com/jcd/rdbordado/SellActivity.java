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
import com.jcd.rdbordado.adapters.SellsAdapter;
import com.jcd.rdbordado.entity.EPlaces;
import com.jcd.rdbordado.local.RutaDB;

import java.util.List;

public class SellActivity extends Fragment {


    List<EPlaces> listPlaces;

    RecyclerView rcListaRanking;
    SellsAdapter adapterList;

    View view;
    RutaDB nDB;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_sell, container , false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        nDB = new RutaDB(getContext());

        getListPlaces();

        rcListaRanking = (RecyclerView) view.findViewById(R.id.rv_sells_lista);
        adapterList = new SellsAdapter(listPlaces, getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rcListaRanking.setLayoutManager(layoutManager);
        rcListaRanking.setAdapter(adapterList);


    }

    private void getListPlaces() {
        try {
            nDB.openDB();

            listPlaces = nDB.listPlaces();
            nDB.closeDB();

        } catch (Exception e) {
            Log.e("Error BD: ", e.getMessage());
        }
    }

}
