package com.owosuperadmin.owoshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.owosuperadmin.Network.RetrofitClient;
import com.owosuperadmin.model.Brands;
import com.owosuperadmin.model.Owo_product;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

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
                CropImage.activity(imageuri)
                        .setAspectRatio(1, 1)
                        .start(AddABrand.this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageuri = result.getUri();
            brand_image.setImageURI(imageuri);
        } else {
            Toast.makeText(this, "Error, Try again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddABrand.this, AddASubCategory.class));
            finish();
        }
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

}