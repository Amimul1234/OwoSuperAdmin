package com.owoSuperAdmin.userManagement.shopKeeperUser.disabledShopKeepers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.owoSuperAdmin.userManagement.shopKeeperUser.entity.ShopKeeperUser;

public class ShopKeeperUserViewModel extends ViewModel {

    public LiveData<PagedList<ShopKeeperUser>> itemPagedList;
    LiveData<PageKeyedDataSource<Integer, ShopKeeperUser>> liveDataSource;

    public ShopKeeperUserViewModel() {

        ShopKeeperUserDataSourceFactory shopKeeperUserDataSourceFactory = new ShopKeeperUserDataSourceFactory();
        liveDataSource = shopKeeperUserDataSourceFactory.getItemLiveDataSource();

        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setPageSize(10)                          //setPageSize() is a mandatory for paging list.
                        .setEnablePlaceholders(false)
                        .build();

        itemPagedList = (new LivePagedListBuilder(shopKeeperUserDataSourceFactory, config)).build();
    }
}
