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
import com.owoSuperAdmin.categoryManagement.category.entity.CategoryEntity;
import com.owoSuperAdmin.categoryManagement.subCategory.entity.SubCategoryEntity;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
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

                String priceWithDiscount = "৳ "+ discount_price + "( "+ String.format("%.2f", discount_percentage)+ "% )";

                discountedPrice.setText(priceWithDiscount);
            }
        });

        addNewProduct.setOnClickListener(v -> ValidateProductData());

    }

    private void collectInfo() {

        progressBar.setVisibility(VISIBLE);

        RetrofitClient.getInstance().getApi()
                .getAllSubCategories(categoryEntity.getCategoryId())
                .enqueue(new Callback<List<SubCategoryEntity>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<SubCategoryEntity>> call, @NotNull Response<List<SubCategoryEntity>> response) {
                        if(response.isSuccessful())
                        {
                            progressBar.setVisibility(GONE);
                            AddProductSubCategorySpinnerAdapter addProductSubCategorySpinnerAdapter = 
                                    new AddProductSubCategorySpinnerAdapter(AddAProduct.this, response.body());
                            subCategorySelectorSpinner.setAdapter(addProductSubCategorySpinnerAdapter);
                        }
                        else
                        {
                            Toast.makeText(AddAProduct.this, "Can not get sub categories, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<SubCategoryEntity>> call, @NotNull Throwable t) {
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
        String subCategory = subCategorySelectorSpinner.getSelectedItem().toString();
        String brand = brandSelectorSpinner.getSelectedItem().toString();

        if(productImage.getDrawable().getConstantState() == Objects.requireNonNull(ContextCompat.getDrawable(
                AddAProduct.this, R.drawable.select_product_image)).getConstantState())
        {
            Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if (description.isEmpty())
        {
            Toast.makeText(this, "Please write product description...", Toast.LENGTH_SHORT).show();
        }
        else if (price.isEmpty())
        {
            Toast.makeText(this, "Please write product price...", Toast.LENGTH_SHORT).show();
        }
        else if (name.isEmpty())
        {
            Toast.makeText(this, "Please write product name...", Toast.LENGTH_SHORT).show();
        }
        else if (discount.isEmpty())
        {
            Toast.makeText(this, "Please write product discount...", Toast.LENGTH_SHORT).show();
        }
        else if (quantity.isEmpty())
        {
            Toast.makeText(AddAProduct.this, "Product quantity can not be empty", Toast.LENGTH_SHORT).show();
        }
        else if(subCategory.isEmpty())
        {
            Toast.makeText(this, "Please select a sub-category", Toast.LENGTH_SHORT).show();
        }
        else if(brand.isEmpty())
        {
            Toast.makeText(this, "Please select a brand", Toast.LENGTH_SHORT).show();
        }
        else {
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

                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {

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
