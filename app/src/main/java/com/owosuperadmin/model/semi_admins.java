package com.owosuperadmin.model;

import java.io.Serializable;

public class semi_admins implements Serializable {

    private Boolean Approve_shop, Maintain_shops,
        Add_products, Update_products, Create_offers, Maintain_users, Messaging;

    private String Phone;
    private String profileImage;
    private String SemiAdminName;

    public semi_admins() {
    }

    public semi_admins(String profileImage, String SemiAdminName, String phone, Boolean approve_shop, Boolean maintain_shops,
                       Boolean add_products, Boolean update_products, Boolean create_offers, Boolean maintain_users, Boolean messaging) {

        Phone=phone;
        Approve_shop = approve_shop;
        Maintain_shops = maintain_shops;
        Add_products = add_products;
        Update_products = update_products;
        Create_offers = create_offers;
        Maintain_users = maintain_users;
        Messaging = messaging;
        this.profileImage = profileImage;
        this.SemiAdminName = SemiAdminName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getSemiAdminName() {
        return SemiAdminName;
    }

    public void setSemiAdminName(String semiAdminName) {
        SemiAdminName = semiAdminName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public Boolean getApprove_shop() {
        return Approve_shop;
    }

    public void setApprove_shop(Boolean approve_shop) {
        Approve_shop = approve_shop;
    }

    public Boolean getMaintain_shops() {
        return Maintain_shops;
    }

    public void setMaintain_shops(Boolean maintain_shops) {
        Maintain_shops = maintain_shops;
    }

    public Boolean getAdd_products() {
        return Add_products;
    }

    public void setAdd_products(Boolean add_products) {
        Add_products = add_products;
    }

    public Boolean getUpdate_products() {
        return Update_products;
    }

    public void setUpdate_products(Boolean update_products) {
        Update_products = update_products;
    }

    public Boolean getCreate_offers() {
        return Create_offers;
    }

    public void setCreate_offers(Boolean create_offers) {
        Create_offers = create_offers;
    }

    public Boolean getMaintain_users() {
        return Maintain_users;
    }

    public void setMaintain_users(Boolean maintain_users) {
        Maintain_users = maintain_users;
    }

    public Boolean getMessaging() {
        return Messaging;
    }

    public void setMessaging(Boolean messaging) {
        Messaging = messaging;
    }
}
