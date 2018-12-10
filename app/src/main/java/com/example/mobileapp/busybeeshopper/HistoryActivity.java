package com.example.mobileapp.busybeeshopper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    String username,userGroup, split_group;
    int usertype;
    RecyclerViewHistory recyclerViewHistory;
    SharedPreferences sharedPreferences;
    ArrayList<Group_Split> history = new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myref,myref2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        /**Initialize objects**/
        recyclerView= (RecyclerView)findViewById(R.id.HistoryItems);
        recyclerViewHistory= new RecyclerViewHistory(this,history);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sharedPreferences = this.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        username=sharedPreferences.getString("username","nothing is passed");
        userGroup=sharedPreferences.getString("group","nothing is passed");
        usertype=sharedPreferences.getInt("type",100);
        split_group="Split_"+userGroup;
        myref = database.getReference(split_group);
        Query query = myref.orderByChild("addedBy").equalTo(username);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snaps : dataSnapshot.getChildren()){
                        Group_Split split = snaps.getValue(Group_Split.class);
                        if (!split.getBoughtBy().equals(username)){
                            history.add(split);
                        }
                    }
                    recyclerView.setAdapter(recyclerViewHistory);
                }

                myref2=database.getReference(split_group);
                Query query1 = myref2.orderByChild("boughtBy").equalTo(username);
                query1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                Group_Split split1 =snapshot.getValue(Group_Split.class);
                                if (!split1.getAddedBy().equals(username)){
                                    history.add(split1);
                                }
                            }
                        }
                        recyclerView.setAdapter(recyclerViewHistory);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        /***************bottom code for text view to test menu****************************/



        /***************code for navigation bar below****************************/

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_items:
                        Intent intent0 = new Intent(HistoryActivity.this, MainActivity.class);
                        startActivity(intent0);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.ic_map:
                        Intent intent1 = new Intent(HistoryActivity.this, MapsActivity.class);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.ic_split:
                        Intent intent2 = new Intent(HistoryActivity.this, SplitActivity.class);
                        startActivity(intent2);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.ic_history:

                        break;

                    case R.id.ic_account:
                        Intent intent4 = new Intent(HistoryActivity.this, AccountActivity.class);
                        startActivity(intent4);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                }


                return false;
            }
        });
        /***************code for navigation bar ends here****************************/
    }


}