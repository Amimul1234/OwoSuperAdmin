package com.owoSuperAdmin.shopManagement.approveShop.shopRequestsPagination;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;
import com.owoSuperAdmin.shopManagement.entity.Shops;
import com.owoSuperAdmin.utilities.NetworkState;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class ItemViewModelShopRegistrationRequests extends ViewModel {

    private final LiveData<NetworkState> networkState;
    public LiveData itemPagedList;
    LiveData<PageKeyedDataSource<Long, Shops>> liveDataSource;

    public ItemViewModelShopRegistrationRequests() {

        Executor executor = Executors.newFixedThreadPool(5);

        ItemDataSourceShopRegistrationFactory itemDataSourceShopRegistrationFactory = new ItemDataSourceShopRegistrationFactory();

        networkState = Transformations.switchMap(itemDataSourceShopRegistrationFactory.getMutableLiveData(),
                ItemDataSourceShopRegistrationRequests::getNetworkState);

        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setPageSize(10)
                        .setEnablePlaceholders(false)
                        .build();

        itemPagedList = (new LivePagedListBuilder(itemDataSourceShopRegistrationFactory, config))
                .setFetchExecutor(executor)
                .build();

    }


    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public LiveData<PagedList<Shops>> getItemPagedList() {
        return itemPagedList;
    }
}
