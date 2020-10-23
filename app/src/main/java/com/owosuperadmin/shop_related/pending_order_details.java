package com.owosuperadmin.shop_related;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.owosuperadmin.model.Order_item_adapter;
import com.owosuperadmin.model.Order_model_class;
import com.owosuperadmin.model.Ordered_products;
import com.owosuperadmin.owoshop.R;

import java.util.List;

public class pending_order_details extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Ordered_products> ordered_products_list;
    private ImageView back_from_order_details;
    private TextView shipping_method;
    private Button confirm_button, cancel_button;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        Order_model_class order_model_class = (Order_model_class) getIntent().getSerializableExtra("pending_order");

        TextView order_number = findViewById(R.id.order_number);
        TextView order_date  = findViewById(R.id.order_date);
        TextView total_taka = findViewById(R.id.total_taka);
        TextView discount = findViewById(R.id.discount_taka);
        TextView sub_total = findViewById(R.id.sub_total);
        TextView customer_name = findViewById(R.id.person_name);
        TextView shipping_address = findViewById(R.id.shipping_address);
        TextView mobile_number = findViewById(R.id.mobile_number);
        TextView additional_comments = findViewById(R.id.additional_comments);
        back_from_order_details  = findViewById(R.id.back_from_order_details);
        shipping_method = findViewById(R.id.shipping_method);
        confirm_button = findViewById(R.id.confirm_order_button);
        cancel_button = findViewById(R.id.cancel_order);

        ordered_products_list = order_model_class.getProduct_ids();
        recyclerView = findViewById(R.id.ordered_products);


        progressBar = findViewById(R.id.log_in_progress);

        Order_item_adapter adapter = new Order_item_adapter(this, ordered_products_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        order_number.setText(order_model_class != null ? "#"+order_model_class.getOrder_number() : null);
        order_date.setText(order_model_class.getDate());
        total_taka.setText(order_model_class.getTotalAmount());
        discount.setText(String.valueOf(order_model_class.getCoupon_discount()));

        Double sub = Double.parseDouble(order_model_class.getTotalAmount()) - order_model_class.getCoupon_discount();

        sub_total.setText(String.valueOf(sub));

        additional_comments.setText(order_model_class.getAdditional_comments());

        customer_name.setText(order_model_class.getName());
        shipping_address.setText(order_model_class.getDelivery_address());
        mobile_number.setText(order_model_class.getReceiver_phone());
        shipping_method.setText(order_model_class.getDelivery_method());

        back_from_order_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                order_model_class.setState("Confirmed");

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                databaseReference.child("Shop Keeper Orders").child(order_model_class.getOrder_number()).setValue(order_model_class).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(pending_order_details.this, "Order state Updated", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.VISIBLE);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }
}