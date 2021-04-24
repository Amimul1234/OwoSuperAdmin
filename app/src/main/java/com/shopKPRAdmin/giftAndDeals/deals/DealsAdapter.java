package com.shopKPRAdmin.giftAndDeals.deals;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shopKPRAdmin.configurationsFile.HostAddress;
import com.shopKPRAdmin.giftAndDeals.entity.Deals;
import com.shopKPRAdmin.network.RetrofitClient;
import com.shopKPRAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.ViewHolder> {

    private final Context mctx;
    private final List<Deals> dealsList;

    public DealsAdapter(Context mctx, List<Deals> dealsList) {
        this.mctx = mctx;
        this.dealsList = dealsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mctx);
        View view = inflater.inflate(R.layout.gifts_sample, parent, false);
        return new DealsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(mctx).load(HostAddress.HOST_ADDRESS.getAddress() + dealsList.get(position).getDealImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(holder.dealImage);

        holder.dealDetails.setText(dealsList.get(position).getDealDetails());
    }

    @Override
    public int getItemCount() {
        return dealsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView dealImage;
        private final TextView dealDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dealImage = itemView.findViewById(R.id.giftImage);
            dealDetails = itemView.findViewById(R.id.giftsDetails);

            itemView.setOnLongClickListener(v -> {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(mctx);
                builder1.setMessage("Delete Deal");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes", (dialog, id) ->
                                RetrofitClient.getInstance().getApi()
                                        .deleteDeal(dealsList.get(getAdapterPosition()).getDealsId())
                                        .enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                                                if(response.isSuccessful())
                                                {
                                                    Toast.makeText(mctx, "Deal deleted successfully",
                                                            Toast.LENGTH_SHORT).show();

                                                    dealsList.remove(dealsList.get(getAdapterPosition()));

                                                    notifyDataSetChanged();
                                                }
                                                else
                                                {
                                                    Toast.makeText(mctx, "Can not delete deal, please try again", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                                                Log.e("Deals", t.getMessage());
                                                Toast.makeText(mctx, "Can not delete deal, please try again", Toast.LENGTH_SHORT).show();
                                            }
                                        }));

                builder1.setNegativeButton("Cancel", (dialog, which) -> {

                });

                AlertDialog alert11 = builder1.create();
                alert11.show();

                return true;
            });
        }
    }
}
