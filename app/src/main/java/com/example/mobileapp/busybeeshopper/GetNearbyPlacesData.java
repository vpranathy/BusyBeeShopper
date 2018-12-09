package com.example.mobileapp.busybeeshopper;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import android.database.Cursor;


public class GetNearbyPlacesData extends Service {
    private int PROXIMITY_RADIUS = 50;
    LocationManager locationManager;
    LocationListener locationListener1;
    LocationListener locationListener2;
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
        locationListener1 = new LocationListener() {
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



        locationListener2 = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                SQLiteDatabase database = openOrCreateDatabase("mydata",MODE_PRIVATE,null);
                database.execSQL("CREATE TABLE IF NOT EXISTS Entries(Latitude DOUBLE,Longitude DOUBLE,Name VARCHAR,Vicinity VARCHAR)");
                Cursor c = database.rawQuery("Select * from Entries", null);
                int Latitude = c.getColumnIndex("Latitude");
                int Longitude = c.getColumnIndex("Longitude");
                int name = c.getColumnIndex("Name");
                int Vicinity = c.getColumnIndex("Vicinity");
                while (c.moveToNext()) {
                    LatLng latLng = new LatLng(c.getDouble(Latitude), c.getDouble(Longitude));
                    Location location2 = new Location("");
                    location2.setLongitude(c.getDouble(Longitude));
                    location2.setLatitude(c.getDouble(Latitude));
                    String placeName = c.getString(name);
                    String vicinity = c.getString(Vicinity);
                    float distance = location2.distanceTo(location);
                    if (distance < 20){
                        // Build notification
                        // Actions are just fake
                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(), intent, 0);
                        Notification noti = new Notification.Builder(getApplicationContext())
                                .setContentTitle("Onr of the items you wish to purchase is available at "+placeName)
                                .setContentText("Subject").setSmallIcon(R.drawable.bussybee)
                                .setContentIntent(pIntent).build();
                                //.addAction(R.drawable.icon, "Call")
                                //.addAction(R.drawable.icon, "More", pIntent)
                                //.addAction(R.drawable.icon, "And more", pIntent).build();
                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        noti.defaults |= Notification.DEFAULT_SOUND;

                        noti.flags |= Notification.FLAG_AUTO_CANCEL;

                        notificationManager.notify(0, noti);
                    }

                }


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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10000, locationListener1);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 20, locationListener2);

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