package com.owoSuperAdmin.categoryManagement.brand.updateBrand;

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

public class AllSubCategoriesOfACategory extends AppCompatActivity {

    private Long categoryId;
    private AllSubCategoriesOfACategoryAdapter subCategoryAdapter;
    private final List<SubCategoryEntity> subCategoryEntityList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView updateSubCategoryRecyclerView;

    private String indicate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_sub_categories_of_a_category);

        categoryId = Long.parseLong(getIntent().getStringExtra("categoryEntityId"));
        indicate = getIntent().getStringExtra("indicate");

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
                        Toast.makeText(AllSubCategoriesOfACategory.this, "Error fetching sub-categories, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void showRecycler() {
        subCategoryAdapter = new AllSubCategoriesOfACategoryAdapter(AllSubCategoriesOfACategory.this, subCategoryEntityList, indicate);
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