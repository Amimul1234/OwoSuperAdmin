package com.owoSuperAdmin.shopManagement.allRegisteredShops;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.owoSuperAdmin.owoshop.R;
import com.owoSuperAdmin.shopManagement.allRegisteredShops.registeredShopsPagination.RegisteredShopViewModel;
import com.owoSuperAdmin.shopManagement.entity.Shops;

public class ManageRegisteredShops extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView registeredShopsRecyclerView;
    private RegisteredShopAdapter registeredShopAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);

        registeredShopAdapter = new RegisteredShopAdapter(this);

        getShops();

        ImageView backFromRegisteredShops = findViewById(R.id.backFromRegisteredShops);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        registeredShopsRecyclerView = findViewById(R.id.registeredShopsRecyclerView);

        backFromRegisteredShops.setOnClickListener(v -> onBackPressed());

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.blue));
        swipeRefreshLayout.setOnRefreshListener(this::getShops);
    }

    private void getShops() {
        RegisteredShopViewModel registeredShopViewModel = new RegisteredShopViewModel();

        registeredShopViewModel.itemPagedList.observe(this, (Observer<PagedList<Shops>>) items -> {
            registeredShopAdapter.submitList(items);
            showOnRecyclerView();
        });

        registeredShopViewModel.getNetworkState().observe(this, networkState -> {
            registeredShopAdapter.setNetworkState(networkState);
        });
    }

    private void showOnRecyclerView() {
        registeredShopsRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        registeredShopsRecyclerView.setLayoutManager(layoutManager);
        registeredShopsRecyclerView.setAdapter(registeredShopAdapter);
        registeredShopAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

}
