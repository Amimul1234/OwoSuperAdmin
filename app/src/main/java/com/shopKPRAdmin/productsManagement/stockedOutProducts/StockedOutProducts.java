package com.shopKPRAdmin.productsManagement.stockedOutProducts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.shopKPRAdmin.owoshop.R;
import com.shopKPRAdmin.productsManagement.SearchedProducts;

public class StockedOutProducts extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView allAvailableProductsRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private StockedOutAdapter stockedOutAdapter;


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

        stockedOutAdapter = new StockedOutAdapter(this);
        StockedOutViewModel stockedOutViewModel = new StockedOutViewModel();

        stockedOutViewModel.itemPagedList.observe(this, items -> {
            stockedOutAdapter.submitList(items);
            showOnRecyclerView();
        });

    }

    private void showOnRecyclerView()
    {
        allAvailableProductsRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        allAvailableProductsRecyclerView.setLayoutManager(layoutManager);
        allAvailableProductsRecyclerView.setAdapter(stockedOutAdapter);
        stockedOutAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View v)
    {
        Intent intent = new Intent(StockedOutProducts.this, SearchedProducts.class);
        startActivity(intent);
    }
}
