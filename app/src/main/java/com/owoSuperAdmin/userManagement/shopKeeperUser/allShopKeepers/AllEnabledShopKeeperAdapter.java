package com.owoSuperAdmin.userManagement.shopKeeperUser.allShopKeepers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.owoSuperAdmin.owoshop.R;
import com.owoSuperAdmin.userManagement.shopKeeperUser.entity.ShopKeeperUser;
import org.jetbrains.annotations.NotNull;

public class AllEnabledShopKeeperAdapter extends PagedListAdapter<ShopKeeperUser, AllEnabledShopKeeperAdapter.ItemViewHolder>{

    private final Context mCtx;

    public AllEnabledShopKeeperAdapter(Context mCtx) {
        super(DIFF_CALLBACK);
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.shop_keeper_user_sample_card, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        ShopKeeperUser shopKeeperUser = getItem(position);

        if (shopKeeperUser != null) {

            if(shopKeeperUser.getImageUri() != null)
                Glide.with(mCtx).load(HostAddress.HOST_ADDRESS.getAddress()+shopKeeperUser.getImageUri()).into(holder.shopKeeperImage);

            holder.shopKeeperMobileNumber.setText(shopKeeperUser.getMobileNumber());
            holder.shopKeeperName.setText(shopKeeperUser.getName());

        } else {
            Toast.makeText(mCtx, "Item is null", Toast.LENGTH_LONG).show();
        }

    }

    private static final DiffUtil.ItemCallback<ShopKeeperUser> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<ShopKeeperUser>() {
                @Override
                public boolean areItemsTheSame(ShopKeeperUser oldItem, ShopKeeperUser newItem) {
                    return oldItem.getShopKeeperId().equals(newItem.getShopKeeperId());
                }

                @Override
                public boolean areContentsTheSame(@NotNull ShopKeeperUser oldItem, @NotNull ShopKeeperUser newItem) {
                    return oldItem.equals(newItem);
                }
            };


    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView shopKeeperName, shopKeeperMobileNumber;
        public ImageView shopKeeperImage;

        public ItemViewHolder(View itemView) {
            super(itemView);

            shopKeeperImage = itemView.findViewById(R.id.shopKeeperImage);

            shopKeeperName = itemView.findViewById(R.id.shopKeeperName);
            shopKeeperMobileNumber = itemView.findViewById(R.id.shopKeeperMobileNumber);

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
            ShopKeeperUser shopKeeperUser = getItem(getAdapterPosition());

            Intent intent = new Intent(mCtx, ShopKeeperUserDetails.class);
            intent.putExtra("shopKeeperDetails", shopKeeperUser);
            mCtx.startActivity(intent);

        }
    }

}