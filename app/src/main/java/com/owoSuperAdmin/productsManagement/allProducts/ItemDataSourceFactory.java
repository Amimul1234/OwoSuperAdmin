package com.owoSuperAdmin.productsManagement.allProducts;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.owoSuperAdmin.productsManagement.allProducts.ItemDataSource;
import com.owoSuperAdmin.productsManagement.entity.OwoProduct;

public class ItemDataSourceFactory extends DataSource.Factory {

    private final MutableLiveData<PageKeyedDataSource<Integer, OwoProduct>> itemLiveDataSource = new MutableLiveData<>();


    @Override
    public DataSource create() {
        ItemDataSource itemDataSource = new ItemDataSource();
        itemLiveDataSource.postValue(itemDataSource);
        return itemDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, OwoProduct>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
