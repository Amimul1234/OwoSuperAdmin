package com.owoSuperAdmin.productsManagement.addProduct;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.owoSuperAdmin.adminHomePanel.HomeActivity;
import com.owoSuperAdmin.categoryManagement.brand.addBrand.Brands;
import com.owoSuperAdmin.categoryManagement.category.entity.CategoryEntity;
import com.owoSuperAdmin.categoryManagement.subCategory.entity.SubCategoryEntity;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.owoshop.R;
import com.owoSuperAdmin.productsManagement.entity.OwoProduct;
import org.jetbrains.annotations.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class AddAProduct extends AppCompatActivity {

    private final String TAG = "Add Product activity";
    private final int STORAGE_PERMISSION_CODE = 1;

    private ImageView productImage;
    private EditText productName, productDescription, productQuantity, productPrice, productDiscount;
    private TextView discountedPrice;
    private Spinner subCategorySelectorSpinner, brandSelectorSpinner;
    private CategoryEntity categoryEntity;
    private ProgressBar progressBar;

    private AddProductBrandSpinnerAdapter addProductBrandSpinnerAdapter;
    private AddProductSubCategorySpinnerAdapter addProductSubCategorySpinnerAdapter;

    private final List<SubCategoryEntity> subCategoryEntityList = new ArrayList<>();
    private final List<Brands> brandsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product_add);


        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productDescription = findViewById(R.id.productDescription);
        productQuantity = findViewById(R.id.productQuantity);
        productPrice = findViewById(R.id.productPrice);
        productDiscount = findViewById(R.id.productDiscount);
        Button calculateDiscount = findViewById(R.id.calculateDiscount);
        Button addNewProduct = findViewById(R.id.addNewProduct);
        discountedPrice = findViewById(R.id.discountedPrice);
        subCategorySelectorSpinner = findViewById(R.id.subCategorySelectorSpinner);
        brandSelectorSpinner = findViewById(R.id.brandSelectorSpinner);
        progressBar = findViewById(R.id.progressBar);

        addProductSubCategorySpinnerAdapter = new AddProductSubCategorySpinnerAdapter(AddAProduct.this, subCategoryEntityList);
        addProductBrandSpinnerAdapter = new AddProductBrandSpinnerAdapter(AddAProduct.this, brandsList);

        subCategorySelectorSpinner.setAdapter(addProductSubCategorySpinnerAdapter);
        brandSelectorSpinner.setAdapter(addProductBrandSpinnerAdapter);


        categoryEntity = (CategoryEntity) getIntent().getSerializableExtra("category");

        collectInfo();

        ImageView backFromProductAdding = findViewById(R.id.backFromProductAdding);
        backFromProductAdding.setOnClickListener(v -> onBackPressed());

        productImage.setOnClickListener(v -> requestStoragePermission());

        calculateDiscount.setOnClickListener(v ->
        {

            if(productPrice.getText().toString().isEmpty())
            {
                productPrice.setError("Product Price can not be empty");
                productPrice.requestFocus();
            }
            else if(productDiscount.getText().toString().isEmpty())
            {
                productDiscount.setError("Product Discount Can Not Be Empty");
                productDiscount.requestFocus();
            }
            else
            {
                double product_price = Double.parseDouble(productPrice.getText().toString());
                double product_discount = Double.parseDouble(productDiscount.getText().toString());

                if (product_price < 0)
                {
                    productPrice.setError("Product price can not be negative");
                    productPrice.requestFocus();
                }
                else if(product_discount < 0)
                {
                    productDiscount.setError("Product discount can not be negative");
                    productDiscount.requestFocus();
                }
                else if(product_discount > product_price)
                {
                    productDiscount.setError("Product discount can not be greater than price");
                    productDiscount.requestFocus();
                }

                double discount_price = product_price - product_discount;
                double discount_percentage = (product_discount/product_price) * 100.00;

                String priceWithDiscount = "৳ "+ discount_price + "( "+ String.format(Locale.ENGLISH, "%.2f", discount_percentage)+ "% )";

                discountedPrice.setText(priceWithDiscount);
            }
        });

        subCategorySelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!subCategoryEntityList.isEmpty())
                    fetchBrands(subCategoryEntityList.get(position));
                else
                {
                    Toast.makeText(AddAProduct.this, "Can not fetch category, please wait", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addNewProduct.setOnClickListener(v -> ValidateProductData());

    }

    private void fetchBrands(SubCategoryEntity subCategoryEntity) {

        RetrofitClient.getInstance().getApi()
                .getAllBrands(subCategoryEntity.getSub_category_id())
                .enqueue(new Callback<List<Brands>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Brands>> call, @NotNull Response<List<Brands>> response) {
                        if(response.isSuccessful())
                        {
                            brandsList.clear();
                            assert response.body() != null;
                            brandsList.addAll(response.body());
                            addProductBrandSpinnerAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            brandsList.clear();
                            addProductBrandSpinnerAdapter.notifyDataSetChanged();
                            Toast.makeText(AddAProduct.this, "No brands available", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Brands>> call, @NotNull Throwable t) {
                        Log.e("Add product", "Error is: "+t.getMessage());
                        Toast.makeText(AddAProduct.this, "Can not get brands, please try again", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void collectInfo() {

        progressBar.setVisibility(VISIBLE);

        subCategoryEntityList.clear();

        RetrofitClient.getInstance().getApi()
                .getAllSubCategories(categoryEntity.getCategoryId())
                .enqueue(new Callback<List<SubCategoryEntity>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<SubCategoryEntity>> call, @NotNull Response<List<SubCategoryEntity>> response) {
                        if(response.isSuccessful())
                        {
                            progressBar.setVisibility(GONE);
                            assert response.body() != null;
                            subCategoryEntityList.addAll(response.body());
                            addProductSubCategorySpinnerAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            progressBar.setVisibility(GONE);
                            Toast.makeText(AddAProduct.this, "Can not get sub categories, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<SubCategoryEntity>> call, @NotNull Throwable t) {
                        progressBar.setVisibility(GONE);
                        Log.e(TAG, "Error occurred, Error is: "+t.getMessage());
                        Toast.makeText(AddAProduct.this, "Can not get sub category, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void ValidateProductData()
    {
        String name = productName.getText().toString();
        String description = productDescription.getText().toString();
        String quantity = productQuantity.getText().toString();
        String price = productPrice.getText().toString();
        String discount = productDiscount.getText().toString();

        if(productImage.getDrawable().getConstantState() == Objects.requireNonNull(ContextCompat.getDrawable(
                AddAProduct.this, R.drawable.select_product_image)).getConstantState())
        {
            Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if (name.isEmpty())
        {
            productName.setError("Please write product name...");
            productName.requestFocus();
        }
        else if (description.isEmpty())
        {
            productDescription.setError("Please write product description...");
            productDescription.requestFocus();
        }
        else if (quantity.isEmpty())
        {
            productQuantity.setError("Product quantity can not be empty");
            productQuantity.requestFocus();
        }
        else if (price.isEmpty())
        {
            productPrice.setError("Please write product price...");
            productPrice.requestFocus();
        }
        else if (discount.isEmpty())
        {
            productDiscount.setError("Please write product discount...");
            productDiscount.requestFocus();
        }
        else if(subCategorySelectorSpinner.getSelectedItem() == null)
        {
            Toast.makeText(this, "Please select a sub-category", Toast.LENGTH_SHORT).show();
        }
        else if(brandSelectorSpinner.getSelectedItem() == null)
        {
            Toast.makeText(this, "Please select a brand", Toast.LENGTH_SHORT).show();
        }
        else {
            progressBar.setVisibility(VISIBLE);
            storeProductInformation();
        }
    }

    private void storeProductInformation() {
        Bitmap bitmap = ((BitmapDrawable) productImage.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        String filename = UUID.randomUUID().toString();

        File file = new File(AddAProduct.this.getCacheDir() + File.separator + filename + ".jpg");

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
                .uploadImageToServer("Products", multipartFile)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {
                            try {
                                assert response.body() != null;
                                String path = response.body().string();

                                OwoProduct owoProduct = new OwoProduct();

                                owoProduct.setProductName(productName.getText().toString());
                                owoProduct.setProductCategoryId(categoryEntity.getCategoryId());
                                SubCategoryEntity subCategoryEntity = (SubCategoryEntity) subCategorySelectorSpinner.getSelectedItem();
                                owoProduct.setProductSubCategoryId(subCategoryEntity.getSub_category_id());
                                owoProduct.setProductPrice(Double.parseDouble(productPrice.getText().toString()));
                                owoProduct.setProductDiscount(Double.parseDouble(productDiscount.getText().toString()));
                                owoProduct.setProductQuantity(Integer.parseInt(productQuantity.getText().toString()));
                                owoProduct.setProductDescription(productDescription.getText().toString());

                                owoProduct.setProductCreationDate(new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(new Date()));
                                owoProduct.setProductCreationTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US).format(new Date()));

                                owoProduct.setProductImage(path);
                                owoProduct.setBrands((Brands) brandSelectorSpinner.getSelectedItem());

                                RetrofitClient.getInstance().getApi()
                                        .createProduct(owoProduct)
                                        .enqueue(new Callback<OwoProduct>() {
                                            @Override
                                            public void onResponse(@NotNull Call<OwoProduct> call, @NotNull Response<OwoProduct> response) {
                                                if(response.isSuccessful())
                                                {
                                                    progressBar.setVisibility(GONE);
                                                    Toast.makeText(AddAProduct.this, "Product created successfully", Toast.LENGTH_SHORT).show();

                                                    Intent intent = new Intent(AddAProduct.this, HomeActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                else
                                                {
                                                    progressBar.setVisibility(GONE);
                                                    Toast.makeText(AddAProduct.this, "Failed to upload product, please try again", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(@NotNull Call<OwoProduct> call, @NotNull Throwable t) {
                                                progressBar.setVisibility(GONE);
                                                Log.e("AddAProduct", "Error occurred, Error is: "+t.getMessage());
                                                Toast.makeText(AddAProduct.this, "Failed to add product, please try again", Toast.LENGTH_SHORT).show();
                                            }
                                        });


                            } catch (IOException e) {
                                e.printStackTrace();
                                progressBar.setVisibility(GONE);
                                Log.e("AddAProduct", "Error occurred, Error is: "+e.getMessage());
                            }
                        }
                        else
                        {
                            progressBar.setVisibility(GONE);
                            Toast.makeText(AddAProduct.this, "Failed to upload product image, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        progressBar.setVisibility(GONE);
                        Log.e("AddAProduct", "Error occurred, Error is: "+t.getMessage());
                        Toast.makeText(AddAProduct.this, "Failed to upload image, please try again", Toast.LENGTH_SHORT).show();
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

                        ActivityCompat.requestPermissions(AddAProduct.this,
                                new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

                        selectImage(AddAProduct.this);
                    })
                    .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss())
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

            selectImage(AddAProduct.this);
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
                        productImage.setImageBitmap(selectedImage);
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
                                productImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }

}
