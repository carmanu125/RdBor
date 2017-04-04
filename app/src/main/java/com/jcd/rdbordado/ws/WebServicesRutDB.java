package com.jcd.rdbordado.ws;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jcd.rdbordado.DiscountActivity;
import com.jcd.rdbordado.MainDrawerActivity;
import com.jcd.rdbordado.async.DownloadImageTask;
import com.jcd.rdbordado.entity.EPlaces;
import com.jcd.rdbordado.entity.EPlacesImage;
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


    public static final String URL_WEB_SERVICES = " http://rutadelbordado.somee.com/api/";
    public static final String URL_WEB = " http://rutadelbordado.somee.com/";
    public static final String URL_WEB_IMAGE = "images/places/";
    //public static final String URL_WEB_SERVICES = "http://192.168.1.110:56394/api/";
    private final String URL_GET_PLACES = "Places/places/";
    public static final String URL_GET_PLACES_IMAGES = "Places/ImagesPlaces/";
    public static final String URL_POST_DEVICES = "Devices/discountInMarker/";
    public static final String URL_POST_RATING = "Ranking/RankingPlace/";


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

    public void getPlacesImage(String idPlace){
        TareaWSListarImages listar = new TareaWSListarImages(context);
        listar.execute(idPlace);
    }

    public void posDevices(String idPlace){
        TareaWSPostDevices sendDevices = new TareaWSPostDevices(context);
        sendDevices.execute(idPlace);
    }

    public void posRating(String idPlace, String value, Activity activity){
        TareaWSPostRating sendRating = new TareaWSPostRating(context, activity);
        sendRating.execute(idPlace, value);
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

            progressDialog =ProgressDialog.show(context, "Por favor espere", "Conectando con el servidor", true, false);
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
                    places.setUrlImage(obj.getString("Url_image"));
                    places.setUrlLogo(obj.getString("Url_logo"));

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

            progressDialog =ProgressDialog.show(context, "Enviando", "Por favor Espere", true, false);
        }

        @Override
        protected String doInBackground(String... params) {
            String jsonString = "";

            try {

                HttpURLConnection urlConnection = null;

                URL url = new URL(URL_WEB_SERVICES + URL_POST_DEVICES + params[0]);

                Log.e("URL WS: ", url.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestMethod("POST");

                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);

                TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

                JSONObject cliente   = new JSONObject();
                //cliente.put("brand", Build.BRAND);
                //cliente.put("device", Build.DEVICE);
                //cliente.put("hardware", Build.HARDWARE);
                cliente.put("imei", telephonyManager.getDeviceId());
                cliente.put("model", Build.MODEL);
                cliente.put("serial", Build.SERIAL);
                cliente.put("user", Build.USER);
                cliente.put("versionSdk", Build.VERSION.SDK);


                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(cliente.toString());
                wr.flush();


                StringBuilder sb = new StringBuilder();
                int HttpResult = urlConnection.getResponseCode();
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line.replace("\"", ""));
                    }
                    br.close();
                    System.out.println("" + sb.toString());
                    jsonString = sb.toString();
                } else {
                    System.out.println(urlConnection.getResponseMessage());
                    jsonString = urlConnection.getResponseMessage();
                }

            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);

            }

            return jsonString;

        }

        @Override
        protected void onPostExecute(String valueResponse) {

            if(valueResponse.equals("True")){
                //Correcto
                DiscountActivity.txtQrCode.setText("Descuento Aceptado!");
            } else{
                //Ya Existe
                DiscountActivity.txtQrCode.setText("No es posible, su descuento ya ha sido usado!");

            }
            progressDialog.dismiss();
        }
    }

    private class TareaWSListarImages extends AsyncTask<String,Integer, String[]>{

        ProgressDialog progressDialog;
        private List<EPlacesImage> listPlacesImage = new ArrayList<>();
        Context context;
        RutaDB nDB ;

        public TareaWSListarImages(Context context) {
            this.context = context;
            nDB = new RutaDB(context);
        }

        @Override
        protected void onPreExecute() {

            progressDialog =ProgressDialog.show(context, "Por favor espere", "Conectando con el servidor", true, false);
        }

        protected String[] doInBackground(String... params) {
            String[]listaImages = new String[]{} ;

            try {
                HttpURLConnection urlConnection = null;

                URL url = new URL(URL_WEB_SERVICES + URL_GET_PLACES_IMAGES + params[0]);

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
                listaImages = new String[respJSON.length()];
                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);
                    EPlacesImage placesImage = new EPlacesImage();

                    placesImage.setId(obj.getInt("id"));
                    placesImage.setUrl(obj.getString("url"));
                    placesImage.setIdPlace(obj.getInt("id_Place"));

                    //listPlacesImage.add(placesImage);
                    listaImages[i] = placesImage.getUrl();
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
            return listaImages;
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

        protected void onPostExecute(String[] result) {

            progressDialog.dismiss();
            //activity.navigationView.getMenu().getItem(0).setChecked(true);
            new DownloadImageTask(context).execute(result);
        }
    }


    private class TareaWSPostRating extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        Context context;
        Activity activity;

        public TareaWSPostRating(Context context, Activity activity) {
            this.context = context;
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {

            progressDialog =ProgressDialog.show(context, "Enviando", "Por favor espere", true, false);
        }

        @Override
        protected String doInBackground(String... params) {
            String jsonString = "";

            try {

                HttpURLConnection urlConnection = null;

                URL url = new URL(URL_WEB_SERVICES + URL_POST_RATING );

                Log.e("URL WS: ", url.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestMethod("POST");

                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);

                TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

                JSONObject cliente   = new JSONObject();

                cliente.put("Id_Place", params[0]);
                cliente.put("Id_Imei", telephonyManager.getDeviceId());
                cliente.put("value", params[1]);


                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(cliente.toString());
                wr.flush();


                StringBuilder sb = new StringBuilder();
                int HttpResult = urlConnection.getResponseCode();
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line.replace("\"", ""));
                    }
                    br.close();
                    System.out.println("" + sb.toString());
                    jsonString = sb.toString();
                } else {
                    System.out.println(urlConnection.getResponseMessage());
                    jsonString = urlConnection.getResponseMessage();
                }

            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
            }

            return jsonString;

        }

        @Override
        protected void onPostExecute(String valueResponse) {

            String result = "";
            if(valueResponse.equals("True")){
                //Correcto
                result ="Calificacion Aceptada!";
            } else{
                //NO se completo
                result = "No es posible, no se conecto con el servidor";

            }
            progressDialog.dismiss();
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
            activity.finish();
        }
    }


}
