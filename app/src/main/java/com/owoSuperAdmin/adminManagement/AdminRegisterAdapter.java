package com.owoSuperAdmin.adminManagement;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.owoSuperAdmin.adminManagement.entity.AdminLogin;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class AdminRegisterAdapter extends RecyclerView.Adapter<AdminRegisterAdapter.ViewHolder> {

    private final List<AdminLogin> adminLoginList;
    private final LayoutInflater mInflater;
    private final Context mctx;

    public AdminRegisterAdapter(Context context, List<AdminLogin> adminLoginList) {
        this.mInflater = LayoutInflater.from(context);
        mctx = context;
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

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView adminLetterImage;
        TextView adminName;
        TextView adminEmail;

        ViewHolder(View itemView) {

            super(itemView);

            adminLetterImage = itemView.findViewById(R.id.semi_admin_profile_image);
            adminName = itemView.findViewById(R.id.semi_admin_name);
            adminEmail = itemView.findViewById(R.id.adminEmailAddress);

            itemView.setOnClickListener(v -> {

                AdminLogin adminLogin = adminLoginList.get(getAdapterPosition());

                CharSequence[] adminOptions = new CharSequence[] {"Update Data", "Delete Admin"};

                AlertDialog.Builder builder = new AlertDialog.Builder(mctx);
                builder.setTitle("Admin Data");

                builder.setItems(adminOptions, (dialog, i) -> {
                    switch (i)
                    {
                        case 0:
                        {
                            Intent intent = new Intent(mctx, UpdateAdminData.class);
                            intent.putExtra("adminLogin", adminLogin);
                            mctx.startActivity(intent);
                            break;
                        }
                        case 1:
                        {
                            Intent intent = new Intent(mctx, DeleteAdmin.class);
                            intent.putExtra("adminLogin", adminLogin);
                            mctx.startActivity(intent);
                            break;
                        }
                    }
                });

                builder.show();
            });
        }
    }
}