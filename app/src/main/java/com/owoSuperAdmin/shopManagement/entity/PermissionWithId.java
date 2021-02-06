package com.owoSuperAdmin.shopManagement.entity;

import java.util.ArrayList;
import java.util.List;

public class PermissionWithId {
    private int id;
    private List<String> permissions = new ArrayList<>();

    public PermissionWithId(int id, List<String> permissions) {
        this.id = id;
        this.permissions = permissions;
    }

    public PermissionWithId() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}
