package com.owoSuperAdmin.shopManagement.approveShop.shopRequestsPagination;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("ALL")
public class ItemDataSourceShopRegistrationFactory extends DataSource.Factory {

    private final MutableLiveData<ItemDataSourceShopRegistrationRequests> itemLiveDataSource = new MutableLiveData<>();

    public ItemDataSourceShopRegistrationFactory() {
    }

    @NotNull
    @Override
    public DataSource create() {
        ItemDataSourceShopRegistrationRequests itemDataSourceShopRegistrationRequests = new ItemDataSourceShopRegistrationRequests();
        itemLiveDataSource.postValue(itemDataSourceShopRegistrationRequests);
        return itemDataSourceShopRegistrationRequests;
    }

    public MutableLiveData<ItemDataSourceShopRegistrationRequests> getMutableLiveData() {
        return itemLiveDataSource;
    }
}
