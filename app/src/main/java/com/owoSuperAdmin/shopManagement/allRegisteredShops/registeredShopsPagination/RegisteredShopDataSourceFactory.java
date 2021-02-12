package com.owoSuperAdmin.shopManagement.allRegisteredShops.registeredShopsPagination;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("ALL")
public class RegisteredShopDataSourceFactory extends DataSource.Factory {

    private final MutableLiveData<RegisteredShopsItemDataSource> itemLiveDataSource = new MutableLiveData<>();

    public RegisteredShopDataSourceFactory() {
    }

    @NotNull
    @Override
    public DataSource create() {
        RegisteredShopsItemDataSource itemDataSourceShopRegistrationRequests = new RegisteredShopsItemDataSource();
        itemLiveDataSource.postValue(itemDataSourceShopRegistrationRequests);
        return itemDataSourceShopRegistrationRequests;
    }

    public MutableLiveData<RegisteredShopsItemDataSource> getMutableLiveData() {
        return itemLiveDataSource;
    }
}
