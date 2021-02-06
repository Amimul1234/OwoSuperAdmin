package com.owoSuperAdmin.ordersManagement.cancelledOrders;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;
import com.owoSuperAdmin.ordersManagement.entity.Shop_keeper_orders;

public class Cancel_view_model extends ViewModel {

    public LiveData<PagedList<Shop_keeper_orders>> itemPagedList;
    LiveData<PageKeyedDataSource<Integer, Shop_keeper_orders>> liveDataSource;

    public Cancel_view_model() {

        CancelledDataSourceFactory itemDataSourceFactory = new CancelledDataSourceFactory();
        liveDataSource = itemDataSourceFactory.getItemLiveDataSource();

        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setPageSize(30)                          //setPageSize() is a mandatory for paging list.
                        .setEnablePlaceholders(false)
                        .build();

        itemPagedList = (new LivePagedListBuilder(itemDataSourceFactory, config)).build();
    }
}
