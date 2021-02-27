package com.owoSuperAdmin.offersManagement.avilableOffers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.owoSuperAdmin.configurationsFile.HostAddress;
import com.owoSuperAdmin.offersManagement.deleteOffer.DeleteOfferActivity;
import com.owoSuperAdmin.offersManagement.entity.OffersEntity;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AvailableOffersAdapter extends RecyclerView.Adapter<AvailableOffersAdapter.xyz>{

    private final Context context;
    private final List<OffersEntity> offersEntityList;

    public AvailableOffersAdapter(Context context, List<OffersEntity> offersEntityList) {
        this.context = context;
        this.offersEntityList = offersEntityList;
    }

    @NotNull
    @Override
    public xyz onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.offer_availability_sample,viewGroup,false);
        return new xyz(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final xyz holder, final int position) {
        
        Glide.with(context).load(HostAddress.HOST_ADDRESS.getAddress()+offersEntityList.get(position).getOffer_image()).
                into(holder.imageView);

        Format formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        String startDate = formatter.format(offersEntityList.get(position).getOffer_start_date());
        String endDate = formatter.format(offersEntityList.get(position).getOffer_end_date());

        holder.offerIsFor.setText(offersEntityList.get(position).getOffer_is_for());
        holder.offerStartDate.setText(startDate);
        holder.offerEndDate.setText(endDate);
    }

    @Override
    public int getItemCount() {
        return offersEntityList.size();
    }


    public class xyz extends RecyclerView.ViewHolder{

        private final ImageView imageView;
        private final TextView offerIsFor;
        private final TextView offerStartDate;
        private final TextView offerEndDate;

        public xyz(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.offerImage);
            offerIsFor = itemView.findViewById(R.id.offerIsFor);
            offerStartDate = itemView.findViewById(R.id.offerStartDate);
            offerEndDate = itemView.findViewById(R.id.offerEndDate);

            itemView.setOnClickListener(v -> {

                OffersEntity offersEntity = offersEntityList.get(getAdapterPosition());

                Intent intent = new Intent(context, DeleteOfferActivity.class);
                intent.putExtra("offersEntity", offersEntity);
                context.startActivity(intent);
            });
        }
    }
}

