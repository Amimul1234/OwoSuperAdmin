package com.owoSuperAdmin.categoryManagement.subCategory;

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
import android.util.Log;
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
import com.owoSuperAdmin.categoryManagement.category.entity.CategoryEntity;
import com.owoSuperAdmin.categoryManagement.subCategory.entity.Sub_categories;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddASubCategory extends AppCompatActivity {

    private EditText sub_category_name;
    private ImageView sub_category_image;
    private Spinner category_name;
    private ProgressBar progressBar;
    private Uri imageuri;
    private String myUrl = "";
    private Sub_categories sub_categories = new Sub_categories();
    private StorageTask uploadTask;
    private StorageReference storageSubCategoryReference;

    private int STORAGE_PERMISSION_CODE = 1;

    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_sub_category);

        sub_category_name = findViewById(R.id.sub_category_name);
        sub_category_image = findViewById(R.id.subcategory_image);
        Button create_sub_category = findViewById(R.id.add_new_category);
        category_name = findViewById(R.id.category_spinner);
        progressBar = findViewById(R.id.progress);

        populateSpinner();

        ImageView backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> onBackPressed());

        storageSubCategoryReference = FirebaseStorage.getInstance().getReference().child("Sub Category");

        //For selecting the profile image
        sub_category_image.setOnClickListener(v -> requestStoragePermission());

        create_sub_category.setOnClickListener(view -> {
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
        });


    }

    private void populateSpinner() {
        RetrofitClient.getInstance().getApi()
                .getAllCategories()
                .enqueue(new Callback<List<CategoryEntity>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<CategoryEntity>> call, @NotNull Response<List<CategoryEntity>> response) {
                        if(response.isSuccessful())
                        {
                            CategoryCustomSpinnerAdapter categoryCustomSpinnerAdapter = new CategoryCustomSpinnerAdapter(AddASubCategory.this,
                                    response.body());

                            category_name.setAdapter(categoryCustomSpinnerAdapter);
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


    private void uploadImageofAdmin(String subcategory_name) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Upload Sub Category Image");
        progressDialog.setMessage("Please wait while we are uploading sub category image...");
        progressDialog.setCanceledOnTouchOutside(false);

        if(imageuri!=null)
        {
            final StorageReference fileRef = storageSubCategoryReference.child(subcategory_name+".jpg");

            uploadTask = fileRef.putFile(imageuri);

            uploadTask.continueWithTask((Continuation) task -> {

                if(!task.isSuccessful())
                {
                    throw task.getException();
                }

                return fileRef.getDownloadUrl();

            }).addOnCompleteListener((OnCompleteListener<Uri>) task -> {
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
            sub_category_image.setImageURI(imageuri);
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
                            ActivityCompat.requestPermissions(AddASubCategory.this,
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
                ImagePicker.Companion.with(AddASubCategory.this)
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