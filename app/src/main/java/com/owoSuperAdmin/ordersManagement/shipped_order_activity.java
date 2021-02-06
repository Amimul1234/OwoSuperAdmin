package com.owoSuperAdmin.ordersManagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.owoSuperAdmin.ordersManagement.entity.Order_item_adapter;
import com.owoSuperAdmin.ordersManagement.entity.ShopKeeperOrderedProducts;
import com.owoSuperAdmin.ordersManagement.entity.Shop_keeper_orders;
import com.owoSuperAdmin.owoshop.R;

import java.util.List;

public class shipped_order_activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<ShopKeeperOrderedProducts> shop_keeperOrderedProductsList;
    private ImageView back_from_order_details;
    private TextView shipping_method;
    private Button confirm_button, cancel_button;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipped_order_activity);

        Shop_keeper_orders order_model_class = (Shop_keeper_orders) getIntent().getSerializableExtra("pending_order");

        TextView order_number = findViewById(R.id.order_number);
        TextView order_date  = findViewById(R.id.order_date);
        TextView total_taka = findViewById(R.id.total_taka);
        TextView discount = findViewById(R.id.discount_taka);
        TextView sub_total = findViewById(R.id.sub_total);
        TextView shipping_address = findViewById(R.id.shipping_address);
        TextView mobile_number = findViewById(R.id.mobile_number);
        TextView additional_comments = findViewById(R.id.additional_comments);
        back_from_order_details  = findViewById(R.id.back_from_order_details);
        shipping_method = findViewById(R.id.shipping_method);
        confirm_button = findViewById(R.id.confirm_order_button);
        cancel_button = findViewById(R.id.cancel_order);

        shop_keeperOrderedProductsList = order_model_class.getShop_keeper_ordered_products();
        recyclerView = findViewById(R.id.ordered_products);


        progressBar = findViewById(R.id.log_in_progress);

        Order_item_adapter adapter = new Order_item_adapter(this, shop_keeperOrderedProductsList);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        order_number.setText(order_model_class != null ? "#"+order_model_class.getOrder_number() : null);
        order_date.setText(order_model_class.getDate());
        total_taka.setText(String.valueOf(order_model_class.getTotal_amount()));
        discount.setText(String.valueOf(order_model_class.getCoupon_discount()));

        Double sub = order_model_class.getTotal_amount() - order_model_class.getCoupon_discount();

        sub_total.setText(String.valueOf(sub));

        additional_comments.setText(order_model_class.getAdditional_comments());

        shipping_address.setText(order_model_class.getDelivery_address());
        mobile_number.setText(order_model_class.getReceiver_phone());
        shipping_method.setText(order_model_class.getMethod());

        back_from_order_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                progressBar.setVisibility(View.VISIBLE);
                order_model_class.setState("Shipped");

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                databaseReference.child("Shop Keeper Orders").child(order_model_class.getOrder_number()).setValue(order_model_class).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(shipped_order_activity.this, "Order state Updated", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.VISIBLE);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });

                 */
            }
        });
    }
}