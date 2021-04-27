package com.shopKPRAdmin.shopInfoChange;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shopKPRAdmin.configurationsFile.HostAddress;
import com.shopKPRAdmin.network.RetrofitClient;
import com.shopKPRAdmin.owoshop.R;
import com.shopKPRAdmin.shopManagement.entity.Shops;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeShopInfoActivity extends AppCompatActivity {

    private ChangeShopInfo changeShopInfo;

    private ImageView shopImageOld;
    private ImageView nidImageOld;
    private ImageView tradeLicenseOld;

    private TextView shopOldName;
    private TextView shopOldAddress;
    private TextView shopOwnerOldName;
    private TextView shopServiceMobileOld;

    private Button approveShopChange;
    private ImageView backToHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_shop_info);

        changeShopInfo = (ChangeShopInfo) getIntent().getSerializableExtra("ChangeShopInfo");

        shopImageOld = findViewById(R.id.shopImageOld);
        ImageView shopImageNew = findViewById(R.id.shopImageNew);
        nidImageOld = findViewById(R.id.shopOwnerNidOld);
        ImageView nidImageNew = findViewById(R.id.shopOwnerNidNew);
        tradeLicenseOld = findViewById(R.id.shopTradeLicenseOld);
        ImageView tradeLicenseNew = findViewById(R.id.shopTradeLicenseNew);
        shopOldName = findViewById(R.id.shopNameOld);
        TextView shopNewName = findViewById(R.id.shopNameNew);
        shopOldAddress = findViewById(R.id.shopAddressOld);
        TextView shopNewAddress = findViewById(R.id.shopAddressNew);
        shopOwnerOldName = findViewById(R.id.shopOwnerNameOld);
        TextView shopOwnerNewName = findViewById(R.id.shopOwnerNameNew);
        shopServiceMobileOld = findViewById(R.id.shopServiceMobileOld);
        TextView shopServiceMobileNew = findViewById(R.id.shopServiceMobileNew);
        approveShopChange = findViewById(R.id.approveShopChange);
        backToHome = findViewById(R.id.back_button);

        Glide.with(this).load(HostAddress.HOST_ADDRESS.getAddress() + changeShopInfo.getNewShopImageURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(shopImageNew);

        Glide.with(this).load(HostAddress.HOST_ADDRESS.getAddress() + changeShopInfo.getNewShopOwnerNidFront())
                .diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(nidImageNew);

        Glide.with(this).load(HostAddress.HOST_ADDRESS.getAddress() + changeShopInfo.getNewShopTradeLicenseURI())
                .diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(tradeLicenseNew);

        shopNewName.setText(changeShopInfo.getNewShopName());
        shopNewAddress.setText(changeShopInfo.getNewShopAddress());
        shopOwnerNewName.setText(changeShopInfo.getNewShopOwnerName());
        shopServiceMobileNew.setText(changeShopInfo.getNewShopServiceMobile());

        getOldShopInfo();
        
        approveShopChange.setOnClickListener(v -> approveChange());
        backToHome.setOnClickListener(v -> onBackPressed());
    }

    private void approveChange() {
    }

    private void getOldShopInfo() {

        RetrofitClient.getInstance().getApi()
              .getShopInfo(changeShopInfo.getShopOwnerMobileNumber())
                .enqueue(new Callback<Shops>() {
                    @Override
                    public void onResponse(@NotNull Call<Shops> call, @NotNull Response<Shops> response) {
                        if(response.isSuccessful())
                        {
                            Shops shops = response.body();

                            Glide.with(ChangeShopInfoActivity.this).load(HostAddress.HOST_ADDRESS.getAddress() + shops.getShop_image_uri())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(shopImageOld);

                            Glide.with(ChangeShopInfoActivity.this).load(HostAddress.HOST_ADDRESS.getAddress() + shops.getShop_keeper_nid_front_uri())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(nidImageOld);

                            Glide.with(ChangeShopInfoActivity.this).load(HostAddress.HOST_ADDRESS.getAddress() + shops.getTrade_license_url())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(tradeLicenseOld);

                            shopOldName.setText(shops.getShop_name());
                            shopOldAddress.setText(shops.getShop_address());
                            shopOwnerOldName.setText(shops.getShop_owner_name());
                            shopServiceMobileOld.setText(shops.getShop_service_mobile());
                        }
                        else
                        {
                            Toast.makeText(ChangeShopInfoActivity.this, "Can not get shop info, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Shops> call, @NotNull Throwable t) {
                        Log.e("ChangeShopInfo", t.getMessage());
                        Toast.makeText(ChangeShopInfoActivity.this, "Can not get shop info, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}