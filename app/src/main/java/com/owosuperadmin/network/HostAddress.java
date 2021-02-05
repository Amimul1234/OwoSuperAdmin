package com.owosuperadmin.network;

public enum HostAddress {
    server_address("localhost"); //Should change with server address
    private String address;

    private HostAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
