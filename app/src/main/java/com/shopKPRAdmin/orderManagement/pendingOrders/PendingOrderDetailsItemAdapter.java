package com.shopKPRAdmin.orderManagement.pendingOrders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shopKPRAdmin.configurationsFile.HostAddress;
import com.shopKPRAdmin.orderManagement.ShopKeeperOrderedProducts;
import com.shopKPRAdmin.owoshop.R;
import java.util.List;

public class PendingOrderDetailsItemAdapter extends  RecyclerView.Adapter<PendingOrderDetailsItemAdapter.ViewHolder>{

    private final Context mCtx;
    private final List<ShopKeeperOrderedProducts> orderedProducts;

    public PendingOrderDetailsItemAdapter(Context mCtx, List<ShopKeeperOrderedProducts> orderedProducts) {
        this.mCtx = mCtx;
        this.orderedProducts = orderedProducts;
    }

    @NonNull
    @Override
    public PendingOrderDetailsItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.ordered_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingOrderDetailsItemAdapter.ViewHolder holder, int position)
    {
        Glide.with(mCtx).load(HostAddress.HOST_ADDRESS.getAddress() + orderedProducts.get(position).getProduct_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(holder.imageView);

        holder.product_name.setText(orderedProducts.get(position).getProduct_name());
        holder.product_price_and_quantity.setText('৳'+String.valueOf(orderedProducts.get(position).getProduct_price())+'x'+String.valueOf(orderedProducts.get(position).getProduct_quantity()));
        Double total = orderedProducts.get(position).getProduct_quantity() * orderedProducts.get(position).getProduct_price();
        holder.product_total_price.setText('৳'+ String.valueOf(total));
    }

    @Override
    public int getItemCount() {
        return orderedProducts.size();
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
