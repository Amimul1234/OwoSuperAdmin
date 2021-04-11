package com.owoSuperAdmin.productsManagement.addProduct;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.owoSuperAdmin.categoryManagement.brand.addBrand.Brands;
import com.owoSuperAdmin.configurationsFile.HostAddress;
import com.owoSuperAdmin.owoshop.R;
import java.util.List;

public class AddProductBrandSpinnerAdapter extends BaseAdapter {

    Context context;
    List<Brands> brandsList;
    LayoutInflater inflater;

    public AddProductBrandSpinnerAdapter(Context context, List<Brands> brandsList) {
        this.context = context;
        this.brandsList = brandsList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return brandsList.size();
    }

    @Override
    public Object getItem(int i) {
        return brandsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return brandsList.get(i).getBrandId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder")
        View view = inflater.inflate(R.layout.category_custom_spinner, null);
        ImageView icon = view.findViewById(R.id.category_image);
        TextView names = view.findViewById(R.id.category_name);

        Glide.with(context).load(HostAddress.HOST_ADDRESS.getAddress()+brandsList.get(position).getBrandImage()).into(icon);
        names.setText(brandsList.get(position).getBrandName());

        return view;
    }
}