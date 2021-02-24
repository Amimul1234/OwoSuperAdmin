package com.owoSuperAdmin.ordersManagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Bundle;
import com.owoSuperAdmin.ordersManagement.entity.Shop_keeper_orders;
import com.owoSuperAdmin.owoshop.R;
import com.owoSuperAdmin.ordersManagement.cancelledOrders.Cancel_view_model;
import com.owoSuperAdmin.ordersManagement.cancelledOrders.CancelledAdapter;

public class cancel_order extends AppCompatActivity{

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CancelledAdapter cancelledAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_order);

        recyclerView = findViewById(R.id.product_availability_recyclerview_id);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        getCancelledOrders();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCancelledOrders();
                showOnRecyclerView();
            }
        });

    }

    private void getCancelledOrders() {
        cancelledAdapter = new CancelledAdapter(this);
        Cancel_view_model cancel_view_model = new Cancel_view_model();

        cancel_view_model.itemPagedList.observe(this, new Observer<PagedList<Shop_keeper_orders>>() {
            @Override
            public void onChanged(@Nullable PagedList<Shop_keeper_orders> items) {
                cancelledAdapter.submitList(items);
                showOnRecyclerView();
            }
        });

    }

    private void showOnRecyclerView() {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(cancelledAdapter);
        cancelledAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
}
