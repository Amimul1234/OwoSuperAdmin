package com.owosuperadmin.Network;

import com.owosuperadmin.model.Owo_product;
import com.owosuperadmin.model.Shops;
import com.owosuperadmin.response.DeleteResponse;
import com.owosuperadmin.response.OwoApiResponse;
import com.owosuperadmin.response.UpdatedProductResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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
    Call<OwoApiResponse> getAnswers(
            @Query("page") int page
    );


    @GET("searchProduct")
    Call<OwoApiResponse> searchProduct(
            @Query("product_name") String product_name
    );


    @FormUrlEncoded
    @PUT("updateProduct/{product_id}")
    Call<UpdatedProductResponse> updateProduct(
            @Path("product_id") int product_id,
            @Field("product_image") String product_image,
            @Field("product_name") String product_name,
            @Field("product_category") String product_category,
            @Field("product_price") String product_price,
            @Field("product_discount") String product_discount,
            @Field("product_quantity") String product_quantity,
            @Field("product_description") String product_description,
            @Field("product_date") String product_date,
            @Field("product_time") String product_time,
            @Field("product_sub_category") String product_sub_category
    );

    @DELETE("deleteProduct/{product_id}")
    Call<DeleteResponse> deleteProduct(
            @Path("product_id") int product_id
    );

    @POST("approveShop")
    Call<Shops> approveShop(
            @Body Shops shops
    );

}
