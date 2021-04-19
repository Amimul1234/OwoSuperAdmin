package com.owoSuperAdmin.timeSlot;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddATimeSlot extends AppCompatActivity {

    private EditText timeSlot;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_time_slot);

        timeSlot = findViewById(R.id.time_slot);
        Button createNewTimeSlotButton = findViewById(R.id.add_a_new_time_slot);
        progressBar = findViewById(R.id.progress);

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());

        createNewTimeSlotButton.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            doNecessaryCheck();
        });
    }

    private void doNecessaryCheck()
    {
        if(timeSlot.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Time slot can not be empty", Toast.LENGTH_SHORT).show();
        }
        else
        {
            TimeSlot timeSlot1 = new TimeSlot();

            timeSlot1.setTimeSlotString(timeSlot.getText().toString());

            RetrofitClient.getInstance().getApi()
                    .addNewTimeSlot(timeSlot1)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                            if(response.isSuccessful())
                            {
                                Toast.makeText(AddATimeSlot.this, "Time slot added successfully",
                                        Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                onBackPressed();
                            }
                            else
                            {
                                Toast.makeText(AddATimeSlot.this, "Can not add time slot, please try again",
                                        Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(AddATimeSlot.this, "Can not add time slot, please try again",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }
}