package com.owosuperadmin.owoshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.owosuperadmin.Network.RetrofitClient;
import com.owosuperadmin.model.Brands;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddABrand extends AppCompatActivity {
    private EditText brand_name;
    private ImageView brand_image;
    private Button create_a_new_brand;
    private ProgressBar progressBar;
    private Uri imageuri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageSubCategoryReference;
    private Spinner category_selector;

    private int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_brand);

        brand_name = findViewById(R.id.brand_name);
        brand_image = findViewById(R.id.brand_image);
        create_a_new_brand = findViewById(R.id.add_new_category);
        progressBar = findViewById(R.id.progress);

        category_selector = findViewById(R.id.category_selector);

        storageSubCategoryReference = FirebaseStorage.getInstance().getReference().child("Brands");

        brand_image.setOnClickListener(new View.OnClickListener() {//For selecting the profile image
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
                else if(myUrl == null)
                {
                    Toast.makeText(AddABrand.this, "Image can not be empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    uploadImageofAdmin(brand_name_creation);
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });


    }


    private void uploadImageofAdmin(String subcategory_name) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Upload Brand Image");
        progressDialog.setMessage("Please wait while we are uploading brand category image...");
        progressDialog.setCanceledOnTouchOutside(false);

        if(imageuri!=null)
        {
            final StorageReference fileRef = storageSubCategoryReference.child(subcategory_name+".jpg");

            uploadTask = fileRef.putFile(imageuri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();

                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();

                        Brands brands = new Brands(brand_name.getText().toString(), myUrl, category_selector.getSelectedItem().toString());

                        Call<ResponseBody> call = RetrofitClient
                                .getInstance()
                                .getApi()
                                .addABrand(brands);

                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(response.isSuccessful())
                                {
                                    Toast.makeText(AddABrand.this, "Brand added successfully", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(AddABrand.this, "Can not add brand", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(AddABrand.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });

                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(AddABrand.this, "Error...Can not upload image", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        else
        {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && data != null)
        {
            imageuri = data.getData();
            brand_image.setImageURI(imageuri);
        }
        else
        {
            Toast.makeText(this, "Try again", Toast.LENGTH_SHORT).show();
        }
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
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ImagePicker.Companion.with(AddABrand.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(512)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(540, 540)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

}