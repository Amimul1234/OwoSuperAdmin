package com.owoSuperAdmin.categoryManagement.subCategory.addSubCategory;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import com.owoSuperAdmin.categoryManagement.category.entity.CategoryEntity;
import com.owoSuperAdmin.categoryManagement.subCategory.entity.SubCategoryEntity;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

public class AddASubCategory extends AppCompatActivity {

    private ImageView subCategoryImage;
    private Spinner subCategorySpinner;
    private EditText subCategoryName;
    private ProgressBar progressBar;

    private final int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_sub_category);

        ImageView backButton = findViewById(R.id.backButton);

        subCategorySpinner = findViewById(R.id.categorySpinner);
        subCategoryName = findViewById(R.id.subCategoryName);
        subCategoryImage = findViewById(R.id.subCategoryImage);
        progressBar = findViewById(R.id.progress);
        Button addNewSubCategory = findViewById(R.id.addNewSubCategory);

        populateSpinner();

        backButton.setOnClickListener(v -> onBackPressed());
        subCategoryImage.setOnClickListener(v -> requestStoragePermission());
        addNewSubCategory.setOnClickListener(v -> checkInputValidation());
    }

    private void checkInputValidation() {

        if(subCategoryImage.getDrawable().getConstantState() == Objects.requireNonNull(ContextCompat.getDrawable(
                AddASubCategory.this, R.drawable.category_management)).getConstantState())
        {
            Toast.makeText(this, "Please choose an image for the sub category", Toast.LENGTH_SHORT).show();
        }
        else if(subCategoryName.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Please enter the name of the sub category", Toast.LENGTH_SHORT).show();
        }
        else if(subCategorySpinner.getSelectedItem().toString().isEmpty())
        {
            Toast.makeText(this, "Please select category first", Toast.LENGTH_SHORT).show();
        }
        else
        {
            saveSubCategoryToDatabase();
        }
    }

    private void saveSubCategoryToDatabase() {
        progressBar.setVisibility(View.VISIBLE);

        Bitmap bitmap = ((BitmapDrawable) subCategoryImage.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        String filename = UUID.randomUUID().toString();

        File file = new File(AddASubCategory.this.getCacheDir() + File.separator + filename + ".jpg");

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
                .uploadImageToServer("SubCategory", multipartFile)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {
                            try {
                                assert response.body() != null;
                                String path = response.body().string();

                                Long categoryId = subCategorySpinner.getSelectedItemId();

                                SubCategoryEntity subCategoryEntity = new SubCategoryEntity();

                                subCategoryEntity.setSub_category_image(path);
                                subCategoryEntity.setSub_category_name(subCategoryName.getText().toString());

                                RetrofitClient.getInstance().getApi()
                                        .addNewSubCategory(categoryId, subCategoryEntity)
                                        .enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                                                if(response.isSuccessful())
                                                {
                                                    progressBar.setVisibility(View.GONE);
                                                    Toast.makeText(AddASubCategory.this, "Sub category added successfully", Toast.LENGTH_SHORT).show();
                                                    onBackPressed();
                                                }
                                                else
                                                {
                                                    progressBar.setVisibility(View.GONE);
                                                    Toast.makeText(AddASubCategory.this, "Failed to upload sub category, please try again", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                                                progressBar.setVisibility(View.GONE);
                                                Log.e("Add Sub cat.", "Error occurred, Error is: "+t.getMessage());
                                                Toast.makeText(AddASubCategory.this, "Failed to upload sub category, please try again", Toast.LENGTH_SHORT).show();
                                                t.printStackTrace();
                                            }
                                        });


                            } catch (IOException e) {
                                progressBar.setVisibility(View.GONE);
                                Log.e("Add Sub cat.", "Error occurred, Error is: "+e.getMessage());
                                Toast.makeText(AddASubCategory.this, "Failed to upload image to server, please try again", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(AddASubCategory.this, "Failed to upload image to server, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("Add Sub cat.", "Error occurred, Error is: "+t.getMessage());
                        Toast.makeText(AddASubCategory.this, "Failed to upload image to server, please try again", Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });

    }

    private void populateSpinner()
    {

        RetrofitClient.getInstance().getApi()
                .getAllCategories()
                .enqueue(new Callback<List<CategoryEntity>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<CategoryEntity>> call, @NotNull Response<List<CategoryEntity>> response) {
                        if(response.isSuccessful())
                        {
                            CategoryCustomSpinnerAdapter categoryCustomSpinnerAdapter = new CategoryCustomSpinnerAdapter(AddASubCategory.this,
                                    response.body());

                            subCategorySpinner.setAdapter(categoryCustomSpinnerAdapter);

                        }
                        else
                        {
                            Toast.makeText(AddASubCategory.this, "Can not get categories, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<CategoryEntity>> call, @NotNull Throwable t) {
                        Log.e("Add sub_category", "Error is: "+t.getMessage());
                        Toast.makeText(AddASubCategory.this, "Can not get categories, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of taking image from gallery")
                    .setPositiveButton("ok", (dialog, which) -> {

                        ActivityCompat.requestPermissions(AddASubCategory.this,
                                new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

                        selectImage(AddASubCategory.this);
                    })
                    .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss())
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

            selectImage(AddASubCategory.this);
        }
    }

    private void selectImage(Context context) {

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        subCategoryImage.setImageBitmap(selectedImage);
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
                                subCategoryImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }
}