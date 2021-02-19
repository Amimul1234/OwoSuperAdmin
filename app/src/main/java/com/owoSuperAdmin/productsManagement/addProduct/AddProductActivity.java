package com.owoSuperAdmin.productsManagement.addProduct;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.owoSuperAdmin.categoryManagement.category.entity.CategoryEntity;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity {

    private AddProductAdapter adapter;
    private final List<CategoryEntity> categoryEntityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        ImageView back_to_home = findViewById(R.id.back_to_home_arrow);
        SearchView search_in_category = findViewById(R.id.search_in_category);

        populateCategory();

        back_to_home.setOnClickListener(v -> onBackPressed());

        search_in_category.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });

    }

    private void populateCategory()
    {
        RetrofitClient.getInstance().getApi()
                .getAllCategories()
                .enqueue(new Callback<List<CategoryEntity>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<CategoryEntity>> call, @NotNull Response<List<CategoryEntity>> response) {
                        if(response.isSuccessful())
                        {
                            assert response.body() != null;
                            categoryEntityList.addAll(response.body());

                            RecyclerView recyclerView = findViewById(R.id.add_product_recyclerview_id);

                            adapter = new AddProductAdapter(categoryEntityList, AddProductActivity.this);
                            recyclerView.setAdapter(adapter);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddProductActivity.this);
                            recyclerView.setLayoutManager(linearLayoutManager);

                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(AddProductActivity.this, "Can not fetch categories, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<CategoryEntity>> call, @NotNull Throwable t) {
                        Log.e("AddProduct", "Error occurred, Error is: "+t.getMessage());
                        Toast.makeText(AddProductActivity.this, "Can not fetch categories, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
