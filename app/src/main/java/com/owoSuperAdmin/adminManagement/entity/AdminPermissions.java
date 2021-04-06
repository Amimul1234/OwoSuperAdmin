package com.owoSuperAdmin.adminManagement.entity;

public class AdminPermissions {
    private Long permissionId;
    private String permission;

    public AdminPermissions() {
    }

    public AdminPermissions(Long permissionId, String permission) {
        this.permissionId = permissionId;
        this.permission = permission;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
