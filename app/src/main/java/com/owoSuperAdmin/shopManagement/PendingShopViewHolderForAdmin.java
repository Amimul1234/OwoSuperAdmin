package com.owoSuperAdmin.shopManagement;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.owoSuperAdmin.owoshop.R;

public class PendingShopViewHolderForAdmin extends RecyclerView.ViewHolder{
    public TextView shop_name;
    public ImageView shop_image;

    public PendingShopViewHolderForAdmin(@NonNull View itemView) {
        super(itemView);
        shop_name = itemView.findViewById(R.id.shop_name);
        shop_image = itemView.findViewById(R.id.shop_image);
    }

    /*
    public void bind(PendingShop model, Context mcTx) {
        shop_name.setText(model.getShop_name());
        Glide.with(mcTx).load(model.getShop_image_uri()).into(shop_image);
    }

     */
}