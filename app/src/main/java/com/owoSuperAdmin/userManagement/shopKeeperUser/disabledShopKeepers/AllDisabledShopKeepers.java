package com.owoSuperAdmin.userManagement.shopKeeperUser.disabledShopKeepers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.owoSuperAdmin.owoshop.R;
import com.owoSuperAdmin.productsManagement.SearchedProducts;

public class AllDisabledShopKeepers extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView allEnabledShopKeepers;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AllDisabledShopKeeperAdapter allDisabledShopKeeperAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_disabled_shop_keepers);

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

        allDisabledShopKeeperAdapter = new AllDisabledShopKeeperAdapter(this);

        ShopKeeperUserViewModel shopKeeperUserViewModel = new ShopKeeperUserViewModel();

        shopKeeperUserViewModel.itemPagedList.observe(this, items -> {
            allDisabledShopKeeperAdapter.submitList(items);
            showOnRecyclerView();
        });

    }

    private void showOnRecyclerView() {
        allEnabledShopKeepers.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        allEnabledShopKeepers.setLayoutManager(layoutManager);
        allEnabledShopKeepers.setAdapter(allDisabledShopKeeperAdapter);
        allDisabledShopKeeperAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(AllDisabledShopKeepers.this, SearchedProducts.class);
        startActivity(intent);
    }
}
