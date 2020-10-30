package com.owosuperadmin.Network;

import com.owosuperadmin.model.Owo_product;
import com.owosuperadmin.model.Shops;
import com.owosuperadmin.response.OwoApiResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface Api {

    @POST("addProduct")
    Call<Owo_product> createProduct(
            @Body Owo_product owo_product
    );

    @GET("allProducts")
    Call<OwoApiResponse> getAllProducts(
            @Query("page") int page
    );

    @GET("searchProduct")
    Call<OwoApiResponse> searchProduct(
            @Query("product_name") String product_name
    );

    @PUT("updateProduct")
    Call<Owo_product> updateProduct(
        @Body Owo_product product
    );

    @DELETE("deleteProduct/{product_id}")
    Call<ResponseBody> deleteProduct(
            @Path("product_id") int product_id
    );

    @POST("approveShop")
    Call<Shops> approveShop(
            @Body Shops shops
    );

}
