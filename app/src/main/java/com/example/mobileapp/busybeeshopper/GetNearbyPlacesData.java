package com.example.mobileapp.busybeeshopper;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.HashMap;
import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static android.content.Context.MODE_PRIVATE;

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {


    String googlePlacesData;
    GoogleMap mMap;
    String url;

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
        nearbyPlacesList =  dataParser.parse(result);
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase("mydata", null);
            database.execSQL("INSERT INTO Entries (Latitude, Longitude, Name, Vicinity) VALUES ('" + lat + "','" + lng + "', '" + placeName + "','" + vicinity + "')");
        }
        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
    }


    public void ShowNearbyPlaces() {
        Log.d("onPostExecute","Entered into showing locations");
        MarkerOptions markerOptions = new MarkerOptions();
        ////Ye line check kar
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase("mydata", null);

        Cursor c = database.rawQuery("Select * from Entries", null);
        int Latitude = c.getColumnIndex("Latitude");
        int Longitude = c.getColumnIndex("Longitude");
        int name = c.getColumnIndex("Name");
        int Vicinity = c.getColumnIndex("Vicinity");
        while (c.moveToNext()){
            LatLng latLng = new LatLng(c.getDouble(Latitude),c.getDouble(Longitude));
            String placeName = c.getString(name);
            String vicinity = c.getString(Vicinity);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);
            mMap.addMarker(markerOptions);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }
    }
}
