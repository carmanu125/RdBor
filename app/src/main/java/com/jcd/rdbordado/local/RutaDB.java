package com.jcd.rdbordado.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jcd.rdbordado.entity.EPlaces;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Argosoft03 on 22/03/2017.
 */

public class RutaDB {

    public static String BD_NAME = "RutaDB";
    public static int BD_VERSION = 3;

    public static String T_PLACES = "Places";

    public static String KEY_PLA_ID = "Id";
    public static String KEY_PLA_NAME = "Name";
    public static String KEY_PLA_SHORT_DESC = "Short_description";
    public static String KEY_PLA_DESC = "Description";
    public static String KEY_PLA_ADDRESS = "address";
    public static String KEY_PLA_PHONE = "phone";
    public static String KEY_PLA_EMAIL = "email";
    public static String KEY_PLA_LATLONG = "latLong";
    public static String KEY_PLA_RANKING = "Ranking";
    public static String KEY_PLA_URL_IMAGE = "Url_image";
    public static String KEY_PLA_URL_LOGO = "Url_Logo";
    public static String KEY_PLA_URL_FACE = "Facebook";
    public static String KEY_PLA_URL_INSTA = "Instagram";
    public static String KEY_PLA_URL_TWIT = "Twitter";


    private SQLiteDatabase nDb;
    private Context context;
    private DB_Helper nHelper;


    private class DB_Helper extends SQLiteOpenHelper{

        public DB_Helper(Context context) {
            super(context, BD_NAME, null, BD_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + T_PLACES + "(" + KEY_PLA_ID + " INTEGER PRIMARY KEY, " + KEY_PLA_NAME + " TEXT, " + KEY_PLA_SHORT_DESC + " TEXT, " +
                    KEY_PLA_DESC + " VARCHAR(250), " + KEY_PLA_ADDRESS + " VARCHAR(200), " + KEY_PLA_PHONE + " TEXT, " + KEY_PLA_EMAIL + " VARCHAR(200), " +
                    KEY_PLA_LATLONG + " TEXT, " + KEY_PLA_RANKING + " FLOAT, " + KEY_PLA_URL_IMAGE + " TEXT, " + KEY_PLA_URL_LOGO + " TEXT, " +
                    KEY_PLA_URL_FACE + " TEXT , " + KEY_PLA_URL_INSTA + " TEXT, " + KEY_PLA_URL_TWIT  +" TEXT);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + T_PLACES);
            onCreate(db);
        }
    }

    public RutaDB(Context context){
        this.context = context;
    }

    public void openDB() throws Exception{
        nHelper = new DB_Helper(context);
        nDb = nHelper.getWritableDatabase();
    }

    public void closeDB(){
        nDb.close();
    }

    public void insertPlaces(List<EPlaces> listPlaces){

        for (EPlaces places: listPlaces) {

            ContentValues cv = new ContentValues();


            cv.put(KEY_PLA_ID, places.getId());
            cv.put(KEY_PLA_NAME, places.getName());
            cv.put(KEY_PLA_SHORT_DESC, places.getShort_description());
            cv.put(KEY_PLA_DESC, places.getDescription());
            cv.put(KEY_PLA_ADDRESS, places.getAddress());
            cv.put(KEY_PLA_PHONE, places.getPhone());
            cv.put(KEY_PLA_EMAIL, places.getEmail());
            cv.put(KEY_PLA_LATLONG, places.getLatLong());
            cv.put(KEY_PLA_RANKING, places.getRanking());
            cv.put(KEY_PLA_URL_IMAGE, places.getUrlImage());
            cv.put(KEY_PLA_URL_LOGO, places.getUrlLogo());
            cv.put(KEY_PLA_URL_FACE, places.getUrlFace());
            cv.put(KEY_PLA_URL_INSTA, places.getUrlInsta());
            cv.put(KEY_PLA_URL_TWIT, places.getUrlTwit());


            nDb.insert(T_PLACES,null,cv);
        }
    }

    public List<EPlaces> listPlacesSpimmer (){
        List<EPlaces> lisPlaces = new ArrayList<>();

        Cursor c = nDb.rawQuery("select * from " + T_PLACES + " order by " + KEY_PLA_NAME + "   ", null);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            EPlaces place = new EPlaces();

            place.setId(c.getInt(0));
            place.setName(c.getString(1));
            place.setShort_description(c.getString(2));
            place.setDescription(c.getString(3));
            place.setAddress(c.getString(4));
            place.setPhone(c.getString(5));
            place.setEmail(c.getString(6));
            place.setLatLong(c.getString(7));
            place.setRanking(c.getString(8));
            place.setUrlImage(c.getString(9));
            place.setUrlLogo(c.getString(10));
            place.setUrlFace(c.getString(11));
            place.setUrlInsta(c.getString(12));
            place.setUrlTwit(c.getString(13));

            lisPlaces.add(place);
        }

        return lisPlaces;
    }
    public List<EPlaces> listPlaces (){
        List<EPlaces> lisPlaces = new ArrayList<>();

        Cursor c = nDb.rawQuery("select * from " + T_PLACES + " order by " + KEY_PLA_NAME + "   ", null);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            EPlaces place = new EPlaces();

            place.setId(c.getInt(0));
            place.setName(c.getString(1));
            place.setShort_description(c.getString(2));
            place.setDescription(c.getString(3));
            place.setAddress(c.getString(4));
            place.setPhone(c.getString(5));
            place.setEmail(c.getString(6));
            place.setLatLong(c.getString(7));
            place.setRanking(c.getString(8));
            place.setUrlImage(c.getString(9));
            place.setUrlLogo(c.getString(10));
            place.setUrlFace(c.getString(11));
            place.setUrlInsta(c.getString(12));
            place.setUrlTwit(c.getString(13));

            lisPlaces.add(place);
        }

        return lisPlaces;
    }

    public List<EPlaces> listPlacesTop10(){
        List<EPlaces> lisPlaces = new ArrayList<>();
        //String[] rank = new String[]{ KEY_PLA_NAME };
        //Cursor c = nDb.query(T_PLACES, null, null, null, null, null, KEY_PLA_RANKING+" DESC");
        Cursor c = nDb.rawQuery("select * from " + T_PLACES + " order by " + KEY_PLA_RANKING + " desc limit 10", null);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            EPlaces place = new EPlaces();

            place.setId(c.getInt(0));
            place.setName(c.getString(1));
            place.setShort_description(c.getString(2));
            place.setDescription(c.getString(3));
            place.setAddress(c.getString(4));
            place.setPhone(c.getString(5));
            place.setEmail(c.getString(6));
            place.setLatLong(c.getString(7));
            place.setRanking(c.getString(8));
            place.setUrlImage(c.getString(9));
            place.setUrlLogo(c.getString(10));
            place.setUrlFace(c.getString(11));
            place.setUrlInsta(c.getString(12));
            place.setUrlTwit(c.getString(13));

            lisPlaces.add(place);
        }

        return lisPlaces;
    }

    public void deletePlaces() {
        nDb.delete(T_PLACES, null,null);
    }

}
