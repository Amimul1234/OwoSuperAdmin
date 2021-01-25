package com.owosuperadmin.owoshop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import com.owosuperadmin.Network.RetrofitClient;
import com.owosuperadmin.model.Brands;
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

public class AddABrand extends AppCompatActivity {

    private EditText brand_name;
    private ImageView brand_image;
    private ProgressBar progressBar;
    private Spinner category_selector;

    private final int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_brand);

        brand_name = findViewById(R.id.brand_name);
        brand_image = findViewById(R.id.brand_image);
        Button create_a_new_brand = findViewById(R.id.add_new_category);
        progressBar = findViewById(R.id.progress);

        category_selector = findViewById(R.id.category_selector);

        brand_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestStoragePermission();
            }
        });

        create_a_new_brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String brand_name_creation = brand_name.getText().toString();

                if(brand_name_creation.isEmpty())
                {
                    brand_name.setError("Please give a name to sub category");
                    brand_name.requestFocus();
                }
                else if(brand_image.getDrawable().getConstantState() == Objects.requireNonNull(ContextCompat.getDrawable(AddABrand.this, R.drawable.home11)).getConstantState())
                {
                    Toast.makeText(AddABrand.this, "Image can not be empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    uploadImageOfBrand();
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });


    }


    private void uploadImageOfBrand() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Upload Brand Image");
        progressDialog.setMessage("Please wait while we are uploading brand category image...");
        progressDialog.setCanceledOnTouchOutside(false);

        if (brand_image.getDrawable().getConstantState() != Objects.requireNonNull(ContextCompat.getDrawable(AddABrand.this, R.drawable.home11)).getConstantState()) {

            Bitmap bitmap = ((BitmapDrawable) brand_image.getDrawable()).getBitmap();
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
                                    String path = response.body().string();

                                    Brands brands = new Brands(brand_name.getText().toString(), path, category_selector.getSelectedItem().toString());


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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        brand_image.setImageBitmap(selectedImage);
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
                                brand_image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }


    private void selectImage(Context context) {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);
                }
            }
        });

        builder.setCancelable(false);
        builder.show();
    }


    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of taking image from gallery")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ActivityCompat.requestPermissions(AddABrand.this,
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

                            selectImage(AddABrand.this);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

            selectImage(AddABrand.this);
        }
    }

}