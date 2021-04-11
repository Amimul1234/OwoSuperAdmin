package com.owoSuperAdmin.shopManagement.approveShop;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.owoSuperAdmin.owoshop.R;
import com.owoSuperAdmin.shopManagement.approveShop.shopRequestsPagination.ItemViewModelShopRegistrationRequests;
import com.owoSuperAdmin.shopManagement.entity.Shops;

public class ShopCreationRequestsActivity extends AppCompatActivity {

    private RecyclerView shopApprovalRequestRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ShopRequestsAdapter shopRequestsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_approval);

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        ImageView backButton = findViewById(R.id.backFromShopApproveRequests);
        shopApprovalRequestRecyclerView = findViewById(R.id.shopApprovalRequestRecyclerView);

        backButton.setOnClickListener(v->onBackPressed());

        shopRequestsAdapter = new ShopRequestsAdapter(this);

        getShops();

        swipeRefreshLayout.setOnRefreshListener(()->{
            getShops();
            showOnRecyclerView();
        });

        swipeRefreshLayout.setOnRefreshListener(this::getShops);

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.blue));
    }

    private void getShops() {

        ItemViewModelShopRegistrationRequests itemViewModelShopRegistrationRequests
                = new ItemViewModelShopRegistrationRequests();

        itemViewModelShopRegistrationRequests.itemPagedList.observe(this, (Observer<PagedList<Shops>>) items -> {
            shopRequestsAdapter.submitList(items);
            showOnRecyclerView();
        });

        itemViewModelShopRegistrationRequests.getNetworkState().observe(this, networkState -> {
            shopRequestsAdapter.setNetworkState(networkState);
        });
    }

    private void showOnRecyclerView() {
        shopApprovalRequestRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        shopApprovalRequestRecyclerView.setLayoutManager(layoutManager);
        shopApprovalRequestRecyclerView.setAdapter(shopRequestsAdapter);
        shopRequestsAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getShops();
    }
}
