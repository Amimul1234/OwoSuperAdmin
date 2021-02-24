package com.owoSuperAdmin.categoryManagement.brand.updateBrand;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.owoSuperAdmin.categoryManagement.brand.addBrand.Brands;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllBrandsOfASubCategory extends AppCompatActivity {

    private Long subCategoryId;
    private String indicate;

    private AllBrandsOfASubCategoryAdapter brandsOfASubCategoryAdapter;
    private final List<Brands> brandsList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView updateSubCategoryRecyclerView;
    private ImageView emptyImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_brands_of_a_sub_category);

        indicate = getIntent().getStringExtra("indicate");
        subCategoryId = Long.parseLong(getIntent().getStringExtra("subCategoryId"));

        updateSubCategoryRecyclerView = findViewById(R.id.updateSubCategoryRecyclerView);
        swipeRefreshLayout = findViewById(R.id.updateSwipeRefresh);
        linearLayoutManager = new LinearLayoutManager(this);
        emptyImage = findViewById(R.id.empty);
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

        brandsList.clear();

        RetrofitClient.getInstance().getApi()
                .getAllBrands(subCategoryId)
                .enqueue(new Callback<List<Brands>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Brands>> call, @NotNull Response<List<Brands>> response) {
                        if(response.isSuccessful())
                        {
                            emptyImage.setVisibility(View.GONE);
                            assert response.body() != null;
                            brandsList.addAll(response.body());
                            brandsOfASubCategoryAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            emptyImage.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), "No Brands Available", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Brands>> call, @NotNull Throwable t) {
                        Log.e("Update cat. ", "Error is: "+t.getMessage());
                        Toast.makeText(AllBrandsOfASubCategory.this, "Error fetching sub-categories, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void showRecycler() {
        brandsOfASubCategoryAdapter = new AllBrandsOfASubCategoryAdapter(AllBrandsOfASubCategory.this, brandsList, subCategoryId, indicate);
        updateSubCategoryRecyclerView.setAdapter(brandsOfASubCategoryAdapter);
        updateSubCategoryRecyclerView.setLayoutManager(linearLayoutManager);
        brandsOfASubCategoryAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSubCategoryAdapter();
    }
}