package com.owoSuperAdmin.timeSlot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.owoSuperAdmin.owoshop.R;
import java.util.List;

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
        }
    }
}
