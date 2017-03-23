package com.jcd.rdbordado.ws;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.jcd.rdbordado.MainDrawerActivity;
import com.jcd.rdbordado.entity.EPlaces;
import com.jcd.rdbordado.local.RutaDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
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
    MainDrawerActivity activity;


    //private final String URL_WEB_SERVICES = "http://rutabordado.somee.com/api/";
    private final String URL_WEB_SERVICES = "http://192.168.1.110:56394/api/";
    private final String URL_GET_PLACES = "Places/places/";
    private final String URL_POST_DEVICES = "Devices/discountInMarker/";


    public WebServicesRutDB(Context context, MainDrawerActivity activity) {
        this.context = context;
        this.activity = activity;
    }

    public WebServicesRutDB(Context context) {
        this.context = context;
    }

    public void getPlaces(){
        TareaWSListar listar = new TareaWSListar(context);
        listar.execute();
    }

    public void posDevices(){
        TareaWSPostDevices listar = new TareaWSPostDevices(context);
        listar.execute();
    }

    //Tarea Asíncrona para llamar al WS de listado en segundo plano
    private class TareaWSListar extends AsyncTask<String,Integer,List<EPlaces>> {

        ProgressDialog progressDialog;
        private List<EPlaces> listPlaces = new ArrayList<>();
        Context context;
        RutaDB nDB ;

        public TareaWSListar(Context context) {
            this.context = context;
            nDB = new RutaDB(context);
        }

        @Override
        protected void onPreExecute() {

            progressDialog =ProgressDialog.show(context, "Titulo", "Mensaje", true, false);
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

                if(listPlaces.size() > 0) {

                    saveLocal(listPlaces);
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

        private void saveLocal(List<EPlaces> listPlaces) {
            try {
                nDB.openDB();
                nDB.deletePlaces();
                nDB.insertPlaces(listPlaces);
                nDB.closeDB();
            } catch (Exception e) {
                Log.e("Error BD: ", e.getMessage());
            }
        }

        protected void onPostExecute(List<EPlaces> result) {

            progressDialog.dismiss();
            //activity.navigationView.getMenu().getItem(0).setChecked(true);
            activity.onNavigationItemSelected(activity.navigationView.getMenu().getItem(0));
        }
    }

    private class TareaWSPostDevices extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        Context context;

        public TareaWSPostDevices(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {

            progressDialog =ProgressDialog.show(context, "Titulo", "Mensaje", true, false);
        }

        @Override
        protected String doInBackground(String... params) {
            String jsonString = "";

            try {

                HttpURLConnection urlConnection = null;

                URL url = new URL(URL_WEB_SERVICES + URL_GET_PLACES);
                Log.e("URL WS: ", url.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestMethod("POST");

                urlConnection.setReadTimeout(25000 /* milliseconds */);
                urlConnection.setConnectTimeout(30000 /* milliseconds */);
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);

                JSONObject cliente = new JSONObject();
                cliente.put("brand", Build.BRAND);
                cliente.put("device", Build.DEVICE);
                cliente.put("hardware", Build.HARDWARE);
                cliente.put("imei", Build.ID);
                cliente.put("model", Build.MODEL);
                cliente.put("serial", Build.SERIAL);
                cliente.put("user", Build.USER);
                cliente.put("versionSdk", Build.VERSION.SDK);

                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(cliente.toString());
                wr.flush();


                int HttpResult = urlConnection.getResponseCode();
                if (HttpResult == HttpURLConnection.HTTP_OK) {


                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));

                    //char[] buffer = new char[1024];


                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();

                    jsonString = sb.toString();


                } else {
                    return urlConnection.getResponseMessage();
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
            return jsonString;

        }

        @Override
        protected void onPostExecute(String result) {

            if(result.equals("true")){
                //Correcto

            } else if(result.equals("Error")){
                //Ya Existe

            }else{
                //No se encontro respuesta de insercion

            }

            progressDialog.dismiss();
        }
    }

}
