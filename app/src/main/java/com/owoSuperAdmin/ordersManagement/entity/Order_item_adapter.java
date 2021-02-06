package com.owoSuperAdmin.ordersManagement.entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.owoSuperAdmin.owoshop.R;
import java.util.List;

public class Order_item_adapter extends  RecyclerView.Adapter<Order_item_adapter.ViewHolder>{

    private Context mCtx;
    private List<ShopKeeperOrderedProducts> ordered_products;

    public Order_item_adapter(Context mCtx, List<ShopKeeperOrderedProducts> ordered_products) {
        this.mCtx = mCtx;
        this.ordered_products = ordered_products;
    }

    @NonNull
    @Override
    public Order_item_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.ordered_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Order_item_adapter.ViewHolder holder, int position) {
        Glide.with(mCtx).load(ordered_products.get(position).getProduct_image()).into(holder.imageView);
        holder.product_name.setText(ordered_products.get(position).getProduct_name());
        holder.product_price_and_quantity.setText('৳'+String.valueOf(ordered_products.get(position).getProduct_price())+'x'+String.valueOf(ordered_products.get(position).getProduct_quantity()));
        Double total = ordered_products.get(position).getProduct_quantity() * ordered_products.get(position).getProduct_price();
        holder.product_total_price.setText('৳'+ String.valueOf(total));
    }

    @Override
    public int getItemCount() {
        return ordered_products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView product_name, product_price_and_quantity, product_total_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.single_product_image);
            product_name = itemView.findViewById(R.id.single_product_name);
            product_price_and_quantity = itemView.findViewById(R.id.single_product_price_and_quantity);
            product_total_price = itemView.findViewById(R.id.single_product_total_price);
        }
    }
}
