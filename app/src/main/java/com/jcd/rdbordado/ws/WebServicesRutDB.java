package com.jcd.rdbordado.ws;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
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

/**
 * Created by Argosoft03 on 02/03/2017.
 */

public class WebServicesRutDB {


    private final String URL_WEB_SERVICES = "http://rutabordado.somee.com/api/";
    private final String URL_GET_PLACES = "Places/places/";




    public void getPlaces(){
        TareaWSListar listar = new TareaWSListar();
        listar.execute();
    }


    //Tarea Asíncrona para llamar al WS de listado en segundo plano
    private class TareaWSListar extends AsyncTask<String,Integer,Boolean> {

        private ArrayList<EPlaces> listPlaces;

        protected Boolean doInBackground(String... params) {
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

                JSONObject jObject = new JSONObject(jsonString);

                JSONArray myClients = jObject.getJSONArray("stocks_Stocks]");
                //JSONArray myClients = jObject.getJSONArray("");

                for (int i = 0; i < myClients.length(); i++) {
                    JSONObject obj = myClients.getJSONObject(i);
                //for (int i = 0; i < jObject.length(); i++) {
                    //JSONObject obj = jObject.getJSONObject(i);
/*
                    result += "Posicion_: " + i + "\n";
                    result += "ID_STOCK _: " + obj.getString("id_stock") + "\n";
                    result += "id_Product _: " + obj.getString("id_Product") + "\n";
                    result += "Stock _: " + obj.getString("Stock") + "\n";
                    result += "Value_Current _: " + obj.getString("Value_Current") + "\n";
                    result += "Value_min _: " + obj.getString("Value_min") + "\n";
                    result += "Value_MinPercent _: " + obj.getString("Value_MinPercent") + "\n";
                    result += "date_last_update _: " + obj.getString("date_last_update") + "\n";
                    result += "date_current _: " + obj.getString("date_current") + "\n";*/
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
            return true;
        }

        protected void onPostExecute(Boolean result) {
/*
            if (result)
            {
                //Rellenamos la lista con los nombres de los clientes
                //Rellenamos la lista con los resultados
                ArrayAdapter<String> adaptador =
                        new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_list_item_1, clientes);

                lstClientes.setAdapter(adaptador);
            }*/
        }
    }

}
