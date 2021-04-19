package com.owoSuperAdmin.orderManagement.pendingOrders;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.orderManagement.ShopKeeperOrderedProducts;
import com.owoSuperAdmin.orderManagement.Shop_keeper_orders;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingOrderDetails extends AppCompatActivity
{
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        Shop_keeper_orders order_model_class = (Shop_keeper_orders) getIntent().getSerializableExtra("pending_order");

        TextView order_number = findViewById(R.id.order_number);
        TextView order_date  = findViewById(R.id.order_date);
        TextView total_taka = findViewById(R.id.total_taka);
        TextView discount = findViewById(R.id.discount_taka);
        TextView sub_total = findViewById(R.id.sub_total);
        TextView shipping_address = findViewById(R.id.shipping_address);
        TextView mobile_number = findViewById(R.id.mobile_number);
        TextView additional_comments = findViewById(R.id.additional_comments);
        ImageView back_from_order_details = findViewById(R.id.back_from_order_details);
        TextView shipping_method = findViewById(R.id.shipping_method);
        Button confirm_button = findViewById(R.id.confirm_order_button);
        Button cancel_button = findViewById(R.id.cancel_order);

        List<ShopKeeperOrderedProducts> shop_keeperOrderedProductsList = order_model_class.getShop_keeper_ordered_products();
        RecyclerView recyclerView = findViewById(R.id.ordered_products);


        progressBar = findViewById(R.id.log_in_progress);

        PendingOrderDetailsItemAdapter adapter = new PendingOrderDetailsItemAdapter(this, shop_keeperOrderedProductsList);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        order_number.setText(order_model_class != null ? "#"+order_model_class.getOrder_number() : null);
        order_date.setText(order_model_class.getDate());
        total_taka.setText(String.valueOf(order_model_class.getTotal_amount()));
        discount.setText(String.valueOf(order_model_class.getCoupon_discount()));

        Double sub = order_model_class.getTotal_amount() - order_model_class.getCoupon_discount();

        sub_total.setText(String.valueOf(sub));

        additional_comments.setText(order_model_class.getAdditional_comments());

        shipping_address.setText(order_model_class.getDelivery_address());
        mobile_number.setText(order_model_class.getReceiver_phone());
        shipping_method.setText(order_model_class.getMethod());

        back_from_order_details.setOnClickListener(v -> onBackPressed());


        mobile_number.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+88"+order_model_class.getReceiver_phone()));
            startActivity(intent);
        });

        confirm_button.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);

            RetrofitClient.getInstance().getApi()
                    .setOrderState(order_model_class.getOrder_number(), "Confirmed")
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                            if(response.isSuccessful())
                            {
                                Toast.makeText(PendingOrderDetails.this, "Order settled for confirmation state", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent(PendingOrderDetails.this, PendingOrders.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(PendingOrderDetails.this, "Please try again", Toast.LENGTH_SHORT).show();
                                Log.e("Error", "Server error occurred");
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                            Log.e("Error", t.getMessage());
                        }
                    });
        });

        cancel_button.setOnClickListener(v -> {

            CharSequence options[] = new CharSequence[]{"NO", "YES"};

            AlertDialog.Builder builder = new AlertDialog.Builder(PendingOrderDetails.this);

            builder.setTitle("Are you sure you want to cancel order?");

            builder.setCancelable(false);

            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which == 1)
                    {
                        RetrofitClient.getInstance().getApi()
                                .setOrderState(order_model_class.getOrder_number(), "Cancelled")
                                .enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                                        if(response.isSuccessful())
                                        {
                                            Toast.makeText(PendingOrderDetails.this, "Order cancelled", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Intent intent = new Intent(PendingOrderDetails.this, PendingOrders.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else
                                        {
                                            Toast.makeText(PendingOrderDetails.this, "Please try again", Toast.LENGTH_SHORT).show();
                                            Log.e("Error", "Server error occurred");
                                            progressBar.setVisibility(View.INVISIBLE);
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                                        Log.e("Error", t.getMessage());
                                    }
                                });
                    }
                }
            });
            builder.show();
        });
    }
}