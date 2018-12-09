package com.example.mobileapp.busybeeshopper;

import android.content.Intent;
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

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    ArrayList<String> historyItems= new ArrayList<>();
    RecyclerView recyclerView;
    SampleDatabase db;
    RecyclerViewHistory recyclerViewHistory;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        /***************bottom code for text view to test menu****************************/
        /**Initialize objects**/
        recyclerView= (RecyclerView)findViewById(R.id.HistoryItems);
        db= new SampleDatabase(this);
        recyclerViewHistory= new RecyclerViewHistory(this,historyItems);
        recyclerView.setAdapter(recyclerViewHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        populateHistory();
        recyclerViewHistory.notifyDataSetChanged();

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

    private void populateHistory() {
        historyItems.clear();
        Cursor data = db.getData();
        while (data.moveToNext()) {
            historyItems.add(data.getString(2));
        }
    }
}