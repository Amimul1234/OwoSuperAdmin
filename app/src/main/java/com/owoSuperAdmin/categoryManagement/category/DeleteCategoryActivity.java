package com.owoSuperAdmin.categoryManagement.category;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.owoSuperAdmin.categoryManagement.category.entity.CategoryEntity;
import com.owoSuperAdmin.configurationsFile.HostAddress;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.owoshop.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteCategoryActivity extends AppCompatActivity {

    private ImageView categoryImage;
    private TextView categoryName;
    private ProgressBar categoryDeleteProgressBar;
    private CategoryEntity categoryEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_category);

        ImageView backButton = findViewById(R.id.backButton);
        categoryImage = findViewById(R.id.categoryImage);
        categoryName = findViewById(R.id.categoryName);
        Button deleteCategoryDetails = findViewById(R.id.deleteCategoryDetails);
        categoryDeleteProgressBar= findViewById(R.id.categoryDeleteProgressBar);

        categoryEntity = (CategoryEntity) getIntent().getSerializableExtra("categoryEntity");

        backButton.setOnClickListener(v -> onBackPressed());

        categoryName.setText(categoryEntity.getCategoryName());
        Glide.with(this).load(HostAddress.HOST_ADDRESS.getAddress()+categoryEntity.getCategoryImage()).into(categoryImage);


        deleteCategoryDetails.setOnClickListener(v -> deleteCategory());
    }

    private void deleteCategory() {
        categoryDeleteProgressBar.setVisibility(View.VISIBLE);
        RetrofitClient.getInstance().getApi().deleteCategory(categoryEntity.getCategoryId()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    categoryDeleteProgressBar.setVisibility(View.GONE);
                    Toast.makeText(DeleteCategoryActivity.this, "Category deleted successfully", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                else{
                    categoryDeleteProgressBar.setVisibility(View.GONE);
                    Toast.makeText(DeleteCategoryActivity.this, "Failed to delete category, please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                categoryDeleteProgressBar.setVisibility(View.GONE);
                Toast.makeText(DeleteCategoryActivity.this, "Failed to delete category, please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}