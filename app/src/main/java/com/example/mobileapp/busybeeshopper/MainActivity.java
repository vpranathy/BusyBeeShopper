package com.example.mobileapp.busybeeshopper;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        /***************bottom code for text view to test menu****************************/
        TextView title = (TextView) findViewById(R.id.tvm);
        title.setText("This is Main Activity");

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
                        Intent intent1 = new Intent(MainActivity.this, LocationActivity.class);
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
}
