package com.owoSuperAdmin.shopManagement.entity;

import java.util.ArrayList;
import java.util.List;

public class PermissionWithId {
    private Long id;
    private List<String> permissions = new ArrayList<>();

    public PermissionWithId() {
    }

    public PermissionWithId(Long id, List<String> permissions) {
        this.id = id;
        this.permissions = permissions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}
