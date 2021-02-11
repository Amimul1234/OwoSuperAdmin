package com.owoSuperAdmin.shopManagement.approveShop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.owoSuperAdmin.configurationsFile.HostAddress;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.shopManagement.entity.ShopKeeperPermissions;
import com.owoSuperAdmin.shopManagement.entity.Shops;
import com.owoSuperAdmin.owoshop.LocationFromMap;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApproveAPendingShop extends AppCompatActivity {

    private TextView req_category_1;
    private TextView req_category_2;
    private TextView req_category_3;
    private CheckBox checkbox1, checkbox2, checkBox3;
    private Shops shops;
    private ProgressBar loader;

    private final List<String> permissions = new ArrayList<>();

    int size = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_shop_details);

        shops = (Shops) getIntent().getSerializableExtra("ShopRequest");

        TextView shop_name = findViewById(R.id.shop_name);
        TextView shop_keeper_name = findViewById(R.id.shop_owner_name);
        TextView shop_keeper_mobile = findViewById(R.id.shop_owner_mobile);
        TextView shop_address = findViewById(R.id.shop_address);
        TextView shop_service_mobile = findViewById(R.id.shop_service_mobile);
        req_category_1 = findViewById(R.id.requested_category_1);
        req_category_2 = findViewById(R.id.requested_category_2);
        req_category_3 = findViewById(R.id.requested_category_3);


        checkbox1 = findViewById(R.id.category_1_check_box);
        checkbox2 = findViewById(R.id.category_2_check_box);
        checkBox3 = findViewById(R.id.category_3_check_box);

        ImageView shop_image = findViewById(R.id.shop_image);
        ImageView shop_keeper_nid = findViewById(R.id.shop_owner_nid);
        ImageView shop_trade_license = findViewById(R.id.shop_trade_licence);

        Button see_in_map = findViewById(R.id.shop_address_in_map);
        Button approve_shop = findViewById(R.id.approve_shop);

        loader = findViewById(R.id.progressBar);

        shop_name.setText(shops.getShop_name());
        shop_keeper_name.setText(shops.getShop_owner_name());
        shop_keeper_mobile.setText(shops.getShop_owner_mobile());
        shop_service_mobile.setText(shops.getShop_service_mobile());
        shop_address.setText(shops.getShop_address());

        Glide.with(getApplicationContext()).load(HostAddress.HOST_ADDRESS.getAddress()+shops.getShop_image_uri()).into(shop_image);
        Glide.with(getApplicationContext()).load(HostAddress.HOST_ADDRESS.getAddress()+shops.getShop_keeper_nid_front_uri()).into(shop_keeper_nid);
        Glide.with(getApplicationContext()).load(HostAddress.HOST_ADDRESS.getAddress()+shops.getTrade_license_url()).into(shop_trade_license);

        size = shops.getShopKeeperPermissions().size();

        if(size == 1)
        {
            req_category_1.setText(shops.getShopKeeperPermissions().get(0).getPermittedCategory());
            checkbox2.setVisibility(View.GONE);
            checkBox3.setVisibility(View.GONE);
        }
        else if(size == 2)
        {
            req_category_1.setText(shops.getShopKeeperPermissions().get(0).getPermittedCategory());
            req_category_2.setText(shops.getShopKeeperPermissions().get(1).getPermittedCategory());
            checkBox3.setVisibility(View.GONE);
        }
        else
        {
            req_category_1.setText(shops.getShopKeeperPermissions().get(0).getPermittedCategory());
            req_category_2.setText(shops.getShopKeeperPermissions().get(1).getPermittedCategory());
            req_category_3.setText(shops.getShopKeeperPermissions().get(2).getPermittedCategory());
        }

        see_in_map.setOnClickListener(v -> {
            com.google.android.gms.maps.model.LatLng mapsLatLng = new com.google.android.gms.maps.model.LatLng(shops.getLatitude(), shops.getLongitude());
            Intent intent = new Intent(ApproveAPendingShop.this, LocationFromMap.class);
            intent.putExtra("latlang", mapsLatLng);
            startActivity(intent);
        });

        approve_shop.setOnClickListener(view -> {
            if(size == 1)
            {
                if(!checkbox1.isChecked())
                {
                    Toast.makeText(ApproveAPendingShop.this, "Please give minimum one permission", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    validate();
                }
            }
            else if(size == 2)
            {
                if(!checkbox1.isChecked() && !checkbox2.isChecked())
                {
                    Toast.makeText(ApproveAPendingShop.this, "Please give minimum one permission", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    validate();
                }
            }
            else
            {
                if(!checkbox1.isChecked() && !checkbox2.isChecked() && !checkBox3.isChecked())
                {
                    Toast.makeText(ApproveAPendingShop.this, "Please give minimum one permission", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    validate();
                }
            }

        });
    }

    private void validate() {

        if(checkbox1.isChecked())
        {
            permissions.add(req_category_1.getText().toString());
        }
        if(checkbox2.isChecked())
        {
            permissions.add(req_category_2.getText().toString());
        }
        if(checkBox3.isChecked())
        {
            permissions.add(req_category_3.getText().toString());
        }

        loader.setVisibility(View.VISIBLE);

        Shops newShop = new Shops();
        List<ShopKeeperPermissions> shopKeeperPermissionsList = new ArrayList<>();


        for(int i=0; i<permissions.size(); i++)
        {
            shops.getShopKeeperPermissions().add(new ShopKeeperPermissions(permissions.get(i)));
        }

        newShop.setShop_id(shops.getShop_id());
        newShop.setLatitude(shops.getLatitude());
        newShop.setLongitude(shops.getLongitude());
        newShop.setApproved(true);
        newShop.setShop_address(shops.getShop_address());
        newShop.setShop_image_uri(shops.getShop_image_uri());
        newShop.setShop_keeper_nid_front_uri(shops.getShop_keeper_nid_front_uri());
        newShop.setShop_name(shops.getShop_name());
        newShop.setShop_owner_mobile(shops.getShop_owner_mobile());
        newShop.setShop_owner_name(shops.getShop_owner_name());
        newShop.setShop_service_mobile(shops.getShop_service_mobile());
        newShop.setTrade_license_url(shops.getTrade_license_url());
        newShop.setShopKeeperPermissions(shopKeeperPermissionsList);

        RetrofitClient.getInstance().getApi()
                .approveShop(newShop)
                .enqueue(new Callback<Shops>() {
                    @Override
                    public void onResponse(@NotNull Call<Shops> call, @NotNull Response<Shops> response) {
                        if(response.isSuccessful())
                        {
                            Toast.makeText(ApproveAPendingShop.this, "Permission given to open shop", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                        else
                        {
                            Toast.makeText(ApproveAPendingShop.this, "Failed, try again", Toast.LENGTH_SHORT).show();
                            loader.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Shops> call, @NotNull Throwable t) {
                        Log.d("Error on creating shop", Objects.requireNonNull(t.getMessage()));
                    }
                });
    }
}