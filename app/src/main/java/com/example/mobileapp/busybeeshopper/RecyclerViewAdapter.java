package com.example.mobileapp.busybeeshopper;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    ArrayList<String> itemAdd;
    EditText price;
    AlertDialog ad;
    String deletedName;
    Integer deletedImg;
    String deletedByUser;


    public RecyclerViewAdapter(Context thisContext, ArrayList<String> list, ArrayList<Integer> imgid, ArrayList<String> addedBy) {
        this.context=thisContext;
        this.itemList=list;
        this.imgId=imgid;
        this.itemAdd=addedBy;
        Log.d(TAG, "RecyclerViewAdapter: lists "+itemList);
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
        viewHolder.itemAddedBy.setText(itemAdd.get(i));

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
        deletedName=itemList.get(position);
        deletedImg=imgId.get(position);
        deletedByUser=itemAdd.get(position);
        int recentlyDeleted=position;
        itemList.remove(position);
        imgId.remove(position);
        itemAdd.remove(position);
        notifyItemRemoved(position);



        createPurchaseAlert(position,recentlyDeleted,deletedName,deletedImg,deletedByUser);


    }

    private void createPurchaseAlert(final int position, final int recentlyDel, final String delName, final Integer delId, final String delUser) {
        AlertDialog.Builder alert= new AlertDialog.Builder(context);
        alert.setTitle("Enter Price of purchased item");
        /**Create layout of alert dialog box**/
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        price= new EditText(context);
        price.setHint("Enter Price");
        layout.addView(price);

        alert.setView(layout);
        alert.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String item= price.getText().toString();
                String itemName=itemList.get(position);
                String addedBy=itemAdd.get(position);




            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                itemList.add(recentlyDel,delName);
                imgId.add(recentlyDel,delId);
                itemAdd.add(recentlyDel,delUser);
                notifyItemInserted(recentlyDel);
                dialog.dismiss();

            }
        });
        ad= alert.create();
        ad.show();


    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView itemIcon;
        TextView itemName;
        TextView itemAddedBy;
        RelativeLayout itemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemIcon= itemView.findViewById(R.id.userIcon);
            itemName= itemView.findViewById(R.id.individualItem);
            itemAddedBy=itemView.findViewById(R.id.itemAddedBy);
            itemLayout=itemView.findViewById(R.id.layout_item);

        }
    }
}

