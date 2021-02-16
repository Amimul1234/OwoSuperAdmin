package com.owoSuperAdmin.categoryManagement.subCategory;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.owoSuperAdmin.categoryManagement.category.UpdateCategoryAdapter;
import com.owoSuperAdmin.categoryManagement.category.UpdateExistentCategory;
import com.owoSuperAdmin.categoryManagement.category.entity.CategoryEntity;
import com.owoSuperAdmin.categoryManagement.subCategory.entity.SubCategoryEntity;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateSubCategoryActivity extends AppCompatActivity {

    private Long categoryId;
    private SubCategoryAdapter subCategoryAdapter;
    private List<SubCategoryEntity> subCategoryEntityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_sub_category2);

        categoryId = Long.parseLong(getIntent().getStringExtra("categoryEntityId"));
    }

    private void getSubCategoryAdapter() {

        subCategoryEntityList.clear();

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
        getSubCategoryAdapter();
    }
}