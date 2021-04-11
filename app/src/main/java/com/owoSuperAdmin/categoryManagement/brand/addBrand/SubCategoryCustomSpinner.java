package com.owoSuperAdmin.categoryManagement.brand.addBrand;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.owoSuperAdmin.categoryManagement.subCategory.entity.SubCategoryEntity;
import com.owoSuperAdmin.configurationsFile.HostAddress;
import com.owoSuperAdmin.owoshop.R;
import java.util.List;

public class SubCategoryCustomSpinner extends BaseAdapter {
    Context context;
    List<SubCategoryEntity> subCategoryEntityList;
    LayoutInflater inflater;

    public SubCategoryCustomSpinner(Context context, List<SubCategoryEntity> subCategoryEntityList) {
        this.context = context;
        this.subCategoryEntityList = subCategoryEntityList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return subCategoryEntityList.size();
    }

    @Override
    public Object getItem(int i) {
        return subCategoryEntityList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return subCategoryEntityList.get(i).getSub_category_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint({"ViewHolder", "InflateParams"})
        View view = inflater.inflate(R.layout.category_custom_spinner, null);
        ImageView icon = view.findViewById(R.id.category_image);
        TextView names = view.findViewById(R.id.category_name);
        Glide.with(context).load(HostAddress.HOST_ADDRESS.getAddress()+subCategoryEntityList.get(position).getSub_category_image()).into(icon);
        names.setText(subCategoryEntityList.get(position).getSub_category_name());
        return view;
    }
}