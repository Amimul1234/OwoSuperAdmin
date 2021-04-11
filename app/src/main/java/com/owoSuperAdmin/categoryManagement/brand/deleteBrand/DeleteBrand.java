package com.owoSuperAdmin.categoryManagement.brand.deleteBrand;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.owoSuperAdmin.adminHomePanel.HomeActivity;
import com.owoSuperAdmin.categoryManagement.brand.addBrand.Brands;
import com.owoSuperAdmin.configurationsFile.HostAddress;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class DeleteBrand extends AppCompatActivity {

    private ProgressBar categoryDeleteProgressBar;

    private Brands brands;
    private Long subCategoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_brand);

        ImageView backButton = findViewById(R.id.backButton);
        ImageView categoryImage = findViewById(R.id.categoryImage);
        TextView categoryName = findViewById(R.id.categoryName);
        Button deleteCategoryDetails = findViewById(R.id.deleteCategoryDetails);
        categoryDeleteProgressBar= findViewById(R.id.categoryDeleteProgressBar);

        brands = (Brands) getIntent().getSerializableExtra("brand");
        subCategoryId = Long.parseLong(getIntent().getStringExtra("subCategoryId"));

        backButton.setOnClickListener(v -> onBackPressed());

        categoryName.setText(brands.getBrandName());
        Glide.with(this).load(HostAddress.HOST_ADDRESS.getAddress()+brands.getBrandImage()).into(categoryImage);

        deleteCategoryDetails.setOnClickListener(v -> deleteCategory());
    }

    private void deleteCategory() {
        categoryDeleteProgressBar.setVisibility(View.VISIBLE);

        String imagePath = brands.getBrandImage();
        String cleanPath = imagePath.substring(34);


        RetrofitClient.getInstance().getApi()
                .deleteBrand(subCategoryId, brands.getBrandId())
                .enqueue(new Callback<ResponseBody>()
                {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    Toast.makeText(DeleteBrand.this, "Brand deleted successfully", Toast.LENGTH_SHORT).show();

                    RetrofitClient.getInstance().getApi()
                            .deleteImage(cleanPath)
                            .enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                                    if(response.isSuccessful())
                                    {
                                        categoryDeleteProgressBar.setVisibility(View.GONE);
                                        Toast.makeText(DeleteBrand.this, "Brand deleted successfully", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(DeleteBrand.this, HomeActivity.class);
                                        intent.setFlags(FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();

                                    }
                                    else
                                    {
                                        categoryDeleteProgressBar.setVisibility(View.GONE);
                                        Toast.makeText(DeleteBrand.this, "Failed to delete image", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(DeleteBrand.this, HomeActivity.class);
                                        intent.setFlags(FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                                @Override
                                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                                    Log.e("Del brand", "Error occurred, Error is: "+t.getMessage());
                                    categoryDeleteProgressBar.setVisibility(View.GONE);
                                    Toast.makeText(DeleteBrand.this, "Failed to delete brand image", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(DeleteBrand.this, HomeActivity.class);
                                    intent.setFlags(FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            });


                }
                else{
                    categoryDeleteProgressBar.setVisibility(View.GONE);
                    Toast.makeText(DeleteBrand.this, "Failed to delete brand, please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Log.e("Del brand", "Error occurred, Error is: "+t.getMessage());
                categoryDeleteProgressBar.setVisibility(View.GONE);
                Toast.makeText(DeleteBrand.this, "Failed to delete brand, please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}