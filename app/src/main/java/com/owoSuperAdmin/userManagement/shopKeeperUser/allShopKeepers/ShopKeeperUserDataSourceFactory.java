package com.owoSuperAdmin.userManagement.shopKeeperUser.allShopKeepers;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;
import com.owoSuperAdmin.userManagement.shopKeeperUser.entity.ShopKeeperUser;

public class ShopKeeperUserDataSourceFactory extends DataSource.Factory {

    private final MutableLiveData<PageKeyedDataSource<Integer, ShopKeeperUser>> itemLiveDataSource = new MutableLiveData<>();

    @Override
    public DataSource create() {
        ShopKeeperUserDataSource shopKeeperUserDataSource = new ShopKeeperUserDataSource();
        itemLiveDataSource.postValue(shopKeeperUserDataSource);
        return shopKeeperUserDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, ShopKeeperUser>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
