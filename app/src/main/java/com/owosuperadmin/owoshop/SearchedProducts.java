package com.owosuperadmin.owoshop;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.owosuperadmin.model.Owo_product;
import com.owosuperadmin.pagination.SearchAdapter;
import com.owosuperadmin.pagination.SearchViewModel;

public class SearchedProducts extends AppCompatActivity {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private SearchAdapter adapter = new SearchAdapter(this);
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_products);

        searchView = findViewById(R.id.search_product_view);
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
        SearchViewModel searchViewModel = new SearchViewModel(query);//Refreshing the model for new filtration

        searchViewModel.itemPagedList.observe(this, new Observer<PagedList<Owo_product>>() {
            @Override
            public void onChanged(@Nullable PagedList<Owo_product> items) {
                adapter.submitList(items);
                progressBar.setVisibility(View.GONE);
                showOnRecyclerView();
            }
        });
    }


    private void showOnRecyclerView() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }



}