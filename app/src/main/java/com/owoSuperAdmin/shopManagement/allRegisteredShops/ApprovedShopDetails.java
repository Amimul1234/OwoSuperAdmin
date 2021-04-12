package com.owoSuperAdmin.shopManagement.allRegisteredShops;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.model.LatLng;
import com.owoSuperAdmin.configurationsFile.HostAddress;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.owoshop.LocationFromMap;
import com.owoSuperAdmin.owoshop.R;
import com.owoSuperAdmin.shopManagement.entity.ShopKeeperPermissions;
import com.owoSuperAdmin.shopManagement.entity.Shops;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApprovedShopDetails extends AppCompatActivity
{
    private final List<String> shopKeeperPermittedCategories = new ArrayList<>();
    private ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_shop_details);

        Shops shops = (Shops) getIntent().getSerializableExtra("ShopRequest");

        ListView listView = findViewById(R.id.shop_keeper_permitted_categories);

        TextView shop_name = findViewById(R.id.shop_name);
        TextView shop_keeper_name = findViewById(R.id.shop_owner_name);
        TextView shop_keeper_mobile = findViewById(R.id.shop_owner_mobile);
        TextView shop_address = findViewById(R.id.shop_address);
        TextView shop_service_mobile = findViewById(R.id.shop_service_mobile);

        ImageView shop_image = findViewById(R.id.shop_image);
        ImageView shop_keeper_nid = findViewById(R.id.shop_owner_nid);
        ImageView shop_trade_license = findViewById(R.id.shop_trade_licence);
        ImageView backButton = findViewById(R.id.backButton);

        shop_name.setText(shops.getShop_name());
        shop_keeper_name.setText(shops.getShop_owner_name());
        shop_keeper_mobile.setText(shops.getShop_owner_mobile());
        shop_service_mobile.setText(shops.getShop_service_mobile());
        shop_address.setText(shops.getShop_address());

        listAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, shopKeeperPermittedCategories);

        listView.setAdapter(listAdapter);

        Glide.with(getApplicationContext()).load(HostAddress.HOST_ADDRESS.getAddress() + shops.getShop_image_uri()).
                timeout(6000).diskCacheStrategy(DiskCacheStrategy.ALL).into(shop_image);
        Glide.with(getApplicationContext()).load(HostAddress.HOST_ADDRESS.getAddress() + shops.getShop_keeper_nid_front_uri()).
                timeout(6000).diskCacheStrategy(DiskCacheStrategy.ALL).into(shop_keeper_nid);
        Glide.with(getApplicationContext()).load(HostAddress.HOST_ADDRESS.getAddress() + shops.getTrade_license_url()).
                timeout(6000).diskCacheStrategy(DiskCacheStrategy.ALL).into(shop_trade_license);

        backButton.setOnClickListener(v -> onBackPressed());

        getPermittedCategories(shops);

        Button seeShopInMap = findViewById(R.id.shop_address_in_map);

        seeShopInMap.setOnClickListener(v ->
        {
            LatLng mapsLatLng = new LatLng(shops.getLatitude(), shops.getLongitude());

            Intent intent = new Intent(ApprovedShopDetails.this, LocationFromMap.class);
            intent.putExtra("latlang", mapsLatLng);
            startActivity(intent);

        });

    }

    private void getPermittedCategories(Shops shops) {

        List<Long> categoryId = new ArrayList<>();

        for(ShopKeeperPermissions shopKeeperPermissions : shops.getShopKeeperPermissions())
        {
            categoryId.add(shopKeeperPermissions.getPermittedCategoryId());
        }

        RetrofitClient.getInstance().getApi()
                .getCategoryListBasedOnId(categoryId)
                .enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<String>> call, @NotNull Response<List<String>> response) {
                        if(response.isSuccessful())
                        {
                            shopKeeperPermittedCategories.clear();

                            int size = response.body().size();

                            for(int i=0; i<size; i++)
                            {
                                shopKeeperPermittedCategories.add(i+1 + " . " +response.body().get(i));
                            }

                            listAdapter.notifyDataSetChanged();
                        }
                        else
                        {
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