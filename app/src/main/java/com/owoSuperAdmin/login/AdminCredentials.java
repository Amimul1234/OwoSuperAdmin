package com.owoSuperAdmin.login;

import java.util.List;

public class AdminCredentials {

    private static String adminName;
    private static String adminPassword;
    private static List<String> adminPermissionList;

    public static String getAdminName() {
        return adminName;
    }

    public static void setAdminName(String adminName) {
        AdminCredentials.adminName = adminName;
    }

    public static String getAdminPassword() {
        return adminPassword;
    }

    public static void setAdminPassword(String adminPassword) {
        AdminCredentials.adminPassword = adminPassword;
    }

    public static List<String> getAdminPermissionList() {
        return adminPermissionList;
    }

    public static void setAdminPermissionList(List<String> adminPermissionList) {
        AdminCredentials.adminPermissionList = adminPermissionList;
    }
}
