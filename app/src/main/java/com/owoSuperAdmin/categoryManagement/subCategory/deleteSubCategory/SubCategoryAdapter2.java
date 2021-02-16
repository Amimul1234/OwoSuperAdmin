package com.owoSuperAdmin.categoryManagement.subCategory.deleteSubCategory;

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


import java.util.List;

public class SubCategoryAdapter2 extends RecyclerView.Adapter<SubCategoryAdapter2.xyz>{

    private final Context context;
    private final List<SubCategoryEntity> subCategoryEntityList;

    public SubCategoryAdapter2(Context context, List<SubCategoryEntity> subCategoryEntityList) {
        this.context = context;
        this.subCategoryEntityList = subCategoryEntityList;
    }


    @NonNull
    @Override
    public SubCategoryAdapter2.xyz onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.category_sample,parent,false);
        return new SubCategoryAdapter2.xyz(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryAdapter2.xyz holder, int position) {
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

                Intent intent = new Intent(context, SubCategoryDeleteActivity.class);
                intent.putExtra("subCategoryEntity", subCategoryEntity);
                context.startActivity(intent);
            });
        }
    }
}
