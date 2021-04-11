package com.owoSuperAdmin.productsManagement.allProducts;

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

public class AvailableProducts extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView allAvailableProductsRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ItemAdapter productsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_availability);

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        allAvailableProductsRecyclerView = findViewById(R.id.allAvailableProductsRecyclerView);

        ImageView searchProduct = findViewById(R.id.searchProduct);
        ImageView backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> onBackPressed());

        searchProduct.setOnClickListener(this);

        getProducts();

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.blue));

        swipeRefreshLayout.setOnRefreshListener(() -> {
            getProducts();
            showOnRecyclerView();
        });
    }


    public void getProducts() {

        productsAdapter = new ItemAdapter(this);
        ItemViewModel itemViewModel = new ItemViewModel();

        itemViewModel.itemPagedList.observe(this, items -> {
            productsAdapter.submitList(items);
            showOnRecyclerView();
        });

    }

    private void showOnRecyclerView() {
        allAvailableProductsRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        allAvailableProductsRecyclerView.setLayoutManager(layoutManager);
        allAvailableProductsRecyclerView.setAdapter(productsAdapter);
        productsAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(AvailableProducts.this, SearchedProducts.class);
        startActivity(intent);
    }
}
