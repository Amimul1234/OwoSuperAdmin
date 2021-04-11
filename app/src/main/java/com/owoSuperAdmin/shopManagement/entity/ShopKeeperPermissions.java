package com.owoSuperAdmin.shopManagement.entity;

import java.io.Serializable;

public class ShopKeeperPermissions implements Serializable {
    private Long id;
    private Long permittedCategoryId;

    public ShopKeeperPermissions() {
    }

    public ShopKeeperPermissions(Long permittedCategoryId) {
        this.permittedCategoryId = permittedCategoryId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPermittedCategoryId() {
        return permittedCategoryId;
    }

    public void setPermittedCategoryId(Long permittedCategoryId) {
        this.permittedCategoryId = permittedCategoryId;
    }
}
