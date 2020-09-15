package com.owosuperadmin.owoshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SingleShopApprovalActivity extends AppCompatActivity {

    private Button approveBtn,rejectBtn;
    private ImageView unapprovedShopImage;
    private TextView shopLicence,shopkeeperNID,shopDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_shop_approval);

        approveBtn=(Button)findViewById(R.id.approve_btn);
        rejectBtn=(Button)findViewById(R.id.reject_btn);
        unapprovedShopImage=(ImageView)findViewById(R.id.unapproved_shop_image);
        shopLicence=(TextView)findViewById(R.id.shop_licence);
        shopkeeperNID=(TextView)findViewById(R.id.shopkeeper_nid);
        shopDetails=(TextView)findViewById(R.id.shop_details);

        approveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SingleShopApprovalActivity.this, "Shop Approved Successfully.", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(SingleShopApprovalActivity.this,ShopApprovalActivity.class);
                startActivity(intent);
            }
        });

        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SingleShopApprovalActivity.this, "Requested Shop Rejected .", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(SingleShopApprovalActivity.this,ShopApprovalActivity.class);
                startActivity(intent);
            }
        });

    }
}
