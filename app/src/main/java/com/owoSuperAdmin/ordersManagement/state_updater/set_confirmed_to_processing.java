package com.owoSuperAdmin.ordersManagement.state_updater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.ordersManagement.order_adapter.ConfirmedOrderAdapter;
import com.owoSuperAdmin.ordersManagement.entity.Shop_keeper_orders;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class set_confirmed_to_processing extends AppCompatActivity {

    private ImageView back_to_home;
    private ProgressBar allianceLoader;
    private RecyclerView pending_orders;
    private List<Shop_keeper_orders> shop_keeper_ordersList = new ArrayList<>();
    private ConfirmedOrderAdapter confirmedOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_order_state);

        back_to_home = findViewById(R.id.back_to_home);
        allianceLoader = findViewById(R.id.loader);
        pending_orders = findViewById(R.id.update_order_state);

        loadConfirmedOrders();

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void loadConfirmedOrders() {

        allianceLoader.setVisibility(View.VISIBLE);

        RetrofitClient.getInstance().getApi()
                .getShopkeeperConfirmedOrders()
                .enqueue(new Callback<List<Shop_keeper_orders>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Shop_keeper_orders>> call, @NotNull Response<List<Shop_keeper_orders>> response) {
                        if(response.isSuccessful())
                        {
                            assert response.body() != null;
                            shop_keeper_ordersList.addAll(response.body());
                            allianceLoader.setVisibility(View.INVISIBLE);
                            confirmedOrderAdapter = new ConfirmedOrderAdapter(set_confirmed_to_processing.this, shop_keeper_ordersList);
                            pending_orders.setAdapter(confirmedOrderAdapter);
                            pending_orders.setLayoutManager(new LinearLayoutManager(set_confirmed_to_processing.this));
                            confirmedOrderAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(set_confirmed_to_processing.this, "Error occurred...please try again", Toast.LENGTH_SHORT).show();
                            allianceLoader.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Shop_keeper_orders>> call, @NotNull Throwable t) {
                        Toast.makeText(set_confirmed_to_processing.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        allianceLoader.setVisibility(View.INVISIBLE);
                    }
                });
    }

}