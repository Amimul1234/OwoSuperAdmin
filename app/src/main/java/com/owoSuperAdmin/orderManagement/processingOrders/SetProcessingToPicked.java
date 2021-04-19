package com.owoSuperAdmin.orderManagement.processingOrders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class SetProcessingToPicked extends AppCompatActivity {

    private ProgressBar allianceLoader;
    private final List<Shop_keeper_orders> shop_keeper_ordersList = new ArrayList<>();
    private RecyclerView picked_orders_recyclerview;
    private ProcessingOrderAdapter processingOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picked_order_state);

        ImageView back_to_home = findViewById(R.id.back_to_home);
        allianceLoader = findViewById(R.id.loader);
        picked_orders_recyclerview = findViewById(R.id.update_order_state);

        loadProcessingOrder();

        back_to_home.setOnClickListener(v -> onBackPressed());
    }

    private void loadProcessingOrder() {

        allianceLoader.setVisibility(View.VISIBLE);

        RetrofitClient.getInstance().getApi()
                .getProcessingOrders()
                .enqueue(new Callback<List<Shop_keeper_orders>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Shop_keeper_orders>> call, @NotNull Response<List<Shop_keeper_orders>> response) {
                        if(response.isSuccessful())
                        {
                            assert response.body() != null;
                            shop_keeper_ordersList.addAll(response.body());
                            allianceLoader.setVisibility(View.INVISIBLE);
                            processingOrderAdapter = new ProcessingOrderAdapter(SetProcessingToPicked.this, shop_keeper_ordersList);
                            picked_orders_recyclerview.setAdapter(processingOrderAdapter);
                            picked_orders_recyclerview.setLayoutManager(new LinearLayoutManager(SetProcessingToPicked.this));
                            processingOrderAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(SetProcessingToPicked.this, "Error occurred...please try again", Toast.LENGTH_SHORT).show();
                            allianceLoader.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Shop_keeper_orders>> call, @NotNull Throwable t) {
                        Toast.makeText(SetProcessingToPicked.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        allianceLoader.setVisibility(View.INVISIBLE);
                    }
                });
    }


}