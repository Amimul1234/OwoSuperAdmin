package com.owoSuperAdmin.ordersManagement.detailsOfCurrentState;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.ordersManagement.entity.Order_item_adapter;
import com.owoSuperAdmin.ordersManagement.entity.ShopKeeperOrderedProducts;
import com.owoSuperAdmin.ordersManagement.entity.Shop_keeper_orders;
import com.owoSuperAdmin.owoshop.R;
import com.owoSuperAdmin.ordersManagement.state_updater.set_confirmed_to_processing;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class confirmed_order_details extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<ShopKeeperOrderedProducts> shop_keeperOrderedProductsList;
    private ImageView back_from_order_details;
    private TextView shipping_method;
    private Button confirm_button, cancel_button;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_order_details);

        Shop_keeper_orders order_model_class = (Shop_keeper_orders) getIntent().getSerializableExtra("pending_order");

        TextView order_number = findViewById(R.id.order_number);
        TextView order_date  = findViewById(R.id.order_date);
        TextView total_taka = findViewById(R.id.total_taka);
        TextView discount = findViewById(R.id.discount_taka);
        TextView sub_total = findViewById(R.id.sub_total);
        TextView shipping_address = findViewById(R.id.shipping_address);
        TextView mobile_number = findViewById(R.id.mobile_number);
        TextView additional_comments = findViewById(R.id.additional_comments);
        back_from_order_details  = findViewById(R.id.back_from_order_details);
        shipping_method = findViewById(R.id.shipping_method);
        confirm_button = findViewById(R.id.confirm_order_button);
        cancel_button = findViewById(R.id.cancel_order);
        progressBar = findViewById(R.id.log_in_progress);

        shop_keeperOrderedProductsList = order_model_class.getShop_keeper_ordered_products();
        recyclerView = findViewById(R.id.ordered_products);

        Order_item_adapter adapter = new Order_item_adapter(this, shop_keeperOrderedProductsList);
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

        back_from_order_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[] = new CharSequence[]{"NO", "YES"};

                AlertDialog.Builder builder = new AlertDialog.Builder(confirmed_order_details.this);

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
                                                Toast.makeText(confirmed_order_details.this, "Order cancelled", Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.INVISIBLE);
                                                Intent intent = new Intent(confirmed_order_details.this, set_confirmed_to_processing.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                finish();
                                            }
                                            else
                                            {
                                                Toast.makeText(confirmed_order_details.this, "Please try again", Toast.LENGTH_SHORT).show();
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
            }
        });

        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                RetrofitClient.getInstance().getApi()
                        .setOrderState(order_model_class.getOrder_number(), "Processing")
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                                if(response.isSuccessful())
                                {
                                    Toast.makeText(confirmed_order_details.this, "Order settled for processing state", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Intent intent = new Intent(confirmed_order_details.this, set_confirmed_to_processing.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(confirmed_order_details.this, "Please try again", Toast.LENGTH_SHORT).show();
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
        });
    }
}