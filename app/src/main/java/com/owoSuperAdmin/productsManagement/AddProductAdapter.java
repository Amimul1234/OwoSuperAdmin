package com.owoSuperAdmin.productsManagement;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.owoSuperAdmin.owoshop.R;

import java.util.ArrayList;
import java.util.List;

public class AddProductAdapter extends RecyclerView.Adapter<AddProductAdapter.xyz>{
    public static List<Pair<String, Integer>> product1;
    public List<Pair<String, Integer>> product2 = new ArrayList<Pair<String, Integer>>();
    private Context context;

    public AddProductAdapter() {
    }

    public AddProductAdapter(List<Pair<String, Integer>> product1, Context context) {
        AddProductAdapter.product1 = product1;
        this.context = context;
        product2.addAll(product1);
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
        holder.imageView.setImageResource(product1.get(position).second);
        holder.textView.setText(product1.get(position).first);
    }

    @Override
    public int getItemCount() {
        return product1.size();
    }

    public void filter(String text) {

        product1.clear();

        if(text.isEmpty()){
            product1.addAll(product2);
        }

        else{
            text = text.toLowerCase();

            for(Pair<String, Integer> item: product2){
                if(item.first.toLowerCase().contains(text)){
                    product1.add(item);
                }
            }
        }

        notifyDataSetChanged();
    }

    public class xyz extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textView;

        public xyz(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageViewId);
            textView = itemView.findViewById(R.id.textViewId);
        }

        @Override
        public void onClick(View v) {
            /*
            Intent intent = new Intent(context, AddAProduct.class);
            intent.putExtra("category", product1.get());
            context.startActivity(intent);

             */
        }
    }
}
