package com.shopKPRAdmin.shopInfoChange;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import com.shopKPRAdmin.network.RetrofitClient;
import com.shopKPRAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApproveShopInfoChange extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ShopInfoChangeAdapter shopInfoChangeAdapter;

    private final List<ChangeShopInfo> changeShopInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_shop_info_change);

        ImageView backButton = findViewById(R.id.backButton);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        RecyclerView infoChangeRecyclerView = findViewById(R.id.shop_info_change_recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        infoChangeRecyclerView.setLayoutManager(linearLayoutManager);
        shopInfoChangeAdapter = new ShopInfoChangeAdapter(this, changeShopInfoList);
        infoChangeRecyclerView.setAdapter(shopInfoChangeAdapter);

        getShopChangeList();

        backButton.setOnClickListener(v -> onBackPressed());

        swipeRefreshLayout.setOnRefreshListener(this::getShopChangeList);

    }

    private void getShopChangeList() {
        RetrofitClient.getInstance().getApi()
                .getAllShopChangeRequests()
                .enqueue(new Callback<List<ChangeShopInfo>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<ChangeShopInfo>> call, @NotNull Response<List<ChangeShopInfo>> response) {
                        if(response.isSuccessful())
                        {
                            changeShopInfoList.clear();
                            assert response.body() != null;
                            changeShopInfoList.addAll(response.body());
                            shopInfoChangeAdapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        else
                        {
                            Toast.makeText(ApproveShopInfoChange.this, "Can not get shop change requests list", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<ChangeShopInfo>> call, @NotNull Throwable t) {
                        Log.e("ApproveChange", t.getMessage());
                        Toast.makeText(ApproveShopInfoChange.this, "Can not get shop change requests list", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getShopChangeList();
    }
}