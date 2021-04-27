package com.shopKPRAdmin.shopInfoChange;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shopKPRAdmin.configurationsFile.HostAddress;
import com.shopKPRAdmin.owoshop.R;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class ShopInfoChangeAdapter extends RecyclerView.Adapter<ShopInfoChangeAdapter.ViewHolder>
{
    private final Context mCtx;
    private final List<ChangeShopInfo> changeShopInfoList;

    public ShopInfoChangeAdapter(Context mCtx, List<ChangeShopInfo> changeShopInfoList) {
        this.mCtx = mCtx;
        this.changeShopInfoList = changeShopInfoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.single_pending_shop_item, parent, false);
        return new ShopInfoChangeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChangeShopInfo changeShopInfo = changeShopInfoList.get(position);


        Glide.with(mCtx).load(HostAddress.HOST_ADDRESS.getAddress() + changeShopInfo.getNewShopImageURL()).
                diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(holder.shopImage);

        holder.shopName.setText(changeShopInfo.getNewShopName());
        holder.shopOwnerMobile.setText(changeShopInfo.getShopOwnerMobileNumber());
        holder.shopAddress.setText(changeShopInfo.getNewShopAddress());

    }

    @Override
    public int getItemCount() {
        return changeShopInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final CircleImageView shopImage;
        private final TextView shopName;
        private final TextView shopOwnerMobile;
        private final TextView shopAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            shopImage = itemView.findViewById(R.id.shopImage);
            shopName = itemView.findViewById(R.id.shopName);
            shopOwnerMobile = itemView.findViewById(R.id.shopOwnerMobileNumber);
            shopAddress = itemView.findViewById(R.id.shopAddress);
            
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(mCtx, ChangeShopInfoActivity.class);
                intent.putExtra("ChangeShopInfo", changeShopInfoList.get(getBindingAdapterPosition()));
                mCtx.startActivity(intent);
            });
        }
    }
}
