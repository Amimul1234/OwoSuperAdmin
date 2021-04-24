package com.shopKPRAdmin.orderManagement.pendingOrders;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.shopKPRAdmin.orderManagement.Shop_keeper_orders;
import com.shopKPRAdmin.owoshop.R;

import java.util.List;

public class PendingOrderAdapter extends RecyclerView.Adapter<PendingOrderAdapter.ViewHolder>
{
    private final Context mCtx;
    private final List<Shop_keeper_orders> shopKeeperOrders;

    public PendingOrderAdapter(Context mCtx, List<Shop_keeper_orders> shopKeeperOrders) {
        this.mCtx = mCtx;
        this.shopKeeperOrders = shopKeeperOrders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.orders_layout, parent,false);
        return new PendingOrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Shop_keeper_orders model = shopKeeperOrders.get(position);

        holder.order_number.setText("Order :"+model.getOrder_number());
        holder.order_phone_number.setText(model.getReceiver_phone());


        Double total_with_discount = model.getTotal_amount() - model.getCoupon_discount();

        holder.total_amount_with_discount.setText("৳ "+ String.valueOf(total_with_discount));

        holder.order_address_city.setText(model.getDelivery_address());

        holder.time_and_date.setText(model.getDate() + ", "+model.getOrder_time());

    }

    @Override
    public int getItemCount() {
        return shopKeeperOrders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView order_number, order_phone_number, total_amount_with_discount, order_address_city, time_and_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            order_number = itemView.findViewById(R.id.order_number);
            order_phone_number = itemView.findViewById(R.id.order_phone_number);
            total_amount_with_discount = itemView.findViewById(R.id.order_total_price);
            order_address_city = itemView.findViewById(R.id.order_address_city);
            time_and_date = itemView.findViewById(R.id.order_date_time);

            itemView.setOnClickListener(v ->
            {
                Intent intent = new Intent(mCtx, PendingOrderDetails.class);
                intent.putExtra("pending_order", shopKeeperOrders.get(getAdapterPosition()));
                mCtx.startActivity(intent);
            });
        }
    }
}
