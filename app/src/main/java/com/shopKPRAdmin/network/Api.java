package com.shopKPRAdmin.network;

import com.shopKPRAdmin.adminManagement.entity.AdminLogin;
import com.shopKPRAdmin.adminManagement.entity.AdminLoginWrapper;
import com.shopKPRAdmin.adminManagement.entity.AdminPermissions;
import com.shopKPRAdmin.categoryManagement.brand.addBrand.Brands;
import com.shopKPRAdmin.categoryManagement.category.entity.CategoryEntity;
import com.shopKPRAdmin.categoryManagement.subCategory.entity.SubCategoryEntity;
import com.shopKPRAdmin.giftAndDeals.entity.Deals;
import com.shopKPRAdmin.giftAndDeals.entity.Gifts;
import com.shopKPRAdmin.giftAndDeals.entity.Notifications;
import com.shopKPRAdmin.offersManagement.entity.OffersEntity;
import com.shopKPRAdmin.offersManagement.qupon.Qupon;
import com.shopKPRAdmin.productsManagement.entity.OwoProduct;
import com.shopKPRAdmin.orderManagement.Shop_keeper_orders;
import com.shopKPRAdmin.pushNotification.NotificationData;
import com.shopKPRAdmin.shopInfoChange.ChangeShopInfo;
import com.shopKPRAdmin.shopManagement.entity.Shops;
import com.shopKPRAdmin.timeSlot.TimeSlot;
import com.shopKPRAdmin.userManagement.shopKeeperUser.entity.ShopKeeperUser;
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

public interface Api
{
    //Time slot management
    @POST("shopKpr/admin/addNewTimeSlot")
    Call<ResponseBody> addNewTimeSlot(@Body TimeSlot timeSlot);

    @DELETE("shopKpr/admin/deleteTimeSlot")
    Call<ResponseBody> deleteTimeSlot(@Query("timeSlotId") Long timeSlotId);

    @GET("shopKpr/admin/getAllAvailableTimeSlots")
    Call<List<TimeSlot>> getAllAvailableTimeSlots();

    //Shops Management
    @POST("shopKpr/admin/approveShop")
    Call<Shops> approveShop(@Body Shops shops);

    @GET("shopKpr/admin/getAllShopRegistrationRequests")
    Call<List<Shops>> getAllShopRegistrationRequests(@Query("pageNumber") int pageNumber);

    @GET("shopKpr/admin/getAllRegisteredShops")
    Call<List<Shops>> getAllRegisteredShops(@Query("pageNumber") int pageNumber);

    @GET("shopKpr/admin/getShopInfo")
    Call<Shops> getShopInfo(@Query("shop_phone") String shop_phone);

    //Product Management
    @GET("shopKpr/admin/getAllProducts")
    Call<List<OwoProduct>> getAllProducts(@Query("page") int page);

    @GET("shopKpr/admin/getAllStockedOutProducts")
    Call<List<OwoProduct>> getAllStockedOutProducts(@Query("page") int page);

    @GET("shopKpr/admin/getAProduct")
    Call<OwoProduct> getAProduct(@Query("productId") Long productId);

    @POST("shopKpr/admin/addProduct")
    Call<OwoProduct> createProduct(@Body OwoProduct owo_product);

    @PUT("shopKpr/admin/updateProduct")
    Call<OwoProduct> updateProduct(@Body OwoProduct product);

    @DELETE("shopKpr/admin/deleteProduct")
    Call<ResponseBody> deleteProduct(@Query("productId") Long productId);

    @GET("shopKpr/admin/searchProductWithName")
    Call<List<OwoProduct>> searchProduct(
            @Query("page") int page,
            @Query("product_name") String product_name
    );

    //Orders management
    @GET("shopKpr/admin/getPendingOrders")
    Call<List<Shop_keeper_orders>> getShopKeeperPendingOrders();

    @GET("shopKpr/admin/getConfirmedOrders")
    Call<List<Shop_keeper_orders>> getShopkeeperConfirmedOrders();

    @GET("shopKpr/admin/getProcessingOrders")
    Call<List<Shop_keeper_orders>> getProcessingOrders();

    @GET("shopKpr/admin/getPickedOrders")
    Call<List<Shop_keeper_orders>> getPickedOrders();

    @GET("shopKpr/admin/getShippedOrders")
    Call<List<Shop_keeper_orders>> getShippedOrders();

    @GET("shopKpr/admin/getDeliveredOrders")
    Call<List<Shop_keeper_orders>> getDeliveredOrders(@Query("page_num") int page_num);

    @GET("shopKpr/admin/getCancelledOrders")
    Call<List<Shop_keeper_orders>> getCancelledOrders(@Query("page_num") int page_num);

    @PUT("shopKpr/admin/setOrderState")
    Call<ResponseBody> setOrderState(
            @Query("order_id") long order_id,
            @Query("order_state") String order_state
    );

    //Category Management
    @POST("shopKpr/admin/addNewCategory")
    Call<ResponseBody> addNewCategory(@Body CategoryEntity categoryEntity);

    @PUT("shopKpr/admin/updateCategory")
    Call<ResponseBody> updateCategory(@Query("categoryId") Long categoryId,
                                      @Body CategoryEntity categoryEntity);

    @DELETE("shopKpr/admin/deleteCategory")
    Call<ResponseBody> deleteCategory(@Query("categoryId") Long categoryId);

    @GET("shopKpr/admin/getAllCategories")
    Call<List<CategoryEntity>> getAllCategories();

    @GET("shopKpr/admin/getCategoryListBasedOnId")
    Call<List<String>> getCategoryListBasedOnId(@Query("categoryIds") List<Long> categoryIds);

    //Sub categories management
    @POST("shopKpr/admin/addNewSubCategory")
    Call<ResponseBody> addNewSubCategory(@Query("categoryId") Long categoryId,
                                         @Body SubCategoryEntity subCategoryEntity);

    @GET("shopKpr/admin/getAllSubCategories")
    Call<List<SubCategoryEntity>> getAllSubCategories(@Query("categoryId") Long categoryId);

    @PUT("shopKpr/admin/updateSubCategory")
    Call<ResponseBody> updateSubCategory(@Query("categoryId") Long categoryId,
                                         @Body SubCategoryEntity subCategoryEntity);

    @DELETE("shopKpr/admin/deleteSubCategory")
    Call<ResponseBody> deleteSubCategory(@Query("subCategoryId") Long subCategoryId);

    //Brands Management
    @GET("shopKpr/admin/getAllBrandsOfASubCategory")
    Call<List<Brands>> getAllBrands(@Query("subCategoryId") Long subCategoryId);

    @POST("shopKpr/admin/addABrand")
    Call<ResponseBody> addABrand(@Body Brands brands);

    @PUT("shopKpr/admin/updateBrand")
    Call<ResponseBody> updateBrand(@Query("subCategoryId") Long subCategoryId, @Body Brands brands);

    @DELETE("shopKpr/admin/deleteBrand")
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

    @DELETE("/deleteOffer")
    Call<ResponseBody> deleteAnOffer(@Query("offerId") Long offerId);

    //ShopKeeper User management
    @GET("/getAllEnabledShopKeepers")
    Call<List<ShopKeeperUser>> getAllEnabledShopKeepers(@Query("page") int page);

    @GET("/getAllDisabledAccounts")
    Call<List<ShopKeeperUser>> getAllDisabledAccounts(@Query("page") int page);

    @PUT("/disableShopKeeper")
    Call<ResponseBody> disableShopKeeper(@Query("mobileNumber") String mobileNumber);

    @PUT("/enableShopKeeper")
    Call<ResponseBody> enableShopKeeper(@Query("mobileNumber") String mobileNumber);

    @DELETE("/deleteShopKeeper")
    Call<ResponseBody> deleteShopKeeper(@Query("mobileNumber") String mobileNumber);

    //admin management
    @GET("/admin/adminLogin/v1/getAllAdminInfo/")
    Call<List<AdminLogin>> getAllAdmins();

    @POST("/admin/adminLogin/v1/addAnAdmin")
    Call<ResponseBody> addAnAdmin(@Body AdminLoginWrapper adminLoginWrapper);

    @GET("/admin/adminLogin/v1/getAdminPermissions")
    Call<List<AdminPermissions>> getAdminAllPermissions(@Query("adminId") Integer adminId);

    @PUT("/admin/adminLogin/v1/updateAdminPermission")
    Call<ResponseBody> updateAdminPermissions(@Body AdminLoginWrapper adminLoginWrapper);

    @DELETE("/admin/adminLogin/v1/deleteAdmin")
    Call<ResponseBody> deleteAdmin(@Query("adminId") Integer adminId);

    @GET("/admin/adminLogin/v1/getAdminInfo")
    Call<AdminLoginWrapper> getAdminCredential(@Query("adminEmailAddress") String adminEmailAddress);

    //push notification api
    @POST("/send-notification")
    Call<ResponseBody> sendNotification(@Body NotificationData notificationData, @Query("topic") String topic);

    //Gifts and deals management
    @POST("/createGiftCard")
    Call<ResponseBody> createGiftCard(@Body Gifts gifts);

    @GET("/getAllGiftCards")
    Call<List<Gifts>> getGistsCardList();

    @DELETE("/deleteGiftCard")
    Call<ResponseBody> deleteGiftCard(@Query("giftCardId") Long giftCardId);

    @POST("/createNewDeal")
    Call<ResponseBody> createNewDeal(@Body Deals deals);

    @GET("/getAllDeals")
    Call<List<Deals>> getAllDeals();

    @DELETE("/deleteDeal")
    Call<ResponseBody> deleteDeal(@Query("dealsId") Long dealsId);

    @POST("/addNewQupon")
    Call<ResponseBody> addNewCoupon(@Body Qupon qupon);

    @GET("/getAllQupon")
    Call<List<Qupon>> quponList();

    @DELETE("/deleteQupon")
    Call<ResponseBody> deleteCoupon(@Query("quponId") Long quponId);

    @GET("/getAllChangeRequests")
    Call<List<ChangeShopInfo>> getAllShopChangeRequests();

    @POST("/approveShopInfoChange")
    Call<ResponseBody> approveShopInfoChange(@Body ChangeShopInfo changeShopInfo);

    @POST("/createNewNotification")
    Call<ResponseBody> createNewNotification(@Body Notifications notifications);

    @GET("/getAllNotifications")
    Call<List<Notifications>> getAllNotifications();

    @DELETE("/deleteNotification")
    Call<ResponseBody> deleteNotification(@Query("notificationId") Long notificationId);

}
