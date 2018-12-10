package com.example.mobileapp.busybeeshopper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class SplitAdapter extends RecyclerView.Adapter<SplitAdapter.ViewHolder3> {
    private static final String TAG = "SplitAdapter";
    private ArrayList<splitData> mPerson = new ArrayList<>();
    private Context mContext;
    private Boolean flag1;

    public SplitAdapter(Context mContext, ArrayList<splitData> mPerson, Boolean flag ) {
        Log.d(TAG, "SplitAdapter: called constructor");
        this.mPerson = mPerson;
        this.mContext = mContext;
        this.flag1=flag;
        Log.d(TAG, "SplitAdapter: testing"+mPerson.get(0).getUsernam().toString());
    }

    @NonNull
    @Override
    public ViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.split_item, parent, false);
        ViewHolder3 holder = new ViewHolder3(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder3 viewHolder3, int i) {
        Log.d(TAG, "onBindViewHolder: in the viewHolder");
        String Name;
        if (flag1){
             Name = "Amount owed by "+ mPerson.get(i).getUsernam();
        }else {
             Name = "You owe " + mPerson.get(i).getUsernam();
        }
        int Amount = mPerson.get(i).getAmount();
        viewHolder3.person.setText(Name);
        viewHolder3.amount.setText(Integer.toString(Amount));
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: the size of the view is "+mPerson.size());
        return mPerson.size();
    }

    public class ViewHolder3 extends RecyclerView.ViewHolder{
        TextView person;
        TextView amount;
        LinearLayout splitlayout;
        public ViewHolder3(@NonNull View itemView) {
            super(itemView);
            person = itemView.findViewById(R.id.Person);
            amount = itemView.findViewById(R.id.AmountOwed);
            splitlayout = itemView.findViewById(R.id.splititem);
        }
    }
}
