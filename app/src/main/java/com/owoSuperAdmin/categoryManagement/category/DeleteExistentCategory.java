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

public class DeleteExistentCategory extends AppCompatActivity {

    private RecyclerView deleteRecyclerView;
    private DeleteCategoryAdapter deleteCategoryAdapter;
    private LinearLayoutManager linearLayoutManager;
    private final List<CategoryEntity> categoryEntities = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_existent_category);

        deleteRecyclerView=findViewById(R.id.deleteCategoryRecyclerView);
        swipeRefreshLayout=findViewById(R.id.deleteSwipeRefresh);
        linearLayoutManager = new LinearLayoutManager(this);

        ImageView backButton = findViewById(R.id.backIcon);
        backButton.setOnClickListener(v -> onBackPressed());

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.blue));

        swipeRefreshLayout.setOnRefreshListener(()->{
            getData();
            showRecycler();
        });

        swipeRefreshLayout.setOnRefreshListener(this::showRecycler);

        deleteRecyclerView.setHasFixedSize(true);

        showRecycler();
    }

    private void showRecycler() {
        deleteCategoryAdapter = new DeleteCategoryAdapter(DeleteExistentCategory.this, categoryEntities);
        deleteRecyclerView.setAdapter(deleteCategoryAdapter);
        deleteRecyclerView.setLayoutManager(linearLayoutManager);
        deleteCategoryAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    public void getData()
    {
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
                            deleteCategoryAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "No more categories", Toast.LENGTH_SHORT).show();
                        }

                    }
                    @Override
                    public void onFailure(@NotNull Call<List<CategoryEntity>> call, @NotNull Throwable t) {
                        Log.e("Update cat. ", "Error is: "+t.getMessage());
                        Toast.makeText(DeleteExistentCategory.this, "Error fetching categories, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        deleteCategoryAdapter.notifyDataSetChanged();
    }
}