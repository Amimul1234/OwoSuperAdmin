package com.owoSuperAdmin.shopManagement.allRegisteredShops.registeredShopsPagination;

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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisteredShopsItemDataSource extends PageKeyedDataSource<Integer, Shops> {

    private static final int FIRST_PAGE = 0;
    private final Api restApiFactory;

    private final MutableLiveData<NetworkState> networkState;

    public RegisteredShopsItemDataSource() {
        restApiFactory = RetrofitClient.getInstance().getApi();
        networkState = new MutableLiveData<>();
    }

    public MutableLiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Shops> callback) {

        networkState.postValue(NetworkState.LOADING);

        restApiFactory.getAllRegisteredShops(FIRST_PAGE)
                .enqueue(new Callback<List<Shops>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Shops>> call, @NotNull Response<List<Shops>> response) {
                        if(response.isSuccessful())
                        {
                            assert response.body() != null;
                            callback.onResult(response.body(), null, FIRST_PAGE+1);
                            networkState.postValue(NetworkState.LOADED);
                        }
                        else
                        {
                            Log.e("Error", "Failed on server");
                            networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                        }

                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Shops>> call, @NotNull Throwable t) {
                        String errorMessage = t.getMessage();
                        Log.e("Data Source Shop Reg.", errorMessage);
                        networkState.postValue(new NetworkState(NetworkState.Status.FAILED, "Please try again"));
                    }
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Shops> callback) {
        networkState.postValue(NetworkState.LOADING);

        restApiFactory.getAllRegisteredShops(params.key)
                .enqueue(new Callback<List<Shops>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Shops>> call, @NotNull Response<List<Shops>> response) {
                        if(response.isSuccessful())
                        {
                            Integer key = (params.key > 0) ? params.key - 1 : null;
                            assert response.body() != null;
                            callback.onResult(response.body(), key);
                            networkState.postValue(NetworkState.LOADED);
                        }
                        else
                        {
                            Log.e("Error", "Error caught on server");
                            networkState.postValue(new NetworkState(NetworkState.Status.FAILED, "Please try again"));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Shops>> call, @NotNull Throwable t) {
                        String errorMessage = t.getMessage();
                        networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                    }
                });
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Shops> callback) {

        networkState.postValue(NetworkState.LOADING);

        restApiFactory.getAllRegisteredShops(params.key)
                .enqueue(new Callback<List<Shops>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Shops>> call, @NotNull Response<List<Shops>> response) {
                        if(response.isSuccessful())
                        {
                            if(params.key < 12)
                            {
                                Log.d("loadAfter", String.valueOf(params.key));
                                assert response.body() != null;
                                callback.onResult(response.body(), params.key+1);

                                networkState.postValue(NetworkState.LOADED);
                            }
                            else
                            {
                                assert response.body() != null;
                                callback.onResult(response.body(), null);
                            }
                        }
                        else
                        {
                            Log.e("Error", "Server error occurred");
                            networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Shops>> call, @NotNull Throwable t) {
                        Log.e("Error", t.getMessage());
                        String errorMessage = t.getMessage();
                        networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                    }
                });
    }
}
