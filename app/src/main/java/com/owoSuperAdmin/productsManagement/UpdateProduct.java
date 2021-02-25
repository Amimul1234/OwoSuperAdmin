package com.owoSuperAdmin.productsManagement;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.productsManagement.entity.OwoProduct;
import com.owoSuperAdmin.owoshop.R;

import org.jetbrains.annotations.NotNull;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProduct extends AppCompatActivity {

    private EditText descriptionUpdate, priceUpdate, discountUpdate, quantity_update;
    private Button updateButton;
    private ImageView imageUpdate;
    private ProgressDialog loadingbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Button calculate_new_price, delete_product;
    private ImageView back_to_home;
    private TextView updated_price;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        descriptionUpdate = (EditText)findViewById(R.id.product_description_update);
        priceUpdate = (EditText)findViewById(R.id.product_price_update);
        discountUpdate = (EditText)findViewById(R.id.product_discount_update);
        updateButton = (Button)findViewById(R.id.update_products_button);
        imageUpdate = (ImageView)findViewById(R.id.product_image_update);
        collapsingToolbarLayout = findViewById(R.id.product_name_update);
        calculate_new_price = findViewById(R.id.new_price_update_calculate);
        delete_product = findViewById(R.id.delete_products_button);
        back_to_home = findViewById(R.id.back_to_home);
        updated_price = findViewById(R.id.new_price_update);
        quantity_update = findViewById(R.id.product_quantity_update);

        loadingbar =new ProgressDialog(this);

        final OwoProduct product = (OwoProduct) getIntent().getSerializableExtra("Products");

        Glide.with(this).load(product.getProductImage()).into(imageUpdate);
        collapsingToolbarLayout.setTitle(product.getProductName());
        descriptionUpdate.setText(product.getProductDescription());
        priceUpdate.setText(String.valueOf(product.getProductPrice()));
        discountUpdate.setText(String.valueOf(product.getProductDiscount()));
        quantity_update.setText(String.valueOf(product.getProductQuantity()));

        calculate_new_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double discounted_price = Double.parseDouble(priceUpdate.getText().toString()) - Double.parseDouble(discountUpdate.getText().toString());
                updated_price.setText(String.valueOf(discounted_price));
            }
        });

        imageUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UpdateProduct.this, "Product Image can not be changed.", Toast.LENGTH_SHORT).show();
            }
        });

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        delete_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence options[]=new CharSequence[]{"Yes","No"};
                final AlertDialog.Builder builder=new AlertDialog.Builder(UpdateProduct.this);
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

                                    Toast.makeText(UpdateProduct.this, "Product Image removed successfully", Toast.LENGTH_SHORT).show();

                                    Call<ResponseBody> call = RetrofitClient.getInstance()
                                            .getApi().deleteProduct(product.getProductId());

                                    call.enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            if(response.isSuccessful()) {
                                                Toast.makeText(UpdateProduct.this, "Product Deleted", Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(UpdateProduct.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                            loadingbar.dismiss();
                                        }
                                    });


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UpdateProduct.this, "Can not delete product.  Try again", Toast.LENGTH_SHORT).show();
                                    loadingbar.dismiss();
                                }
                            });


                        }

                        else if(i == 1)
                        {

                            dialog.cancel();
                        }

                    }
                });

                builder.show();

            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                            Toast.makeText(UpdateProduct.this, "Product updated", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UpdateProduct.this, AvailableProducts.class);
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
                        Toast.makeText(UpdateProduct.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}
