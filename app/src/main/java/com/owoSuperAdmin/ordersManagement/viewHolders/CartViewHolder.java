package com.owoSuperAdmin.ordersManagement.viewHolders;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.owoSuperAdmin.owoshop.R;

public class CartViewHolder extends RecyclerView.ViewHolder{

    public TextView txtProductName,txtProductPrice,txtProductQuantity;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        txtProductName=itemView.findViewById(R.id.cart_product_name);
        txtProductPrice=itemView.findViewById(R.id.cart_product_price);
        txtProductQuantity=itemView.findViewById(R.id.cart_product_quantity);
    }
}
