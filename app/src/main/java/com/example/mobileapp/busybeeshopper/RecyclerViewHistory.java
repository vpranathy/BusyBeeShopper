package com.example.mobileapp.busybeeshopper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewHistory extends RecyclerView.Adapter<RecyclerViewHistory.ViewHolder1> {
    private static final String TAG = "RecyclerViewAdapter";
    Context context;
    ArrayList<Group_Split> itemList;
    LayoutInflater inflater;


    public RecyclerViewHistory(Context thisContext, ArrayList<Group_Split> list){
        this.context=thisContext;
        this.itemList=list;
        inflater=LayoutInflater.from(thisContext);
    }
    @NonNull
    @Override
    public ViewHolder1 onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=inflater.inflate(R.layout.history_items,viewGroup,false);
        ViewHolder1 holder1= new ViewHolder1(view);
        return holder1;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder1 viewHolder, int i) {

        viewHolder.itemName.setText(itemList.get(i).getItemName()+"  added by "+itemList.get(i).getAddedBy()+" was bought by "+itemList.get(i).getBoughtBy()
        +"  for $"+itemList.get(i).getItemPrice());

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class ViewHolder1 extends RecyclerView.ViewHolder {

        TextView itemName;
        RelativeLayout itemLayout;

        public ViewHolder1(@NonNull View itemView) {
            super(itemView);
            itemName= itemView.findViewById(R.id.indHistItem);
            itemLayout=itemView.findViewById(R.id.layout_history);

        }
    }
}
