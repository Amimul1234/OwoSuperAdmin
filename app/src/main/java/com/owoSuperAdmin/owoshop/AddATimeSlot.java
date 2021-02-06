package com.owoSuperAdmin.owoshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddATimeSlot extends AppCompatActivity {

    private EditText time_slot;
    private Button create_a_new_time_slot;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_time_slot);

        time_slot = findViewById(R.id.time_slot);
        create_a_new_time_slot = findViewById(R.id.add_a_new_time_slot);
        progressBar = findViewById(R.id.progress);

        create_a_new_time_slot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                String time = time_slot.getText().toString();

                if(time.isEmpty())
                {
                    time_slot.setError("Please enter a time slot");
                    time_slot.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                database.child("Time Slots").push().setValue(time).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AddATimeSlot.this, "Time Slot added", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddATimeSlot.this, "Failed to add time slot", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        finish();
                    }
                });
            }
        });
    }
}