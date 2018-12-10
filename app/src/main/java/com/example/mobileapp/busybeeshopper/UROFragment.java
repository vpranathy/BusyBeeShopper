package com.example.mobileapp.busybeeshopper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UROFragment extends Fragment {
    private static final String TAG = "UROFragment";
    ArrayList<splitData> youOwe = new ArrayList<>();
    ArrayList<String> groupMembers = new ArrayList<>();
    FirebaseDatabase database= FirebaseDatabase.getInstance();
    String username, usergroup;
    private LinearLayoutManager manager= new LinearLayoutManager(getActivity());
    int usertype;
    public UROFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_uro, container, false);
        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.UROnames);
        recyclerView.setLayoutManager(manager);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "nothing is passed");
        usergroup = sharedPreferences.getString("group", "nothing is passed");
        usertype = sharedPreferences.getInt("type", 100);
        final DatabaseReference myref = database.getReference("Split_" + usergroup);
        if (usertype==1){
            DatabaseReference myref2 = database.getReference("Users");
            Query query = myref2.orderByChild("group").equalTo(usergroup);
            query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snaps : dataSnapshot.getChildren()){
                        Users users = snaps.getValue(Users.class);
                        groupMembers.add(users.getUsername());
                    }
                }

                for (int i = 0; i < groupMembers.size(); i++) {
                    if (!groupMembers.get(i).equals(username)) {
                        Query query = myref.orderByChild("boughtBy").equalTo(username);
                        Log.d(TAG, "onCreateView: getting the amount owed by " + groupMembers.get(i));
                        final int finalI = i;
                        final int[] amountOwed = new int[1];
                        amountOwed[0] = 0;
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Log.d(TAG, "onDataChange: entering the query for getting the amount");
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot snaps : dataSnapshot.getChildren()) {
                                        Log.d(TAG, "onDataChange: " + snaps);
                                        Group_Split split = snaps.getValue(Group_Split.class);
                                        Log.d(TAG, "onDataChange: the amount of " + split.getItemName() + "  is " + split.getItemPrice());

                                        if (split.getAddedBy().equals(groupMembers.get(finalI))) {
                                            Log.d(TAG, "onDataChange: the amount  inside the compare statement " + split.getItemName() + "  is " + split.getItemPrice());
                                            amountOwed[0] = amountOwed[0] + split.getItemPrice();

                                        }

                                    }
                                    Log.d(TAG, "onDataChange: the amount owed by " + groupMembers.get(finalI) + " is " + amountOwed[0]);
                                    youOwe.add(new splitData(groupMembers.get(finalI), amountOwed[0]));
                                }

                                SplitAdapter adapter = new SplitAdapter(getActivity(), youOwe, true);
                                Log.d(TAG, "onDataChange: calling the adapter");

                                recyclerView.setAdapter(adapter);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    return rootView;
    }

}