package com.example.mobileapp.busybeeshopper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
    String username,userGroup;
    int usertype;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public RecyclerViewAdapter(Context thisContext, ArrayList<String> list, ArrayList<Integer> imgid, ArrayList<String> addedBy) {
        this.context=thisContext;
        this.itemList=list;
        this.imgId=imgid;
        this.itemAdd=addedBy;
        Log.d(TAG, "RecyclerViewAdapter: lists "+itemList);
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserData",Context.MODE_PRIVATE);
        username=sharedPreferences.getString("username","nothing is passed");
        userGroup=sharedPreferences.getString("group","nothing is passed");
        usertype=sharedPreferences.getInt("type",100);
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
        Log.d(TAG, "onBindViewHolder: testing the on bind");
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

        /**Create layout of alert dialog box**/
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        price= new EditText(context);
        price.setHint("Enter Price");
        layout.addView(price);

        final AlertDialog ad1= new AlertDialog.Builder(context).setView(layout).setTitle("Enter item Price").setPositiveButton(android.R.string.ok,null).setNegativeButton(android.R.string.cancel,null).create();
        ad1.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = ((AlertDialog) ad1).getButton(AlertDialog.BUTTON_POSITIVE);
                Button button1 = ((AlertDialog) ad1).getButton(AlertDialog.BUTTON_NEGATIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String itemPrice= price.getText().toString();
                        if(!itemPrice.trim().isEmpty()){
                            String itemName = delName;
                            String addedBy = delUser;
                            String[] temp = addedBy.split(" ");
                            addedBy = temp[2];
                            Log.d(TAG, "onClick: name" + addedBy);

                            final DatabaseReference myref = database.getReference(userGroup).child(addedBy).child(itemName);
                            myref.removeValue();
                            Log.d(TAG, "onClick: myref in delete " + myref.getKey());
                            String SplitGroup = "Split_" + userGroup;
                            DatabaseReference split = database.getReference(SplitGroup).child(itemName);
                            Group_Split newEntry = new Group_Split(addedBy, username, itemName, Integer.parseInt(itemPrice));
                            split.setValue(newEntry);
                            ad1.dismiss();

                        }
                        else {
                            Toast.makeText(context,"Enter Price",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemList.add(recentlyDel,delName);
                        imgId.add(recentlyDel,delId);
                        itemAdd.add(recentlyDel,delUser);
                        notifyItemInserted(recentlyDel);
                        ad1.dismiss();

                    }
                });
            }
        });
        ad1.show();

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

