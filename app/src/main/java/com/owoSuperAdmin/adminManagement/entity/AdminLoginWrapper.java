package com.owoSuperAdmin.adminManagement.entity;

import java.util.List;

public class AdminLoginWrapper {
    private AdminLogin adminLogin;
    private List<AdminPermissions> adminPermissionsList;

    public AdminLoginWrapper(AdminLogin adminLogin, List<AdminPermissions> adminPermissionsList) {
        this.adminLogin = adminLogin;
        this.adminPermissionsList = adminPermissionsList;
    }

    public AdminLogin getAdminLogin() {
        return adminLogin;
    }

    public void setAdminLogin(AdminLogin adminLogin) {
        this.adminLogin = adminLogin;
    }

    public List<AdminPermissions> getAdminPermissionsList() {
        return adminPermissionsList;
    }

    public void setAdminPermissionsList(List<AdminPermissions> adminPermissionsList) {
        this.adminPermissionsList = adminPermissionsList;
    }
}
