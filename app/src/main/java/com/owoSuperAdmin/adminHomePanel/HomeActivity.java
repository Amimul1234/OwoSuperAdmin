package com.owoSuperAdmin.adminHomePanel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.owoSuperAdmin.owoshop.R;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewId);
        HomeAdapter adapter = new HomeAdapter(HomeActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

        View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.custom_exit_alert_dialog, null);

        Button yes = view.findViewById(R.id.leave);
        Button no = view.findViewById(R.id.not_leave);

        builder.setView(view);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        yes.setOnClickListener(v -> finish());

        no.setOnClickListener(v -> alertDialog.cancel());
    }
}
