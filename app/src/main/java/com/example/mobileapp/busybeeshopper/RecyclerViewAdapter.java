package com.example.mobileapp.busybeeshopper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    Context context;
    ArrayList<String> itemList;
    LayoutInflater inflater;
    ArrayList<Integer> imgId;


    public RecyclerViewAdapter(Context thisContext, ArrayList<String> list, ArrayList<Integer> imgid) {
        this.context=thisContext;
        this.itemList=list;
        this.imgId=imgid;
        inflater=LayoutInflater.from(thisContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=inflater.inflate(R.layout.list_items,viewGroup,false);
        ViewHolder holder= new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Glide.with(context).asBitmap().load(imgId.get(i)).into(viewHolder.itemIcon);
        viewHolder.itemName.setText(itemList.get(i));

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
    public void delete(int position){
        itemList.remove(position);
        imgId.remove(position);
        notifyItemRemoved(position);

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView itemIcon;
        TextView itemName;
        RelativeLayout itemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemIcon= itemView.findViewById(R.id.userIcon);
            itemName= itemView.findViewById(R.id.individualItem);
            itemLayout=itemView.findViewById(R.id.layout_item);

        }
    }
}

