package com.owoSuperAdmin.ordersManagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Bundle;
import com.owoSuperAdmin.owoshop.R;
import com.owoSuperAdmin.ordersManagement.completedOrders.CompletedAdapter;
import com.owoSuperAdmin.ordersManagement.completedOrders.CompletedViewModel;

public class CompletedOrders extends AppCompatActivity{

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

        swipeRefreshLayout.setOnRefreshListener(() -> getCompletedOrders());

    }

    private void getCompletedOrders() {
        completedAdapter = new CompletedAdapter(this);
        CompletedViewModel completedViewModel = new CompletedViewModel();

        completedViewModel.itemPagedList.observe(this, items -> {
            completedAdapter.submitList(items);
            showOnRecyclerView();
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
