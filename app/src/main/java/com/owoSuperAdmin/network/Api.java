package com.owoSuperAdmin.network;

import com.owoSuperAdmin.categoryManagement.brand.entity.Brands;
import com.owoSuperAdmin.categoryManagement.category.entity.CategoryEntity;
import com.owoSuperAdmin.categoryManagement.subCategory.entity.SubCategoryEntity;
import com.owoSuperAdmin.offersManagement.entity.OffersEntity;
import com.owoSuperAdmin.productsManagement.entity.Owo_product;
import com.owoSuperAdmin.ordersManagement.entity.Shop_keeper_orders;
import com.owoSuperAdmin.shopManagement.entity.Shops;
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
    //Shops Management
    @POST("/approveShop")
    Call<Shops> approveShop(@Body Shops shops);

    @GET("/getAllShopRegistrationRequests")
    Call<List<Shops>> getAllShopRegistrationRequests(@Query("pageNumber") int pageNumber);

    @GET("/getAllRegisteredShops")
    Call<List<Shops>> getAllRegisteredShops(@Query("pageNumber") int pageNumber);

    //Product Management
    @POST("/addProduct")
    Call<Owo_product> createProduct(@Body Owo_product owo_product);

    @POST("/addABrand")
    Call<ResponseBody> addABrand(@Body Brands brands);

    @PUT("/updateProduct")
    Call<Owo_product> updateProduct(@Body Owo_product product);

    @DELETE("/deleteProduct/{product_id}")
    Call<ResponseBody> deleteProduct(@Path("product_id") int product_id);

    @GET("/allProducts")
    Call<List<Owo_product>> getAllProducts(@Query("page") int page);

    @PUT("/setOrderState")
    Call<ResponseBody> setOrderState(
            @Query("order_id") long order_id,
            @Query("order_state") String order_state
    );

    @GET("/getBrandsAdmin")
    Call<List<String>> getBrands(@Query("category") String category);


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


    @POST("/addAnOffer")
    Call<ResponseBody> addAnOffer(@Body OffersEntity offersEntity);

    //Category Management
    @POST("/addNewCategory")
    Call<ResponseBody> addNewCategory(@Body CategoryEntity categoryEntity);

    @GET("/getAllCategories")
    Call<List<CategoryEntity>> getAllCategories();

    @PUT("/updateCategory")
    Call<ResponseBody> updateCategory(@Query("categoryId") Long categoryId, @Body CategoryEntity categoryEntity);

    @DELETE("/deleteCategory")
    Call<ResponseBody> deleteCategory(@Query("categoryId") Long categoryId);

    //Sub categories management
    @POST("/addNewSubCategory")
    Call<ResponseBody> addNewSubCategory(@Query("categoryId") Long categoryId, @Body SubCategoryEntity subCategoryEntity);

    //Image Controller
    @Multipart
    @POST("/imageController/{directory}")
    Call<ResponseBody> uploadImageToServer(
            @Path("directory") String directory,
            @Part MultipartBody.Part multipartFile
    );

    @DELETE("/getImageFromServer")
    Call<ResponseBody> deleteImage(@Query("path_of_image") String path_of_image);

}
