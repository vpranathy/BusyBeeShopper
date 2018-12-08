package com.example.mobileapp.busybeeshopper;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG="MainActivity";
    ArrayList<String> items= new ArrayList<>();
    ArrayList<Integer> itemImageID= new ArrayList<>();
    RecyclerView recyclerView;
    EditText addItem;
    EditText itemDesc;
    AlertDialog ad;
    RecyclerViewAdapter recyclerViewAdapter;
    SampleDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        /***************bottom code for text view to test menu****************************/

        /** Initialize items **/

        ImageView addbtn= (ImageView)findViewById(R.id.addButton);
        recyclerView = (RecyclerView) findViewById(R.id.listOfItems);
        db= new SampleDatabase(this);
        recyclerViewAdapter = new RecyclerViewAdapter(this, items,itemImageID);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper itemTouchHelper= new ItemTouchHelper(new SwipeToDeleteCallback(recyclerViewAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);


        populateList();
        recyclerViewAdapter.notifyDataSetChanged();

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
                        break;

                    case R.id.ic_split:
                        Intent intent2 = new Intent(MainActivity.this, SplitActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.ic_history:
                        Intent intent3 = new Intent(MainActivity.this, HistoryActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.ic_account:
                        Intent intent4 = new Intent(MainActivity.this, AccountActivity.class);
                        startActivity(intent4);
                        break;
                }


                return false;
            }
        });

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

                String name= "Parul";
                Integer id;
                String item= addItem.getText().toString();
                String descText=itemDesc.getText().toString();
                db.add(name,item,descText);
                populateList();
                recyclerViewAdapter.notifyDataSetChanged();

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
        Cursor data= db.getData();
        while (data.moveToNext()){
            items.add(data.getString(2));


                Integer imageResourceId=this.getResources().getIdentifier("ic_person","drawable",this.getPackageName());
                itemImageID.add(imageResourceId);

        }


    }
}
