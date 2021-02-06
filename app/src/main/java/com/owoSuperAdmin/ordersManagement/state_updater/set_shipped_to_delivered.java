package com.owoSuperAdmin.ordersManagement.state_updater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.ordersManagement.order_adapter.ShippedOrderAdapter;
import com.owoSuperAdmin.ordersManagement.entity.Shop_keeper_orders;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class set_shipped_to_delivered extends AppCompatActivity {

    private ImageView back_to_home;
    private List<Shop_keeper_orders> shop_keeper_ordersList = new ArrayList<>();
    private RecyclerView shop_orders_recyclerview;
    private ShippedOrderAdapter shippedOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_shipped_to_delivered);

        loadShippedOrders();

        back_to_home = findViewById(R.id.back_to_home);
        shop_orders_recyclerview = findViewById(R.id.update_order_state);

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadShippedOrders() {
        RetrofitClient.getInstance().getApi()
                .getShippedOrders()
                .enqueue(new Callback<List<Shop_keeper_orders>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Shop_keeper_orders>> call, @NotNull Response<List<Shop_keeper_orders>> response) {
                        if(response.isSuccessful())
                        {
                            assert response.body() != null;
                            shop_keeper_ordersList.addAll(response.body());
                            shippedOrderAdapter = new ShippedOrderAdapter(set_shipped_to_delivered.this, shop_keeper_ordersList);
                            shop_orders_recyclerview.setAdapter(shippedOrderAdapter);
                            shop_orders_recyclerview.setLayoutManager(new LinearLayoutManager(set_shipped_to_delivered.this));
                            shippedOrderAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(set_shipped_to_delivered.this, "Error occurred...please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Shop_keeper_orders>> call, @NotNull Throwable t) {
                        Toast.makeText(set_shipped_to_delivered.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}