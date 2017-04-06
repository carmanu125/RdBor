package com.jcd.rdbordado;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;

import android.location.Criteria;
import android.location.Location;

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jcd.rdbordado.adapters.SpinnerPlacesAdapter;
import com.jcd.rdbordado.async.DownloadImageBannerTask;
import com.jcd.rdbordado.entity.EPlaces;
import com.jcd.rdbordado.local.RutaDB;
import com.jcd.rdbordado.ws.WebServicesRutDB;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.value;
import static android.content.Context.LOCATION_SERVICE;
import static com.google.ads.AdRequest.LOGTAG;

public class MapsActivity extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, LocationListener, View.OnClickListener, AdapterView.OnItemSelectedListener {

    private GoogleMap mMap;
    Spinner spiPlaces;
    Button bt_maps_go;

    double latitude; // latitude
    double longitude; // longitude
    int positionSpinner = 0;

    String[] datosSpinner = new String[]{"Frixio","Talle 1","Talle 2","Talle 3","Talle 4","Talle 5"};

    Polyline line;
    GoogleApiClient apiClient;
    private static final int PETICION_PERMISO_LOCALIZACION = 101;
    private static final int REQUEST_LOCATION = 2;
    SpinnerPlacesAdapter adapter;

    RutaDB nDb;

    View view;
    List<EPlaces> listPlaces;
    EPlaces placeCurrent;

    public static final String EXTRA_CODE = "Maps";

    LocationManager locationManager;
    Criteria criteria;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        nDb = new RutaDB(getContext());
        getListPlaces();


        spiPlaces = (Spinner) view.findViewById(R.id.spi_maps_location);
        bt_maps_go = (Button) view.findViewById(R.id.bt_maps_go);

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.item_spi_places, datosSpinner);
        adapter = new SpinnerPlacesAdapter(getContext(), listPlaces);
        spiPlaces.setAdapter(adapter);
        spiPlaces.setOnItemSelectedListener(this);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);

        bt_maps_go.setOnClickListener(this);
        gpsInit();

    }

    private void setPlaceRoute() {
        try
        {
            placeCurrent = (EPlaces) getArguments().getSerializable(EXTRA_CODE);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private List<EPlaces> getListPlaces() {
        listPlaces = new ArrayList<>();

        try {
            nDb.openDB();
            listPlaces = nDb.listPlaces();
            nDb.closeDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listPlaces;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_maps, container, false);
        return view;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        //Ocultando iconos
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        setPlaceRoute();
        if(placeCurrent != null){
            trazarRuta();
        }else {

            int positionList = 0;
            for (EPlaces places : listPlaces) {

                String[] latlong = places.getLatLong().split(",");
                double latitudeCurrent = Double.parseDouble(latlong[0]);
                double longitudeCurrent = Double.parseDouble(latlong[1]);
                String positionCurrent = String.valueOf(positionList);

                addMArker(places.getName(), new LatLng(latitudeCurrent, longitudeCurrent), positionCurrent, places.getShort_description());
                positionList++;
            }
        }

    }

    private void addMArker(String tittle, LatLng latLong, String tag, String shortDescription) {
        // Add a marker in Sydney and move the camera

        BitmapDescriptor iconInit = BitmapDescriptorFactory.fromResource(R.mipmap.default_marker);
        mMap.addMarker(new MarkerOptions().position(latLong).title(tittle).snippet(shortDescription).icon(iconInit)).setTag(tag);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, 15f));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        try {

            final Marker newMarker = marker;
            ImageView im = new ImageView(getContext());
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(150, 150);
            im.setLayoutParams(params);
            getImagePlace(listPlaces.get(Integer.parseInt(marker.getTag().toString())).getUrlImage(), im);
            //im.setImageResource(R.mipmap.logo_cicle);

            AlertDialog.Builder db = new AlertDialog.Builder(getContext());
            db.setView(im);
            db.setTitle(marker.getTitle());
            db.setPositiveButton("Ver perfil", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    callNewActivity(newMarker);
                }
            });
            AlertDialog dialog = db.show();

        }catch (Exception e){
            Log.e("No onClick:: ", e.toString());
        }
        return false;
    }

    private void getImagePlace(String urlLogo, ImageView imProfile) {

        DownloadImageBannerTask tasImage = new DownloadImageBannerTask(imProfile, getContext());
        //WebServicesRutDB.URL_WEB + WebServicesRutDB.URL_WEB_IMAGE + place.getId() + "_banner.jpg"
        String url = urlLogo;
        tasImage.execute(url);

    }

    private void callNewActivity(Marker marker){
        try {
            //if (marker.getTag().equals("Frixio")) {
            Intent intent = new Intent(getActivity(), ProfilePlacesActivity.class);
            int positionList = Integer.parseInt(marker.getTag().toString());
            intent.putExtra("Place", listPlaces.get(positionList));
            startActivity(intent);
            //}

        } catch (Exception e) {

        }
    }


    public void trazarRuta() {

        final LocationManager manager = (LocationManager) getActivity().getSystemService( LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }else{
            llamarAsyncTask();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Se requiere que el GPS este activo, desea activarlo?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    private void gpsInit(){
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);


        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            // Check Permissions Now
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);

            return;
        }else {
            Location location = locationManager.getLastKnownLocation(provider);

            if (location != null) {
                onLocationChanged(location);
            }
            locationManager.requestLocationUpdates(provider, 3000, 5000, this);
        }
    }

    private void llamarAsyncTask() {

        mMap.clear();
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);


        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            // Check Permissions Now
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);

            return;
        }else{
        Location location = locationManager.getLastKnownLocation(provider);

        if(location!=null){
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(provider, 3000, 5000, this);

            String[] latlong ;
            double latitudePlaces ;
            double longitudePlaces ;

            if(placeCurrent != null){

                latlong = placeCurrent.getLatLong().split(",");
                latitudePlaces = Double.parseDouble(latlong[0]);
                longitudePlaces = Double.parseDouble(latlong[1]);
            }else{
                latlong = listPlaces.get(positionSpinner).getLatLong().split(",");
                latitudePlaces = Double.parseDouble(latlong[0]);
                longitudePlaces = Double.parseDouble(latlong[1]);
            }

        String urlTopass = makeURL(latitude, longitude,
                latitudePlaces, longitudePlaces);
        new connectAsyncTask(urlTopass, getActivity()).execute();
        }
    }




    private String makeURL(double init_latitude, double init_longitude, double end_latitude, double end_longitude) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(init_latitude));
        urlString.append(",");
        urlString.append(Double.toString(init_longitude));
        urlString.append("&destination=");// to
        urlString.append(Double.toString(end_latitude));
        urlString.append(",");
        urlString.append(Double.toString(end_longitude));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        return urlString.toString();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == REQUEST_LOCATION) {
            if(grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We can now safely use the API we requested access to
                llamarAsyncTask();
            } else {
                // Permission was denied or request was cancelled
            }
        }
        if (requestCode == PETICION_PERMISO_LOCALIZACION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Permiso concedido

                @SuppressWarnings("MissingPermission")
                Location lastLocation =
                        LocationServices.FusedLocationApi.getLastLocation(apiClient);

                llamarAsyncTask();

            } else {
                //Permiso denegado:
                //Deberíamos deshabilitar toda la funcionalidad relativa a la localización.

                Log.e(LOGTAG, "Permiso denegado");
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // Getting latitude of the current location
        latitude = location.getLatitude();

        // Getting longitude of the current location
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onClick(View v) {
        trazarRuta();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.positionSpinner = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static Fragment newInstance(EPlaces places) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CODE, places);

        MapsActivity fragment = new MapsActivity();
        fragment.setArguments(args);

        return fragment;
    }


    private class connectAsyncTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressDialog;
        String url;
        Context context;

        connectAsyncTask(String urlPass, Context context) {
            this.context = context;
            url = urlPass;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Fetching route, Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            JSONParser jParser = new JSONParser();
            String json = jParser.getJSONFromUrl(url);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.hide();
            if (result != null) {
                drawPath(result);
            }
        }
    }

    public class JSONParser {

        InputStream is = null;
        JSONObject jObj = null;
        String json = "";

        // constructor
        public JSONParser() {
        }

        public String getJSONFromUrl(String string_url) {

            // Making HTTP request
            try {
                /*// defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();*/

                URL url = new URL(string_url);
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

                //char[] buffer = new char[1024];

                String jsonString = new String();

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line + "");
                }

                json = sb.toString();
                is.close();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            return json;

        }
    }

    public void drawPath(String result) {
        if (line != null) {
            mMap.clear();
        }
        BitmapDescriptor iconInit = BitmapDescriptorFactory.fromResource(R.mipmap.default_marker);
        BitmapDescriptor iconFinish = BitmapDescriptorFactory.fromResource(R.mipmap.finish_marker);

        EPlaces places;
        if(placeCurrent != null){
            places = placeCurrent;
        }else {
            places = listPlaces.get(positionSpinner);
        }
        String[] latlong = places.getLatLong().split(",");
        double latitudePlaces = Double.parseDouble(latlong[0]);
        double longitudePlaces = Double.parseDouble(latlong[1]);

        mMap.addMarker(new MarkerOptions().position(new LatLng(latitudePlaces, longitudePlaces)).icon(iconFinish).title(places.getName())).setTag(String.valueOf(positionSpinner));
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).icon(iconInit).title("Mi posicion"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15f));
        try {
            // Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes
                    .getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);

            for (int z = 0; z < list.size() - 1; z++) {
                LatLng src = list.get(z);
                LatLng dest = list.get(z + 1);
                line = mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(src.latitude, src.longitude),
                                new LatLng(dest.latitude, dest.longitude))
                        .width(5).color(Color.BLUE).geodesic(true));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }


}
