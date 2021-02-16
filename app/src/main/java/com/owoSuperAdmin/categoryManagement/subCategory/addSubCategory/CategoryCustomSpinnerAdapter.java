package com.owoSuperAdmin.categoryManagement.subCategory.addSubCategory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.owoSuperAdmin.categoryManagement.category.entity.CategoryEntity;
import com.owoSuperAdmin.configurationsFile.HostAddress;
import com.owoSuperAdmin.owoshop.R;
import java.util.List;

public class CategoryCustomSpinnerAdapter extends BaseAdapter {
    Context context;
    List<CategoryEntity> categoryEntityList;
    LayoutInflater inflater;

    public CategoryCustomSpinnerAdapter(Context context, List<CategoryEntity> body) {
        this.context = context;
        this.categoryEntityList = body;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return categoryEntityList.size();
    }

    @Override
    public Object getItem(int i) {
        return categoryEntityList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return categoryEntityList.get(i).getCategoryId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder")
        View view = inflater.inflate(R.layout.category_custom_spinner, null);
        ImageView icon = view.findViewById(R.id.category_image);
        TextView names = view.findViewById(R.id.category_name);
        Glide.with(context).load(HostAddress.HOST_ADDRESS.getAddress()+categoryEntityList.get(position).getCategoryImage()).into(icon);
        names.setText(categoryEntityList.get(position).getCategoryName());
        return view;
    }
}