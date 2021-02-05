package com.owosuperadmin.pagination.products;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;
import com.owosuperadmin.network.RetrofitClient;
import com.owosuperadmin.models.Owo_product;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDataSource extends PageKeyedDataSource<Integer, Owo_product> {

    private static final int FIRST_PAGE = 0;

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Owo_product> callback) {

        RetrofitClient.getInstance()
                .getApi()
                .getAllProducts(FIRST_PAGE)
                .enqueue(new Callback<List<Owo_product>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Owo_product>> call, @NotNull Response<List<Owo_product>> response) {

                        if (response.isSuccessful()) {
                            callback.onResult(response.body(), null, FIRST_PAGE + 1);
                        }
                        else
                        {
                            Log.e("Error", "Server error occurred");
                        }

                    }
                    @Override
                    public void onFailure(@NotNull Call<List<Owo_product>> call, @NotNull Throwable t) {
                        Log.e("Error", t.getMessage());
                    }
                });

    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Owo_product> callback) {

        RetrofitClient.getInstance()
                .getApi()
                .getAllProducts(params.key)
                .enqueue(new Callback<List<Owo_product>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Owo_product>> call, @NotNull Response<List<Owo_product>> response) {
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
                    public void onFailure(@NotNull Call<List<Owo_product>> call, @NotNull Throwable t) {
                        Log.e("Error", t.getMessage());
                    }
                });

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Owo_product> callback) {

        RetrofitClient.getInstance()
                .getApi()
                .getAllProducts(params.key)
                .enqueue(new Callback<List<Owo_product>>() {
                    @Override
                    public void onResponse(Call<List<Owo_product>> call, Response<List<Owo_product>> response) {

                        if(response.isSuccessful()){
                            callback.onResult(response.body(), params.key+1);
                        }
                        else
                        {
                            Log.e("Error", "Server error occurred");
                        }

                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Owo_product>> call, @NotNull Throwable t) {
                        Log.e("Error", t.getMessage());
                    }
                });
    }
}
