package com.owoSuperAdmin.offersManagement;

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
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.owoSuperAdmin.offersManagement.entity.Offers;
import com.owoSuperAdmin.owoshop.R;

public class AvailableOffersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference OffersRef;
    private ImageView back_to_home;
    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avilable_offers);


        shimmerFrameLayout = findViewById(R.id.shimmer_avilable_offer);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();

        OffersRef = FirebaseDatabase.getInstance().getReference().child("Offers");

        back_to_home = findViewById(R.id.back_to_home);


        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        recyclerView = findViewById(R.id.offer_availability_recyclerview_id);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Offers> options = new FirebaseRecyclerOptions.Builder<Offers>()
                .setQuery(OffersRef, Offers.class).build();

        FirebaseRecyclerAdapter<Offers, AvailableOffersActivity.OfferViewHolder> adapter=
                new FirebaseRecyclerAdapter<Offers, OfferViewHolder>(options) {

                    @Override
                    public void onDataChanged() {
                        super.onDataChanged();
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull final OfferViewHolder holder, int position, @NonNull final Offers model) {

                        holder.txtOfferName.setText(model.getName());
                        holder.txtOfferStartDate.setText("Start Date: "+model.getStartdate());
                        holder.txtOfferEndDate.setText("End Date: "+model.getEnddate());

                        Glide.with(AvailableOffersActivity.this).load(model.getImage()).into(holder.imageView);

                    }

                    @NonNull
                    @Override
                    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_availability_sample,parent,false);
                        return new OfferViewHolder(view);
                    }
                };

        /*
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(AvailableOffersActivity.this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(AvailableOffersActivity.this, UpdateOfferActivity.class);
                        intent.putExtra("Offers",adapter.getItem(position));
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );

         */

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class OfferViewHolder extends RecyclerView.ViewHolder{

        public TextView txtOfferName,txtOfferStartDate,txtOfferEndDate;
        public ImageView imageView;

        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.offer_image_id);
            txtOfferName = (TextView)itemView.findViewById(R.id.offer_name_id);
            txtOfferStartDate = (TextView)itemView.findViewById(R.id.start_date);
            txtOfferEndDate = (TextView)itemView.findViewById(R.id.end_date);
        }
    }
}
