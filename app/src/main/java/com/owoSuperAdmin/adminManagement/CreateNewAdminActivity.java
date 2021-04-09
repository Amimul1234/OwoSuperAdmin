package com.owoSuperAdmin.adminManagement;

import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.owoSuperAdmin.owoshop.R;

public class CreateNewAdminActivity extends AppCompatActivity {

    private Boolean isShowPin = false, isShowConfirmPin = false;

    private ImageView backFromView, showPassword, showConfirmPassword;
    private EditText adminName, adminEmailAddress, adminPassword,
        adminConfirmPassword;

    private SwitchMaterial approveShop, shopManagement, productManagement,
        offerManagement, userManagement, orderManagement;

    private Button createAdmin;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_admin);

        backFromView = findViewById(R.id.backFromAdminCreation);

        adminName = findViewById(R.id.new_admin_name);
        adminEmailAddress = findViewById(R.id.admin_email_address);
        adminPassword = findViewById(R.id.new_admin_password);
        adminConfirmPassword = findViewById(R.id.new_admin_confirm_password);

        createAdmin = findViewById(R.id.create_new_admin_btn);

        showPassword = findViewById(R.id.show_password);
        showConfirmPassword = findViewById(R.id.show_confirmed_password);

        approveShop = findViewById(R.id.approve_shop);
        shopManagement = findViewById(R.id.shop_management);
        productManagement = findViewById(R.id.product_management);
        offerManagement = findViewById(R.id.offer_management);
        userManagement = findViewById(R.id.user_management);
        orderManagement = findViewById(R.id.order_management);

        progressBar = findViewById(R.id.complete_progress);

        backFromView.setOnClickListener(v -> {
            onBackPressed();
        });

        showPassword.setOnClickListener(v -> {
            if (isShowPin) {
                adminPassword.setTransformationMethod(new PasswordTransformationMethod());
                showPassword.setImageResource(R.drawable.ic_visibility_off);
                isShowPin = false;

            }else{
                adminPassword.setTransformationMethod(null);
                showPassword.setImageResource(R.drawable.ic_visibility);
                isShowPin = true;
            }
        });


        showConfirmPassword.setOnClickListener(v -> {
            if (isShowConfirmPin) {
                adminConfirmPassword.setTransformationMethod(new PasswordTransformationMethod());
                showConfirmPassword.setImageResource(R.drawable.ic_visibility_off);
                isShowConfirmPin = false;

            }else{
                adminConfirmPassword.setTransformationMethod(null);
                showConfirmPassword.setImageResource(R.drawable.ic_visibility);
                isShowConfirmPin = true;
            }
        });

        createAdmin.setOnClickListener(v -> {
            checkValidation();
        });

    }

    private void checkValidation() {
        if(adminName.getText().toString().isEmpty())
        {
            adminName.setError("Admin Name can not be empty");
            adminName.requestFocus();
        }
        else if(adminEmailAddress.getText().toString().isEmpty())
        {
            adminEmailAddress.setError("Admin email address can not be empty");
            adminEmailAddress.requestFocus();
        }
        else if(adminPassword.getText().toString().isEmpty())
        {
            adminPassword.setError("Admin password can not be empty");
            adminPassword.requestFocus();
        }
        else if(adminConfirmPassword.getText().toString().isEmpty())
        {
            adminConfirmPassword.setError("Admin confirm password can not be empty");
            adminConfirmPassword.requestFocus();
        }
        else if(!adminPassword.getText().toString().equals(adminConfirmPassword.getText().toString()))
        {
            adminConfirmPassword.setError("Confirm password didn't match with password");
            adminConfirmPassword.requestFocus();
        }
        else
        {

        }
    }
}
