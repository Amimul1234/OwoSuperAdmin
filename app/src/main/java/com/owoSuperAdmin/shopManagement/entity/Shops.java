package com.owoSuperAdmin.shopManagement.entity;

import androidx.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Shops implements Serializable {
    private Long shop_id;
    private Double latitude;
    private Double longitude;
    private Boolean approved;
    private Boolean blocked;
    private String shop_address;
    private String shop_image_uri;
    private String shop_keeper_nid_front_uri;
    private String shop_name;
    private String shop_owner_mobile;
    private String shop_owner_name;
    private String shop_service_mobile;
    private String trade_license_url;
    private List<ShopKeeperPermissions> shopKeeperPermissions = new ArrayList<>();

    public Shops() {
    }

    public Shops(Long shop_id, Double latitude, Double longitude, Boolean approved,
                 Boolean blocked, String shop_address, String shop_image_uri,
                 String shop_keeper_nid_front_uri, String shop_name, String shop_owner_mobile,
                 String shop_owner_name, String shop_service_mobile, String trade_license_url,
                 List<ShopKeeperPermissions> shopKeeperPermissions)
    {
        this.shop_id = shop_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.approved = approved;
        this.blocked = blocked;
        this.shop_address = shop_address;
        this.shop_image_uri = shop_image_uri;
        this.shop_keeper_nid_front_uri = shop_keeper_nid_front_uri;
        this.shop_name = shop_name;
        this.shop_owner_mobile = shop_owner_mobile;
        this.shop_owner_name = shop_owner_name;
        this.shop_service_mobile = shop_service_mobile;
        this.trade_license_url = trade_license_url;
        this.shopKeeperPermissions = shopKeeperPermissions;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public List<ShopKeeperPermissions> getShopKeeperPermissions() {
        return shopKeeperPermissions;
    }

    public void setShopKeeperPermissions(List<ShopKeeperPermissions> shopKeeperPermissions) {
        this.shopKeeperPermissions = shopKeeperPermissions;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }

    public Long getShop_id() {
        return shop_id;
    }

    public void setShop_id(Long shop_id) {
        this.shop_id = shop_id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public String getShop_address() {
        return shop_address;
    }

    public void setShop_address(String shop_address) {
        this.shop_address = shop_address;
    }

    public String getShop_image_uri() {
        return shop_image_uri;
    }

    public void setShop_image_uri(String shop_image_uri) {
        this.shop_image_uri = shop_image_uri;
    }

    public String getShop_keeper_nid_front_uri() {
        return shop_keeper_nid_front_uri;
    }

    public void setShop_keeper_nid_front_uri(String shop_keeper_nid_front_uri) {
        this.shop_keeper_nid_front_uri = shop_keeper_nid_front_uri;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_owner_mobile() {
        return shop_owner_mobile;
    }

    public void setShop_owner_mobile(String shop_owner_mobile) {
        this.shop_owner_mobile = shop_owner_mobile;
    }

    public String getShop_owner_name() {
        return shop_owner_name;
    }

    public void setShop_owner_name(String shop_owner_name) {
        this.shop_owner_name = shop_owner_name;
    }

    public String getShop_service_mobile() {
        return shop_service_mobile;
    }

    public void setShop_service_mobile(String shop_service_mobile) {
        this.shop_service_mobile = shop_service_mobile;
    }

    public String getTrade_license_url() {
        return trade_license_url;
    }

    public void setTrade_license_url(String trade_license_url) {
        this.trade_license_url = trade_license_url;
    }
}
