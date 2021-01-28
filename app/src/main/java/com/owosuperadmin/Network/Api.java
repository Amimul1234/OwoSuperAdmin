package com.owosuperadmin.Network;

import com.owosuperadmin.models.Brands;
import com.owosuperadmin.models.OffersEntity;
import com.owosuperadmin.models.Owo_product;
import com.owosuperadmin.models.Shop_keeper_orders;
import com.owosuperadmin.models.Shops;
import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @POST("/addProduct")
    Call<Owo_product> createProduct(
            @Body Owo_product owo_product
    );

    @POST("/addABrand")
    Call<ResponseBody> addABrand(
            @Body Brands brands
    );

    @POST("/approveShop")
    Call<Shops> approveShop(
            @Body Shops shops
    );

    @PUT("/updateProduct")
    Call<Owo_product> updateProduct(
            @Body Owo_product product
    );

    @PUT("/setOrderState")
    Call<ResponseBody> setOrderState(
            @Query("order_id") long order_id,
            @Query("order_state") String order_state
    );

    @DELETE("/deleteProduct/{product_id}")
    Call<ResponseBody> deleteProduct(
            @Path("product_id") int product_id
    );


    @GET("/allProducts")
    Call<List<Owo_product>> getAllProducts(
            @Query("page") int page
    );

    @GET("/getBrandsAdmin")
    Call<List<String>> getBrands(
            @Query("category") String category
    );


    @GET("/searchProduct_admin")
    Call<List<Owo_product>> searchProduct(
            @Query("page") int page,
            @Query("product_name") String product_name
    );

    @GET("/getPendingOrders")
    Call<List<Shop_keeper_orders>> getShopKeeperPendingOrders();

    @GET("/getConfirmedOrders")
    Call<List<Shop_keeper_orders>> getShopkeeperConfirmedOrders();

    @GET("/getProcessingOrders")
    Call<List<Shop_keeper_orders>> getProcessingOrders();

    @GET("/getPickedOrders")
    Call<List<Shop_keeper_orders>> getPickedOrders();

    @GET("/getShippedOrders")
    Call<List<Shop_keeper_orders>> getShippedOrders();

    @GET("/getDeliveredOrders")
    Call<List<Shop_keeper_orders>> getDeliveredOrders(@Query("page_num") int page_num);

    @GET("/getCancelledOrders")
    Call<List<Shop_keeper_orders>> getCancelledOrders(@Query("page_num") int page_num);

    @Multipart
    @POST("/imageController/{directory}")
    Call<ResponseBody> uploadImageToServer(
            @Path("directory") String directory,
            @Part MultipartBody.Part multipartFile
    );

    @POST("/addAnOffer")
    Call<ResponseBody> addAnOffer(
            @Body OffersEntity offersEntity
    );

}
