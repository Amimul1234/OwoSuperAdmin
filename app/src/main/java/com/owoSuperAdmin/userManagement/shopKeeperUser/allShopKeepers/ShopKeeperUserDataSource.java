package com.owoSuperAdmin.userManagement.shopKeeperUser.allShopKeepers;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.userManagement.shopKeeperUser.entity.ShopKeeperUser;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopKeeperUserDataSource extends PageKeyedDataSource<Integer, ShopKeeperUser> {

    private static final int FIRST_PAGE = 0;

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, ShopKeeperUser> callback) {

        RetrofitClient.getInstance()
                .getApi()
                .getAllEnabledShopKeepers(FIRST_PAGE)
                .enqueue(new Callback<List<ShopKeeperUser>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<ShopKeeperUser>> call, @NotNull Response<List<ShopKeeperUser>> response) {
                        if (response.isSuccessful()) {
                            callback.onResult(response.body(), null, FIRST_PAGE + 1);
                        }
                        else
                        {
                            Log.e("Error", "Server error occurred");
                        }

                    }
                    @Override
                    public void onFailure(@NotNull Call<List<ShopKeeperUser>> call, @NotNull Throwable t) {
                        Log.e("Error", t.getMessage());
                    }
                });

    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, ShopKeeperUser> callback) {

        RetrofitClient.getInstance()
                .getApi()
                .getAllEnabledShopKeepers(params.key)
                .enqueue(new Callback<List<ShopKeeperUser>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<ShopKeeperUser>> call, @NotNull Response<List<ShopKeeperUser>> response) {
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
                    public void onFailure(@NotNull Call<List<ShopKeeperUser>> call, @NotNull Throwable t) {
                        Log.e("Error", t.getMessage());
                    }
                });

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, ShopKeeperUser> callback) {

        RetrofitClient.getInstance()
                .getApi()
                .getAllEnabledShopKeepers(params.key)
                .enqueue(new Callback<List<ShopKeeperUser>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<ShopKeeperUser>> call, @NotNull Response<List<ShopKeeperUser>> response) {

                        if(response.isSuccessful()){
                            callback.onResult(response.body(), params.key+1);
                        }
                        else
                        {
                            Log.e("Error", "Server error occurred");
                        }

                    }

                    @Override
                    public void onFailure(@NotNull Call<List<ShopKeeperUser>> call, @NotNull Throwable t) {
                        Log.e("Error", t.getMessage());
                    }
                });
    }
}
