package com.owoSuperAdmin.offersManagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.owoSuperAdmin.adminHomePanel.HomeActivity;
import com.owoSuperAdmin.owoshop.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class QuponActivity extends AppCompatActivity {

    private EditText quponCode,quponDiscount;
    private Button createQuponBtn;
    private String code,discount,saveCurrentDate,saveCurrentTime,quponRandomKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qupon);

        quponCode = findViewById(R.id.qupon_code);
        quponDiscount = findViewById(R.id.qupon_discount);
        createQuponBtn = findViewById(R.id.create_qupon_btn);


        createQuponBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                code=quponCode.getText().toString();
                discount=quponDiscount.getText().toString();

                if (TextUtils.isEmpty(code))
                {
                    Toast.makeText(QuponActivity.this, "Please enter a qupon code", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(discount))
                {
                    Toast.makeText(QuponActivity.this, "Please enter qupon discount", Toast.LENGTH_SHORT).show();
                }
                else {
                    savequpon(code,discount);
                }
            }
        });
    }

    private void savequpon(String code, String discount) {
        Calendar calendar= Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());

        quponRandomKey=saveCurrentDate+saveCurrentTime;

        final DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference().child("Qupon");
        HashMap<String, Object> quponMap=new HashMap<>();
        quponMap.put("qupon_code",code);
        quponMap.put("qupon_discount",discount);

        RootRef.child(quponRandomKey).updateChildren(quponMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Intent intent=new Intent(QuponActivity.this, HomeActivity.class);
                            startActivity(intent);
                            Toast.makeText(QuponActivity.this, "Voucher is added successfully...", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String message=task.getException().toString();
                            Toast.makeText(QuponActivity.this, "Error : "+message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
