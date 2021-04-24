package com.shopKPRAdmin.timeSlot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.shopKPRAdmin.network.RetrofitClient;
import com.shopKPRAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllAvailableTimeSlots extends AppCompatActivity {

    private AllAvailableTimeSlotsAdapter allAvailableTimeSlotsAdapter;
    private ProgressDialog progressDialog;
    private final List<TimeSlot> timeSlotList = new ArrayList<>();
    private RecyclerView timeSlotRecyclerView;
    private ImageView emptyImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_avilable_time_slots);

        progressDialog = new ProgressDialog(this);
        timeSlotRecyclerView = findViewById(R.id.timeSlotsRecyclerView);
        emptyImageView = findViewById(R.id.empty_image_view);
        allAvailableTimeSlotsAdapter = new AllAvailableTimeSlotsAdapter(this, timeSlotList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        timeSlotRecyclerView.setLayoutManager(linearLayoutManager);

        progressDialog.setTitle("All Available Time Slots");
        progressDialog.setMessage("Please wait while we are fetching all available time slots");
        progressDialog.setCancelable(false);
        progressDialog.show();

        getTimeSlots();

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> onBackPressed());

    }

    private void getTimeSlots()
    {
        RetrofitClient.getInstance().getApi()
                .getAllAvailableTimeSlots()
                .enqueue(new Callback<List<TimeSlot>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<TimeSlot>> call, @NotNull Response<List<TimeSlot>> response) {
                        if(response.isSuccessful())
                        {
                            assert response.body() != null;
                            if(response.body().size() == 0)
                            {
                                progressDialog.dismiss();
                                emptyImageView.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                progressDialog.dismiss();
                                emptyImageView.setVisibility(View.GONE);
                                assert response.body() != null;
                                timeSlotList.addAll(response.body());
                                timeSlotRecyclerView.setAdapter(allAvailableTimeSlotsAdapter);
                                allAvailableTimeSlotsAdapter.notifyDataSetChanged();
                            }
                        }
                        else
                        {
                            progressDialog.dismiss();

                            Toast.makeText(AllAvailableTimeSlots.this, "Can not get time slots, please try again",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<TimeSlot>> call, @NotNull Throwable t) {
                        progressDialog.dismiss();
                        Log.e("AllTimeSlots", t.getMessage());
                        Toast.makeText(AllAvailableTimeSlots.this, "Can not get time slots, please try again",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}