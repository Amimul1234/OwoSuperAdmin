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
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class AddABrand extends AppCompatActivity {

    private EditText brand_name;
    private ImageView brand_image;
    private Button create_a_new_brand;
    private ProgressBar progressBar;

    private Uri imageuri;
    private String myUrl = "";

    private int brand_count = 0;

    private StorageTask uploadTask;
    private StorageReference storageSubCategoryReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_brand);

        brand_name = findViewById(R.id.brand_name);
        brand_image = findViewById(R.id.brand_image);
        create_a_new_brand = findViewById(R.id.add_new_category);
        progressBar = findViewById(R.id.progress);

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

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                        databaseReference.child("Brand Count").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists())
                                {
                                    brand_count = Integer.parseInt(snapshot.getValue(String.class));

                                    HashMap<String, String> brands = new HashMap<>();

                                    brands.put("Name", subcategory_name);
                                    brands.put("Image", myUrl);

                                    brand_count++;

                                    databaseReference.child("Brands").child(String.valueOf(brand_count)).setValue(brands)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    databaseReference.child("Brand Count").setValue(String.valueOf(brand_count))
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    progressDialog.dismiss();
                                                                    Toast.makeText(AddABrand.this, "Brand Added Successfully", Toast.LENGTH_SHORT).show();
                                                                    finish();
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(AddABrand.this, "Can not add brand", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }
                                                    });
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(AddABrand.this, "Can not add brand", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                                else{
                                    brand_count = 0;

                                    HashMap<String, String> brands = new HashMap<>();

                                    brands.put("Name", subcategory_name);
                                    brands.put("Image", myUrl);

                                    databaseReference.child("Brands").child(String.valueOf(brand_count)).setValue(brands)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    databaseReference.child("Brand Count").setValue(String.valueOf(brand_count))
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    progressDialog.dismiss();
                                                                    Toast.makeText(AddABrand.this, "Brand Added Successfully", Toast.LENGTH_SHORT).show();
                                                                    finish();
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(AddABrand.this, "Can not add brand", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }
                                                    });
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(AddABrand.this, "Can not add brand", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(AddABrand.this, "Failed to add brand", Toast.LENGTH_SHORT).show();
                                finish();
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