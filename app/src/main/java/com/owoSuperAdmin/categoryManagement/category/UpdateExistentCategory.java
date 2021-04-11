package com.owoSuperAdmin.categoryManagement.category;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import com.owoSuperAdmin.categoryManagement.category.entity.CategoryEntity;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateExistentCategory extends AppCompatActivity {

    private RecyclerView updateRecyclerView;
    private UpdateCategoryAdapter updateCategoryAdapter;
    private LinearLayoutManager linearLayoutManager;
    private final List<CategoryEntity>categoryEntities = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_existent_category);

        updateRecyclerView=findViewById(R.id.updateCategoryRecyclerView);
        swipeRefreshLayout=findViewById(R.id.updateSwipeRefresh);
        linearLayoutManager = new LinearLayoutManager(this);

        ImageView backButton = findViewById(R.id.backIcon);
        backButton.setOnClickListener(v -> onBackPressed());

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.blue));

        swipeRefreshLayout.setOnRefreshListener(() -> {
            getCategoryData();
            showRecycler();
        });

        updateRecyclerView.setHasFixedSize(true);

        showRecycler();
    }

    private void getCategoryData() {
        categoryEntities.clear();

        RetrofitClient.getInstance().getApi()
                .getAllCategories()
                .enqueue(new Callback<List<CategoryEntity>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<CategoryEntity>> call, @NotNull Response<List<CategoryEntity>> response) {
                        if(response.isSuccessful())
                        {
                            assert response.body() != null;
                            categoryEntities.addAll(response.body());
                            updateCategoryAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "No more categories", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(@NotNull Call<List<CategoryEntity>> call, @NotNull Throwable t) {
                        Log.e("Update cat. ", "Error is: "+t.getMessage());
                        Toast.makeText(UpdateExistentCategory.this, "Error fetching categories, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void showRecycler() {
        updateCategoryAdapter = new UpdateCategoryAdapter(UpdateExistentCategory.this, categoryEntities);
        updateRecyclerView.setAdapter(updateCategoryAdapter);
        updateRecyclerView.setLayoutManager(linearLayoutManager);
        updateCategoryAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getCategoryData();
    }
}