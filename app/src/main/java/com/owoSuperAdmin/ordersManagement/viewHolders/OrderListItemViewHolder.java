package com.owoSuperAdmin.ordersManagement.viewHolders;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.owoSuperAdmin.owoshop.R;

public class OrderListItemViewHolder extends RecyclerView.ViewHolder{

    public TextView order_number, order_phone_number, total_amount_with_discount, order_address_city, time_and_date;

    public OrderListItemViewHolder(@NonNull View itemView) {
        super(itemView);
        order_number = itemView.findViewById(R.id.order_number);
        order_phone_number = itemView.findViewById(R.id.order_phone_number);
        total_amount_with_discount = itemView.findViewById(R.id.order_total_price);
        order_address_city = itemView.findViewById(R.id.order_address_city);
        time_and_date = itemView.findViewById(R.id.order_date_time);
    }
}