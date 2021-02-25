package com.owoSuperAdmin.productsManagement.search;

import android.content.Context;
import android.content.Intent;
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
import com.owoSuperAdmin.productsManagement.entity.OwoProduct;
import com.owoSuperAdmin.owoshop.R;
import com.owoSuperAdmin.productsManagement.UpdateProduct;

public class SearchAdapter extends PagedListAdapter<OwoProduct, SearchAdapter.ItemViewHolder>{

    private Context mCtx;

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

            Glide.with(mCtx).load(item.getProductImage()).into(holder.imageView);

            holder.txtProductName.setText(item.getProductName());
            holder.txtProductPrice.setText(String.valueOf(item.getProductPrice()));

        } else {
            Toast.makeText(mCtx, "Item is null", Toast.LENGTH_LONG).show();
        }

    }

    private static DiffUtil.ItemCallback<OwoProduct> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<OwoProduct>() {
                @Override
                public boolean areItemsTheSame(OwoProduct oldItem, OwoProduct newItem) {
                    return oldItem.getProductId() == newItem.getProductId();
                }

                @Override
                public boolean areContentsTheSame(OwoProduct oldItem, OwoProduct newItem) {
                    return true;
                }
            };


    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtProductName, txtProductPrice;
        public ImageView imageView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.product_image);
            txtProductName=(TextView)itemView.findViewById(R.id.product_name);
            txtProductPrice=(TextView)itemView.findViewById(R.id.product_price);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            OwoProduct products = getItem(position);
            Intent intent = new Intent(mCtx, UpdateProduct.class);
            intent.putExtra("Products", products);
            mCtx.startActivity(intent);
        }
    }

}