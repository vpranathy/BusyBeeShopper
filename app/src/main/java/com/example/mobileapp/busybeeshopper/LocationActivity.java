package com.example.mobileapp.busybeeshopper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class LocationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);




        /***************bottom code for text view to test menu****************************/
        TextView title = (TextView) findViewById(R.id.tvl);
        title.setText("This is Location Activity");

        /***************code for navigation bar below****************************/
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_items:
                        Intent intent0 = new Intent(LocationActivity.this, MainActivity.class);
                        startActivity(intent0);
                        break;

                    case R.id.ic_map:

                        break;

                    case R.id.ic_split:
                        Intent intent2 = new Intent(LocationActivity.this, SplitActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.ic_history:
                        Intent intent3 = new Intent(LocationActivity.this, HistoryActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.ic_account:
                        Intent intent4 = new Intent(LocationActivity.this, AccountActivity.class);
                        startActivity(intent4);
                        break;
                }


                return false;
            }
        });
        /***************code for navigation bar ends here****************************/
    }
}
