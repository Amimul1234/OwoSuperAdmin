package com.owoSuperAdmin.adminManagement;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.owoSuperAdmin.adminManagement.entity.AdminLogin;
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

public class DeleteAdmin extends AppCompatActivity {

    private AdminLogin adminLogin;

    private SwitchMaterial approveShop, shopManagement, productManagement, offerManagement,
            userManagement, orderManagement;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_admin);

        adminLogin = (AdminLogin) getIntent().getSerializableExtra("adminLogin");

        progressDialog = new ProgressDialog(this);

        ImageView backToHome = findViewById(R.id.backFromAdminCreation);

        EditText newAdminName = findViewById(R.id.new_admin_name);
        EditText newAdminEmail = findViewById(R.id.admin_email_address);

        ImageView adminImage = findViewById(R.id.admin_image);
        Button deleteAdmin = findViewById(R.id.delete_admin);

        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
        int color = colorGenerator.getRandomColor();
        char c = adminLogin.getAdminName().charAt(0);
        TextDrawable textDrawable = TextDrawable.builder().buildRound(String.valueOf(c), color);

        adminImage.setImageDrawable(textDrawable);

        approveShop = findViewById(R.id.approve_shop);
        shopManagement = findViewById(R.id.shop_management);
        productManagement = findViewById(R.id.product_management);
        offerManagement = findViewById(R.id.offer_management);
        userManagement = findViewById(R.id.user_management);
        orderManagement = findViewById(R.id.order_management);

        newAdminName.setText(adminLogin.getAdminName());
        newAdminEmail.setText(adminLogin.getAdminEmailAddress());

        progressDialog.setTitle("Get Admin Permissions");
        progressDialog.setMessage("Please wait while we are fetching admin permissions");
        progressDialog.setCancelable(false);
        progressDialog.show();

        fetchPermissionsData();

        backToHome.setOnClickListener(v -> onBackPressed());

        deleteAdmin.setOnClickListener(v -> deleteAdminData());
    }

    private void deleteAdminData() {

        progressDialog.setTitle("Delete admin");
        progressDialog.setMessage("Please wait while we are deleting admin");
        progressDialog.setCancelable(false);
        progressDialog.show();

        RetrofitClient.getInstance().getApi()
                .deleteAdmin(adminLogin.getAdminId())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Toast.makeText(DeleteAdmin.this, "Admin deleted successfully", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(DeleteAdmin.this, "Can not delete admin, please try again", Toast.LENGTH_SHORT).show();
                            Log.e("Delete admin", "Error while deleting admin, please try again, Error code: "+response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(DeleteAdmin.this, "Can not delete admin, please try again", Toast.LENGTH_SHORT).show();
                        Log.e("Delete admin", "Error while deleting admin, please try again, Error is: "+t.getMessage());
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
                            Toast.makeText(DeleteAdmin.this, "Can not get admin permissions, please try again", Toast.LENGTH_SHORT).show();
                            Log.e("Update Admin", "Error while fetching admin permission, Error code is: "+response.code());
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<AdminPermissions>> call, @NotNull Throwable t) {
                        Toast.makeText(DeleteAdmin.this, "Can not get admin permissions, please try again", Toast.LENGTH_SHORT).show();
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

        if(permissionsList.contains("Approve Shop"))
            approveShop.setChecked(true);
        if(permissionsList.contains("Shop Management"))
            shopManagement.setChecked(true);
        if(permissionsList.contains("Product Management"))
            productManagement.setChecked(true);
        if(permissionsList.contains("Offer Management"))
            offerManagement.setChecked(true);
        if(permissionsList.contains("User Management"))
            userManagement.setChecked(true);
        if(permissionsList.contains("Order Management"))
            orderManagement.setChecked(true);

        progressDialog.dismiss();
    }
}