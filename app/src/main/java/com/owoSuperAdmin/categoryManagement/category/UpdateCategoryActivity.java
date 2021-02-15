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
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.owoSuperAdmin.categoryManagement.category.entity.CategoryEntity;
import com.owoSuperAdmin.configurationsFile.HostAddress;
import com.owoSuperAdmin.owoshop.R;

public class UpdateCategoryActivity extends AppCompatActivity {

    private ImageView categoryImage;
    private TextView categoryName;
    private Button updateCategoryDetails;
    private ProgressBar categoryUpdateProgressBar;
    private final int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_category);

        ImageView backButton = findViewById(R.id.backButton);
        categoryImage = findViewById(R.id.categoryImage);
        categoryName = findViewById(R.id.categoryName);
        updateCategoryDetails = findViewById(R.id.updateCategoryDetails);
        categoryUpdateProgressBar = findViewById(R.id.categoryUpdateProgressBar);

        CategoryEntity categoryEntity = (CategoryEntity) getIntent().getSerializableExtra("categoryEntity");

        backButton.setOnClickListener(v -> onBackPressed());

        categoryName.setText(categoryEntity.getCategoryName());
        Glide.with(this).load(HostAddress.HOST_ADDRESS.getAddress()+categoryEntity.getCategoryImage()).into(categoryImage);

        categoryImage.setOnClickListener(v -> requestStoragePermission());
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