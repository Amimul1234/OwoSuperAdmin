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
import com.owoSuperAdmin.ordersManagement.order_adapter.PickedOrderAdapter;
import com.owoSuperAdmin.ordersManagement.entity.Shop_keeper_orders;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class set_picked_to_shipped extends AppCompatActivity {

    private ImageView back_to_home;
    private ProgressBar allianceLoader;
    private RecyclerView picked_order_recycler_view;
    private List<Shop_keeper_orders> shop_keeper_ordersList = new ArrayList<>();
    private PickedOrderAdapter pickedOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipped_order_state);

        back_to_home = findViewById(R.id.back_to_home);
        allianceLoader = findViewById(R.id.loader);
        picked_order_recycler_view = findViewById(R.id.update_order_state);

        loadPickedOrders();

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadPickedOrders() {

        allianceLoader.setVisibility(View.VISIBLE);

        RetrofitClient.getInstance().getApi()
                .getPickedOrders()
                .enqueue(new Callback<List<Shop_keeper_orders>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Shop_keeper_orders>> call, @NotNull Response<List<Shop_keeper_orders>> response) {
                        if(response.isSuccessful())
                        {
                            assert response.body() != null;
                            shop_keeper_ordersList.addAll(response.body());
                            allianceLoader.setVisibility(View.INVISIBLE);
                            pickedOrderAdapter = new PickedOrderAdapter(set_picked_to_shipped.this, shop_keeper_ordersList);
                            picked_order_recycler_view.setAdapter(pickedOrderAdapter);
                            picked_order_recycler_view.setLayoutManager(new LinearLayoutManager(set_picked_to_shipped.this));
                            pickedOrderAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(set_picked_to_shipped.this, "Error occurred...please try again", Toast.LENGTH_SHORT).show();
                            allianceLoader.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Shop_keeper_orders>> call, @NotNull Throwable t) {
                        Toast.makeText(set_picked_to_shipped.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        allianceLoader.setVisibility(View.INVISIBLE);
                    }
                });
    }

}