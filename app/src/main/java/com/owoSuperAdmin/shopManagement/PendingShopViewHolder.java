package com.owoSuperAdmin.shopManagement;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.owoSuperAdmin.owoshop.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class PendingShopViewHolder extends RecyclerView.ViewHolder{

    public TextView shopName, shopOwnerMobileNumber, shopAddress;
    public CircleImageView shopImage;

    public PendingShopViewHolder(@NonNull View itemView) {
        super(itemView);

        shopName = itemView.findViewById(R.id.shopName);
        shopOwnerMobileNumber = itemView.findViewById(R.id.shopOwnerMobileNumber);
        shopAddress = itemView.findViewById(R.id.shopAddress);
        shopImage = itemView.findViewById(R.id.shopImage);
    }
}