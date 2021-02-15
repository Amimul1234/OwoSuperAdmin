package com.owoSuperAdmin.categoryManagement.category;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import org.jetbrains.annotations.NotNull;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteCategoryActivity extends AppCompatActivity {

    private ProgressBar categoryDeleteProgressBar;
    private CategoryEntity categoryEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_category);

        ImageView backButton = findViewById(R.id.backButton);
        ImageView categoryImage = findViewById(R.id.categoryImage);
        TextView categoryName = findViewById(R.id.categoryName);
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

        String imagePath = categoryEntity.getCategoryImage();
        String cleanPath = imagePath.substring(34);

        RetrofitClient.getInstance().getApi().deleteCategory(categoryEntity.getCategoryId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    Toast.makeText(DeleteCategoryActivity.this, "Category deleted successfully", Toast.LENGTH_SHORT).show();

                    RetrofitClient.getInstance().getApi().deleteImage(cleanPath)
                            .enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                                    if(response.isSuccessful())
                                    {
                                        categoryDeleteProgressBar.setVisibility(View.GONE);
                                        Toast.makeText(DeleteCategoryActivity.this, "Category deleted successfully", Toast.LENGTH_SHORT).show();
                                        onBackPressed();
                                    }
                                    else
                                    {
                                        categoryDeleteProgressBar.setVisibility(View.GONE);
                                        Toast.makeText(DeleteCategoryActivity.this, "Failed to delete image", Toast.LENGTH_SHORT).show();
                                        onBackPressed();
                                    }
                                }

                                @Override
                                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                                    Log.e("Del cat.", "Error occurred, Error is: "+t.getMessage());
                                    categoryDeleteProgressBar.setVisibility(View.GONE);
                                    Toast.makeText(DeleteCategoryActivity.this, "Failed to delete category image", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }
                            });


                }
                else{
                    categoryDeleteProgressBar.setVisibility(View.GONE);
                    Toast.makeText(DeleteCategoryActivity.this, "Failed to delete category, please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Log.e("Del cat.", "Error occurred, Error is: "+t.getMessage());
                categoryDeleteProgressBar.setVisibility(View.GONE);
                Toast.makeText(DeleteCategoryActivity.this, "Failed to delete category, please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}