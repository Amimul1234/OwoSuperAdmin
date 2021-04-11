package com.owoSuperAdmin.ordersManagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.owoSuperAdmin.owoshop.R;

public class completed_order_state extends AppCompatActivity {

    private ImageView back_to_home;
    private ProgressBar loader;
    private RecyclerView update_order_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_order_state);

        back_to_home = findViewById(R.id.back_to_home);
        loader = findViewById(R.id.loader);
        update_order_state = findViewById(R.id.update_order_state);

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /*
    @Override
    protected void onStart() {
        super.onStart();

        allianceLoader.setVisibility(View.VISIBLE);

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference();

        Query query = cartListRef.child("Shop Keeper Orders").orderByChild("state").equalTo("Shipped");

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
                        Intent intent = new Intent(completed_order_state.this, CompletedOrders.class);//For giving product description to the user when clicks on a cart item
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

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if(getItemCount() == 0)
                {
                    Toast.makeText(completed_order_state.this, "No orders", Toast.LENGTH_SHORT).show();
                    allianceLoader.setVisibility(View.INVISIBLE);
                }
            }
        };

        update_order_state.setAdapter(adapter);
        update_order_state.setLayoutManager(new LinearLayoutManager(this));
        adapter.startListening();
    }



     */

}