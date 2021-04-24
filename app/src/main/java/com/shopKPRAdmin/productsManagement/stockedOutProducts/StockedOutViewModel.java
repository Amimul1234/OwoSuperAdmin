package com.shopKPRAdmin.productsManagement.stockedOutProducts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.shopKPRAdmin.productsManagement.entity.OwoProduct;

public class StockedOutViewModel extends ViewModel {

    public LiveData<PagedList<OwoProduct>> itemPagedList;
    LiveData<PageKeyedDataSource<Integer, OwoProduct>> liveDataSource;

    public StockedOutViewModel() {

        StockedOutDataSourceFactory stockedOutDataSourceFactory = new StockedOutDataSourceFactory();
        liveDataSource = stockedOutDataSourceFactory.getItemLiveDataSource();

        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setPageSize(10)                          //setPageSize() is a mandatory for paging list.
                        .setEnablePlaceholders(false)
                        .build();

        itemPagedList = (new LivePagedListBuilder(stockedOutDataSourceFactory, config)).build();
    }
}
