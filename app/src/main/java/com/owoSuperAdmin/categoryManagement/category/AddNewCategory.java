package com.owoSuperAdmin.categoryManagement.category;

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
import android.widget.Toast;
import com.owoSuperAdmin.categoryManagement.category.entity.CategoryEntity;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewCategory extends AppCompatActivity {
    private ImageView categoryImage;
    private EditText categoryName;
    private ProgressBar categoryAddProgressbar;

    private final String TAG = "Add New Category Class";
    private final int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_category);

        ImageView back_button = findViewById(R.id.back_button);
        categoryImage = findViewById(R.id.category_image);
        categoryName = findViewById(R.id.category_name);
        categoryAddProgressbar = findViewById(R.id.category_add_progressbar);

        Button addNewCategory = findViewById(R.id.addNewCategory);

        categoryImage.setOnClickListener(v -> requestStoragePermission());
        addNewCategory.setOnClickListener(v-> validateData());
        back_button.setOnClickListener(v->onBackPressed());
    }

    private void validateData() {
        if(categoryImage.getDrawable().getConstantState() == Objects.requireNonNull(ContextCompat.getDrawable(
                AddNewCategory.this, R.drawable.category_management)).getConstantState())
        {
            Toast.makeText(this, "Please select image for category", Toast.LENGTH_SHORT).show();
        }
        else if(categoryName.getText().toString().isEmpty())
        {
            categoryName.setError("Category name can not be empty");
        }
        else
        {
            uploadImageToServer();
        }
    }

    private void uploadImageToServer() {
        categoryAddProgressbar.setVisibility(View.VISIBLE);

        Bitmap bitmap = ((BitmapDrawable) categoryImage.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        String filename = UUID.randomUUID().toString();

        File file = new File(AddNewCategory.this.getCacheDir() + File.separator + filename + ".jpg");

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
                .uploadImageToServer("Category", multipartFile)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {
                            try {
                                assert response.body() != null;
                                String path = response.body().string();

                                CategoryEntity categoryEntity = new CategoryEntity();
                                categoryEntity.setCategoryImage(path);
                                categoryEntity.setCategoryName(categoryName.getText().toString());

                                RetrofitClient.getInstance().getApi()
                                        .addNewCategory(categoryEntity)
                                        .enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                                                if(response.isSuccessful())
                                                {
                                                    categoryAddProgressbar.setVisibility(View.INVISIBLE);
                                                    Toast.makeText(AddNewCategory.this, "Category added successfully", Toast.LENGTH_SHORT).show();
                                                    onBackPressed();
                                                }
                                                else
                                                {
                                                    categoryAddProgressbar.setVisibility(View.INVISIBLE);
                                                    Toast.makeText(AddNewCategory.this, "Can not add new category, please try again", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                                                categoryAddProgressbar.setVisibility(View.INVISIBLE);
                                                Log.e(TAG, "Error is: "+t.getMessage());
                                                Toast.makeText(AddNewCategory.this, "Can not add new category, please try again", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            categoryAddProgressbar.setVisibility(View.INVISIBLE);
                            Toast.makeText(AddNewCategory.this, "Can not upload image, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        categoryAddProgressbar.setVisibility(View.INVISIBLE);
                        Log.e(TAG, "Error is: "+t.getMessage());
                        Toast.makeText(AddNewCategory.this, "Can not upload image, please try again", Toast.LENGTH_SHORT).show();
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

                        ActivityCompat.requestPermissions(AddNewCategory.this,
                                new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

                        selectImage(AddNewCategory.this);
                    })
                    .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss())
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

            selectImage(AddNewCategory.this);
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