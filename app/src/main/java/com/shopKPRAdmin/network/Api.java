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

public interface Api {

    //Time slot management
    @POST("/addNewTimeSlot")
    Call<ResponseBody> addNewTimeSlot(@Body TimeSlot timeSlot);

    @DELETE("/deleteTimeSlot")
    Call<ResponseBody> deleteTimeSlot(@Query("timeSlotId") Long timeSlotId);

    @GET("/getAllAvailableTimeSlots")
    Call<List<TimeSlot>> getAllAvailableTimeSlots();

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

    @GET("/getAllStockedOutProducts")
    Call<List<OwoProduct>> getAllStockedOutProducts(@Query("page") int page);

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

    @GET("/getCategoryListBasedOnId")
    Call<List<String>> getCategoryListBasedOnId(@Query("categoryIds") List<Long> categoryIds);

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

    @GET("/getShopInfo")
    Call<Shops> getShopInfo(@Query("shop_phone") String shop_phone);

    @POST("/approveShopInfoChange")
    Call<ResponseBody> approveShopInfoChange(@Body ChangeShopInfo changeShopInfo);

    @POST("/createNewNotification")
    Call<ResponseBody> createNewNotification(@Body Notifications notifications);

    @GET("/getAllNotifications")
    Call<List<Notifications>> getAllNotifications();

    @DELETE("/deleteNotification")
    Call<ResponseBody> deleteNotification(@Query("notificationId") Long notificationId);

}
