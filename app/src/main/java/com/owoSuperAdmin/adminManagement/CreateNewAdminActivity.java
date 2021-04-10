package com.owoSuperAdmin.adminManagement;

import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.owoSuperAdmin.adminManagement.entity.AdminLogin;
import com.owoSuperAdmin.adminManagement.entity.AdminLoginWrapper;
import com.owoSuperAdmin.adminManagement.entity.AdminPermissions;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.owoshop.R;
import com.owoSuperAdmin.utilities.PasswordHashing;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateNewAdminActivity extends AppCompatActivity {

    private Boolean isShowPin = false, isShowConfirmPin = false;
    private final List<String> permissionList = new ArrayList<>();

    private ImageView showPassword;
    private ImageView showConfirmPassword;

    private EditText adminName, adminEmailAddress, adminPassword, adminConfirmPassword;

    private SwitchMaterial adminManagement, shopManagement, productManagement,
        offerManagement, categoryManagement, userManagement, orderManagement;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_admin);

        ImageView backFromView = findViewById(R.id.backFromAdminCreation);

        adminName = findViewById(R.id.new_admin_name);
        adminEmailAddress = findViewById(R.id.admin_email_address);
        adminPassword = findViewById(R.id.new_admin_password);
        adminConfirmPassword = findViewById(R.id.new_admin_confirm_password);

        Button createAdmin = findViewById(R.id.create_new_admin_btn);

        showPassword = findViewById(R.id.show_password);
        showConfirmPassword = findViewById(R.id.show_confirmed_password);

        adminManagement = findViewById(R.id.admin_management);
        shopManagement = findViewById(R.id.shop_management);
        productManagement = findViewById(R.id.product_management);
        offerManagement = findViewById(R.id.offer_management);
        userManagement = findViewById(R.id.user_management);
        orderManagement = findViewById(R.id.order_management);
        categoryManagement = findViewById(R.id.category_management);

        progressBar = findViewById(R.id.complete_progress);

        backFromView.setOnClickListener(v -> onBackPressed());

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

        createAdmin.setOnClickListener(v -> checkValidation());

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
            checkPermissionSwitches();
        }
    }

    private void checkPermissionSwitches() {
        if(adminManagement.isChecked())
            permissionList.add("Admin Management");
        if(shopManagement.isChecked())
            permissionList.add("Shop Management");
        if(productManagement.isChecked())
            permissionList.add("Product Management");
        if(offerManagement.isChecked())
            permissionList.add("Offer Management");
        if(categoryManagement.isChecked())
            permissionList.add("Category Management");
        if(userManagement.isChecked())
            permissionList.add("User Management");
        if(orderManagement.isChecked())
            permissionList.add("Order Management");

        addToDatabase();
    }

    private void addToDatabase() {
        AdminLogin adminLogin = new AdminLogin();

        adminLogin.setAdminName(adminName.getText().toString());
        adminLogin.setAdminEmailAddress(adminEmailAddress.getText().toString());

        adminLogin.setAdminPassword(PasswordHashing.sha256(adminPassword.getText().toString()));

        List<AdminPermissions> adminPermissionsList = new ArrayList<>();

        for(int i=0; i<permissionList.size(); i++)
        {
            AdminPermissions adminPermissions = new AdminPermissions();
            adminPermissions.setPermission(permissionList.get(i));

            adminPermissionsList.add(adminPermissions);
        }

        AdminLoginWrapper adminLoginWrapper = new AdminLoginWrapper(adminLogin, adminPermissionsList);

        saveToDatabase(adminLoginWrapper);

    }

    private void saveToDatabase(AdminLoginWrapper adminLoginWrapper) {
        progressBar.setVisibility(View.VISIBLE);

        RetrofitClient.getInstance().getApi()
                .addAnAdmin(adminLoginWrapper)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(CreateNewAdminActivity.this, "Successfully added new admin", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                        else
                        {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(CreateNewAdminActivity.this, "Can not add admin", Toast.LENGTH_SHORT).show();
                            Log.e("Admin Add", "Error while adding admin, Error code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(CreateNewAdminActivity.this, "Can not add admin", Toast.LENGTH_SHORT).show();
                        Log.e("Admin Add", "Error while adding admin, Error is: " + t.getMessage());
                    }
                });
    }

}
