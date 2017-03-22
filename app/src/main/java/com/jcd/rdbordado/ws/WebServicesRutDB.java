package com.jcd.rdbordado.ws;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.jcd.rdbordado.MainDrawerActivity;
import com.jcd.rdbordado.entity.EPlaces;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Argosoft03 on 02/03/2017.
 */

public class WebServicesRutDB {

    Context context;


    //private final String URL_WEB_SERVICES = "http://rutabordado.somee.com/api/";
    private final String URL_WEB_SERVICES = "http://192.168.1.110:56394/api/";
    private final String URL_GET_PLACES = "Places/places/";


    public WebServicesRutDB(Context context) {
        this.context = context;
    }

    public void getPlaces(){
        TareaWSListar listar = new TareaWSListar(context);
        listar.execute();
    }

    //Tarea Asíncrona para llamar al WS de listado en segundo plano
    private class TareaWSListar extends AsyncTask<String,Integer,List<EPlaces>> {



        private List<EPlaces> listPlaces = new ArrayList<>();
        Context context;

        public TareaWSListar(Context context) {
            this.context = context;
        }

        protected List<EPlaces> doInBackground(String... params) {
            try {
                HttpURLConnection urlConnection = null;

                URL url = new URL(URL_WEB_SERVICES + URL_GET_PLACES);

                Log.e("URL WS: ", url.toString());

                urlConnection = (HttpURLConnection) url.openConnection();


                urlConnection.setRequestMethod("GET");

                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);

                urlConnection.setDoOutput(true);

                urlConnection.connect();

                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

                //char[] buffer = new char[1024];

                String jsonString = new String();

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();

                jsonString = sb.toString();

                Log.e("JSON: " , jsonString);

                //Gson gson = new Gson();
                //Places frutas = gson.fromJson(jsonString, EPlaces.class);


                JSONArray respJSON = new JSONArray(jsonString);

                //List<EPlaces> listPlaces = new ArrayList<>();

                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);
                    EPlaces places = new EPlaces();

                    places.setId(obj.getInt("id"));
                    places.setName(obj.getString("name"));
                    places.setShort_description(obj.getString("short_description"));
                    places.setDescription(obj.getString("description"));
                    places.setAddress(obj.getString("address"));
                    places.setPhone(obj.getString("phone"));
                    places.setEmail(obj.getString("email"));
                    places.setLatLong(obj.getString("latLong"));
                    places.setRanking(obj.getString("Ranking"));

                    listPlaces.add(places);
                }



            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return listPlaces;
        }

        protected void onPostExecute(List<EPlaces> result) {

            MainDrawerActivity.navigationView.getMenu().getItem(0).setChecked(true);
        }
    }

}
