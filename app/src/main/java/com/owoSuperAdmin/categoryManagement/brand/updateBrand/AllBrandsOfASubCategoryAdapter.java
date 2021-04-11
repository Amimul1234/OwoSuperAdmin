package com.owoSuperAdmin.categoryManagement.brand.updateBrand;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.owoSuperAdmin.categoryManagement.brand.addBrand.Brands;
import com.owoSuperAdmin.categoryManagement.brand.deleteBrand.DeleteBrand;
import com.owoSuperAdmin.configurationsFile.HostAddress;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class AllBrandsOfASubCategoryAdapter extends RecyclerView.Adapter<AllBrandsOfASubCategoryAdapter.xyz>{

    private final Context context;
    private final List<Brands> brandsList;
    private final Long subCategoryId;

    private final String indicate;

    public AllBrandsOfASubCategoryAdapter(Context context, List<Brands> brandsList, Long subCategoryId, String indicate) {
        this.context = context;
        this.brandsList = brandsList;
        this.subCategoryId = subCategoryId;
        this.indicate = indicate;
    }

    @NotNull
    @Override
    public xyz onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.category_sample,viewGroup,false);
        return new  xyz(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final xyz holder, final int position) {
        Glide.with(context).load(HostAddress.HOST_ADDRESS.getAddress()+brandsList.get(position).getBrandImage()).
                into(holder.imageView);

        holder.textView.setText(brandsList.get(position).getBrandName());
    }

    @Override
    public int getItemCount() {
        return brandsList.size();
    }


    public class xyz extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;

        public xyz(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.categoryImageView);
            textView = itemView.findViewById(R.id.categoryName);

            itemView.setOnClickListener(v -> {

                Brands brands = brandsList.get(getAdapterPosition());

                if(indicate.equals("update"))
                {
                    Intent intent = new Intent(context, UpdateBrand.class);
                    intent.putExtra("brand", brands);
                    intent.putExtra("subCategoryId", String.valueOf(subCategoryId));
                    context.startActivity(intent);
                }
                else {
                    Intent intent = new Intent(context, DeleteBrand.class);
                    intent.putExtra("brand", brands);
                    intent.putExtra("subCategoryId", String.valueOf(subCategoryId));
                    context.startActivity(intent);
                }
            });
        }
    }
}

