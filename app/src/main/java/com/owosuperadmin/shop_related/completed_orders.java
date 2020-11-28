package com.owosuperadmin.shop_related;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Bundle;
import com.owosuperadmin.model.Shop_keeper_orders;
import com.owosuperadmin.owoshop.R;
import com.owosuperadmin.pagination.completed_orders.CompletedAdapter;
import com.owosuperadmin.pagination.completed_orders.CompletedViewModel;

public class completed_orders extends AppCompatActivity{

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CompletedAdapter completedAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_orders);

        recyclerView = findViewById(R.id.product_availability_recyclerview_id);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        getCompletedOrders();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCompletedOrders();
            }
        });

    }

    private void getCompletedOrders() {
        completedAdapter = new CompletedAdapter(this);
        CompletedViewModel completedViewModel = new CompletedViewModel();

        completedViewModel.itemPagedList.observe(this, new Observer<PagedList<Shop_keeper_orders>>() {
            @Override
            public void onChanged(@Nullable PagedList<Shop_keeper_orders> items) {
                completedAdapter.submitList(items);
                showOnRecyclerView();
            }
        });

    }

    private void showOnRecyclerView() {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(completedAdapter);
        completedAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
}
