package com.owoSuperAdmin.adminManagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.owoSuperAdmin.adminManagement.entity.SemiAdmins;
import com.owoSuperAdmin.owoshop.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class SemiAdminActivity extends AppCompatActivity {

    private RecyclerView adminsList;
    private ImageView back_button_to_home;
    private DatabaseReference adminsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semi_admin);

        adminsList = findViewById(R.id.semi_admin_recyclerviewid);
        back_button_to_home = findViewById(R.id.back_from_semi_admins);

        adminsRef= FirebaseDatabase.getInstance().getReference().child("Semi Admins");

        adminsList.setHasFixedSize(true);
        adminsList.setLayoutManager(new LinearLayoutManager(this));
        adminsList.setVisibility(View.GONE);


        back_button_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SemiAdminActivity.super.onBackPressed();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<SemiAdmins> options=
                new FirebaseRecyclerOptions.Builder<SemiAdmins>()
                        .setQuery(adminsRef, SemiAdmins.class).build();

        FirebaseRecyclerAdapter<SemiAdmins,SemiAdminViewHolder> adapter=
                new FirebaseRecyclerAdapter<SemiAdmins, SemiAdminViewHolder>(options) {

                    @Override
                    public void onDataChanged() {
                        super.onDataChanged();
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull final SemiAdminViewHolder holder, int position, @NonNull final SemiAdmins model) {

                        Glide.with(SemiAdminActivity.this).load(model.getProfileImage()).into(holder.profile_pic);
                        holder.Name.setText(model.getSemiAdminName());
                        holder.Phone.setText(model.getPhone());

                        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(SemiAdminActivity.this, UpdateSemiAdminActivity.class);
                                intent.putExtra("Semi Admin", model);
                                startActivity(intent);
                            }
                        });*/
                    }

                    @NonNull
                    @Override
                    public SemiAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.semiadmin_sample,parent,false);
                        return new SemiAdminViewHolder(view);
                    }
                };



        adminsList.setVisibility(View.VISIBLE);

        /*
        adminsList.addOnItemTouchListener(
                new RecyclerItemClickListener(SemiAdminActivity.this, adminsList ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(SemiAdminActivity.this, UpdateSemiAdminActivity.class);
                        intent.putExtra("Semi Admin",adapter.getItem(position));
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );

         */
        adminsList.setAdapter(adapter);
        adapter.startListening();

    }

    public static class SemiAdminViewHolder extends RecyclerView.ViewHolder{

        public TextView Name, Phone;
        public CircleImageView profile_pic;

        public SemiAdminViewHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.semi_admin_name);
            Phone = itemView.findViewById(R.id.semi_admin_mobile_number);
            profile_pic = itemView.findViewById(R.id.semi_admin_profile_image);
        }
    }

}
