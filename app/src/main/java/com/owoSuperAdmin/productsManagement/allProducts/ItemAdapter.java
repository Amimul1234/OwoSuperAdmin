package com.owoSuperAdmin.productsManagement.allProducts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.DisplayMetrics;
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

public class ItemAdapter extends PagedListAdapter<OwoProduct, ItemAdapter.ItemViewHolder>{
    private final Context mCtx;

    public ItemAdapter(Context mCtx) {
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

            Glide.with(mCtx).load(HostAddress.HOST_ADDRESS.getAddress()+item.getProductImage()).into(holder.product_image);

            holder.product_name.setText(item.getProductName());

            String price = "৳ "+item.getProductPrice();

            holder.product_price.setText(price);
            holder.product_price.setPaintFlags(holder.product_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.product_price.setVisibility(View.VISIBLE);

            double discounted_price = item.getProductPrice() - item.getProductDiscount();

            String discountPrice = "৳ "+ discounted_price;

            holder.product_discounted_price.setText(discountPrice);


            double percentage = (item.getProductDiscount() / item.getProductPrice()) * 100.00;

            int val = (int) percentage;

            String discountPercent = val + " % ";

            holder.discount_percentage.setText(discountPercent);

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

        public TextView product_name, product_price, product_discounted_price, discount_percentage;
        public ImageView product_image;

        public ItemViewHolder(View itemView) {
            super(itemView);

            product_image = itemView.findViewById(R.id.product_image);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            product_discounted_price = itemView.findViewById(R.id.product_discounted_price);
            discount_percentage = itemView.findViewById(R.id.discount_percentage);

            DisplayMetrics displaymetrics = new DisplayMetrics(); //Setting things dynamically

            ((Activity) mCtx).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);


            int devicewidth = displaymetrics.widthPixels / 2;
            int deviceheight = displaymetrics.heightPixels / 3;

            itemView.getLayoutParams().width = devicewidth;
            itemView.getLayoutParams().height = deviceheight;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            OwoProduct owoProduct = getItem(getAdapterPosition());

            Intent intent = new Intent(mCtx, ProductDetailsModification.class);
            intent.putExtra("Products", owoProduct);
            mCtx.startActivity(intent);

        }
    }
    
}