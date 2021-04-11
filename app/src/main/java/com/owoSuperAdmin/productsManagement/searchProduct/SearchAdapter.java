package com.owoSuperAdmin.productsManagement.searchProduct;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.owoSuperAdmin.configurationsFile.HostAddress;
import com.owoSuperAdmin.productsManagement.ProductDetailsModification;
import com.owoSuperAdmin.productsManagement.entity.OwoProduct;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;

public class SearchAdapter extends PagedListAdapter<OwoProduct, SearchAdapter.ItemViewHolder>{

    private final Context mCtx;

    public SearchAdapter(Context mCtx) {
        super(DIFF_CALLBACK);
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.product_availability_sample, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        OwoProduct item = getItem(position);

        if (item != null) {

            Glide.with(mCtx).load(HostAddress.HOST_ADDRESS.getAddress()+item.getProductImage()).into(holder.imageView);

            holder.productName.setText(item.getProductName());

            String price = "৳ "+item.getProductPrice();

            holder.productPrice.setText(price);
            holder.productPrice.setPaintFlags(holder.productPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.productPrice.setVisibility(View.VISIBLE);

            double discounted_price = item.getProductPrice() - item.getProductDiscount();

            String discountPrice = "৳ "+ discounted_price;

            holder.productDiscountedPrice.setText(discountPrice);


            double percentage = (item.getProductDiscount() / item.getProductPrice()) * 100.00;

            int val = (int) percentage;

            String discountPercent = val + " % ";

            holder.productDiscountedPrice.setText(discountPercent);

        } else {
            Toast.makeText(mCtx, "Item is null", Toast.LENGTH_LONG).show();
        }

    }

    private static final DiffUtil.ItemCallback<OwoProduct> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<OwoProduct>() {
                @Override
                public boolean areItemsTheSame(OwoProduct oldItem, OwoProduct newItem) {
                    return oldItem.getProductId() == newItem.getProductId();
                }

                @Override
                public boolean areContentsTheSame(@NotNull OwoProduct oldItem, @NotNull OwoProduct newItem) {
                    return oldItem.equals(newItem);
                }
            };


    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView productName, productPrice, productDiscountedPrice;
        public ImageView imageView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productDiscountedPrice = itemView.findViewById(R.id.product_discounted_price);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            OwoProduct products = getItem(getAdapterPosition());

            Intent intent = new Intent(mCtx, ProductDetailsModification.class);
            intent.putExtra("Products", products);
            mCtx.startActivity(intent);
        }
    }

}