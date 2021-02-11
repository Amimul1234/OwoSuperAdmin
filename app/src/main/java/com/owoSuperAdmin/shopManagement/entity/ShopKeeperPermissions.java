package com.owoSuperAdmin.shopManagement.entity;

import java.io.Serializable;

public class ShopKeeperPermissions implements Serializable {
    private Long id;
    private String permittedCategory;

    public ShopKeeperPermissions() {
    }

    public ShopKeeperPermissions(Long id, String permittedCategory) {
        this.id = id;
        this.permittedCategory = permittedCategory;
    }

    public ShopKeeperPermissions(String permittedCategory) {
        this.permittedCategory = permittedCategory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPermittedCategory() {
        return permittedCategory;
    }

    public void setPermittedCategory(String permittedCategory) {
        this.permittedCategory = permittedCategory;
    }
}
