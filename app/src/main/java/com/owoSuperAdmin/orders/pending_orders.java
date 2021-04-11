package com.owoSuperAdmin.orders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.ordersManagement.order_adapter.PendingOrderAdapter;
import com.owoSuperAdmin.ordersManagement.entity.Shop_keeper_orders;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class pending_orders extends AppCompatActivity {
    private ImageView back_to_home;
    private RecyclerView pending_orders;
    private ProgressBar allianceLoader;
    private List<Shop_keeper_orders> shop_keeper_ordersList = new ArrayList<>();
    private PendingOrderAdapter pendingOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_orders);

        back_to_home = findViewById(R.id.back_to_home);
        pending_orders = findViewById(R.id.pending_orders);
        allianceLoader = findViewById(R.id.loader);

        pending_orders.setHasFixedSize(true);

        loadPendingOrder();

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadPendingOrder() {

        allianceLoader.setVisibility(View.VISIBLE);

        RetrofitClient.getInstance().getApi()
                .getShopKeeperPendingOrders()
                .enqueue(new Callback<List<Shop_keeper_orders>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Shop_keeper_orders>> call, @NotNull Response<List<Shop_keeper_orders>> response) {
                        if(response.isSuccessful())
                        {
                            assert response.body() != null;
                            shop_keeper_ordersList.addAll(response.body());
                            allianceLoader.setVisibility(View.INVISIBLE);
                            pendingOrderAdapter = new PendingOrderAdapter(pending_orders.this, shop_keeper_ordersList);
                            pending_orders.setAdapter(pendingOrderAdapter);
                            pending_orders.setLayoutManager(new LinearLayoutManager(pending_orders.this));
                            pendingOrderAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(pending_orders.this, "Error occurred...please try again", Toast.LENGTH_SHORT).show();
                            allianceLoader.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Shop_keeper_orders>> call, @NotNull Throwable t) {
                        Toast.makeText(pending_orders.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        allianceLoader.setVisibility(View.INVISIBLE);
                    }
                });
    }
}