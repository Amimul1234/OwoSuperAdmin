package com.owoSuperAdmin.categoryManagement.category;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.owoSuperAdmin.categoryManagement.category.entity.CategoryEntity;
import com.owoSuperAdmin.configurationsFile.HostAddress;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateCategoryActivity extends AppCompatActivity {

    private ImageView categoryImage;
    private TextView categoryName;
    private ProgressBar categoryUpdateProgressBar;
    private final int STORAGE_PERMISSION_CODE = 1;
    private CategoryEntity categoryEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_category);

        ImageView backButton = findViewById(R.id.backButton);
        categoryImage = findViewById(R.id.categoryImage);
        categoryName = findViewById(R.id.categoryName);
        Button updateCategoryDetails = findViewById(R.id.updateCategoryDetails);
        categoryUpdateProgressBar = findViewById(R.id.categoryUpdateProgressbar);

        categoryEntity = (CategoryEntity) getIntent().getSerializableExtra("categoryEntity");

        backButton.setOnClickListener(v -> onBackPressed());

        categoryName.setText(categoryEntity.getCategoryName());

        Glide.with(this).load(HostAddress.HOST_ADDRESS.getAddress()+categoryEntity.getCategoryImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(categoryImage);

        categoryImage.setOnClickListener(v -> requestStoragePermission());

        updateCategoryDetails.setOnClickListener(v -> validateChecker());
    }

    private void validateChecker() {
        if(categoryName.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Please enter the updated category name", Toast.LENGTH_SHORT).show();
        }
        else
        {
            updateCategory();
        }
    }

    private void updateCategory() {

        Bitmap bitmap = ((BitmapDrawable) categoryImage.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        String filename = UUID.randomUUID().toString();

        File file = new File(UpdateCategoryActivity.this.getCacheDir() + File.separator + filename + ".jpg");

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

        categoryUpdateProgressBar.setVisibility(View.VISIBLE);

        RetrofitClient.getInstance().getApi()
                .uploadImageToServer("Category", multipartFile)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {
                            try {
                                assert response.body() != null;
                                String path = response.body().string();

                                String previousImagePath = categoryEntity.getCategoryImage();

                                String cleanedAddress = previousImagePath.substring(34);

                                categoryEntity.setCategoryImage(path);
                                categoryEntity.setCategoryName(categoryName.getText().toString());

                                RetrofitClient.getInstance().getApi().updateCategory(categoryEntity.getCategoryId(), categoryEntity)
                                        .enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                                                if(response.isSuccessful())
                                                {
                                                    RetrofitClient.getInstance()
                                                            .getApi().deleteImage(cleanedAddress)
                                                            .enqueue(new Callback<ResponseBody>() {
                                                                @Override
                                                                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                                                                    if(response.isSuccessful())
                                                                    {
                                                                        categoryUpdateProgressBar.setVisibility(View.GONE);
                                                                        Toast.makeText(UpdateCategoryActivity.this, "Category updated successfully", Toast.LENGTH_SHORT).show();
                                                                        onBackPressed();
                                                                    }
                                                                    else //here is the error occurring
                                                                    {
                                                                        categoryUpdateProgressBar.setVisibility(View.GONE);
                                                                        Toast.makeText(UpdateCategoryActivity.this, "Failed to update category, please try again", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                                                                    Log.e("Update cat.", "Error occurred, Error is: "+t.getMessage());
                                                                    categoryUpdateProgressBar.setVisibility(View.GONE);
                                                                    Toast.makeText(UpdateCategoryActivity.this, "Failed to update category, please try again", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }
                                                else
                                                {
                                                    categoryUpdateProgressBar.setVisibility(View.GONE);
                                                    Toast.makeText(UpdateCategoryActivity.this, "Failed to update category, please try again", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                                                Log.e("Update cat.", "Error occurred, Error is: "+t.getMessage());
                                                categoryUpdateProgressBar.setVisibility(View.GONE);
                                                Toast.makeText(UpdateCategoryActivity.this, "Failed to update category, please try again", Toast.LENGTH_SHORT).show();
                                            }
                                        });



                            }catch (Exception e)
                            {
                                Log.e("Update cat.", "Error occurred, Error is: "+e.getMessage());
                                categoryUpdateProgressBar.setVisibility(View.GONE);
                                Toast.makeText(UpdateCategoryActivity.this, "Failed to upload image, please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            categoryUpdateProgressBar.setVisibility(View.GONE);
                            Toast.makeText(UpdateCategoryActivity.this, "Failed to upload image, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        Log.e("Update cat.", "Error occurred, Error is: "+t.getMessage());
                        categoryUpdateProgressBar.setVisibility(View.GONE);
                        Toast.makeText(UpdateCategoryActivity.this, "Failed to upload image, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void requestStoragePermission()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of taking image from gallery")
                    .setPositiveButton("ok", (dialog, which) -> {
                        ActivityCompat.requestPermissions(UpdateCategoryActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        selectImage(UpdateCategoryActivity.this);
                    })
                    .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss())
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

            selectImage(UpdateCategoryActivity.this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        categoryImage.setImageBitmap(selectedImage);
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
                                categoryImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }

}