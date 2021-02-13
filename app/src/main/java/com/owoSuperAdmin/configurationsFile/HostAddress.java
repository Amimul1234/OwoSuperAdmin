package com.owoSuperAdmin.configurationsFile;

public enum HostAddress {

    HOST_ADDRESS("http://192.168.1.5/");

    private final String address;

    HostAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
