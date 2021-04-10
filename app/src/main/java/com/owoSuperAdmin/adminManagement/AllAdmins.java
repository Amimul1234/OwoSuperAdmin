package com.owoSuperAdmin.adminManagement;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.owoSuperAdmin.adminManagement.entity.AdminLogin;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllAdmins extends AppCompatActivity {

    private final List<AdminLogin> adminLoginList = new ArrayList<>();
    private AdminRegisterAdapter adminRegisterAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semi_admin);

        adminRegisterAdapter = new AdminRegisterAdapter(AllAdmins.this, adminLoginList);

        progressDialog = new ProgressDialog(this);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);

        ImageView backToHome = findViewById(R.id.back_from_semi_admins);
        RecyclerView recyclerView = findViewById(R.id.all_admins_recycler_view);

        progressDialog.setTitle("All Admins");
        progressDialog.setMessage("Please wait while we are getting all admins info");
        progressDialog.setCancelable(false);
        progressDialog.show();

        recyclerView.setAdapter(adminRegisterAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        loadAdminData();

        swipeRefreshLayout.setOnRefreshListener(this::loadAdminData);

        backToHome.setOnClickListener(v -> onBackPressed());
    }

    private void loadAdminData() {
        RetrofitClient.getInstance().getApi()
                .getAllAdmins()
                .enqueue(new Callback<List<AdminLogin>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<AdminLogin>> call, @NotNull Response<List<AdminLogin>> response) {
                        if(response.isSuccessful())
                        {
                            adminLoginList.clear();
                            assert response.body() != null;
                            adminLoginList.addAll(response.body());
                            adminRegisterAdapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        else
                        {
                            Toast.makeText(AllAdmins.this, "Can not get admin data, " +
                                    "please try again", Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<AdminLogin>> call, @NotNull Throwable t) {
                        Toast.makeText(AllAdmins.this, "Failed to get admin data, " +
                                "please try again", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Log.e("Admin Login", "Error is: " + t.getMessage());
                    }
                });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadAdminData();
    }
}
