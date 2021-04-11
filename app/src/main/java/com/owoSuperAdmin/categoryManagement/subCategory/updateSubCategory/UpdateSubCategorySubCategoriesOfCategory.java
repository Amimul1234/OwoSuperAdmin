package com.owoSuperAdmin.categoryManagement.subCategory.updateSubCategory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import com.owoSuperAdmin.categoryManagement.subCategory.entity.SubCategoryEntity;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateSubCategorySubCategoriesOfCategory extends AppCompatActivity {

    private Long categoryId;
    private SubCategoryAdapter subCategoryAdapter;
    private final List<SubCategoryEntity> subCategoryEntityList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView updateSubCategoryRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_sub_category2);

        categoryId = Long.parseLong(getIntent().getStringExtra("categoryEntityId"));

        updateSubCategoryRecyclerView = findViewById(R.id.updateSubCategoryRecyclerView);
        swipeRefreshLayout = findViewById(R.id.updateSwipeRefresh);
        linearLayoutManager = new LinearLayoutManager(this);
        ImageView backIcon = findViewById(R.id.backIcon);

        backIcon.setOnClickListener(v -> onBackPressed());

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.blue));

        swipeRefreshLayout.setOnRefreshListener(()->{
            getSubCategoryAdapter();
            showRecycler();
        });

        updateSubCategoryRecyclerView.setHasFixedSize(true);
        showRecycler();
    }

    private void getSubCategoryAdapter() {

        subCategoryEntityList.clear();
        RetrofitClient.getInstance().getApi()
                .getAllSubCategories(categoryId)
                .enqueue(new Callback<List<SubCategoryEntity>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<SubCategoryEntity>> call, @NotNull Response<List<SubCategoryEntity>> response) {
                        if(response.isSuccessful())
                        {
                            assert response.body() != null;
                            subCategoryEntityList.addAll(response.body());
                            subCategoryAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "No more sub-categories", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(@NotNull Call<List<SubCategoryEntity>> call, @NotNull Throwable t) {
                        Log.e("Update cat. ", "Error is: "+t.getMessage());
                        Toast.makeText(UpdateSubCategorySubCategoriesOfCategory.this, "Error fetching sub-categories, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void showRecycler() {
        subCategoryAdapter = new SubCategoryAdapter(UpdateSubCategorySubCategoriesOfCategory.this, subCategoryEntityList, categoryId);
        updateSubCategoryRecyclerView.setAdapter(subCategoryAdapter);
        updateSubCategoryRecyclerView.setLayoutManager(linearLayoutManager);
        subCategoryAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSubCategoryAdapter();
    }
}