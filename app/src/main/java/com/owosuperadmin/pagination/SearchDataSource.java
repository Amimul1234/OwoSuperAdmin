package com.owosuperadmin.pagination;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.owosuperadmin.Network.RetrofitClient;
import com.owosuperadmin.model.Owo_product;
import com.owosuperadmin.response.OwoApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchDataSource extends PageKeyedDataSource<Integer, Owo_product> {

    private static final int FIRST_PAGE = 0;
    private String searchedProduct;


    public SearchDataSource(String searchedProduct) {
        this.searchedProduct = searchedProduct;
    }


    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Owo_product> callback) {

        RetrofitClient.getInstance()
                .getApi()
                .searchProduct(FIRST_PAGE, searchedProduct)
                .enqueue(new Callback<OwoApiResponse>() {
                    @Override
                    public void onResponse(Call<OwoApiResponse> call, Response<OwoApiResponse> response) {

                        if(response.isSuccessful()){
                            callback.onResult(response.body().products, null, FIRST_PAGE+1);//(First page +1) is representing next page
                        }
                    }

                    @Override
                    public void onFailure(Call<OwoApiResponse> call, Throwable t) {
                        Log.e("Error", "Error searching from database");
                    }
                });

    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Owo_product> callback) {

        RetrofitClient.getInstance()
                .getApi()
                .searchProduct(params.key, searchedProduct)
                .enqueue(new Callback<OwoApiResponse>() {
                    @Override
                    public void onResponse(Call<OwoApiResponse> call, Response<OwoApiResponse> response) {

                        if(response.isSuccessful()){
                            Integer key = (params.key > 0) ? params.key - 1 : null;
                            callback.onResult(response.body().products, key);
                        }
                    }

                    @Override
                    public void onFailure(Call<OwoApiResponse> call, Throwable t) {
                        Log.e("Error", "Error searching from database");
                    }
                });

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Owo_product> callback) {

        RetrofitClient.getInstance()
                .getApi()
                .searchProduct(params.key, searchedProduct)
                .enqueue(new Callback<OwoApiResponse>() {
                    @Override
                    public void onResponse(Call<OwoApiResponse> call, Response<OwoApiResponse> response) {

                        if(response.isSuccessful()){
                            callback.onResult(response.body().products, params.key+1);
                        }

                    }

                    @Override
                    public void onFailure(Call<OwoApiResponse> call, Throwable t) {
                        Log.e("Error", "Error searching from database");
                    }
                });


    }
}
