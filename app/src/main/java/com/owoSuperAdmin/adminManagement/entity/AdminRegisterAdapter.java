package com.owoSuperAdmin.adminManagement.entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class AdminRegisterAdapter extends RecyclerView.Adapter<AdminRegisterAdapter.ViewHolder> {

    private List<AdminLogin> adminLoginList;
    private final LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public AdminRegisterAdapter(Context context, List<AdminLogin> adminLoginList) {
        this.mInflater = LayoutInflater.from(context);
        this.adminLoginList = adminLoginList;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.semiadmin_sample, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;

        int color = colorGenerator.getRandomColor();

        char c = adminLoginList.get(position).getAdminName().charAt(0);

        TextDrawable textDrawable = TextDrawable.builder().buildRound(String.valueOf(c), color);

        holder.adminLetterImage.setImageDrawable(textDrawable);
        holder.adminEmail.setText(adminLoginList.get(position).getAdminEmailAddress());
        holder.adminName.setText(adminLoginList.get(position).getAdminName());
    }

    @Override
    public int getItemCount() {
        return adminLoginList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView adminLetterImage;
        TextView adminName;
        TextView adminEmail;

        ViewHolder(View itemView) {

            super(itemView);

            adminLetterImage = itemView.findViewById(R.id.semi_admin_profile_image);
            adminName = itemView.findViewById(R.id.semi_admin_name);
            adminEmail = itemView.findViewById(R.id.adminEmailAddress);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    AdminLogin getItem(int id) {
        return adminLoginList.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}