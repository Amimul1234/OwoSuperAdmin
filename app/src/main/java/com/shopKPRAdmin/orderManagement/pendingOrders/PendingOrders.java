package com.shopKPRAdmin.orderManagement.pendingOrders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.shopKPRAdmin.network.RetrofitClient;
import com.shopKPRAdmin.orderManagement.Shop_keeper_orders;
import com.shopKPRAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingOrders extends AppCompatActivity
{
    private RecyclerView pending_orders;
    private ProgressBar allianceLoader;
    private final List<Shop_keeper_orders> shopKeeperOrdersList = new ArrayList<>();
    private PendingOrderAdapter pendingOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_orders);

        ImageView back_to_home = findViewById(R.id.back_to_home);
        pending_orders = findViewById(R.id.pending_orders);
        allianceLoader = findViewById(R.id.loader);

        pending_orders.setHasFixedSize(true);

        loadPendingOrder();

        back_to_home.setOnClickListener(v -> onBackPressed());
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
                            shopKeeperOrdersList.addAll(response.body());
                            allianceLoader.setVisibility(View.INVISIBLE);
                            pendingOrderAdapter = new PendingOrderAdapter(PendingOrders.this, shopKeeperOrdersList);
                            pending_orders.setAdapter(pendingOrderAdapter);
                            pending_orders.setLayoutManager(new LinearLayoutManager(PendingOrders.this));
                            pendingOrderAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(PendingOrders.this, "Error occurred...please try again", Toast.LENGTH_SHORT).show();
                            allianceLoader.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Shop_keeper_orders>> call, @NotNull Throwable t) {
                        Toast.makeText(PendingOrders.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        allianceLoader.setVisibility(View.INVISIBLE);
                    }
                });
    }
}