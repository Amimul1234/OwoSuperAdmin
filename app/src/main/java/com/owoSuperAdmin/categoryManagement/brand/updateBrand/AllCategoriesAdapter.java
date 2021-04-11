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
import com.owoSuperAdmin.categoryManagement.category.entity.CategoryEntity;
import com.owoSuperAdmin.configurationsFile.HostAddress;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class AllCategoriesAdapter extends RecyclerView.Adapter<AllCategoriesAdapter.xyz>{

    private final Context context;
    private final List<CategoryEntity> categoryEntities;

    private final String indicate;

    public AllCategoriesAdapter(Context context, List<CategoryEntity> categoryEntities, String indicate) {
        this.context = context;
        this.categoryEntities = categoryEntities;
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
        Glide.with(context).load(HostAddress.HOST_ADDRESS.getAddress()+categoryEntities.get(position).getCategoryImage()).
                into(holder.imageView);

        holder.textView.setText(categoryEntities.get(position).getCategoryName());
    }

    @Override
    public int getItemCount() {
        return categoryEntities.size();
    }


    public class xyz extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;

        public xyz(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.categoryImageView);
            textView = itemView.findViewById(R.id.categoryName);

            itemView.setOnClickListener(v -> {

                CategoryEntity categoryEntity = categoryEntities.get(getAdapterPosition());

                Intent intent = new Intent(context, AllSubCategoriesOfACategory.class);
                intent.putExtra("indicate", indicate);
                intent.putExtra("categoryEntityId", String.valueOf(categoryEntity.getCategoryId()));
                context.startActivity(intent);

            });
        }
    }
}

