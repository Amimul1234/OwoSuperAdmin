package com.owosuperadmin.pagination.completed_orders;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.owosuperadmin.model.Shop_keeper_orders;
import com.owosuperadmin.owoshop.R;
import com.owosuperadmin.shop_related.details_of_current_state.Cancelled_order_details;
import com.owosuperadmin.shop_related.details_of_current_state.CompletedOrderDetails;

public class CompletedAdapter extends PagedListAdapter<Shop_keeper_orders, CompletedAdapter.ViewHolder>{

    private Context mCtx;


    public CompletedAdapter(Context mCtx) {
        super(DIFF_CALLBACK);
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.orders_layout, parent,false);
        return new CompletedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Shop_keeper_orders model = getItem(position);

        holder.order_number.setText("Order :"+model.getOrder_number());
        holder.order_phone_number.setText(model.getReceiver_phone());


        Double total_with_discount = model.getTotal_amount() - model.getCoupon_discount();

        holder.total_amount_with_discount.setText("৳ "+ String.valueOf(total_with_discount));

        holder.order_address_city.setText(model.getDelivery_address());

        holder.time_and_date.setText(model.getDate() + ", "+model.getOrder_time());

    }

    private static DiffUtil.ItemCallback<Shop_keeper_orders> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Shop_keeper_orders>() {
                @Override
                public boolean areItemsTheSame(Shop_keeper_orders shop_keeper_orders_old, Shop_keeper_orders shop_keeper_orders_new) {
                    return shop_keeper_orders_old.getOrder_number() == shop_keeper_orders_new.getOrder_number();
                }

                @Override
                public boolean areContentsTheSame(Shop_keeper_orders shop_keeper_orders_old, Shop_keeper_orders shop_keeper_orders_new) {
                    return true;
                }
            };


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView order_number, order_phone_number, total_amount_with_discount, order_address_city, time_and_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            order_number = itemView.findViewById(R.id.order_number);
            order_phone_number = itemView.findViewById(R.id.order_phone_number);
            total_amount_with_discount = itemView.findViewById(R.id.order_total_price);
            order_address_city = itemView.findViewById(R.id.order_address_city);
            time_and_date = itemView.findViewById(R.id.order_date_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mCtx, CompletedOrderDetails.class);//For giving product description to the user when clicks on a cart item
                    intent.putExtra("pending_order", getItem(getAdapterPosition()));
                    mCtx.startActivity(intent);
                }
            });
        }
    }

}