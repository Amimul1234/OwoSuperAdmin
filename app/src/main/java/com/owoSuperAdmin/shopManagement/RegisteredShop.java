package com.owoSuperAdmin.shopManagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.agrawalsuneet.dotsloader.loaders.AllianceLoader;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.owoSuperAdmin.LatLng;
import com.owoSuperAdmin.shopManagement.approveShop.entities.PendingShop;
import com.owoSuperAdmin.owoshop.LocationFromMap;
import com.owoSuperAdmin.owoshop.R;
import java.util.ArrayList;
import java.util.List;

public class RegisteredShop extends AppCompatActivity {

    private TextView shop_name, shop_keeper_name, shop_keeper_mobile, shop_address,
            shop_service_mobile, req_category_1, req_category_2, req_category_3;

    private ImageView shop_image, shop_keeper_nid, shop_trade_license;

    private Button see_in_map, delete_shop;

    private PendingShop pendingShop;

    private AllianceLoader loader;

    private List<String> permissions = new ArrayList<>();
    int size = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_shop_details);

        pendingShop = (PendingShop) getIntent().getSerializableExtra("approvedShop");

        shop_name = findViewById(R.id.shop_name);
        shop_keeper_name = findViewById(R.id.shop_owner_name);
        shop_keeper_mobile = findViewById(R.id.shop_owner_mobile);
        shop_address = findViewById(R.id.shop_address);
        shop_service_mobile = findViewById(R.id.shop_service_mobile);
        req_category_1 = findViewById(R.id.requested_category_1);
        req_category_2 = findViewById(R.id.requested_category_2);
        req_category_3 = findViewById(R.id.requested_category_3);

        shop_image = findViewById(R.id.shop_image);
        shop_keeper_nid = findViewById(R.id.shop_owner_nid);
        shop_trade_license = findViewById(R.id.shop_trade_licence);

        see_in_map = findViewById(R.id.shop_address_in_map);
        delete_shop = findViewById(R.id.delete_shop);

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
        }
        else if(size == 2)
        {
            req_category_1.setText(pendingShop.getHaveAccess().get(0));
            req_category_2.setText(pendingShop.getHaveAccess().get(1));
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
                Intent intent = new Intent(RegisteredShop.this, LocationFromMap.class);
                intent.putExtra("latlang", mapsLatLng);
                startActivity(intent);
            }
        });

        delete_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });
    }

    private void delete() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Are you sure to delete shop?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        loader.setVisibility(View.VISIBLE);
                        //Initiating deletion process for shop
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                        //Deleting shop related images first
                        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(pendingShop.getShop_image_uri());
                        storageReference.delete();
                        StorageReference storageReference1 = FirebaseStorage.getInstance().getReferenceFromUrl(pendingShop.getShop_keeper_nid_front_uri());
                        storageReference1.delete();
                        StorageReference storageReference2 = FirebaseStorage.getInstance().getReferenceFromUrl(pendingShop.getTrade_license_uri());
                        storageReference2.delete();

                        databaseReference.child("approvedShops").child(pendingShop.getShop_owner_mobile()).removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        databaseReference.child("permittedShopKeeper").child(pendingShop.getShop_owner_mobile()).removeValue()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(RegisteredShop.this, "Shop Deleted", Toast.LENGTH_SHORT).show();
                                                        loader.setVisibility(View.INVISIBLE);
                                                        finish();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(RegisteredShop.this, "Failed to delete shop, Try again", Toast.LENGTH_SHORT).show();
                                                loader.setVisibility(View.INVISIBLE);
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisteredShop.this, "Can not delete shop, Please try again", Toast.LENGTH_SHORT).show();
                                loader.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

}