package com.owoSuperAdmin.productsManagement.allProducts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.owoSuperAdmin.productsManagement.allProducts.ItemDataSourceFactory;
import com.owoSuperAdmin.productsManagement.entity.OwoProduct;

public class ItemViewModel extends ViewModel {

    public LiveData<PagedList<OwoProduct>> itemPagedList;
    LiveData<PageKeyedDataSource<Integer, OwoProduct>> liveDataSource;

    public ItemViewModel() {

        ItemDataSourceFactory itemDataSourceFactory = new ItemDataSourceFactory();
        liveDataSource = itemDataSourceFactory.getItemLiveDataSource();

        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setPageSize(10)                          //setPageSize() is a mandatory for paging list.
                        .setEnablePlaceholders(false)
                        .build();

        itemPagedList = (new LivePagedListBuilder(itemDataSourceFactory, config)).build();
    }
}
