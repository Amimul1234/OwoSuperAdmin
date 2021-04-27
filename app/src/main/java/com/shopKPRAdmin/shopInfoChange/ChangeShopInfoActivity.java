package com.shopKPRAdmin.shopInfoChange;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.shopKPRAdmin.owoshop.R;

public class ChangeShopInfoActivity extends AppCompatActivity {

    private ChangeShopInfo changeShopInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_shop_info);

        changeShopInfo = (ChangeShopInfo) getIntent().getSerializableExtra("ChangeShopInfo");


    }
}