package com.example.mobileapp.busybeeshopper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class SplitAdapter extends RecyclerView.Adapter<SplitAdapter.ViewHolder3> {
    private static final String TAG = "SplitAdapter";
    private ArrayList<String> mPerson = new ArrayList<>();
    private ArrayList<Float> mAmount = new ArrayList<>();
    private Context mContext;

    public SplitAdapter(Context mContext, ArrayList<String> mPerson, ArrayList<Float> mAmount ) {
        this.mPerson = mPerson;
        this.mAmount = mAmount;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.split_item, parent, false);
        ViewHolder3 holder = new ViewHolder3(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder3 viewHolder3, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
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
