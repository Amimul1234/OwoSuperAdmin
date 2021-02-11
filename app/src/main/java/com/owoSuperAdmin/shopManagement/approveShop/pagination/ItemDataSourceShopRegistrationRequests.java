package com.owoSuperAdmin.shopManagement.approveShop.pagination;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;
import com.owoSuperAdmin.network.Api;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.shopManagement.entity.Shops;
import com.owoSuperAdmin.utilities.NetworkState;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDataSourceShopRegistrationRequests extends PageKeyedDataSource<Long, Shops> {

    private static final Long FIRST_PAGE = 0L;
    private final Api restApiFactory;

    private final MutableLiveData<NetworkState> networkState;

    public ItemDataSourceShopRegistrationRequests() {
        restApiFactory = RetrofitClient.getInstance().getApi();
        networkState = new MutableLiveData<>();
    }

    public MutableLiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<Long, Shops> callback) {
        networkState.postValue(NetworkState.LOADING);

        restApiFactory.getAllShopRegistrationRequests(FIRST_PAGE)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {

                        if(response.isSuccessful())
                        {
                            callback.onResult((List<Shops>) response.body(), null, (long) (FIRST_PAGE+1));
                            networkState.postValue(NetworkState.LOADED);
                        }
                        else
                        {
                            Log.e("Error", "Failed on server");
                            networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                        }

                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        String errorMessage = t == null ? "unknown error" : t.getMessage();
                        Log.e("Dara Source Shop Reg.", errorMessage);
                        networkState.postValue(new NetworkState(NetworkState.Status.FAILED, "Please try again"));
                    }
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Shops> callback) {
        networkState.postValue(NetworkState.LOADING);

        restApiFactory.getAllShopRegistrationRequests(params.key)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {
                            Long key = (params.key > 0) ? params.key - 1 : null;
                            callback.onResult((List<Shops>) response.body(), key);
                            networkState.postValue(NetworkState.LOADED);
                        }
                        else
                        {
                            Log.e("Error", "Error caught on server");
                            networkState.postValue(new NetworkState(NetworkState.Status.FAILED, "Please try again"));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        String errorMessage = t == null ? "unknown error" : t.getMessage();
                        networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                    }
                });
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Shops> callback) {

        networkState.postValue(NetworkState.LOADING);

        restApiFactory.getAllShopRegistrationRequests(params.key)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {
                            if(params.key < 12)
                            {
                                Log.d("loadAfter", String.valueOf(params.key));
                                callback.onResult((List<Shops>) response.body(), params.key+1);

                                networkState.postValue(NetworkState.LOADED);
                            }
                            else
                            {
                                callback.onResult((List<Shops>) response.body(), null);
                            }
                        }
                        else
                        {
                            Log.e("Error", "Server error occurred");
                            networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        Log.e("Error", t.getMessage());
                        String errorMessage = t == null ? "unknown error" : t.getMessage();
                        networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                    }
                });
    }
}
