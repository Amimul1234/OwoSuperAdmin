package com.shopKPRAdmin.orderManagement.completedOrders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.shopKPRAdmin.orderManagement.pendingOrders.PendingOrderDetailsItemAdapter;
import com.shopKPRAdmin.orderManagement.ShopKeeperOrderedProducts;
import com.shopKPRAdmin.orderManagement.Shop_keeper_orders;
import com.shopKPRAdmin.owoshop.R;
import java.util.List;

public class CompletedOrderDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_order_details);

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

        List<ShopKeeperOrderedProducts> shop_keeperOrderedProductsList = order_model_class.getShop_keeper_ordered_products();
        RecyclerView recyclerView = findViewById(R.id.ordered_products);

        PendingOrderDetailsItemAdapter adapter = new PendingOrderDetailsItemAdapter(this, shop_keeperOrderedProductsList);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        order_number.setText("#" + order_model_class.getOrder_number());
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
    }
}