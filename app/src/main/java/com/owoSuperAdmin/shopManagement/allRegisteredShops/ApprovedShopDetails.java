package com.owoSuperAdmin.shopManagement.allRegisteredShops;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.owoSuperAdmin.configurationsFile.HostAddress;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.shopManagement.entity.Shops;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApprovedShopDetails extends AppCompatActivity {

    private TextView req_category_1;
    private TextView req_category_2, req_category_3;

    int size = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_shop_details);

        Shops shops = (Shops) getIntent().getSerializableExtra("ShopRequest");

        TextView shop_name = findViewById(R.id.shop_name);
        TextView shop_keeper_name = findViewById(R.id.shop_owner_name);
        TextView shop_keeper_mobile = findViewById(R.id.shop_owner_mobile);
        TextView shop_address = findViewById(R.id.shop_address);
        TextView shop_service_mobile = findViewById(R.id.shop_service_mobile);
        req_category_1 = findViewById(R.id.requested_category_1);
        req_category_2 = findViewById(R.id.requested_category_2);
        req_category_3 = findViewById(R.id.requested_category_3);

        ImageView shop_image = findViewById(R.id.shop_image);
        ImageView shop_keeper_nid = findViewById(R.id.shop_owner_nid);
        ImageView shop_trade_license = findViewById(R.id.shop_trade_licence);

        ImageView backButton = findViewById(R.id.backButton);

        shop_name.setText(shops.getShop_name());
        shop_keeper_name.setText(shops.getShop_owner_name());
        shop_keeper_mobile.setText(shops.getShop_owner_mobile());
        shop_service_mobile.setText(shops.getShop_service_mobile());
        shop_address.setText(shops.getShop_address());

        Glide.with(getApplicationContext()).load(HostAddress.HOST_ADDRESS.getAddress() + shops.getShop_image_uri()).
                timeout(6000).diskCacheStrategy(DiskCacheStrategy.ALL).into(shop_image);

        Glide.with(getApplicationContext()).load(HostAddress.HOST_ADDRESS.getAddress() + shops.getShop_keeper_nid_front_uri()).
                timeout(6000).diskCacheStrategy(DiskCacheStrategy.ALL).into(shop_keeper_nid);

        Glide.with(getApplicationContext()).load(HostAddress.HOST_ADDRESS.getAddress() + shops.getTrade_license_url()).
                timeout(6000).diskCacheStrategy(DiskCacheStrategy.ALL).into(shop_trade_license);

        size = shops.getShopKeeperPermissions().size();

        backButton.setOnClickListener(v -> onBackPressed());

        if (size == 1) {
            List<Long> categoryId = new ArrayList<>();

            categoryId.add(shops.getShopKeeperPermissions().get(0).getPermittedCategoryId());

            RetrofitClient.getInstance().getApi()
                    .getCategoryListBasedOnId(categoryId)
                    .enqueue(new Callback<List<String>>() {
                        @Override
                        public void onResponse(@NotNull Call<List<String>> call, @NotNull Response<List<String>> response) {
                            if (response.isSuccessful()) {
                                assert response.body() != null;
                                req_category_1.setText(response.body().get(0));
                            } else {
                                Toast.makeText(ApprovedShopDetails.this, "Can not get category name, please try again", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<List<String>> call, @NotNull Throwable t) {
                            Log.e("ApproveShop", "Error is: " + t.getMessage());
                            Toast.makeText(ApprovedShopDetails.this, "Can not get category name, please try again", Toast.LENGTH_SHORT).show();
                        }
                    });

        }
        else if (size == 2)
        {
            List<Long> categoryId = new ArrayList<>();

            categoryId.add(shops.getShopKeeperPermissions().get(0).getPermittedCategoryId());
            categoryId.add(shops.getShopKeeperPermissions().get(1).getPermittedCategoryId());

            RetrofitClient.getInstance().getApi()
                    .getCategoryListBasedOnId(categoryId)
                    .enqueue(new Callback<List<String>>() {
                        @Override
                        public void onResponse(@NotNull Call<List<String>> call, @NotNull Response<List<String>> response) {
                            if (response.isSuccessful()) {
                                assert response.body() != null;
                                req_category_1.setText(response.body().get(0));
                                req_category_2.setText(response.body().get(1));
                            } else {
                                Toast.makeText(ApprovedShopDetails.this, "Can not get category name, please try again", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<List<String>> call, @NotNull Throwable t) {
                            Log.e("ApproveShop", "Error is: " + t.getMessage());
                            Toast.makeText(ApprovedShopDetails.this, "Can not get category name, please try again", Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {

            List<Long> categoryId = new ArrayList<>();

            categoryId.add(shops.getShopKeeperPermissions().get(0).getPermittedCategoryId());
            categoryId.add(shops.getShopKeeperPermissions().get(1).getPermittedCategoryId());
            categoryId.add(shops.getShopKeeperPermissions().get(2).getPermittedCategoryId());

            RetrofitClient.getInstance().getApi()
                    .getCategoryListBasedOnId(categoryId)
                    .enqueue(new Callback<List<String>>() {
                        @Override
                        public void onResponse(@NotNull Call<List<String>> call, @NotNull Response<List<String>> response) {
                            if (response.isSuccessful()) {
                                assert response.body() != null;
                                req_category_1.setText(response.body().get(0));
                                req_category_2.setText(response.body().get(1));
                                req_category_3.setText(response.body().get(2));
                            } else {
                                Toast.makeText(ApprovedShopDetails.this, "Can not get category name, please try again", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<List<String>> call, @NotNull Throwable t) {
                            Log.e("ApproveShop", "Error is: " + t.getMessage());
                            Toast.makeText(ApprovedShopDetails.this, "Can not get category name, please try again", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}