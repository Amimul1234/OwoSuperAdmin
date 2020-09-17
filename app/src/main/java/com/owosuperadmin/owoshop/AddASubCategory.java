package com.owosuperadmin.owoshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.owosuperadmin.model.Sub_categories;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class AddASubCategory extends AppCompatActivity {

    private EditText sub_category_name;
    private ImageView sub_category_image;
    private Button create_sub_category;
    private Spinner category_name;
    private ProgressBar progressBar;

    private Uri imageuri;
    private String myUrl = "";

    private Sub_categories sub_categories = new Sub_categories();

    private StorageTask uploadTask;
    private StorageReference storageSubCategoryReference;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_sub_category);

        sub_category_name = findViewById(R.id.sub_category_name);
        sub_category_image = findViewById(R.id.subcategory_image);
        create_sub_category = findViewById(R.id.add_new_category);
        category_name = findViewById(R.id.category_spinner);
        progressBar = findViewById(R.id.progress);

        storageSubCategoryReference = FirebaseStorage.getInstance().getReference().child("Sub Category");

        sub_category_image.setOnClickListener(new View.OnClickListener() {//For selecting the profile image
            @Override
            public void onClick(View v) {
                CropImage.activity(imageuri)
                        .setAspectRatio(1, 1)
                        .start(AddASubCategory.this);
            }
        });

        create_sub_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sub_category_name_creation = sub_category_name.getText().toString();

                if(sub_category_name_creation.isEmpty())
                {
                    sub_category_name.setError("Please give a name to sub category");
                    sub_category_name.requestFocus();
                }
                else if(myUrl == null)
                {
                    Toast.makeText(AddASubCategory.this, "Image can not be null", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    uploadImageofAdmin(sub_category_name_creation);
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
            sub_category_image.setImageURI(imageuri);
        } else {
            Toast.makeText(this, "Error, Try again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddASubCategory.this, AddASubCategory.class));
            finish();
        }
    }


    private void uploadImageofAdmin(String subcategory_name) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Upload Sub Category Image");
        progressDialog.setMessage("Please wait while we are uploading sub category image...");
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

                        database.child("Categories").child(category_name.getSelectedItem().toString())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists())
                                        {
                                            sub_categories =  snapshot.getValue(Sub_categories.class);

                                            HashMap<String, String> sub_category_under_category = new HashMap<>();

                                            sub_category_under_category.put("Name", subcategory_name);
                                            sub_category_under_category.put("Image", myUrl);

                                            sub_categories.add(sub_category_under_category);


                                            database.child("Categories").child(category_name.getSelectedItem().toString())
                                                    .setValue(sub_categories).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(AddASubCategory.this, "Subcategory added", Toast.LENGTH_SHORT).show();
                                                    progressBar.setVisibility(View.GONE);
                                                    finish();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(AddASubCategory.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    progressBar.setVisibility(View.GONE);
                                                }
                                            });
                                        }

                                        else
                                        {
                                            HashMap<String, String> sub_category_under_category = new HashMap<>();

                                            sub_category_under_category.put("Name", subcategory_name);
                                            sub_category_under_category.put("Image", myUrl);

                                            Sub_categories sub_categories2 = new Sub_categories();
                                            sub_categories2.add(sub_category_under_category);

                                            database.child("Categories").child(category_name.getSelectedItem().toString())
                                                    .setValue(sub_categories2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(AddASubCategory.this, "Subcategory added", Toast.LENGTH_SHORT).show();
                                                    progressBar.setVisibility(View.GONE);
                                                    finish();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(AddASubCategory.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    progressBar.setVisibility(View.GONE);
                                                }
                                            });
                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }

                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(AddASubCategory.this, "Error...Can not upload image", Toast.LENGTH_SHORT).show();
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