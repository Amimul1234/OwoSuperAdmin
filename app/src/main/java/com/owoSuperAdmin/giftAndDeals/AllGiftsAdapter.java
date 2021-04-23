package com.owoSuperAdmin.giftAndDeals;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.owoSuperAdmin.giftAndDeals.entity.Gifts;
import java.util.List;

public class AllGiftsAdapter extends RecyclerView.Adapter<AllGiftsAdapter.ViewHolder>{

    private final Context context;
    private final List<Gifts> giftsList;

    public AllGiftsAdapter(Context context, List<Gifts> giftsList) {
        this.context = context;
        this.giftsList = giftsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
