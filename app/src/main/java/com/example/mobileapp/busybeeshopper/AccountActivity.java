package com.example.mobileapp.busybeeshopper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountActivity extends AppCompatActivity {
    private static final String TAG = "AccountActivity";
    Button create, add, leave;
    String userGroup,userName;
    String newUser;
    int userType;
    EditText addNewUser;
    Long finalGroupNumber;
    AlertDialog ad;
    //firebase
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myref2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


        create=findViewById(R.id.Create);
        add = findViewById(R.id.Add);
        leave=findViewById(R.id.Leave);

        //TextView title = (TextView) findViewById(R.id.tva);
        //title.setText("This is Account Activity");

        //Getting sharedPreferences
        SharedPreferences sharedPreferences = AccountActivity.this.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        userGroup=sharedPreferences.getString("group","no group received");
        userName=sharedPreferences.getString("username","no username received");
        userType=sharedPreferences.getInt("type",100);

        if (!userName.equals(userGroup)){
            create.setVisibility(View.INVISIBLE);
            leave.setVisibility(View.VISIBLE);
            add.setVisibility(View.VISIBLE);
        }
        myref2= database.getReference("Users").child(userName);
        /***************code for navigation bar below****************************/
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(4);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_items:
                        Intent intent0 = new Intent(AccountActivity.this, MainActivity.class);
                        startActivity(intent0);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.ic_map:
                        Intent intent1 = new Intent(AccountActivity.this, MapsActivity.class);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.ic_split:
                        Intent intent2 = new Intent(AccountActivity.this, SplitActivity.class);
                        startActivity(intent2);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.ic_history:
                        Intent intent3 = new Intent(AccountActivity.this, HistoryActivity.class);
                        startActivity(intent3);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.ic_account:

                        break;
                }


                return false;
            }
        });
        /***************code for navigation bar ends here****************************/
    }

    public void CreateGroup(View view) {
        Log.d(TAG, "CreateGroup:  started ");

        Log.d(TAG, "CreateGroup: the group name is "+userGroup);
        final DatabaseReference myref = database.getReference("GroupNum");
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long groupNumber = (Long) dataSnapshot.getValue();
                Log.d(TAG, "onDataChange: the group number is "+groupNumber);
                finalGroupNumber =groupNumber;
                userGroup="Group_"+Long.toString(finalGroupNumber);
                groupNumber=groupNumber+1;
                myref.setValue(groupNumber);
                userType=1;

                myref2.child("type").setValue(userType);  //update type and group of the user in Users
                myref2.child("group").setValue(userGroup);
                final DatabaseReference myref3=database.getReference(userGroup);
                final DatabaseReference myref4 = database.getReference("PersonalList").child(userName);
                myref4.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot snaps: dataSnapshot.getChildren()){
                                String Name = snaps.child("itemName").getValue().toString();
                                String ID = snaps.child("itemID").getValue().toString();
                                String description = snaps.child("description").getValue().toString();
                                Item newItem= new Item(description,Name,ID);
                                myref3.child(userName).child(Name).setValue(newItem);
                            }
                            myref4.removeValue();
                            SharedPreferences sharedPreferences = AccountActivity.this.getSharedPreferences("UserData",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.remove("group");
                            editor.remove("type");
                            editor.putString("group",userGroup);
                            editor.putInt("type",userType);
                            editor.apply();
                        }
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

    }

    public void addUser(View view) {
        AlertDialog.Builder alert= new AlertDialog.Builder(this);
        alert.setTitle("Enter Username");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        addNewUser= new EditText(this);
        addNewUser.setHint("Item");
        layout.addView(addNewUser);
        alert.setView(layout);
        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newUser= addNewUser.getText().toString();
                DatabaseReference myref5 = database.getReference("PersonalList").child(newUser);
                myref5.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snaps : dataSnapshot.getChildren()) {
                                String Name = snaps.child("itemName").getValue().toString();
                                String ID = snaps.child("itemID").getValue().toString();
                                String description = snaps.child("description").getValue().toString();
                                Item newItem = new Item(description, Name, ID);
                                database.getReference(userGroup).child(newUser).child(Name).setValue(newItem);
                            }
                            database.getReference("PersonalList").child(newUser).removeValue();
                            DatabaseReference userNew= database.getReference("Users").child(newUser);
                            userNew.child("group").setValue(userGroup);
                            userNew.child("type").setValue(userType);
                        }
                        Toast.makeText(getApplicationContext()," No Such user Exists",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ad=alert.create();
        ad.show();
    }

    public void leaveGroup(View view) {
        DatabaseReference leave = database.getReference(userGroup).child(userName);
        leave.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snaps : dataSnapshot.getChildren()){
                        String Name = snaps.child("itemName").getValue().toString();
                        String ID = snaps.child("itemID").getValue().toString();
                        String description = snaps.child("description").getValue().toString();
                        Item newItem = new Item(description, Name, ID);
                        database.getReference("PersonalList").child(userName).setValue(newItem);
                    }
                    database.getReference(userGroup).child(userName).removeValue();
                    userGroup=userName;
                    userType=0;
                    myref2.child("type").setValue(userType);  //update type and group of the user in Users
                    myref2.child("group").setValue(userGroup);
                    SharedPreferences sharedPreferences = AccountActivity.this.getSharedPreferences("UserData",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.remove("group");
                    editor.remove("type");
                    editor.putString("group",userGroup);
                    editor.putInt("type",userType);
                    editor.apply();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
