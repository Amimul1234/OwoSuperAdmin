package com.owosuperadmin.orders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.AllianceLoader;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.owosuperadmin.model.Order_model_class;
import com.owosuperadmin.owoshop.R;
import com.owosuperadmin.shop_related.order_details;
import com.owosuperadmin.viewHolder.OrderListItemViewHolder;

public class pending_orders extends AppCompatActivity {

    private ImageView back_to_home;
    private RecyclerView pending_orders;
    private AllianceLoader allianceLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_orders);

        back_to_home = findViewById(R.id.back_to_home);
        pending_orders = findViewById(R.id.pending_orders);
        allianceLoader = findViewById(R.id.loader);

        pending_orders.setHasFixedSize(true);
    }


    @Override
    protected void onStart() {
        super.onStart();

        allianceLoader.setVisibility(View.VISIBLE);

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference();

        Query query = cartListRef.child("Shop Keeper Orders").orderByChild("state").equalTo("Pending");

        FirebaseRecyclerOptions<Order_model_class> options =
                new FirebaseRecyclerOptions.Builder<Order_model_class>()
                        .setQuery(query, Order_model_class.class).build();


        FirebaseRecyclerAdapter<Order_model_class, OrderListItemViewHolder> adapter
                = new FirebaseRecyclerAdapter<Order_model_class, OrderListItemViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final OrderListItemViewHolder holder, int position, @NonNull final Order_model_class model) {

                holder.order_number.setText("Order :"+model.getOrder_number());
                holder.order_phone_number.setText(model.getShop_number());


                Double total_with_discount = Double.parseDouble(model.getTotalAmount()) - model.getCoupon_discount();
                holder.total_amount_with_discount.setText("৳ "+ String.valueOf(total_with_discount));

                holder.order_address_city.setText(model.getDelivery_address());

                holder.time_and_date.setText(model.getDate() + ", "+model.getTime());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(pending_orders.this, order_details.class);//For giving product description to the user when clicks on a cart item
                        intent.putExtra("pending_order", model);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public OrderListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                OrderListItemViewHolder holder = new OrderListItemViewHolder(view);
                allianceLoader.setVisibility(View.INVISIBLE);
                return holder;
            }

        };

        pending_orders.setAdapter(adapter);
        pending_orders.setLayoutManager(new LinearLayoutManager(this));
        adapter.startListening();
    }

}