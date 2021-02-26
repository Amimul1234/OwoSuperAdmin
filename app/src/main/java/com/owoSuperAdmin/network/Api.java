package com.owoSuperAdmin.network;

import com.owoSuperAdmin.categoryManagement.brand.addBrand.Brands;
import com.owoSuperAdmin.categoryManagement.category.entity.CategoryEntity;
import com.owoSuperAdmin.categoryManagement.subCategory.entity.SubCategoryEntity;
import com.owoSuperAdmin.offersManagement.entity.OffersEntity;
import com.owoSuperAdmin.productsManagement.entity.OwoProduct;
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
    @GET("/getAllProducts")
    Call<List<OwoProduct>> getAllProducts(@Query("page") int page);

    @GET("/getAProduct")
    Call<OwoProduct> getAProduct(@Query("productId") Long productId);

    @POST("/addProduct")
    Call<OwoProduct> createProduct(@Body OwoProduct owo_product);

    @PUT("/updateProduct")
    Call<OwoProduct> updateProduct(@Body OwoProduct product);

    @DELETE("/deleteProduct")
    Call<ResponseBody> deleteProduct(@Query("productId") Long productId);

    @PUT("/setOrderState")
    Call<ResponseBody> setOrderState(
            @Query("order_id") long order_id,
            @Query("order_state") String order_state
    );


    @GET("/searchProductWithName")
    Call<List<OwoProduct>> searchProduct(
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

    @GET("/getAllSubCategories")
    Call<List<SubCategoryEntity>> getAllSubCategories(@Query("categoryId") Long categoryId);

    @PUT("/updateSubCategory")
    Call<ResponseBody> updateSubCategory(@Query("categoryId") Long categoryId, @Body SubCategoryEntity subCategoryEntity);

    @DELETE("/deleteSubCategory")
    Call<ResponseBody> deleteSubCategory(@Query("subCategoryId") Long subCategoryId);

    //Brands Management
    @GET("/getAllBrandsOfASubCategory")
    Call<List<Brands>> getAllBrands(@Query("subCategoryId") Long subCategoryId);

    @POST("/addABrand")
    Call<ResponseBody> addABrand(@Body Brands brands);

    @PUT("/updateBrand")
    Call<ResponseBody> updateBrand(@Query("subCategoryId") Long subCategoryId, @Body Brands brands);

    @DELETE("/deleteBrand")
    Call<ResponseBody> deleteBrand(@Query("subCategoryId") Long subCategoryId, @Query("brandsId") Long brandsId);

    //Image Controller
    @Multipart
    @POST("/imageController/{directory}")
    Call<ResponseBody> uploadImageToServer(
            @Path("directory") String directory,
            @Part MultipartBody.Part multipartFile
    );

    @DELETE("/getImageFromServer")
    Call<ResponseBody> deleteImage(@Query("path_of_image") String path_of_image);

    //Offers Management
    @POST("/addAnOffer")
    Call<ResponseBody> addAnOffer(@Body OffersEntity offersEntity);

    @GET("/getAllOffers")
    Call<List<OffersEntity>> getAllOffers();

}
