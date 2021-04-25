package com.shopKPRAdmin.offersManagement.qupon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import com.shopKPRAdmin.network.RetrofitClient;
import com.shopKPRAdmin.offersManagement.entity.OffersEntity;
import com.shopKPRAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllAvailableCoupons extends AppCompatActivity {

    private RecyclerView allAvailableOffersRecyclerView;
    private AllCouponsAdapter allCouponsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private final List<Qupon> quponList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avilable_offers);

        allAvailableOffersRecyclerView = findViewById(R.id.allAvailableOffersRecyclerView);
        swipeRefreshLayout = findViewById(R.id.updateSwipeRefresh);
        linearLayoutManager = new LinearLayoutManager(this);

        ImageView backButton = findViewById(R.id.backIcon);
        backButton.setOnClickListener(v -> onBackPressed());

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.blue));

        swipeRefreshLayout.setOnRefreshListener(()->{
            getOffersData();
            showRecycler();
        });

        allAvailableOffersRecyclerView.setHasFixedSize(true);

        showRecycler();
    }

    private void getOffersData() {

        quponList.clear();

        RetrofitClient.getInstance().getApi()
                .quponList()
                .enqueue(new Callback<List<Qupon>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Qupon>> call, @NotNull Response<List<Qupon>> response) {
                        if(response.isSuccessful())
                        {
                            assert response.body() != null;
                            quponList.addAll(response.body());
                            allCouponsAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "No more offers", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Qupon>> call, @NotNull Throwable t) {
                        Log.e("Coupons", "Error is: "+t.getMessage());
                        Toast.makeText(AllAvailableCoupons.this, "Error fetching coupons, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void showRecycler() {
        allCouponsAdapter = new AllCouponsAdapter(AllAvailableCoupons.this, quponList);
        allAvailableOffersRecyclerView.setAdapter(allCouponsAdapter);
        allAvailableOffersRecyclerView.setLayoutManager(linearLayoutManager);
        allCouponsAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getOffersData();
    }
}