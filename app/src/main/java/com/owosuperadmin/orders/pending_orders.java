package com.owosuperadmin.orders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.owosuperadmin.owoshop.R;

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

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Toast.makeText(pending_orders.this, snapshot.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        /*
        FirebaseRecyclerOptions<Ordered_products_model> options =
                new FirebaseRecyclerOptions.Builder<Ordered_products_model>()
                        .setQuery(query, Ordered_products_model.class).build();


        FirebaseRecyclerAdapter<Ordered_products_model, OrderListItemViewHolder> adapter
                = new FirebaseRecyclerAdapter<Ordered_products_model, OrderListItemViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final OrderListItemViewHolder holder, int position, @NonNull final Ordered_products_model model) {

                holder.order_number.setText(model.getOrder_number());

                Double total_with_discount = Double.parseDouble(model.getTotalAmount()) - model.getCoupon_discount();
                holder.total_amount_with_discount.setText("৳ "+ String.valueOf(total_with_discount));

                holder.order_status.setText(model.getState());

                holder.time_and_date.setText(model.getDate() + ", "+model.getTime());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Order_list.this, Order_details_for_single_item.class);//For giving product description to the user when clicks on a cart item
                        intent.putExtra("Order", model);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public OrderListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_card, parent, false);
                OrderListItemViewHolder holder = new OrderListItemViewHolder(view);
                allianceLoader.setVisibility(View.INVISIBLE);
                return holder;
            }

            @Override
            public void onDataChanged() {
                if(getItemCount() == 0)
                {
                    empty_image_view.setVisibility(View.VISIBLE);
                    empty_text_view.setVisibility(View.VISIBLE);
                    allianceLoader.setVisibility(View.INVISIBLE);
                }
            }
        };

        order_list_recycler_view.setAdapter(adapter);
        adapter.startListening();
         */
    }

}