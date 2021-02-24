package com.owoSuperAdmin.categoryManagement.subCategory.deleteSubCategory;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.owoSuperAdmin.categoryManagement.subCategory.entity.SubCategoryEntity;
import com.owoSuperAdmin.configurationsFile.HostAddress;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategoryDeleteActivity extends AppCompatActivity {

    private ProgressBar subCategoryDeleteProgressBar;
    private SubCategoryEntity subCategoryEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_delete);

        ImageView backButton = findViewById(R.id.backButton);
        ImageView subCategoryImage = findViewById(R.id.subCategoryImage);
        EditText subCategoryName = findViewById(R.id.subCategoryName);
        Button deleteSubCategoryDetails = findViewById(R.id.deleteSubCategoryDetails);

        subCategoryDeleteProgressBar= findViewById(R.id.subCategoryDeleteProgressBar);

        subCategoryEntity = (SubCategoryEntity) getIntent().getSerializableExtra("subCategoryEntity");

        backButton.setOnClickListener(v -> onBackPressed());

        subCategoryName.setText(subCategoryEntity.getSub_category_name());
        Glide.with(this).load(HostAddress.HOST_ADDRESS.getAddress()+ subCategoryEntity.getSub_category_image()).into(subCategoryImage);


        deleteSubCategoryDetails.setOnClickListener(v -> deleteSubCategory());
    }

    private void deleteSubCategory() {
        subCategoryDeleteProgressBar.setVisibility(View.VISIBLE);


        String imagePath = subCategoryEntity.getSub_category_image();
        String cleanPath = imagePath.substring(34);

        RetrofitClient.getInstance().getApi().deleteSubCategory(subCategoryEntity.getSub_category_id()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    Toast.makeText(SubCategoryDeleteActivity.this, "Sub Category deleted successfully", Toast.LENGTH_SHORT).show();

                    RetrofitClient.getInstance().getApi().
                            deleteImage(cleanPath)
                            .enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                                    if(response.isSuccessful())
                                    {
                                        subCategoryDeleteProgressBar.setVisibility(View.GONE);
                                        Toast.makeText(SubCategoryDeleteActivity.this, "Sub Category deleted successfully", Toast.LENGTH_SHORT).show();
                                        onBackPressed();
                                    }
                                    else
                                    {
                                        subCategoryDeleteProgressBar.setVisibility(View.GONE);
                                        Toast.makeText(SubCategoryDeleteActivity.this, "Failed to delete image", Toast.LENGTH_SHORT).show();
                                        onBackPressed();
                                    }
                                }

                                @Override
                                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                                    Log.e("Del cat.", "Error occurred, Error is: "+t.getMessage());
                                    subCategoryDeleteProgressBar.setVisibility(View.GONE);
                                    Toast.makeText(SubCategoryDeleteActivity.this, "Failed to delete sub category image", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }
                            });


                }
                else{
                    subCategoryDeleteProgressBar.setVisibility(View.GONE);
                    Toast.makeText(SubCategoryDeleteActivity.this, "Failed to delete sub category, please try again", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Log.e("Del cat.", "Error occurred, Error is: "+t.getMessage());
                subCategoryDeleteProgressBar.setVisibility(View.GONE);
                Toast.makeText(SubCategoryDeleteActivity.this, "Failed to delete sub category, please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}