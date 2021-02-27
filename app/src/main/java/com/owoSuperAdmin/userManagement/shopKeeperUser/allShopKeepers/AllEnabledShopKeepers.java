package com.owoSuperAdmin.userManagement.shopKeeperUser.allShopKeepers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.owoSuperAdmin.owoshop.R;
import com.owoSuperAdmin.productsManagement.SearchedProducts;

public class AllEnabledShopKeepers extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView allEnabledShopKeepers;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AllEnabledShopKeeperAdapter allEnabledShopKeeperAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_enabled_shop_keepers);

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        allEnabledShopKeepers = findViewById(R.id.allEnabledShopKeepers);

        ImageView searchProduct = findViewById(R.id.searchShopKeeper);
        ImageView backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> onBackPressed());

        searchProduct.setOnClickListener(this);

        getAllEnabledShopKeepers();

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.blue));

        swipeRefreshLayout.setOnRefreshListener(() -> {
            getAllEnabledShopKeepers();
            showOnRecyclerView();
        });
    }


    public void getAllEnabledShopKeepers() {

        allEnabledShopKeeperAdapter = new AllEnabledShopKeeperAdapter(this);

        ShopKeeperUserViewModel shopKeeperUserViewModel = new ShopKeeperUserViewModel();

        shopKeeperUserViewModel.itemPagedList.observe(this, items -> {
            allEnabledShopKeeperAdapter.submitList(items);
            showOnRecyclerView();
        });

    }

    private void showOnRecyclerView() {
        allEnabledShopKeepers.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        allEnabledShopKeepers.setLayoutManager(layoutManager);
        allEnabledShopKeepers.setAdapter(allEnabledShopKeeperAdapter);
        allEnabledShopKeeperAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(AllEnabledShopKeepers.this, SearchedProducts.class);
        startActivity(intent);
    }
}
