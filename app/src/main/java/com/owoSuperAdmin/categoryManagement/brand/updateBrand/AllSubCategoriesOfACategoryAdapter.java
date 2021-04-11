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
import com.owoSuperAdmin.categoryManagement.subCategory.entity.SubCategoryEntity;
import com.owoSuperAdmin.configurationsFile.HostAddress;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class AllSubCategoriesOfACategoryAdapter extends RecyclerView.Adapter<AllSubCategoriesOfACategoryAdapter.xyz>{

    private final Context context;
    private final List<SubCategoryEntity> subCategoryEntityList;

    private final String indicate;

    public AllSubCategoriesOfACategoryAdapter(Context context, List<SubCategoryEntity> subCategoryEntityList, String indicate) {
        this.context = context;
        this.subCategoryEntityList = subCategoryEntityList;
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
        Glide.with(context).load(HostAddress.HOST_ADDRESS.getAddress()+subCategoryEntityList.get(position).getSub_category_image()).
                into(holder.imageView);

        holder.textView.setText(subCategoryEntityList.get(position).getSub_category_name());
    }

    @Override
    public int getItemCount() {
        return subCategoryEntityList.size();
    }


    public class xyz extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;

        public xyz(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.categoryImageView);
            textView = itemView.findViewById(R.id.categoryName);

            itemView.setOnClickListener(v -> {

                SubCategoryEntity subCategoryEntity = subCategoryEntityList.get(getAdapterPosition());

                Intent intent = new Intent(context, AllBrandsOfASubCategory.class);
                intent.putExtra("indicate", indicate);
                intent.putExtra("subCategoryId", String.valueOf(subCategoryEntity.getSub_category_id()));
                context.startActivity(intent);
            });
        }
    }
}

