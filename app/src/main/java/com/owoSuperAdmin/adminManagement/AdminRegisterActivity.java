package com.owoSuperAdmin.adminManagement;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.owoSuperAdmin.adminManagement.entity.AdminLogin;
import com.owoSuperAdmin.adminManagement.entity.AdminRegisterAdapter;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminRegisterActivity extends AppCompatActivity {

    private final List<AdminLogin> adminLoginList = new ArrayList<>();
    private AdminRegisterAdapter adminRegisterAdapter;

    private final ProgressDialog progressDialog = new ProgressDialog(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semi_admin);

        adminRegisterAdapter = new AdminRegisterAdapter(AdminRegisterActivity.this, adminLoginList);

        progressDialog.setTitle("All Admins");
        progressDialog.setMessage("Please wait while we are getting all admins info");
        progressDialog.setCancelable(false);
        progressDialog.show();
        loadAdminData();
    }

    private void loadAdminData() {

        RetrofitClient.getInstance().getApi()
                .getAllAdmins()
                .enqueue(new Callback<List<AdminLogin>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<AdminLogin>> call, @NotNull Response<List<AdminLogin>> response) {
                        if(response.isSuccessful())
                        {
                            adminLoginList.addAll(response.body());
                            adminRegisterAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }
                        else
                        {
                            Toast.makeText(AdminRegisterActivity.this, "Can not get admin data, " +
                                    "please try again", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<AdminLogin>> call, @NotNull Throwable t) {
                        Toast.makeText(AdminRegisterActivity.this, "Failed to get admin data, " +
                                "please try again", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Log.e("Admin Login", "Error is: " + t.getMessage());
                    }
                });
    }


}
