package com.owoSuperAdmin.giftAndDeals.gifts;

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
import com.owoSuperAdmin.configurationsFile.HostAddress;
import com.owoSuperAdmin.giftAndDeals.entity.Gifts;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllGiftsAdapter extends RecyclerView.Adapter<AllGiftsAdapter.ViewHolder>{

    private final Context context;
    private final List<Gifts> giftsList;

    public AllGiftsAdapter(Context context, List<Gifts> giftsList) {
        this.context = context;
        this.giftsList = giftsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.gifts_sample, parent, false);
        return new AllGiftsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(HostAddress.HOST_ADDRESS.getAddress() + giftsList.get(position).getGiftImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(holder.giftImage);

        holder.giftDetails.setText(giftsList.get(position).getGiftDetails());
    }

    @Override
    public int getItemCount() {
        return giftsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView giftImage;
        private final TextView giftDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            giftImage = itemView.findViewById(R.id.giftImage);
            giftDetails = itemView.findViewById(R.id.giftsDetails);

            itemView.setOnLongClickListener(v -> {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Delete Gift Card");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes", (dialog, id) ->

                                RetrofitClient.getInstance().getApi()
                                .deleteGiftCard(giftsList.get(getAdapterPosition()).getGiftId())
                                .enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                                        if(response.isSuccessful())
                                        {
                                            Toast.makeText(context, "Gift card deleted successfully",
                                                    Toast.LENGTH_SHORT).show();

                                            giftsList.remove(giftsList.get(getAdapterPosition()));
                                            notifyDataSetChanged();
                                        }
                                        else
                                        {
                                            Toast.makeText(context, "Can not delete gift card, please try again", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                                        Log.e("GiftCards", t.getMessage());
                                        Toast.makeText(context, "Can not delete gift card, please try again", Toast.LENGTH_SHORT).show();
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
