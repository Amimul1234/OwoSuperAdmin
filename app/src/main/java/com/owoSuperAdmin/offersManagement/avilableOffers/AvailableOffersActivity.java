package com.owoSuperAdmin.offersManagement.avilableOffers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.offersManagement.entity.OffersEntity;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AvailableOffersActivity extends AppCompatActivity {

    private RecyclerView allAvailableOffersRecyclerView;
    private AvailableOffersAdapter availableOffersAdapter;
    private LinearLayoutManager linearLayoutManager;
    private final List<OffersEntity> offersEntityList = new ArrayList<>();
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

        offersEntityList.clear();

        RetrofitClient.getInstance().getApi()
                .getAllOffers()
                .enqueue(new Callback<List<OffersEntity>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<OffersEntity>> call, @NotNull Response<List<OffersEntity>> response) {
                        if(response.isSuccessful())
                        {
                            assert response.body() != null;
                            offersEntityList.addAll(response.body());
                            availableOffersAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "No more offers", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<OffersEntity>> call, @NotNull Throwable t) {
                        Log.e("Offers", "Error is: "+t.getMessage());
                        Toast.makeText(AvailableOffersActivity.this, "Error fetching offers, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void showRecycler() {
        availableOffersAdapter = new AvailableOffersAdapter(AvailableOffersActivity.this, offersEntityList);
        allAvailableOffersRecyclerView.setAdapter(availableOffersAdapter);
        allAvailableOffersRecyclerView.setLayoutManager(linearLayoutManager);
        availableOffersAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getOffersData();
    }
}