package com.owoSuperAdmin.productsManagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.storage.UploadTask;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.productsManagement.entity.Owo_product;
import com.owoSuperAdmin.categoryManagement.subCategory.entity.Sub_categories;
import com.owoSuperAdmin.owoshop.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAProduct extends AppCompatActivity {

    private String CategoryName, Description, Price, Pname, Discount,
            saveCurrentDate, saveCurrentTime, productRandomKey, downloadImageUrl, quantity;

    private Button AddNewProductButton, preview_new_product, calculate_discount;
    private ImageView InputProductImage;
    private EditText InputProductName, InputProductDescription, InputProductPrice, InputProductDiscount, product_quantity;
    private TextView discounted_price;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private ProgressDialog loadingbar;

    private List<String> sub_category_names = new ArrayList<>();
    private List<String> brand_names = new ArrayList<>();

    private Sub_categories sub_categories;

    private Spinner sub_category_selection, brand_selector;

    private String selected_sub_category;
    private String selected_brand;

    int i = 1;

    ArrayAdapter<String> adapter;
    ArrayAdapter<String> brand_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product_add);


        CategoryName = getIntent().getExtras().get("category").toString();
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("ProductImage");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        collectInfo();


        AddNewProductButton = (Button)findViewById(R.id.add_new_product);
        InputProductImage = (ImageView) findViewById(R.id.select_product_image);
        InputProductName = (EditText)findViewById(R.id.product_name);
        InputProductDescription = (EditText)findViewById(R.id.product_description);
        InputProductDiscount = (EditText)findViewById(R.id.product_discount);
        InputProductPrice = (EditText)findViewById(R.id.product_price);
        preview_new_product = findViewById(R.id.preview_new_product);
        discounted_price = findViewById(R.id.discounted_price);
        calculate_discount = findViewById(R.id.calculate_discount);
        product_quantity = findViewById(R.id.product_quantity);

        loadingbar = new ProgressDialog(this);
        sub_category_selection = findViewById(R.id.sub_category_selector);
        brand_selector = findViewById(R.id.brand_selector);

        preview_new_product.setOnClickListener(new View.OnClickListener() {// Have to give product preview
            @Override
            public void onClick(View v) {

                Description = InputProductDescription.getText().toString();
                Price = InputProductPrice.getText().toString();
                Pname = InputProductName.getText().toString();
                Discount = InputProductDiscount.getText().toString();
                quantity = product_quantity.getText().toString();

                if(ImageUri == null)
                {
                    Toast.makeText(AddAProduct.this, "Product image is mandatory...", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(Description))
                {
                    Toast.makeText(AddAProduct.this, "Please write product description...", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(Price))
                {
                    Toast.makeText(AddAProduct.this, "Please write product price...", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(Pname))
                {
                    Toast.makeText(AddAProduct.this, "Please write product name...", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(Discount))
                {
                    Toast.makeText(AddAProduct.this, "Please write product discount...", Toast.LENGTH_SHORT).show();
                }

                else if(TextUtils.isEmpty(quantity))
                {
                    Toast.makeText(AddAProduct.this, "Product quantity can not be empty", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    Intent intent = new Intent(AddAProduct.this, PreViewProductDetails.class);
                    intent.putExtra("image", ImageUri.toString());
                    intent.putExtra("title", InputProductName.getText().toString());
                    intent.putExtra("price", InputProductPrice.getText().toString());
                    intent.putExtra("discount", InputProductDiscount.getText().toString());
                    intent.putExtra("description", InputProductDescription.getText().toString());
                    intent.putExtra("quantity", product_quantity.getText().toString());
                    intent.putExtra("brand", brand_selector.getSelectedItem().toString());
                    startActivity(intent);
                }
            }
        });

        calculate_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String price = InputProductPrice.getText().toString();
                String discount = InputProductDiscount.getText().toString();

                if(price.isEmpty())
                {
                    InputProductPrice.setError("Product Price can not be empty");
                    InputProductPrice.requestFocus();
                    InputProductDiscount.setError(null);
                }
                else if(discount.isEmpty())
                {
                    InputProductDiscount.setError("Product Discount Can Not Be Empty");
                    InputProductDiscount.requestFocus();
                    InputProductPrice.setError(null);
                }

                else
                {
                    InputProductPrice.setError(null);
                    InputProductDiscount.setError(null);

                    double product_price = Double.parseDouble(price);
                    double product_discount = Double.parseDouble(discount);

                    if (product_price < 0)
                    {
                        InputProductPrice.setError("Product price can not be negative");
                        InputProductPrice.requestFocus();
                        return;
                    }

                    else if(product_discount < 0)
                    {
                        InputProductDiscount.setError("Product discount can not be negative");
                        InputProductDiscount.requestFocus();
                        return;
                    }

                    else if(product_discount > product_price)
                    {
                        InputProductDiscount.setError("Product discount can not be greater than price");
                        InputProductDiscount.requestFocus();
                        return;
                    }

                    double discount_price = product_price - product_discount;
                    double discount_percentage = (product_discount/product_price) * 100.00;

                    discounted_price.setText("৳ "+ String.valueOf(discount_price) +
                            "( "+ String.format("%.2f", discount_percentage)+ "% )");
                }
            }
        });

        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        AddNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });

    }

    private void collectInfo() {
        DatabaseReference sub_cat_ref = FirebaseDatabase.getInstance().getReference();
        sub_cat_ref.child("Categories").child(CategoryName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    sub_categories = snapshot.getValue(Sub_categories.class);
                    int size = sub_categories.getSub_categories().size();
                    for (int i = 0; i < size; i++) {
                        sub_category_names.add(sub_categories.getSub_categories().get(i).get("Name"));
                    }

                    adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_item, sub_category_names);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sub_category_selection.setAdapter(adapter);
                }
                else
                {
                    Toast.makeText(AddAProduct.this, "No sub category exists", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddAProduct.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        Call<List<String>> call = RetrofitClient.getInstance().getApi().getBrands(CategoryName);

        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful())
                {
                    assert response.body() != null;
                    brand_names.addAll(response.body());

                    brand_adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_item, brand_names);
                    brand_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    brand_selector.setAdapter(brand_adapter);
                }
                else
                {
                    Toast.makeText(AddAProduct.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(AddAProduct.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void OpenGallery() {
        Intent galleryIntent=new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null)
        {
            ImageUri=data.getData();
            InputProductImage.setImageURI(ImageUri);
        }
    }

    private void ValidateProductData() {

        Description = InputProductDescription.getText().toString();
        Price = InputProductPrice.getText().toString();
        Pname = InputProductName.getText().toString();
        Discount = InputProductDiscount.getText().toString();
        quantity = product_quantity.getText().toString();
        selected_sub_category = sub_category_selection.getSelectedItem().toString();
        selected_brand = brand_selector.getSelectedItem().toString();

        if(ImageUri==null)
        {
            Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Please write product description...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Price))
        {
            Toast.makeText(this, "Please write product price...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Pname))
        {
            Toast.makeText(this, "Please write product name...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Discount))
        {
            Toast.makeText(this, "Please write product discount...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(quantity))
        {
            Toast.makeText(AddAProduct.this, "Product quantity can not be empty", Toast.LENGTH_SHORT).show();
        }
        else {
            StoreProductInformation();
        }
    }

    private void StoreProductInformation() {
        loadingbar.setTitle("Add New Product");
        loadingbar.setMessage("Please wait, we are adding the new product.");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();

        Calendar calendar= Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate+saveCurrentTime;

        final StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey +".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);
        
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String message=e.toString();
                Toast.makeText(AddAProduct.this, "Error : "+message, Toast.LENGTH_SHORT).show();
                loadingbar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddAProduct.this, "Product Image uploaded successfully", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful())
                        {
                            throw  task.getException();
                        }
                        downloadImageUrl=filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            downloadImageUrl=task.getResult().toString();
                            Toast.makeText(AddAProduct.this, "Got the Product image url Successfully", Toast.LENGTH_SHORT).show();
                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });

    }

    private void SaveProductInfoToDatabase() {

        Owo_product owo_product = new Owo_product(Pname, CategoryName, Double.parseDouble(Price), Double.parseDouble(Discount), Integer.parseInt(quantity), Description,
                saveCurrentDate, saveCurrentTime, selected_sub_category, selected_brand, downloadImageUrl);

        Call<Owo_product> call = RetrofitClient
                .getInstance()
                .getApi()
                .createProduct(owo_product);

        call.enqueue(new Callback<Owo_product>() {
            @Override
            public void onResponse(Call<Owo_product> call, Response<Owo_product> response) {
                if(response.isSuccessful())
                {
                    Toast.makeText(AddAProduct.this, "Product Added Successfully", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Owo_product> call, Throwable t) {
                Toast.makeText(AddAProduct.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
