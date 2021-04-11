package com.owoSuperAdmin.categoryManagement.brand.addBrand;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import com.owoSuperAdmin.categoryManagement.category.entity.CategoryEntity;
import com.owoSuperAdmin.categoryManagement.subCategory.addSubCategory.CategoryCustomSpinnerAdapter;
import com.owoSuperAdmin.categoryManagement.subCategory.entity.SubCategoryEntity;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddABrand extends AppCompatActivity {

    private EditText brandName;
    private ImageView brandImage;
    private ProgressBar progressBar;
    private Spinner categorySelector, subCategorySelector;

    private final int STORAGE_PERMISSION_CODE = 1;
    private final List<CategoryEntity> categoryEntityList = new ArrayList<>();
    private final List<SubCategoryEntity> subCategoryEntityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_brand);

        brandName = findViewById(R.id.brand_name);
        brandImage = findViewById(R.id.brand_image);
        progressBar = findViewById(R.id.progress);
        categorySelector = findViewById(R.id.category_selector);
        Button addANewBrands = findViewById(R.id.add_new_category);
        subCategorySelector = findViewById(R.id.sub_category_selector);

        ImageView backButton = findViewById(R.id.back_button);

        categoryFetcher();

        progressBar.setVisibility(View.VISIBLE);

        brandImage.setOnClickListener(v -> requestStoragePermission());
        backButton.setOnClickListener(v -> onBackPressed());

        categorySelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!categoryEntityList.isEmpty())
                    fetchSubCategory(categoryEntityList.get(position));
                else
                {
                    Toast.makeText(AddABrand.this, "Can not fetch category, please wait", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addANewBrands.setOnClickListener(view -> {

            SubCategoryEntity subCategoryEntity = (SubCategoryEntity) subCategorySelector.getSelectedItem();
            String name = brandName.getText().toString();

            if(subCategoryEntity == null)
            {
                brandName.setError("Please select a sub category");
                brandName.requestFocus();
            }
            else if(brandImage.getDrawable().getConstantState() == Objects.requireNonNull(
                    ContextCompat.getDrawable(AddABrand.this, R.drawable.category_management)).getConstantState())
            {
                Toast.makeText(AddABrand.this, "Please select a brand image", Toast.LENGTH_SHORT).show();
            }
            else if(name.isEmpty())
            {
                Toast.makeText(this, "Please enter brand name", Toast.LENGTH_SHORT).show();
            }
            else
            {
                uploadImageOfBrand(subCategoryEntity, name);
                progressBar.setVisibility(View.VISIBLE);
            }
        });

    }

    private void fetchSubCategory(CategoryEntity categoryEntity) {

        RetrofitClient.getInstance().getApi()
                .getAllSubCategories(categoryEntity.getCategoryId())
                .enqueue(new Callback<List<SubCategoryEntity>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<SubCategoryEntity>> call, @NotNull Response<List<SubCategoryEntity>> response) {
                        if(response.isSuccessful())
                        {
                            subCategoryEntityList.clear();
                            assert response.body() != null;
                            subCategoryEntityList.addAll(response.body());
                            SubCategoryCustomSpinner subCategoryCustomSpinner = new SubCategoryCustomSpinner(AddABrand.this, subCategoryEntityList);
                            subCategorySelector.setAdapter(subCategoryCustomSpinner);
                        }
                        else
                        {
                            Toast.makeText(AddABrand.this, "Can not get sub category, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<SubCategoryEntity>> call, @NotNull Throwable t) {
                        Log.e("Add sub_category", "Error is: "+t.getMessage());
                        Toast.makeText(AddABrand.this, "Can not get sub categories, please try again", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void categoryFetcher()
    {
        RetrofitClient.getInstance().getApi()
                .getAllCategories()
                .enqueue(new Callback<List<CategoryEntity>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<CategoryEntity>> call, @NotNull Response<List<CategoryEntity>> response) {
                        if(response.isSuccessful())
                        {
                            progressBar.setVisibility(View.GONE);
                            assert response.body() != null;
                            categoryEntityList.addAll(response.body());
                            CategoryCustomSpinnerAdapter categoryCustomSpinnerAdapter = new CategoryCustomSpinnerAdapter(AddABrand.this, categoryEntityList);
                            categorySelector.setAdapter(categoryCustomSpinnerAdapter);
                        }
                        else
                        {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(AddABrand.this, "Can not get categories, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<CategoryEntity>> call, @NotNull Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("Add sub_category", "Error is: "+t.getMessage());
                        Toast.makeText(AddABrand.this, "Can not get categories, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadImageOfBrand(SubCategoryEntity subCategoryEntity, String name)
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Upload Brand Image");
        progressDialog.setMessage("Please wait while we are uploading brand image...");
        progressDialog.setCanceledOnTouchOutside(false);

        if (brandImage.getDrawable().getConstantState() != Objects.requireNonNull(
                ContextCompat.getDrawable(AddABrand.this, R.drawable.category_management)).getConstantState()) {

            Bitmap bitmap = ((BitmapDrawable) brandImage.getDrawable()).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

            String filename = UUID.randomUUID().toString();

            File file = new File(AddABrand.this.getCacheDir() + File.separator + filename + ".jpg");

            try {
                FileOutputStream fo = new FileOutputStream(file);
                fo.write(byteArrayOutputStream.toByteArray());
                fo.flush();
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            MultipartBody.Part multipartFile = MultipartBody.Part.createFormData("multipartFile", file.getName(), requestBody);


            RetrofitClient.getInstance().getApi()
                    .uploadImageToServer("Brands", multipartFile)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                try {
                                    assert response.body() != null;
                                    String path = response.body().string();

                                    Brands brands = new Brands(name, path, subCategoryEntity);

                                    RetrofitClient.getInstance().getApi()
                                            .addABrand(brands)
                                            .enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                                                    if (response.isSuccessful()) {
                                                        Toast.makeText(AddABrand.this, "Brand added successfully", Toast.LENGTH_SHORT).show();
                                                        progressDialog.dismiss();
                                                        finish();
                                                    } else {
                                                        Toast.makeText(AddABrand.this, "Can not add brand", Toast.LENGTH_SHORT).show();
                                                        progressDialog.dismiss();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                                                    Toast.makeText(AddABrand.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                }
                                            });

                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Toast.makeText(AddABrand.this, "Error uploading to server", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(AddABrand.this, "Error...Can not upload image", Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        brandImage.setImageBitmap(selectedImage);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                brandImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }

    private void selectImage(Context context)
    {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo")) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);

            } else if (options[item].equals("Choose from Gallery")) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);
            }
        });

        builder.setCancelable(false);
        builder.show();
    }

    private void requestStoragePermission()
    {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of taking image from gallery")
                    .setPositiveButton("ok", (dialog, which) -> {

                        ActivityCompat.requestPermissions(AddABrand.this,
                                new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

                        selectImage(AddABrand.this);
                    })
                    .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss())
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

            selectImage(AddABrand.this);
        }
    }

}