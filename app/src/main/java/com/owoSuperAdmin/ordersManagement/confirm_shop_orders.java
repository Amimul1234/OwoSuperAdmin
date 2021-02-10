package com.owoSuperAdmin.ordersManagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.owoSuperAdmin.owoshop.R;

public class confirm_shop_orders extends AppCompatActivity {

    private ImageView back;
    private RecyclerView order_management;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_shop_orders);

        back = findViewById(R.id.back_to_home);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        order_management = findViewById(R.id.order_options);
        order_management.setLayoutManager(new GridLayoutManager(this, 2));
        order_management.setHasFixedSize(true);
        order_management.setAdapter(new OrderManagementAdapter(this));
    }
}