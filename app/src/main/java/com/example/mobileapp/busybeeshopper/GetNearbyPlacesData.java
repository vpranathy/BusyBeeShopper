package com.example.mobileapp.busybeeshopper;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import android.database.Cursor;


public class GetNearbyPlacesData extends Service {
    private int PROXIMITY_RADIUS = 50;
    LocationManager locationManager;
    LocationListener locationListener;
    double latitude;
    double longitude;
    String googlePlacesData;
    public SQLiteDatabase database;
    GoogleMap mMap;
    String url;
    private static final String TAG = "GetNearbyPlacesData";

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: service calle");
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Log.d(TAG, "onLocationChanged: " + latitude);
                GetNearbyData getNearbyPlacesData = new GetNearbyData();
                String Restaurant = "milk";
                String url = getUrl(latitude, longitude, Restaurant);
                Log.d(TAG, "URL: " + url);
                Object[] DataTransfer = new Object[2];
                DataTransfer[0] = mMap;
                DataTransfer[1] = url;
                getNearbyPlacesData.execute(DataTransfer);

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 50, locationListener);
    }


    private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?");
        googlePlacesUrl.append("query=" + nearbyPlace);
        googlePlacesUrl.append("&location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        //googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyA9QIgzZaWstBnnRSB61cZeeB6df5f7YrE");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private class GetNearbyData extends AsyncTask<Object, String, String> {
        @Override
        protected String doInBackground(Object... params) {
            try {
                Log.d("GetNearbyPlacesData", "doInBackground entered");
                mMap = (GoogleMap) params[0];
                url = (String) params[1];
                DownloadUrl downloadUrl = new DownloadUrl();
                googlePlacesData = downloadUrl.readUrl(url);
                Log.d("GooglePlacesReadTask", "doInBackground Exit");
            } catch (Exception e) {
                Log.d("GooglePlacesReadTask", e.toString());
            }
            return googlePlacesData;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("GooglePlacesReadTask", "onPostExecute Entered");

            List<HashMap<String, String>> nearbyPlacesList = null;
            DataParser dataParser = new DataParser();
            nearbyPlacesList = dataParser.parse(result);
            for (int i = 0; i < nearbyPlacesList.size(); i++) {

                HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
                double lat = Double.parseDouble(googlePlace.get("lat"));
                double lng = Double.parseDouble(googlePlace.get("lng"));
                String placeName = googlePlace.get("place_name");
                placeName = placeName.replaceAll("'","");
                String vicinity = googlePlace.get("vicinity");
                SQLiteDatabase database = openOrCreateDatabase("mydata", MODE_PRIVATE,null);
                database.execSQL("CREATE TABLE IF NOT EXISTS Entries(Latitude DOUBLE,Longitude DOUBLE,Name VARCHAR,Vicinity VARCHAR)");
                database.execSQL("INSERT INTO Entries(Latitude,Longitude, Name,Vicinity) VALUES ('"+lat+"','"+lng+"','"+placeName+"', '"+vicinity+"')");

                Log.d("GooglePlacesReadTask", "onPostExecute Exit");
            }
        }

    }
}