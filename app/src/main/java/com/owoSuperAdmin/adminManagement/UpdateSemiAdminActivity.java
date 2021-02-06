package com.owoSuperAdmin.adminManagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.owoSuperAdmin.homePanel.HomeActivity;
import com.owoSuperAdmin.adminManagement.entity.SemiAdmins;
import com.owoSuperAdmin.owoshop.R;

public class UpdateSemiAdminActivity extends AppCompatActivity {

    private TextView clickedAdminName, clickedAdminMobile;
    private ImageView admin_profile_pic;
    private Switch approveShop,maintainShop,addProducts,updateProducts,createOffers,maintainUsers,messaging;
    private Button updateAdminBtn;
    private ProgressBar progressBar;
    private ImageView back_to_semi_admins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_semi_admin);

        admin_profile_pic = findViewById(R.id.clicked_admin_profileImage);

        clickedAdminName = (TextView)findViewById(R.id.clicked_admin_name);
        clickedAdminMobile = findViewById(R.id.clicked_admin_mobile);
        progressBar = findViewById(R.id.clicked_complete_progress);
        back_to_semi_admins = findViewById(R.id.back_to_semi_admins);

        approveShop = (Switch)findViewById(R.id.clicked_approve_shop);
        maintainShop = (Switch)findViewById(R.id.clicked_maintain_shop);
        addProducts = (Switch)findViewById(R.id.clicked_add_products);
        updateProducts = (Switch)findViewById(R.id.clicked_update_products);
        createOffers = (Switch)findViewById(R.id.clicked_create_offers);
        maintainUsers = (Switch)findViewById(R.id.clicked_maintain_users);
        messaging = (Switch)findViewById(R.id.clicked_messaging);

        updateAdminBtn = (Button)findViewById(R.id.update_admin_btn);


        final SemiAdmins semiAdmins = (SemiAdmins) getIntent().
                getSerializableExtra("Semi Admin");//Getting the required class from the SemiAdminActivity


        Glide.with(UpdateSemiAdminActivity.this).load(semiAdmins.getProfileImage()).into(admin_profile_pic);

        clickedAdminName.setText(semiAdmins.getSemiAdminName());
        clickedAdminMobile.setText(semiAdmins.getPhone());


        approveShop.setChecked(semiAdmins.getApprove_shop());
        maintainShop.setChecked(semiAdmins.getMaintain_shops());
        addProducts.setChecked(semiAdmins.getAdd_products());
        updateProducts.setChecked(semiAdmins.getUpdate_products());
        createOffers.setChecked(semiAdmins.getCreate_offers());
        maintainUsers.setChecked(semiAdmins.getMaintain_users());
        messaging.setChecked(semiAdmins.getMessaging());



        back_to_semi_admins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        updateAdminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Semi Admins");

                //Getting the current state for updating to database
                semiAdmins.setApprove_shop(approveShop.isChecked());
                semiAdmins.setMaintain_shops(maintainShop.isChecked());
                semiAdmins.setAdd_products(addProducts.isChecked());
                semiAdmins.setUpdate_products(updateProducts.isChecked());
                semiAdmins.setCreate_offers(createOffers.isChecked());
                semiAdmins.setMaintain_users(maintainUsers.isChecked());
                semiAdmins.setMessaging(messaging.isChecked());


                progressBar.setVisibility(View.VISIBLE);

                reference.child(semiAdmins.getPhone()).setValue(semiAdmins).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(UpdateSemiAdminActivity.this, "Semi-admin info updated successfully.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UpdateSemiAdminActivity.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }

                        else
                        {
                            Toast.makeText(UpdateSemiAdminActivity.this, "Can not update Semi admin's data"+task.getException().toString(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });


            }
        });

    }
}
