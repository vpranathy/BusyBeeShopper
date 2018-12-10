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
import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class UOFragment extends Fragment {

    private static final String TAG = "UOFragment";
    ArrayList<splitData> youOwe = new ArrayList<>();
    ArrayList<String> groupMembers = new ArrayList<>();
    FirebaseDatabase database= FirebaseDatabase.getInstance();
    String username, usergroup;
    private LinearLayoutManager manager= new LinearLayoutManager(getActivity());
    int usertype;
    public UOFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_uo,container,false);
        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.UOnames);
        recyclerView.setLayoutManager(manager);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("UserData",Context.MODE_PRIVATE);
        username=sharedPreferences.getString("username","nothing is passed");
        usergroup=sharedPreferences.getString("group","nothing is passed");
        usertype=sharedPreferences.getInt("type",100);
        final DatabaseReference myref = database.getReference("Split_"+usergroup);
        DatabaseReference myref2 =database.getReference(usergroup);
        myref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot reference : dataSnapshot.getChildren()){
                        Log.d(TAG, "onDataChange: reference for getting group members "+reference.getKey());
                        groupMembers.add(reference.getKey());
                    }
                }
                Log.d(TAG, "onCreateView: the group members are "+groupMembers);

                for (int i= 0;i<groupMembers.size();i++) {
                    if (!groupMembers.get(i).equals(usergroup)) {
                        Query query = myref.orderByChild("addedBy").equalTo(username);
                        Log.d(TAG, "onCreateView: getting the amount owed to " + groupMembers.get(i));
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

                                        if (split.getBoughtBy().equals(groupMembers.get(finalI))) {
                                            Log.d(TAG, "onDataChange: the amount of inside the compare statement " + split.getItemName() + "  is " + split.getItemPrice());
                                            amountOwed[0] = amountOwed[0] + split.getItemPrice();

                                        }

                                    }
                                    Log.d(TAG, "onDataChange: the amount owed to " + groupMembers.get(finalI) + " is " + amountOwed[0]);
                                    youOwe.add(new splitData(groupMembers.get(finalI), amountOwed[0]));
                                }
                                if (youOwe.size() != 0) {
                                    SplitAdapter adapter = new SplitAdapter(getActivity(), youOwe, false);
                                    Log.d(TAG, "onDataChange: calling the adapter");

                                    recyclerView.setAdapter(adapter);
                                }
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
        Log.d(TAG, "onCreateView: return fragment");
        //search for added by as the current user's name, get the bought by and the the price
        return rootView;
    }

}