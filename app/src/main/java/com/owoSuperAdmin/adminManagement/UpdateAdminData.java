package com.owoSuperAdmin.adminManagement;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.owoSuperAdmin.adminManagement.entity.AdminLogin;
import com.owoSuperAdmin.adminManagement.entity.AdminLoginWrapper;
import com.owoSuperAdmin.adminManagement.entity.AdminPermissions;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateAdminData extends AppCompatActivity {

    private final List<String> permissionList = new ArrayList<>();

    private AdminLogin adminLogin;

    private SwitchMaterial adminManagement, shopManagement, productManagement, offerManagement,
        categoryManagement, userManagement, orderManagement;

    private EditText newAdminName, newAdminEmail;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_semi_admin);

        adminLogin = (AdminLogin) getIntent().getSerializableExtra("adminLogin");

        progressDialog = new ProgressDialog(this);

        ImageView backToHome = findViewById(R.id.backFromAdminCreation);

        newAdminName = findViewById(R.id.new_admin_name);
        newAdminEmail = findViewById(R.id.admin_email_address);

        ImageView adminImage = findViewById(R.id.admin_image);

        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
        int color = colorGenerator.getRandomColor();
        char c = adminLogin.getAdminName().charAt(0);
        TextDrawable textDrawable = TextDrawable.builder().buildRound(String.valueOf(c), color);

        adminImage.setImageDrawable(textDrawable);

        adminManagement = findViewById(R.id.admin_management);
        shopManagement = findViewById(R.id.shop_management);
        productManagement = findViewById(R.id.product_management);
        offerManagement = findViewById(R.id.offer_management);
        categoryManagement = findViewById(R.id.category_management);
        userManagement = findViewById(R.id.user_management);
        orderManagement = findViewById(R.id.order_management);

        Button updateAdminData = findViewById(R.id.update_admin_data);

        newAdminName.setText(adminLogin.getAdminName());
        newAdminEmail.setText(adminLogin.getAdminEmailAddress());

        progressDialog.setTitle("Get Admin Permissions");
        progressDialog.setMessage("Please wait while we are fetching admin permissions");
        progressDialog.setCancelable(false);
        progressDialog.show();

        fetchPermissionsData();

        backToHome.setOnClickListener(v -> onBackPressed());

        updateAdminData.setOnClickListener(v-> checkValidation());

    }

    private void checkValidation() {
        if(newAdminName.getText().toString().isEmpty())
        {
            newAdminName.setError("Admin Name can not be empty");
            newAdminName.requestFocus();
        }
        else if(newAdminEmail.getText().toString().isEmpty())
        {
            newAdminEmail.setError("Admin email address can not be empty");
            newAdminEmail.requestFocus();
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

        adminLogin.setAdminEmailAddress(newAdminEmail.getText().toString());
        adminLogin.setAdminName(newAdminName.getText().toString());

        addToDataBase();
    }

    private void addToDataBase() {
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

        progressDialog.show();

        RetrofitClient.getInstance().getApi()
                .updateAdminPermissions(adminLoginWrapper)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Toast.makeText(UpdateAdminData.this, "Successfully added new admin", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(UpdateAdminData.this, "Can not add admin", Toast.LENGTH_SHORT).show();
                            Log.e("Admin Add", "Error while adding admin, Error code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(UpdateAdminData.this, "Can not add admin", Toast.LENGTH_SHORT).show();
                        Log.e("Admin Add", "Error while adding admin, Error is: " + t.getMessage());
                    }
                });
    }

    private void fetchPermissionsData()
    {

        int id = adminLogin.getAdminId();

        RetrofitClient.getInstance().getApi()
                .getAdminAllPermissions(id)
                .enqueue(new Callback<List<AdminPermissions>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<AdminPermissions>> call, @NotNull Response<List<AdminPermissions>> response) {
                        if(response.isSuccessful())
                        {
                            assert response.body() != null;
                            turnOnSwitches(response.body());
                        }
                        else
                        {
                            Toast.makeText(UpdateAdminData.this, "Can not get admin permissions, please try again", Toast.LENGTH_SHORT).show();
                            Log.e("Update Admin", "Error while fetching admin permission, Error code is: "+response.code());
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<AdminPermissions>> call, @NotNull Throwable t) {
                        Toast.makeText(UpdateAdminData.this, "Can not get admin permissions, please try again", Toast.LENGTH_SHORT).show();
                        Log.e("Update Admin", "Error while fetching admin permission, Error code is: "+t.getMessage());
                        progressDialog.dismiss();
                    }
                });


    }

    private void turnOnSwitches(List<AdminPermissions> adminPermissionsList)
    {

        List<String> permissionsList = new ArrayList<>();

        for(int i=0; i<adminPermissionsList.size(); i++)
        {
            permissionsList.add(adminPermissionsList.get(i).getPermission());
        }

        if(permissionsList.contains("Admin Management"))
            adminManagement.setChecked(true);
        if(permissionsList.contains("Shop Management"))
            shopManagement.setChecked(true);
        if(permissionsList.contains("Product Management"))
            productManagement.setChecked(true);
        if(permissionsList.contains("Offer Management"))
            offerManagement.setChecked(true);
        if(permissionsList.contains("Category Management"))
            categoryManagement.setChecked(true);
        if(permissionsList.contains("User Management"))
            userManagement.setChecked(true);
        if(permissionsList.contains("Order Management"))
            orderManagement.setChecked(true);

        progressDialog.dismiss();
    }

}
