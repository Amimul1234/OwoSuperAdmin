package com.owoSuperAdmin.configurationsFile;

public enum HostAddress {

    HOST_ADDRESS("192.168.0.2"); //Should change with server address

    private final String address;

    private HostAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
