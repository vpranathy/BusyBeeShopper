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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AccountActivity extends AppCompatActivity {
    private static final String TAG = "AccountActivity";
    Button create, add, leave;
    String userGroup,userName;
    String newUser;
    int userType;
    EditText addNewUser;
    Long finalGroupNumber;
    ArrayList<String> members = new ArrayList<>();
    AlertDialog ad;
    RecyclerView recyclerView;
    //firebase
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myref2, myref3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


        create=findViewById(R.id.Create);
        add = findViewById(R.id.Add);
        leave=findViewById(R.id.Leave);
        recyclerView=findViewById(R.id.groupMembers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //TextView title = (TextView) findViewById(R.id.tva);
        //title.setText("This is Account Activity");

        //Getting sharedPreferences
        final SharedPreferences sharedPreferences = AccountActivity.this.getSharedPreferences("UserData", Context.MODE_PRIVATE);

        userName=sharedPreferences.getString("username","no username received");

        myref3 = database.getReference("Users");
        Query query = myref3.orderByChild("username").equalTo(userName);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snaps : dataSnapshot.getChildren()){
                        Users updatedUser = snaps.getValue(Users.class);
                        userGroup=updatedUser.getGroup();
                        userType=updatedUser.getType();
                        Log.d(TAG, "onDataChange: type "+userGroup+"    name is "+userName);
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.remove("group");
                        edit.remove("type");
                        edit.putString("group", userGroup);
                        edit.putInt("type",userType);
                        edit.apply();

                        if (!userName.equals(userGroup)){
                            Log.d(TAG, "onDataChange: enering equals");
                            create.setVisibility(View.INVISIBLE);
                            leave.setVisibility(View.VISIBLE);
                            add.setVisibility(View.VISIBLE);
                            getgroup(userGroup);

                        }else {
                            Log.d(TAG, "onDataChange: not equals");
                            create.setVisibility(View.VISIBLE);
                            leave.setVisibility(View.INVISIBLE);
                            add.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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

    private void inflate() {
        Log.d(TAG, "inflate: called");
        AccountAdapter adapter = new AccountAdapter(this,members);
        recyclerView.setAdapter(adapter);

    }

    private void getgroup(String userGroup) {
        Log.d(TAG, "getgroup: called");
        members.clear();
        Query memQuery = myref3.orderByChild("group").equalTo(userGroup);
        memQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snaps : dataSnapshot.getChildren()){
                        members.add(snaps.getKey());
                    }
                }
                inflate();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

                        create.setVisibility(View.INVISIBLE);
                        add.setVisibility(View.VISIBLE);
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
                DatabaseReference checknew = database.getReference("Users").child(newUser);
                checknew.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
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
                                        DatabaseReference userNew = database.getReference("Users").child(newUser);
                                        userNew.child("group").setValue(userGroup);
                                        userNew.child("type").setValue(userType);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), " No Such user Exists or already exists in other group", Toast.LENGTH_LONG).show();

                        }
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
                        Log.d(TAG, "onDataChange: the item is"+snaps);
                        String Name = snaps.child("itemName").getValue().toString();
                        String ID = snaps.child("itemID").getValue().toString();
                        String description = snaps.child("description").getValue().toString();
                        Item newItem = new Item(description, Name, ID);
                        Log.d(TAG, "onDataChange: the new item to the personal list when leaving is "+newItem.getItemName());
                        database.getReference("PersonalList").child(userName).child(Name).setValue(newItem);
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
