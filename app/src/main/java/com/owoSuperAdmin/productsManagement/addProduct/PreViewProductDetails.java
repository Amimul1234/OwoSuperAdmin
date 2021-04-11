package com.owoSuperAdmin.productsManagement.addProduct;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.owoSuperAdmin.owoshop.R;

public class PreViewProductDetails extends AppCompatActivity {

    private TextView product_discounted_price_preview,
        product_price_preview, product_discount_preview,
        product_quantity_preview, product_description_preview, product_brand;

    private ImageView product_image_preview, back_to_home;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_product);

        product_discount_preview = findViewById(R.id.product_discount_preview);
        product_discounted_price_preview = findViewById(R.id.product_discounted_price_preview);
        product_price_preview = findViewById(R.id.product_price_preview);
        product_quantity_preview = findViewById(R.id.product_quantity_preview);
        product_description_preview = findViewById(R.id.product_preview_description);

        product_brand = findViewById(R.id.product_brand);

        product_image_preview = findViewById(R.id.product_image_preview);
        collapsingToolbarLayout = findViewById(R.id.product_name_preview);
        appBarLayout = findViewById(R.id.appbar_preview);

        back_to_home = findViewById(R.id.back_to_home);


        String price = getIntent().getStringExtra("price");
        String discount = getIntent().getStringExtra("discount");
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String imageURI = getIntent().getStringExtra("image");
        String quantity = getIntent().getStringExtra("quantity");
        String brand = getIntent().getStringExtra("brand");


        double discounted_price = Double.parseDouble(price) - Double.parseDouble(discount);
        double discount_percent = (Double.parseDouble(discount)/ Double.parseDouble(price))*100;

        product_image_preview.setImageURI(Uri.parse(imageURI));
        collapsingToolbarLayout.setTitle(title);

        product_brand.setText(brand);

        product_price_preview.setText("৳ "+price);
        product_description_preview.setText(description);

        product_discounted_price_preview.setText("৳ "+ String.valueOf(discounted_price));



        product_discount_preview.setText("৳ "+ discount + "( "+ String.format("%.2f", discount_percent) + "% )");

        product_quantity_preview.setText(quantity+" pieces");



        back_to_home.setOnClickListener(v -> finish());

    }
}