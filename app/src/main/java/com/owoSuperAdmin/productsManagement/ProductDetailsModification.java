package com.owoSuperAdmin.productsManagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.owoSuperAdmin.adminHomePanel.HomeActivity;
import com.owoSuperAdmin.configurationsFile.HostAddress;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.productsManagement.entity.OwoProduct;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsModification extends AppCompatActivity {

    private TextView productNewPrice;
    private EditText productPriceUpdate;
    private EditText productDiscountUpdate, productQuantityUpdate, productDescriptionUpdate;
    private ImageView productImageUpdate;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ProgressDialog progressDialog;

    private OwoProduct product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        ImageView backToHome = findViewById(R.id.back_to_home);
        productImageUpdate = findViewById(R.id.product_image_update);

        productNewPrice = findViewById(R.id.new_price_update);
        productPriceUpdate = findViewById(R.id.product_price_update);
        productDiscountUpdate = findViewById(R.id.product_discount_update);
        productQuantityUpdate = findViewById(R.id.product_quantity_update);
        productDescriptionUpdate = findViewById(R.id.product_description_update);
        collapsingToolbarLayout = findViewById(R.id.product_name_update);

        Button calculateNewPrice = findViewById(R.id.new_price_update_calculate);
        Button updateProduct = findViewById(R.id.update_products_button);
        Button deleteProduct = findViewById(R.id.delete_products_button);

        progressDialog = new ProgressDialog(this);

        product = (OwoProduct) getIntent().getSerializableExtra("Products");

        setProductData(product);

        collectProductData();

        calculateNewPrice.setOnClickListener(v -> {
            Double discounted_price = Double.parseDouble(productPriceUpdate.getText().toString()) - Double.parseDouble(productDiscountUpdate.getText().toString());
            productNewPrice.setText(String.valueOf(discounted_price));
        });

        productImageUpdate.setOnClickListener(v ->
                Toast.makeText(ProductDetailsModification.this, "Product Image can not be changed.", Toast.LENGTH_SHORT).show());

        backToHome.setOnClickListener(v -> onBackPressed());

        deleteProduct.setOnClickListener(v -> {
            if(product.getProductImage() != null)
                deleteProductFromRecord();
            else
                Toast.makeText(this, "Product information not available", Toast.LENGTH_SHORT).show();
        });

        updateProduct.setOnClickListener(v -> {
            if(product.getProductImage() != null)
                validateInputFields();
            else
                Toast.makeText(this, "Product information not available", Toast.LENGTH_SHORT).show();
        });

    }

    private void validateInputFields()
    {
        if (productDescriptionUpdate.getText().toString().isEmpty())
        {
            productDescriptionUpdate.setError("Please write product description...");
            productDescriptionUpdate.requestFocus();
        }
        else if (productQuantityUpdate.getText().toString().isEmpty())
        {
            productQuantityUpdate.setError("Product quantity can not be empty");
            productQuantityUpdate.requestFocus();
        }
        else if (productPriceUpdate.getText().toString().isEmpty())
        {
            productPriceUpdate.setError("Please write product price...");
            productPriceUpdate.requestFocus();
        }
        else if (productDiscountUpdate.getText().toString().isEmpty())
        {
            productDiscountUpdate.setError("Please write product discount...");
            productDiscountUpdate.requestFocus();
        }
        else {
            product.setProductDescription(productDescriptionUpdate.getText().toString());
            product.setProductQuantity(Integer.parseInt(productQuantityUpdate.getText().toString()));
            product.setProductPrice(Double.parseDouble(productPriceUpdate.getText().toString()));
            product.setProductDiscount(Double.parseDouble(productDiscountUpdate.getText().toString()));

            updateProductInfo();
        }
    }

    private void updateProductInfo()
    {
        progressDialog.setTitle("Update Product Data");
        progressDialog.setMessage("Please wait while we are updating product data");
        progressDialog.show();

        RetrofitClient.getInstance().getApi()
                .updateProduct(product)
                .enqueue(new Callback<OwoProduct>() {
                    @Override
                    public void onResponse(@NotNull Call<OwoProduct> call, @NotNull Response<OwoProduct> response) {
                        if(response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Toast.makeText(ProductDetailsModification.this, "Product information updated successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(ProductDetailsModification.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(ProductDetailsModification.this, "Can not update product data, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<OwoProduct> call, @NotNull Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(ProductDetailsModification.this, "Can not update product data, please try again", Toast.LENGTH_SHORT).show();
                        Log.e("ProductDetails", "Error occurred, Error is: "+t.getMessage());
                    }
                });
    }

    private void deleteProductFromRecord()
    {

        progressDialog.setTitle("Delete product from record");
        progressDialog.setMessage("Please wait while we are deleting the product");
        progressDialog.show();

        String imagePath = product.getProductImage();
        String cleanPath = imagePath.substring(34);

        RetrofitClient.getInstance().getApi()
                .deleteProduct(product.getProductId())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {
                            RetrofitClient.getInstance().getApi()
                                    .deleteImage(cleanPath)
                                    .enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                                            if(response.isSuccessful())
                                            {
                                                progressDialog.dismiss();

                                                Toast.makeText(ProductDetailsModification.this, "Product successfully deleted", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(ProductDetailsModification.this, HomeActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                finish();
                                            }
                                            else{
                                                progressDialog.dismiss();
                                                Toast.makeText(ProductDetailsModification.this, "Can not delete product image", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(ProductDetailsModification.this, HomeActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                                            progressDialog.dismiss();

                                            Toast.makeText(ProductDetailsModification.this, "Can not delete product image, please try again", Toast.LENGTH_SHORT).show();
                                            Log.e("ProductDetails", "Error occurred, Error is: "+t.getMessage());
                                            Intent intent = new Intent(ProductDetailsModification.this, HomeActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(ProductDetailsModification.this,
                                    "Can not delete product from database, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(ProductDetailsModification.this,
                                "Can not delete product from database, please try again", Toast.LENGTH_SHORT).show();
                        Log.e("ProductDetails", "Error occurred, Error is: "+t.getMessage());
                    }
                });
    }

    private void collectProductData()
    {
        progressDialog.setTitle("Collecting product data");
        progressDialog.setMessage("Please wait while we are collecting product information");
        progressDialog.show();

        RetrofitClient.getInstance().getApi()
                .getAProduct(product.getProductId())
                .enqueue(new Callback<OwoProduct>() {
                    @Override
                    public void onResponse(@NotNull Call<OwoProduct> call, @NotNull Response<OwoProduct> response) {
                        if(response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            product = response.body();
                            setProductData(product);
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(ProductDetailsModification.this, "Can not get product data, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<OwoProduct> call, @NotNull Throwable t) {
                        progressDialog.dismiss();
                        Log.e("ProductDetails", "Error occurred, Error is: "+t.getMessage());
                        Toast.makeText(ProductDetailsModification.this, "Can not get product data, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setProductData(OwoProduct owoProduct)
    {
        Glide.with(this).load(HostAddress.HOST_ADDRESS.getAddress()+owoProduct.getProductImage()).into(productImageUpdate);
        collapsingToolbarLayout.setTitle(owoProduct.getProductName());
        productPriceUpdate.setText(String.valueOf(owoProduct.getProductPrice()));
        productDiscountUpdate.setText(String.valueOf(owoProduct.getProductDiscount()));
        productQuantityUpdate.setText(String.valueOf(owoProduct.getProductQuantity()));
        productDescriptionUpdate.setText(owoProduct.getProductDescription());
    }
}
