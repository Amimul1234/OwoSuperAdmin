package com.owosuperadmin.shopManagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.agrawalsuneet.dotsloader.loaders.AllianceLoader;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.owosuperadmin.LatLng;
import com.owosuperadmin.network.RetrofitClient;
import com.owosuperadmin.models.PendingShop;
import com.owosuperadmin.models.PermissionWithId;
import com.owosuperadmin.models.Shops;
import com.owosuperadmin.owoshop.LocationFromMap;
import com.owosuperadmin.owoshop.R;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApprovePendingShop extends AppCompatActivity {

    private TextView req_category_1;
    private TextView req_category_2;
    private TextView req_category_3;
    private CheckBox checkbox1, checkbox2, checkBox3;
    private PendingShop pendingShop;
    private AllianceLoader loader;

    private final List<String> permissions = new ArrayList<>();
    int size = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_shop_details);

        pendingShop = (PendingShop) getIntent().getSerializableExtra("PendingShop");

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

        loader = findViewById(R.id.alliance_loader);

        shop_name.setText(pendingShop.getShop_name());
        shop_keeper_name.setText(pendingShop.getShop_owner_name());
        shop_keeper_mobile.setText(pendingShop.getShop_owner_mobile());
        shop_service_mobile.setText(pendingShop.getShop_service_mobile());
        shop_address.setText(pendingShop.getShop_address());

        Glide.with(getApplicationContext()).load(pendingShop.getShop_image_uri()).into(shop_image);
        Glide.with(getApplicationContext()).load(pendingShop.getShop_keeper_nid_front_uri()).into(shop_keeper_nid);
        Glide.with(getApplicationContext()).load(pendingShop.getTrade_license_uri()).into(shop_trade_license);

        LatLng latLng = pendingShop.getLatLng();

        size = pendingShop.getHaveAccess().size();

        if(size == 1)
        {
            req_category_1.setText(pendingShop.getHaveAccess().get(0));
            checkbox2.setVisibility(View.GONE);
            checkBox3.setVisibility(View.GONE);
        }
        else if(size == 2)
        {
            req_category_1.setText(pendingShop.getHaveAccess().get(0));
            req_category_2.setText(pendingShop.getHaveAccess().get(1));
            checkBox3.setVisibility(View.GONE);
        }
        else
        {
            req_category_1.setText(pendingShop.getHaveAccess().get(0));
            req_category_2.setText(pendingShop.getHaveAccess().get(1));
            req_category_3.setText(pendingShop.getHaveAccess().get(2));
        }

        see_in_map.setOnClickListener(v -> {
            com.google.android.gms.maps.model.LatLng mapsLatLng = new com.google.android.gms.maps.model.LatLng(latLng.getLatitude(), latLng.getLongitude());
            Intent intent = new Intent(ApprovePendingShop.this, LocationFromMap.class);
            intent.putExtra("latlang", mapsLatLng);
            startActivity(intent);
        });

        approve_shop.setOnClickListener(view -> {
            if(size == 1)
            {
                if(!checkbox1.isChecked())
                {
                    Toast.makeText(ApprovePendingShop.this, "Please give minimum one permission", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ApprovePendingShop.this, "Please give minimum one permission", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ApprovePendingShop.this, "Please give minimum one permission", Toast.LENGTH_SHORT).show();
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

        DatabaseReference permittedShopKeeper = FirebaseDatabase.getInstance().getReference();

        loader.setVisibility(View.VISIBLE);


        Shops shops = new Shops(pendingShop.getLatLng().getLatitude(), pendingShop.getLatLng().getLongitude(), pendingShop.getShop_address(),
                pendingShop.getShop_image_uri(), pendingShop.getShop_keeper_nid_front_uri(), pendingShop.getShop_name(), pendingShop.getShop_owner_mobile(),
                pendingShop.getShop_owner_name(), pendingShop.getShop_service_mobile(), pendingShop.getTrade_license_uri());


        Call<Shops> call = RetrofitClient
                .getInstance()
                .getApi()
                .approveShop(shops);

        call.enqueue(new Callback<Shops>() {
            @Override
            public void onResponse(@NotNull Call<Shops> call, @NotNull Response<Shops> response) {
                if(response.body()!=null)
                {
                    int id = response.body().getId();

                    DatabaseReference deleteReq = FirebaseDatabase.getInstance().getReference();

                    deleteReq.child("PendingShopRequest").child(pendingShop.getShop_owner_mobile()).removeValue()
                            .addOnCompleteListener(task -> {
                                Toast.makeText(ApprovePendingShop.this, "Removed from pending list", Toast.LENGTH_SHORT).show();

                                PermissionWithId permissionWithId = new PermissionWithId(id, permissions);

                                permittedShopKeeper.child("permittedShopKeeper").child(pendingShop.getShop_owner_mobile())
                                        .setValue(permissionWithId).addOnCompleteListener(task1 -> {
                                            Toast.makeText(ApprovePendingShop.this, "Permission given to open shop", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }).addOnFailureListener(e -> Toast.makeText(ApprovePendingShop.this, "Failed, try again", Toast.LENGTH_SHORT).show());

                            }).addOnFailureListener(e -> {
                                Toast.makeText(ApprovePendingShop.this, "Please try again", Toast.LENGTH_SHORT).show();
                                loader.setVisibility(View.GONE);
                            });

                }
            }

            @Override
            public void onFailure(@NotNull Call<Shops> call, @NotNull Throwable t) {
                Log.d("Error on creating shop", Objects.requireNonNull(t.getMessage()));
            }
        });
    }
}