package com.example.mobileapp.busybeeshopper;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
    private RecyclerViewAdapter adapter;


    public SwipeToDeleteCallback(RecyclerViewAdapter adapter1) {
        super(0,ItemTouchHelper.LEFT);
        adapter= adapter1;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onSwiped( RecyclerView.ViewHolder viewHolder, int i) {
        int pos=viewHolder.getAdapterPosition();
        adapter.delete(pos);

    }
}
