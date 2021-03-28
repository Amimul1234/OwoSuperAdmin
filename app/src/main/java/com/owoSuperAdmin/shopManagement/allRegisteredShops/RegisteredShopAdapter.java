package com.owoSuperAdmin.shopManagement.allRegisteredShops;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.owoSuperAdmin.configurationsFile.HostAddress;
import com.owoSuperAdmin.owoshop.R;
import com.owoSuperAdmin.shopManagement.approveShop.ApproveAPendingShop;
import com.owoSuperAdmin.shopManagement.entity.Shops;
import com.owoSuperAdmin.utilities.NetworkState;
import org.jetbrains.annotations.NotNull;
import de.hdodenhof.circleimageview.CircleImageView;

public class RegisteredShopAdapter extends PagedListAdapter<Shops, RecyclerView.ViewHolder> {

    private final Context mCtx;
    private NetworkState networkState;
    private static final int TYPE_PROGRESS = 0;
    private static final int TYPE_ITEM = 1;

    public RegisteredShopAdapter(Context mCtx) {
        super(DIFF_CALLBACK);
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_PROGRESS)
        {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.network_state, parent, false);
            return new NetworkStateItemViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.single_pending_shop_item, parent, false);
            return new PendingShopViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof PendingShopViewHolder)
        {
            PendingShopViewHolder itemViewHolder = (PendingShopViewHolder) holder;

            Shops shops = getItem(position);

            if (shops != null) {
                Glide.with(mCtx).
                        load(HostAddress.HOST_ADDRESS.getAddress()+shops.getShop_image_uri()).
                        into(itemViewHolder.shopImage);

                itemViewHolder.shopName.setText(shops.getShop_name());
                itemViewHolder.shopAddress.setText(shops.getShop_address());
                itemViewHolder.shopOwnerMobileNumber.setText(shops.getShop_owner_mobile());

            } else {
                Toast.makeText(mCtx, "Item is null", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            ((NetworkStateItemViewHolder) holder).bindView(networkState);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount()) {
            return TYPE_PROGRESS;
        } else {
            return TYPE_ITEM;
        }
    }

    private boolean hasExtraRow() {
        return networkState != null && networkState != NetworkState.LOADED;
    }

    public void setNetworkState(NetworkState newNetworkState) {
        NetworkState previousState = this.networkState;
        boolean previousExtraRow = hasExtraRow();
        this.networkState = newNetworkState;
        boolean newExtraRow = hasExtraRow();
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(getItemCount());
            } else {
                notifyItemInserted(getItemCount());
            }
        } else if (newExtraRow && previousState != newNetworkState) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

    private static final DiffUtil.ItemCallback<Shops> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Shops>() {
                @Override
                public boolean areItemsTheSame(Shops shopsOld, Shops shopsNew) {
                    return shopsNew.getShop_id().equals(shopsOld.getShop_id());
                }

                @Override
                public boolean areContentsTheSame(Shops shopsOld, @NotNull Shops shopsNew) {
                    return shopsOld.equals(shopsNew);
                }
            };

    public static class NetworkStateItemViewHolder extends RecyclerView.ViewHolder {

        private final ProgressBar progressBar;
        private final TextView errorMsg;

        public NetworkStateItemViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
            errorMsg = itemView.findViewById(R.id.errorMsg);
        }

        public void bindView(NetworkState networkState) {
            if (networkState != null && networkState.getStatus() == NetworkState.Status.RUNNING) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
            if (networkState != null && networkState.getStatus() == NetworkState.Status.FAILED) {
                errorMsg.setVisibility(View.VISIBLE);
                errorMsg.setText(networkState.getMsg());
            } else {
                errorMsg.setVisibility(View.GONE);
            }
        }
    }

    public class PendingShopViewHolder extends RecyclerView.ViewHolder{

        public TextView shopName, shopOwnerMobileNumber, shopAddress;
        public CircleImageView shopImage;

        public PendingShopViewHolder(@NonNull View itemView) {
            super(itemView);

            shopName = itemView.findViewById(R.id.shopName);
            shopOwnerMobileNumber = itemView.findViewById(R.id.shopOwnerMobileNumber);
            shopAddress = itemView.findViewById(R.id.shopAddress);
            shopImage = itemView.findViewById(R.id.shopImage);

            itemView.setOnClickListener(v -> {

                Shops shops = getItem(getAdapterPosition());

                Intent intent = new Intent(mCtx, ApprovedShopDetails.class);
                intent.putExtra("ShopRequest", shops);
                mCtx.startActivity(intent);
            });
        }
    }

}
