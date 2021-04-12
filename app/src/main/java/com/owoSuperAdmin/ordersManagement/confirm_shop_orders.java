package com.owoSuperAdmin.ordersManagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.ImageView;
import com.owoSuperAdmin.owoshop.R;

public class confirm_shop_orders extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_shop_orders);

        ImageView back = findViewById(R.id.back_to_home);

        back.setOnClickListener(v -> onBackPressed());

        RecyclerView order_management = findViewById(R.id.order_options);
        order_management.setLayoutManager(new GridLayoutManager(this, 2));
        order_management.setHasFixedSize(true);
        order_management.setAdapter(new OrderManagementAdapter(this));
    }
}