package com.owosuperadmin.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.owosuperadmin.orders.pending_orders;
import com.owosuperadmin.owoshop.R;
import com.owosuperadmin.shop_related.cancel_order;
import com.owosuperadmin.shop_related.completed_order_state;
import com.owosuperadmin.shop_related.completed_orders;
import com.owosuperadmin.shop_related.picked_order_state;
import com.owosuperadmin.shop_related.shipped_order_state;
import com.owosuperadmin.shop_related.update_order_state;

public class order_management_adapter  extends RecyclerView.Adapter<order_management_adapter.ProductViewHolder> {

    private int images[] = {R.drawable.pending_order, R.drawable.update_order, R.drawable.cancel_order, R.drawable.complete_order};
    private String[] order_management = {"Pending order", "Update State", "Cancel Order", "Completed Orders"};
    private Context mCtx;

    public order_management_adapter(Context mCtx) {
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public order_management_adapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.orders_management, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull order_management_adapter.ProductViewHolder holder, int position) {
        holder.tag.setText(order_management[position]);

        Glide.with(mCtx).load(images[position]).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView tag;
        private CardView cardView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            tag = itemView.findViewById(R.id.tag);
            cardView = itemView.findViewById(R.id.card);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    switch(position)
                    {
                        case 0:
                        {
                            Intent intent = new Intent(mCtx, pending_orders.class);
                            mCtx.startActivity(intent);
                            break;
                        }
                        case 1:
                        {
                            CharSequence options[]=new CharSequence[]{"Set to Processing","Set to Picked", "Set to Shipped", "Set to Completed"};

                            AlertDialog.Builder builder=new AlertDialog.Builder(mCtx);
                            builder.setTitle("Change Order State");

                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {

                                    if (i==0)
                                    {
                                        Intent intent=new Intent(mCtx, update_order_state.class);
                                        mCtx.startActivity(intent);
                                    }
                                    else if(i == 1)
                                    {
                                        Intent intent=new Intent(mCtx, picked_order_state.class);
                                        mCtx.startActivity(intent);
                                    }
                                    else if(i == 2)
                                    {
                                        Intent intent=new Intent(mCtx, shipped_order_state.class);
                                        mCtx.startActivity(intent);
                                    }
                                    else if(i == 3)
                                    {
                                        Intent intent = new Intent(mCtx, completed_order_state.class);
                                        mCtx.startActivity(intent);
                                    }
                                }
                            });
                            builder.show();
                            break;
                        }
                        case 2:
                        {
                            Intent intent = new Intent(mCtx, cancel_order.class);
                            mCtx.startActivity(intent);
                            break;
                        }
                        case 3:
                        {
                            Intent intent = new Intent(mCtx, completed_orders.class);
                            mCtx.startActivity(intent);
                            break;
                        }
                    }
                }
            });
        }
    }
}
