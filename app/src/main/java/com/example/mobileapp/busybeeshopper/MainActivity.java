package com.example.mobileapp.busybeeshopper;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG="MainActivity";
    ArrayList<String> items= new ArrayList<>();
    ArrayList<Integer> itemImageID= new ArrayList<>();
    ArrayList<String> itemAddBy= new ArrayList<>();
    ArrayList<String> itemD= new ArrayList<>();
    RecyclerView recyclerView;
    EditText addItem;
    EditText itemDesc;
    AlertDialog ad;
    String username,userGroup;
    int usertype;
    RecyclerViewAdapter recyclerViewAdapter;
    SampleDatabase db;
    String itemID;
    LocationManager lm;
    LocationListener locationlistener;
    Location loc;
    //firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myref;
    DatabaseReference getUserUpdate;
    SharedPreferences sharedPreferences;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                //startService(new Intent(MainActivity.this, GetNearbyPlacesData.class));
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,100,0,locationlistener);

                Log.i(TAG, "PERMISSION CHECK");

            }
        }
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



            /***************bottom code for text view to test menu****************************/

        /** Initialize items **/
        sharedPreferences = MainActivity.this.getSharedPreferences("UserData",Context.MODE_PRIVATE);

        username=sharedPreferences.getString("username","nothing is passed");
        userGroup=sharedPreferences.getString("group","nothing is passed");
        usertype=sharedPreferences.getInt("type",100);
        Log.d(TAG, "onCreate: sharedPreferences"+username+"   "+userGroup+"    "+usertype);
        ImageView addbtn= (ImageView)findViewById(R.id.addButton);
        recyclerView = (RecyclerView) findViewById(R.id.listOfItems);
        db= new SampleDatabase(this);
        recyclerViewAdapter = new RecyclerViewAdapter(this, items,itemImageID,itemAddBy, itemD);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper itemTouchHelper= new ItemTouchHelper(new SwipeToDeleteCallback(recyclerViewAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        getUserUpdate =database.getReference("Users");
        Query  query = getUserUpdate.orderByChild("username").equalTo(username);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snaps : dataSnapshot.getChildren()){
                        Users updatedUser = snaps.getValue(Users.class);
                        userGroup=updatedUser.getGroup();
                        usertype=updatedUser.getType();
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.remove("group");
                        edit.remove("type");
                        edit.putString("group", userGroup);
                        edit.putInt("type",usertype);
                        edit.apply();
                        populateList();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Log.d(TAG, "onCreate: ");

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Add an alert dialog to allow user to enter item and group
                createAlert();


            }
        });



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_items:
                        //Stays in this activity when clicked on this
                        break;

                    case R.id.ic_map:
                        Intent intent1 = new Intent(MainActivity.this, MapsActivity.class);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.ic_split:
                        Intent intent2 = new Intent(MainActivity.this, SplitActivity.class);
                        startActivity(intent2);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.ic_history:
                        Intent intent3 = new Intent(MainActivity.this, HistoryActivity.class);
                        startActivity(intent3);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.ic_account:
                        Intent intent4 = new Intent(MainActivity.this, AccountActivity.class);
                        startActivity(intent4);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                }


                return false;
            }
        });

        lm= (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationlistener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG, "onLocationChanged: main"+ location);
                Double latitude = location.getLatitude();
                Double longitude = location.getLongitude();
                Log.d(TAG, "onLocationChanged: check location"+Double.toString(latitude));
                sharedPreferences = MainActivity.this.getSharedPreferences("UserData",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("latitude", Double.toString(latitude));
                editor.putString("longitude",Double.toString(longitude));
                editor.apply();

                getCurrentLocation(location);

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
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }else{
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,100,0,locationlistener);

        }


    }

    private void getCurrentLocation(Location location) {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }else{
            Log.d(TAG, "getCurrentLocation: inside "+location);

            //Location l= lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            startService(new Intent(MainActivity.this, GetNearbyPlacesData.class));
            lm.removeUpdates(locationlistener);
            lm = null;
            Log.d(TAG, "getCurrentLocation: after service call");
            //startService(new Intent(MainActivity.this, GetNearbyPlacesData.class));

            // Write you code here if permission already given.
        }


    }

    /**** Function to create an alert dialogue to enter item name and description ******/

    private void createAlert() {
        AlertDialog.Builder alert= new AlertDialog.Builder(this);
        alert.setTitle("Enter item");
        /**Create layout of alert dialog box**/
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        addItem= new EditText(this);
        addItem.setHint("Item");
        layout.addView(addItem);


        itemDesc= new EditText(this);
        itemDesc.setHint("Item Description");
        layout.addView(itemDesc);



        alert.setView(layout);
        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



                String item= addItem.getText().toString();
                String descText=itemDesc.getText().toString();
                db.add(username,item,descText);

                //pushing data to database in firebase
                if (usertype ==0) {
                     myref = database.getReference("PersonalList").child(username);
                }else{
                     myref =database.getReference(userGroup).child(username);
                }
                itemID= myref.push().getKey();
                Item newItem= new Item(descText,item,itemID);
                myref.child(item).setValue(newItem);
                populateList();
//                recyclerViewAdapter.notifyDataSetChanged();

            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        ad= alert.create();
        ad.show();

    }

    private void populateList() {
        items.clear();
        itemImageID.clear();
        itemAddBy.clear();
        itemD.clear();
        Log.d(TAG, "populateList: items "+items);
        Log.d(TAG, "populateList: addeby "+itemAddBy);
        if (usertype == 0) {
            DatabaseReference myref1 = database.getReference("PersonalList").child(username);
            myref1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataChange: starting to get the data from personal list");
                    Log.d(TAG, "onDataChange: datasnapshot in populate is "+dataSnapshot);
                    items.clear();
                    itemImageID.clear();
                    itemAddBy.clear();
                    itemD.clear();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot reference : dataSnapshot.getChildren()) {
                            Log.d(TAG, "onDataChange:  " + reference);
                            items.add(reference.child("itemName").getValue().toString());
                            itemD.add(reference.child("description").getValue().toString());
                            Log.d(TAG, "onDataChange: name  " + reference.child("itemName").getValue().toString());
                            String add1 = "Added By: " + username;
                            Log.d(TAG, "onDataChange: username is" + add1);
                            itemAddBy.add(add1);
                            Log.d(TAG, "onDataChange: items are " + items);
                            Log.d(TAG, "onDataChange: added by is" + itemAddBy);
                            Integer imageResourceId = MainActivity.this.getResources().getIdentifier("ic_person", "drawable",
                                    MainActivity.this.getPackageName());
                            itemImageID.add(imageResourceId);
                        }
                    }
                    recyclerView.setAdapter(recyclerViewAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
//        Cursor data= db.getData();
//        while (data.moveToNext()){
//            String add1= "Added By: "+data.getString(1);
//            items.add(data.getString(2));
//            itemAddBy.add(add1);
//
//
//                Integer imageResourceId=this.getResources().getIdentifier("ic_person","drawable",this.getPackageName());
//                itemImageID.add(imageResourceId);
//        }

        }else {

            DatabaseReference myref1 = database.getReference(userGroup);
            myref1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataChange: starting to get the data from UserGroup");
                    items.clear();
                    itemImageID.clear();
                    itemAddBy.clear();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot reference : dataSnapshot.getChildren()) {
                            for (DataSnapshot snaps : reference.getChildren()) {
                                Log.d(TAG, "onDataChange: " + snaps);
                                items.add(snaps.child("itemName").getValue().toString());
                                itemD.add(snaps.child("description").getValue().toString());
                                Log.d(TAG, "onDataChange: name  " + snaps.child("itemName").getValue().toString());
                                String add1 = "Added By: " + reference.getKey();
                                Log.d(TAG, "onDataChange: username is" + add1);
                                itemAddBy.add(add1);
                                Log.d(TAG, "onDataChange: items are " + items);
                                Log.d(TAG, "onDataChange: added by is" + itemAddBy);
                                Integer imageResourceId = MainActivity.this.getResources().getIdentifier("ic_group", "drawable",
                                        MainActivity.this.getPackageName());
                                itemImageID.add(imageResourceId);
                            }
                        }
                        recyclerView.setAdapter(recyclerViewAdapter);
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }
}
