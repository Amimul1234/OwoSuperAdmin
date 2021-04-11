package com.owoSuperAdmin.productsManagement;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.owoSuperAdmin.owoshop.R;
import com.owoSuperAdmin.productsManagement.searchProduct.SearchAdapter;
import com.owoSuperAdmin.productsManagement.searchProduct.SearchViewModel;

public class SearchedProducts extends AppCompatActivity {

    private RecyclerView recyclerView;
    private final SearchAdapter adapter = new SearchAdapter(this);
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_products);

        SearchView searchView = findViewById(R.id.search_product_view);
        recyclerView = findViewById(R.id.searched_products_recycler_view);
        progressBar = findViewById(R.id.searched_progressbar);

        searchView.onActionViewExpanded();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressBar.setVisibility(View.VISIBLE);
                getItem(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }

    private void getItem(String query) {

        SearchViewModel searchViewModel = new SearchViewModel(query);

        searchViewModel.itemPagedList.observe(this, items -> {
            adapter.submitList(items);
            progressBar.setVisibility(View.GONE);
            showOnRecyclerView();
        });
    }


    private void showOnRecyclerView() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}