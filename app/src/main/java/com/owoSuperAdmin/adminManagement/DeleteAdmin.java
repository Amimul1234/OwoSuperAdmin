package com.owoSuperAdmin.adminManagement;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.owoSuperAdmin.adminManagement.entity.AdminLogin;
import com.owoSuperAdmin.owoshop.R;

public class DeleteAdmin extends AppCompatActivity {

    private AdminLogin adminLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_admin);

        adminLogin = (AdminLogin) getIntent().getSerializableExtra("adminLogin");
    }
}