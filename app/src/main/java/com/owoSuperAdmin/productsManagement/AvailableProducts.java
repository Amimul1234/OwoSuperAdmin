package com.owoSuperAdmin.productsManagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.owoSuperAdmin.productsManagement.entity.Owo_product;
import com.owoSuperAdmin.owoshop.R;
import com.owoSuperAdmin.productsManagement.products.ItemAdapter;
import com.owoSuperAdmin.productsManagement.products.ItemViewModel;

public class AvailableProducts extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ItemAdapter adapter;
    private ImageView search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_availability);

        search = findViewById(R.id.searchProduct);
        search.setOnClickListener(this);

        getProducts();

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getProducts();
            }
        });

        recyclerView = findViewById(R.id.product_availability_recyclerview_id);

    }


    public void getProducts() {
        adapter = new ItemAdapter(this);
        ItemViewModel itemViewModel = new ItemViewModel();

        itemViewModel.itemPagedList.observe(this, new Observer<PagedList<Owo_product>>() {
            @Override
            public void onChanged(@Nullable PagedList<Owo_product> items) {
                adapter.submitList(items);
                showOnRecyclerView();
            }
        });

    }

    private void showOnRecyclerView() {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(AvailableProducts.this, SearchedProducts.class);
        startActivity(intent);
    }
}
