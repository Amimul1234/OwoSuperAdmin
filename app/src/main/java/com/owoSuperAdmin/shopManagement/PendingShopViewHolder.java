package com.owoSuperAdmin.shopManagement;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.owoSuperAdmin.owoshop.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class PendingShopViewHolder extends RecyclerView.ViewHolder{

    public TextView shop_name, mobile_number;
    public CircleImageView shop_image;

    public PendingShopViewHolder(@NonNull View itemView) {
        super(itemView);
        shop_name = itemView.findViewById(R.id.shop_name);
        mobile_number = itemView.findViewById(R.id.shop_keeper_mobile_number);
        shop_image = itemView.findViewById(R.id.shop_image);
    }
}