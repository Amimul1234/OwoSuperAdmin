package com.owoSuperAdmin.offersManagement.deleteOffer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.owoSuperAdmin.adminHomePanel.HomeActivity;
import com.owoSuperAdmin.configurationsFile.HostAddress;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.offersManagement.entity.OffersEntity;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Locale;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteOfferActivity extends AppCompatActivity {

    private OffersEntity offersEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_offer);

        offersEntity = (OffersEntity) getIntent().getSerializableExtra("offersEntity");

        ImageView offerImage = findViewById(R.id.offerImage);
        ImageView back_to_home = findViewById(R.id.back_to_home);
        EditText offerStartDate = findViewById(R.id.offerStartDate);
        EditText offerEndDate = findViewById(R.id.offerEndDate);
        TextView offerIsFor = findViewById(R.id.offerIsFor);
        SwitchMaterial offerStateIndicatorSwitch = findViewById(R.id.offerStateIndicatorSwitch);
        Button deleteOfferButton = findViewById(R.id.deleteOfferButton);

        Glide.with(this).load(HostAddress.HOST_ADDRESS.getAddress() + offersEntity.getOffer_image())
                .into(offerImage);

        Format formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        offerStartDate.setText(formatter.format(offersEntity.getOffer_start_date()));
        offerEndDate.setText(formatter.format(offersEntity.getOffer_end_date()));
        offerIsFor.setText(offersEntity.getOffer_is_for());
        offerStateIndicatorSwitch.setChecked(offersEntity.isEnabled());


        back_to_home.setOnClickListener(v -> onBackPressed());

        deleteOfferButton.setOnClickListener(v -> deleteOfferFromDatabase());

    }

    private void deleteOfferFromDatabase() {

        ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Delete offer");
        progressDialog.setMessage("Please wait while we are deleting an offer");
        progressDialog.show();

        String offerPreviousImage = offersEntity.getOffer_image();
        String cleanedAddress = offerPreviousImage.substring(34);

        RetrofitClient.getInstance().getApi()
                .deleteAnOffer(offersEntity.getOfferId())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {

                            RetrofitClient.getInstance().getApi()
                                    .deleteImage(cleanedAddress)
                                    .enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                                            if(response.isSuccessful())
                                            {
                                                progressDialog.dismiss();
                                                Toast.makeText(DeleteOfferActivity.this, "Offer deleted successfully", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(DeleteOfferActivity.this, HomeActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                finish();
                                            }
                                            else
                                            {
                                                progressDialog.dismiss();
                                                Toast.makeText(DeleteOfferActivity.this, "Can not delete image", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(DeleteOfferActivity.this, HomeActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                                            progressDialog.dismiss();
                                            Log.e("DeleteOffer", "Error occurred, Error is: "+t.getMessage());
                                            Toast.makeText(DeleteOfferActivity.this, "Can not delete image", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(DeleteOfferActivity.this, HomeActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(DeleteOfferActivity.this, "Can not delete offer, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        progressDialog.dismiss();
                        Log.e("DeleteAnOffer", "Error occurred, Error is: "+t.getMessage());
                        Toast.makeText(DeleteOfferActivity.this, "Can not delete offer, please try again", Toast.LENGTH_SHORT).show();
                    }
                });


    }
}
