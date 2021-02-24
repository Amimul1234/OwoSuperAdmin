package com.owoSuperAdmin.categoryManagement.subCategory.updateSubCategory;

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

public class SubCategoryUpdateActivity extends AppCompatActivity {

    private ImageView subCategoryImage;
    private EditText subCategoryName;
    private ProgressBar subCategoryUpdateProgressBar;
    private SubCategoryEntity subCategoryEntity;
    private String previousPath;

    private final int STORAGE_PERMISSION_CODE = 1;
    private Long categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_update);

        ImageView backButton = findViewById(R.id.backButton);
        subCategoryImage = findViewById(R.id.subCategoryImage);
        subCategoryName = findViewById(R.id.subCategoryName);
        Button updateSubCategoryDetails = findViewById(R.id.updateSubCategoryDetails);
        subCategoryUpdateProgressBar = findViewById(R.id.subCategoryUpdateProgressBar);

        subCategoryEntity = (SubCategoryEntity) getIntent().getSerializableExtra("subCategoryEntity");
        categoryId = Long.parseLong(getIntent().getStringExtra("categoryId"));

        previousPath = subCategoryEntity.getSub_category_image();
        previousPath = previousPath.substring(34);

        backButton.setOnClickListener(v -> onBackPressed());

        subCategoryName.setText(subCategoryEntity.getSub_category_name());
        Glide.with(this).load(HostAddress.HOST_ADDRESS.getAddress()+ subCategoryEntity.getSub_category_image()).into(subCategoryImage);

        subCategoryImage.setOnClickListener(v -> requestStoragePermission());

        updateSubCategoryDetails.setOnClickListener(v -> validateChecker());
    }

    private void validateChecker() {
        if(subCategoryName.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Please enter the updated sub-category name", Toast.LENGTH_SHORT).show();
        }
        else
        {
            updateSubCategory();
        }
    }

    private void updateSubCategory() {

        Bitmap bitmap = ((BitmapDrawable) subCategoryImage.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        String filename = UUID.randomUUID().toString();

        File file = new File(SubCategoryUpdateActivity.this.getCacheDir() + File.separator + filename + ".jpg");

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

        subCategoryUpdateProgressBar.setVisibility(View.VISIBLE);

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

                                subCategoryEntity.setSub_category_image(path);
                                subCategoryEntity.setSub_category_name(subCategoryName.getText().toString());

                                RetrofitClient.getInstance().getApi()
                                        .updateSubCategory(categoryId, subCategoryEntity)
                                        .enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                                                if(response.isSuccessful())
                                                {
                                                    RetrofitClient.getInstance()
                                                            .getApi().deleteImage(previousPath)
                                                            .enqueue(new Callback<ResponseBody>() {
                                                                @Override
                                                                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                                                                    if(response.isSuccessful())
                                                                    {
                                                                        subCategoryUpdateProgressBar.setVisibility(View.GONE);
                                                                        Toast.makeText(SubCategoryUpdateActivity.this, "Sub-category updated successfully", Toast.LENGTH_SHORT).show();
                                                                        finish();
                                                                    }
                                                                    else //here is the error occurring
                                                                    {
                                                                        subCategoryUpdateProgressBar.setVisibility(View.GONE);
                                                                        Toast.makeText(SubCategoryUpdateActivity.this, "Failed to update sub-category, please try again", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                                                                    Log.e("Update cat.", "Error occurred, Error is: "+t.getMessage());
                                                                    subCategoryUpdateProgressBar.setVisibility(View.GONE);
                                                                    Toast.makeText(SubCategoryUpdateActivity.this, "Failed to update category, please try again", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }
                                                else //here is the error occurring
                                                {
                                                    subCategoryUpdateProgressBar.setVisibility(View.GONE);
                                                    Toast.makeText(SubCategoryUpdateActivity.this, "Failed to update sub-category, please try again", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                                                Log.e("Update cat.", "Error occurred, Error is: "+t.getMessage());
                                                subCategoryUpdateProgressBar.setVisibility(View.GONE);
                                                Toast.makeText(SubCategoryUpdateActivity.this, "Failed to update category, please try again", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }catch (Exception e)
                            {
                                Log.e("Update cat.", "Error occurred, Error is: "+e.getMessage());
                                subCategoryUpdateProgressBar.setVisibility(View.GONE);
                                Toast.makeText(SubCategoryUpdateActivity.this, "Failed to upload image, please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            subCategoryUpdateProgressBar.setVisibility(View.GONE);
                            Toast.makeText(SubCategoryUpdateActivity.this, "Failed to upload image, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        Log.e("Update cat.", "Error occurred, Error is: "+t.getMessage());
                        subCategoryUpdateProgressBar.setVisibility(View.GONE);
                        Toast.makeText(SubCategoryUpdateActivity.this, "Failed to upload image, please try again", Toast.LENGTH_SHORT).show();
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
                        ActivityCompat.requestPermissions(SubCategoryUpdateActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        selectImage(SubCategoryUpdateActivity.this);
                    })
                    .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss())
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

            selectImage(SubCategoryUpdateActivity.this);
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