package com.owoSuperAdmin.timeSlot;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllAvailableTimeSlotsAdapter extends RecyclerView.Adapter<AllAvailableTimeSlotsAdapter.ViewHolder>
{
    private final Context mCtx;
    private final List<TimeSlot> timeSlotList;

    public AllAvailableTimeSlotsAdapter(Context mCtx, List<TimeSlot> timeSlotList) {
        this.mCtx = mCtx;
        this.timeSlotList = timeSlotList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.time_slot_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.timeSlot.setText(timeSlotList.get(position).getTimeSlotString());
    }

    @Override
    public int getItemCount() {
        return timeSlotList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView timeSlot;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            timeSlot = itemView.findViewById(R.id.time_slot);

            itemView.setOnLongClickListener(v ->
            {
                new AlertDialog.Builder(mCtx)
                        .setTitle("Delete Time Slot")
                        .setMessage("Are you sure you want to delete time slot?")
                        .setPositiveButton("YES", (dialog, which) ->
                        {

                            RetrofitClient.getInstance().getApi()
                                    .deleteTimeSlot(timeSlotList.get(getAdapterPosition()).getTimeSlotId())
                                    .enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                                            if(response.isSuccessful())
                                            {
                                                Toast.makeText(mCtx, "Time slot deleted successfully", Toast.LENGTH_SHORT).show();
                                                timeSlotList.remove(timeSlotList.get(getAdapterPosition()));
                                                notifyDataSetChanged();
                                            }
                                            else
                                            {
                                                Toast.makeText(mCtx, "Can not delete time slot, please try again", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                                            Log.e("Time Slot", t.getMessage());
                                            Toast.makeText(mCtx, "Can not delete time slot, please try again", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        })
                        .setNegativeButton("NO", (dialog, which) -> {
                        }).
                        show();

                return true;
            });
        }
    }
}
