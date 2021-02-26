package com.owoSuperAdmin.productsManagement;

import android.app.ProgressDialog;
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
import com.owoSuperAdmin.configurationsFile.HostAddress;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.productsManagement.entity.OwoProduct;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetails extends AppCompatActivity {

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
                Toast.makeText(ProductDetails.this, "Product Image can not be changed.", Toast.LENGTH_SHORT).show());

        backToHome.setOnClickListener(v -> {
            onBackPressed();
        });

        deleteProduct.setOnClickListener(v -> {

        });

        /*
        delete_product.setOnClickListener((View.OnClickListener) v -> {

            CharSequence options[]=new CharSequence[]{"Yes","No"};
            final AlertDialog.Builder builder=new AlertDialog.Builder(ProductDetails.this);
            builder.setTitle("Are you sure you want to delete this product?");

            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {

                    if (i==0)
                    {
                        loadingbar.setTitle("Update Product");
                        loadingbar.setMessage("Please wait while we are updating the product...");
                        loadingbar.setCanceledOnTouchOutside(false);
                        loadingbar.show();

                        StorageReference ProductImagesRef = FirebaseStorage.getInstance().getReferenceFromUrl(product.getProductImage());

                        ProductImagesRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(ProductDetails.this, "Product Image removed successfully", Toast.LENGTH_SHORT).show();

                                Call<ResponseBody> call = RetrofitClient.getInstance()
                                        .getApi().deleteProduct(product.getProductId());

                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if(response.isSuccessful()) {
                                            Toast.makeText(ProductDetails.this, "Product Deleted", Toast.LENGTH_SHORT).show();
                                            loadingbar.dismiss();
                                            finish();
                                        }
                                        else
                                        {
                                            loadingbar.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toast.makeText(ProductDetails.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                        loadingbar.dismiss();
                                    }
                                });


                            }
                        }).addOnFailureListener(e -> {
                            Toast.makeText(ProductDetails.this, "Can not delete product.  Try again", Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                        });


                    }

                    else if(i == 1)
                    {

                        dialog.cancel();
                    }

                }
            });

            builder.show();

        });

         */

        /*
        updateButton.setOnClickListener(v -> {

            loadingbar.setTitle("Update Product");
            loadingbar.setMessage("Please wait, we are updating the product description");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();


            product.setProductDescription(descriptionUpdate.getText().toString());
            product.setProductPrice(Double.parseDouble(priceUpdate.getText().toString()));
            product.setProductDiscount(Double.parseDouble(discountUpdate.getText().toString()));
            product.setProductQuantity(Integer.parseInt(quantity_update.getText().toString()));

            Call<OwoProduct> call = RetrofitClient.getInstance()
                    .getApi().updateProduct(product);

            call.enqueue(new Callback<OwoProduct>() {
                @Override
                public void onResponse(@NotNull Call<OwoProduct> call, @NotNull Response<OwoProduct> response) {

                    if(response.isSuccessful())
                    {
                        Toast.makeText(ProductDetails.this, "Product updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ProductDetails.this, AvailableProducts.class);
                        loadingbar.dismiss();
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        loadingbar.dismiss();
                    }

                }

                @Override
                public void onFailure(@NotNull Call<OwoProduct> call, @NotNull Throwable t) {
                    Toast.makeText(ProductDetails.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        });

         */
    }

    private void collectProductData() {
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
                            Toast.makeText(ProductDetails.this, "Can not get product data, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<OwoProduct> call, @NotNull Throwable t) {
                        progressDialog.dismiss();
                        Log.e("ProductDetails", "Error occurred, Error is: "+t.getMessage());
                        Toast.makeText(ProductDetails.this, "Can not get product data, please try again", Toast.LENGTH_SHORT).show();
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
