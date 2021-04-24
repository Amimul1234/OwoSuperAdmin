package com.shopKPRAdmin.orderManagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.shopKPRAdmin.orderManagement.cancelledOrders.CanceledOrderList;
import com.shopKPRAdmin.orderManagement.completedOrders.CompletedOrdersList;
import com.shopKPRAdmin.orderManagement.pendingOrders.PendingOrders;
import com.shopKPRAdmin.owoshop.R;
import com.shopKPRAdmin.orderManagement.processingOrders.SetProcessingToPicked;
import com.shopKPRAdmin.orderManagement.pickedOrders.SetPickedToShipped;
import com.shopKPRAdmin.orderManagement.confirmedOrders.SetConfirmedOrderToProcessingState;
import com.shopKPRAdmin.orderManagement.shipped.SetShippedToDeliveredState;

public class OrderManagementAdapter extends RecyclerView.Adapter<OrderManagementAdapter.ProductViewHolder> {

    private final int[] images = {R.drawable.clock, R.drawable.update_order, R.drawable.cancel_order, R.drawable.complete_order};

    private final String[] order_management = {"Pending order", "Update State", "Canceled Order", "Completed Orders"};

    private final Context mCtx;

    public OrderManagementAdapter(Context mCtx) {
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public OrderManagementAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.orders_management, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderManagementAdapter.ProductViewHolder holder, int position) {

        holder.tag.setText(order_management[position]);
        Glide.with(mCtx).load(images[position]).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        private final ImageView image;
        private final TextView tag;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            tag = itemView.findViewById(R.id.tag);

            DisplayMetrics displaymetrics = new DisplayMetrics();

            ((Activity) mCtx).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

            int devicewidth = displaymetrics.widthPixels / 2;
            int deviceheight = displaymetrics.heightPixels / 3;

            itemView.getLayoutParams().width = devicewidth - 30;
            itemView.getLayoutParams().height = deviceheight - 80;


            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                switch(position)
                {
                    case 0:
                    {
                        Intent intent = new Intent(mCtx, PendingOrders.class);
                        mCtx.startActivity(intent);
                        break;
                    }
                    case 1:
                    {
                        CharSequence[] options =new CharSequence[]{"Set to Processing","Set to Picked", "Set to Shipped", "Set to Completed"};

                        AlertDialog.Builder builder=new AlertDialog.Builder(mCtx);
                        builder.setTitle("Change Order State");

                        builder.setItems(options, (dialog, i) -> {

                            if (i==0)
                            {
                                Intent intent=new Intent(mCtx, SetConfirmedOrderToProcessingState.class);
                                mCtx.startActivity(intent);
                            }
                            else if(i == 1)
                            {
                                Intent intent=new Intent(mCtx, SetProcessingToPicked.class);
                                mCtx.startActivity(intent);
                            }
                            else if(i == 2)
                            {
                                Intent intent=new Intent(mCtx, SetPickedToShipped.class);
                                mCtx.startActivity(intent);
                            }
                            else if(i == 3)
                            {
                                Intent intent = new Intent(mCtx, SetShippedToDeliveredState.class);
                                mCtx.startActivity(intent);
                            }
                        });
                        builder.show();
                        break;
                    }
                    case 2:
                    {
                        Intent intent = new Intent(mCtx, CanceledOrderList.class);
                        mCtx.startActivity(intent);
                        break;
                    }
                    case 3:
                    {
                        Intent intent = new Intent(mCtx, CompletedOrdersList.class);
                        mCtx.startActivity(intent);
                        break;
                    }
                }
            });
        }
    }
}
