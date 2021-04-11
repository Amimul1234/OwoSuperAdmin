package com.owoSuperAdmin.productsManagement.addProduct;

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
import com.owoSuperAdmin.categoryManagement.category.entity.CategoryEntity;
import com.owoSuperAdmin.configurationsFile.HostAddress;
import com.owoSuperAdmin.owoshop.R;
import java.util.ArrayList;
import java.util.List;

public class AddProductAdapter extends RecyclerView.Adapter<AddProductAdapter.xyz>{

    private final List<CategoryEntity> categoryEntityList;
    private final List<CategoryEntity> categoryEntityListSecond = new ArrayList<>();
    private final Context context;

    public AddProductAdapter(List<CategoryEntity> categoryEntityList, Context context) {
        this.context = context;
        this.categoryEntityList = categoryEntityList;
        categoryEntityListSecond.addAll(categoryEntityList);
    }

    @NonNull
    @Override
    public xyz onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.add_product_sample,viewGroup,false);
        return new  xyz(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final xyz holder, final int position) {
        Glide.with(context).load(HostAddress.HOST_ADDRESS.getAddress()+categoryEntityList.get(position).getCategoryImage())
                .into(holder.imageView);
        holder.textView.setText(categoryEntityList.get(position).getCategoryName());
    }

    @Override
    public int getItemCount() {
        return categoryEntityList.size();
    }

    public void filter(String text) {

        categoryEntityList.clear();

        if(text.isEmpty()){
            categoryEntityList.addAll(categoryEntityListSecond);
        }

        else{

            text = text.toLowerCase();

            for(CategoryEntity categoryEntity : categoryEntityListSecond)
            {
                if(categoryEntity.getCategoryName().toLowerCase().contains(text))
                {
                    categoryEntityList.add(categoryEntity);
                }
            }
        }

        notifyDataSetChanged();
    }

    public class xyz extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;

        public xyz(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageViewId);
            textView = itemView.findViewById(R.id.textViewId);

            itemView.setOnClickListener(v ->
            {
                Intent intent = new Intent(context, AddAProduct.class);
                intent.putExtra("category", categoryEntityList.get(getAdapterPosition()));
                context.startActivity(intent);
            });
        }
    }
}
