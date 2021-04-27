package com.shopKPRAdmin.offersManagement.qupon;

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
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.shopKPRAdmin.adminHomePanel.HomeActivity;
import com.shopKPRAdmin.network.RetrofitClient;
import com.shopKPRAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Locale;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteCoupon extends AppCompatActivity {

    private Qupon qupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_coupon);

        qupon = (Qupon) getIntent().getSerializableExtra("coupon");

        ImageView back_to_home = findViewById(R.id.back_to_home);
        EditText offerStartDate = findViewById(R.id.offerStartDate);
        EditText offerEndDate = findViewById(R.id.offerEndDate);
        TextView offerIsFor = findViewById(R.id.offerIsFor);
        SwitchMaterial offerStateIndicatorSwitch = findViewById(R.id.offerStateIndicatorSwitch);
        Button deleteOfferButton = findViewById(R.id.deleteOfferButton);

        Format formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        offerStartDate.setText(formatter.format(qupon.getQuponStartDate()));
        offerEndDate.setText(formatter.format(qupon.getQuponEndDate()));
        offerIsFor.setText(String.valueOf(qupon.getDiscount()));
        offerStateIndicatorSwitch.setChecked(qupon.isEnabled());

        back_to_home.setOnClickListener(v -> onBackPressed());

        deleteOfferButton.setOnClickListener(v -> deleteOfferFromDatabase());

    }

    private void deleteOfferFromDatabase() {

        ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Delete coupon");
        progressDialog.setMessage("Please wait while we are deleting coupon");
        progressDialog.show();

        RetrofitClient.getInstance().getApi()
                .deleteCoupon(qupon.getQuponId())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Toast.makeText(DeleteCoupon.this, "Coupon deleted successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(DeleteCoupon.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(DeleteCoupon.this, "Can not delete coupon, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        progressDialog.dismiss();
                        Log.e("DeleteCoupon", t.getMessage());
                        Toast.makeText(DeleteCoupon.this, "Can not delete coupon, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
