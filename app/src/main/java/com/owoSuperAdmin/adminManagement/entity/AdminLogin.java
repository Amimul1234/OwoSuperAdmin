package com.owoSuperAdmin.adminManagement.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AdminLogin implements Serializable {

    private Integer adminId;
    private String adminName;
    private String adminEmailAddress;
    private String adminPassword;

    private List<AdminPermissions> adminPermissionsList = new ArrayList<>();

    public AdminLogin() {
    }

    public AdminLogin(Integer adminId, String adminName, String adminEmailAddress,
                      String adminPassword, List<AdminPermissions> adminPermissionsList) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.adminEmailAddress = adminEmailAddress;
        this.adminPassword = adminPassword;
        this.adminPermissionsList = adminPermissionsList;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminEmailAddress() {
        return adminEmailAddress;
    }

    public void setAdminEmailAddress(String adminEmailAddress) {
        this.adminEmailAddress = adminEmailAddress;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public List<AdminPermissions> getAdminPermissionsList() {
        return adminPermissionsList;
    }

    public void setAdminPermissionsList(List<AdminPermissions> adminPermissionsList) {
        this.adminPermissionsList.addAll(adminPermissionsList);
    }
}
