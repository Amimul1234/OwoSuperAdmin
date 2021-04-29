package com.shopKPRAdmin.giftAndDeals.notifications;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import com.shopKPRAdmin.giftAndDeals.entity.Notifications;
import com.shopKPRAdmin.network.RetrofitClient;
import com.shopKPRAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllNotifications extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView giftsRecyclerView;

    private final List<Notifications> notificationsList = new ArrayList<>();
    private AllNotificationsAdapter allNotificationsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notifications);

        ImageView backButton = findViewById(R.id.back_button);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        giftsRecyclerView = findViewById(R.id.gift_cards_recycler_view);

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.blue));

        swipeRefreshLayout.setOnRefreshListener(()->{
            fetchNotifications();
            showRecycler();
        });

        showRecycler();

        backButton.setOnClickListener(v -> onBackPressed());
    }

    private void fetchNotifications()
    {
        RetrofitClient.getInstance().getApi()
                .getAllNotifications()
                .enqueue(new Callback<List<Notifications>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Notifications>> call, @NotNull Response<List<Notifications>> response) {
                        if(response.isSuccessful())
                        {
                            notificationsList.clear();
                            assert response.body() != null;
                            notificationsList.addAll(response.body());
                            allNotificationsAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(AllNotifications.this, "No notifications available", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Notifications>> call, @NotNull Throwable t) {
                        Log.e("AllNotifications", t.getMessage());
                        Toast.makeText(AllNotifications.this, "Can not notifications, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void showRecycler() {
        allNotificationsAdapter = new AllNotificationsAdapter(AllNotifications.this, notificationsList);
        giftsRecyclerView.setAdapter(allNotificationsAdapter);
        giftsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        giftsRecyclerView.setHasFixedSize(true);
        allNotificationsAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchNotifications();
    }

}