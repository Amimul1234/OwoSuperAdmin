package com.owoSuperAdmin.ordersManagement.completedOrders;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.ordersManagement.entity.Shop_keeper_orders;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompletedDataSource extends PageKeyedDataSource<Integer, Shop_keeper_orders> {

    private static final int FIRST_PAGE = 0;

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Shop_keeper_orders> callback) {

        RetrofitClient.getInstance()
                .getApi()
                .getDeliveredOrders(FIRST_PAGE)
                .enqueue(new Callback<List<Shop_keeper_orders>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Shop_keeper_orders>> call, @NotNull Response<List<Shop_keeper_orders>> response) {

                        if (response.isSuccessful()) {
                            callback.onResult(response.body(), null, FIRST_PAGE + 1);
                        }
                        else
                        {
                            Log.e("Error", "Server error occurred");
                        }

                    }
                    @Override
                    public void onFailure(@NotNull Call<List<Shop_keeper_orders>> call, @NotNull Throwable t) {
                        Log.e("Error", t.getMessage());
                    }
                });

    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Shop_keeper_orders> callback) {

        RetrofitClient.getInstance()
                .getApi()
                .getDeliveredOrders(params.key)
                .enqueue(new Callback<List<Shop_keeper_orders>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Shop_keeper_orders>> call, @NotNull Response<List<Shop_keeper_orders>> response) {
                        if(response.isSuccessful()){
                            Integer key = (params.key > 0) ? params.key - 1 : null;
                            callback.onResult(response.body(), key);
                        }
                        else
                        {
                            Log.e("Error", "Server error occurred");
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Shop_keeper_orders>> call, @NotNull Throwable t) {
                        Log.e("Error", t.getMessage());
                    }
                });

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Shop_keeper_orders> callback) {

        RetrofitClient.getInstance()
                .getApi()
                .getDeliveredOrders(params.key)
                .enqueue(new Callback<List<Shop_keeper_orders>>() {
                    @Override
                    public void onResponse(Call<List<Shop_keeper_orders>> call, Response<List<Shop_keeper_orders>> response) {

                        if(response.isSuccessful()){
                            callback.onResult(response.body(), params.key+1);
                        }
                        else
                        {
                            Log.e("Error", "Server error occurred");
                        }

                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Shop_keeper_orders>> call, @NotNull Throwable t) {
                        Log.e("Error", t.getMessage());
                    }
                });
    }
}
