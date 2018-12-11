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

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder2> {
    private static final String TAG = "AccountAdapter";

    private ArrayList<String> mParticipants = new ArrayList<>();
    private Context mContext;

    public AccountAdapter(Context mContext, ArrayList<String> mParticipants ) {
        this.mParticipants = mParticipants;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.account_item, parent, false);
        ViewHolder2 holder = new ViewHolder2(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder2 viewHolder2, int i) {
        Log.d(TAG, "onBindViewHolder: in account called");
        viewHolder2.participant.setText(mParticipants.get(i));
    }

    @Override
    public int getItemCount() {
        return mParticipants.size();
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder{
        TextView participant;
        LinearLayout accountlayout;
        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            participant = itemView.findViewById(R.id.Participant);
            accountlayout = itemView.findViewById(R.id.accountitem);
        }
    }


}
