package com.owosuperadmin.owoshop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.agrawalsuneet.dotsloader.loaders.AllianceLoader;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.owosuperadmin.LatLng;
import com.owosuperadmin.Network.RetrofitClient;
import com.owosuperadmin.model.PendingShop;
import com.owosuperadmin.model.PermissionWithId;
import com.owosuperadmin.model.Shops;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingShopDetails extends AppCompatActivity {

    private TextView shop_name, shop_keeper_name, shop_keeper_mobile, shop_address,
            shop_service_mobile, req_category_1, req_category_2, req_category_3;

    private CheckBox checkbox1, checkbox2, checkBox3;

    private ImageView shop_image, shop_keeper_nid, shop_trade_license;

    private Button see_in_map, approve_shop;

    private PendingShop pendingShop;

    private AllianceLoader loader;

    private List<String> permissions = new ArrayList<>();
    int size = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_shop_details);

        pendingShop = (PendingShop) getIntent().getSerializableExtra("PendingShop");

        shop_name = findViewById(R.id.shop_name);
        shop_keeper_name = findViewById(R.id.shop_owner_name);
        shop_keeper_mobile = findViewById(R.id.shop_owner_mobile);
        shop_address = findViewById(R.id.shop_address);
        shop_service_mobile = findViewById(R.id.shop_service_mobile);
        req_category_1 = findViewById(R.id.requested_category_1);
        req_category_2 = findViewById(R.id.requested_category_2);
        req_category_3 = findViewById(R.id.requested_category_3);


        checkbox1 = findViewById(R.id.category_1_check_box);
        checkbox2 = findViewById(R.id.category_2_check_box);
        checkBox3 = findViewById(R.id.category_3_check_box);

        shop_image = findViewById(R.id.shop_image);
        shop_keeper_nid = findViewById(R.id.shop_owner_nid);
        shop_trade_license = findViewById(R.id.shop_trade_licence);

        see_in_map = findViewById(R.id.shop_address_in_map);
        approve_shop = findViewById(R.id.approve_shop);

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

        see_in_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.google.android.gms.maps.model.LatLng mapsLatLng = new com.google.android.gms.maps.model.LatLng(latLng.getLatitude(), latLng.getLongitude());
                Intent intent = new Intent(PendingShopDetails.this, LocationFromMap.class);
                intent.putExtra("latlang", mapsLatLng);
                startActivity(intent);
            }
        });

        approve_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(size == 1)
                {
                    if(!checkbox1.isChecked())
                    {
                        Toast.makeText(PendingShopDetails.this, "Please give minimum one permission", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(PendingShopDetails.this, "Please give minimum one permission", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(PendingShopDetails.this, "Please give minimum one permission", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        validate();
                    }
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
            public void onResponse(Call<Shops> call, Response<Shops> response) {
                if(response.body()!=null)
                {
                    int id = response.body().getId();

                    DatabaseReference deleteReq = FirebaseDatabase.getInstance().getReference();

                    deleteReq.child("PendingShopRequest").child(pendingShop.getShop_owner_mobile()).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(PendingShopDetails.this, "Removed from pending list", Toast.LENGTH_SHORT).show();

                                    PermissionWithId permissionWithId = new PermissionWithId(id, permissions);

                                    permittedShopKeeper.child("permittedShopKeeper").child(pendingShop.getShop_owner_mobile())
                                            .setValue(permissionWithId).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(PendingShopDetails.this, "Permission given to open shop", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(PendingShopDetails.this, "Failed, try again", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PendingShopDetails.this, "Please try again", Toast.LENGTH_SHORT).show();
                            loader.setVisibility(View.GONE);
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<Shops> call, Throwable t) {
                Log.d("Error on creating shop", t.getMessage());
            }
        });
    }
}