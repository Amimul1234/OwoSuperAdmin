package com.owoSuperAdmin.categoryManagement.category;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.Toast;

import com.owoSuperAdmin.adminHomePanel.HomeActivity;
import com.owoSuperAdmin.adminHomePanel.HomeAdapter;
import com.owoSuperAdmin.categoryManagement.category.entity.CategoryEntity;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.owoshop.R;
import com.owoSuperAdmin.shopManagement.allRegisteredShops.RegisteredShopAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateExistentCategory extends AppCompatActivity {


    private RecyclerView updateRecyclerView;
    private UpdateCategoryAdapter adapter;
    private UpdateCategoryAdapter updateAdapter;
    private List<CategoryEntity>categoryEntities=new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_existent_category);
        updateRecyclerView=findViewById(R.id.updateCategoryRecyclerView);
        swipeRefreshLayout=findViewById(R.id.updateSwipeRefresh);

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.blue));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                showRecycler();
            }
        });
        updateRecyclerView.setHasFixedSize(true);
        adapter = new UpdateCategoryAdapter(categoryEntities,this);
        showRecycler();


    }



    private void showRecycler() {
        adapter = new UpdateCategoryAdapter(categoryEntities,this);
        updateRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();

        RetrofitClient.getInstance().getApi()
                .getAllCategories()
                .enqueue(new Callback<List<CategoryEntity>>() {
                    @Override
                    public void onResponse(Call<List<CategoryEntity>> call, Response<List<CategoryEntity>> response) {
                        if(response.code() == 200)
                        {
                            categoryEntities.addAll((List<CategoryEntity>)response.body());
                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "No more category", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<List<CategoryEntity>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}