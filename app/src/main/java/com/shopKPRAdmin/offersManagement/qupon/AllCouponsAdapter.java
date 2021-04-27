package com.shopKPRAdmin.offersManagement.qupon;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shopKPRAdmin.offersManagement.deleteOffer.DeleteOfferActivity;
import com.shopKPRAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AllCouponsAdapter extends RecyclerView.Adapter<AllCouponsAdapter.xyz>{

    private final Context context;
    private final List<Qupon> couponsList;

    public AllCouponsAdapter(Context context, List<Qupon> couponsList) {
        this.context = context;
        this.couponsList = couponsList;
    }


    @NotNull
    @Override
    public xyz onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.coupons_sample,viewGroup,false);
        return new xyz(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final xyz holder, final int position) {

        Format formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        String startDate = formatter.format(couponsList.get(position).getQuponStartDate());
        String endDate = formatter.format(couponsList.get(position).getQuponEndDate());

        holder.couponDiscount.setText("৳ " + String.valueOf(couponsList.get(position).getDiscount()));
        holder.offerStartDate.setText(startDate);
        holder.offerEndDate.setText(endDate);
        holder.couponCode.setText("Coupon Code: ShopKPR#"+couponsList.get(position).getQuponId());
    }

    @Override
    public int getItemCount() {
        return couponsList.size();
    }


    public class xyz extends RecyclerView.ViewHolder{

        private final TextView couponDiscount;
        private final TextView offerStartDate;
        private final TextView offerEndDate,couponCode;

        public xyz(@NonNull View itemView) {
            super(itemView);

            couponDiscount = itemView.findViewById(R.id.couponTaka);
            offerStartDate = itemView.findViewById(R.id.offerStartDate);
            offerEndDate = itemView.findViewById(R.id.offerEndDate);
            couponCode = itemView.findViewById(R.id.coupon_code);

            itemView.setOnClickListener(v -> {

                Qupon qupon = couponsList.get(getBindingAdapterPosition());

                Intent intent = new Intent(context, DeleteCoupon.class);
                intent.putExtra("coupon", qupon);
                context.startActivity(intent);
            });
        }
    }
}

