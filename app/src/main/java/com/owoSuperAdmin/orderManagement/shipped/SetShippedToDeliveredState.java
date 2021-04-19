package com.owoSuperAdmin.orderManagement.shipped;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.orderManagement.Shop_keeper_orders;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetShippedToDeliveredState extends AppCompatActivity {

    private final List<Shop_keeper_orders> shop_keeper_ordersList = new ArrayList<>();
    private RecyclerView shop_orders_recyclerview;
    private ShippedOrderAdapter shippedOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_shipped_to_delivered);

        loadShippedOrders();

        ImageView back_to_home = findViewById(R.id.back_to_home);
        shop_orders_recyclerview = findViewById(R.id.update_order_state);

        back_to_home.setOnClickListener(v -> onBackPressed());
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
                            shippedOrderAdapter = new ShippedOrderAdapter(SetShippedToDeliveredState.this, shop_keeper_ordersList);
                            shop_orders_recyclerview.setAdapter(shippedOrderAdapter);
                            shop_orders_recyclerview.setLayoutManager(new LinearLayoutManager(SetShippedToDeliveredState.this));
                            shippedOrderAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(SetShippedToDeliveredState.this, "Error occurred...please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Shop_keeper_orders>> call, @NotNull Throwable t) {
                        Toast.makeText(SetShippedToDeliveredState.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}