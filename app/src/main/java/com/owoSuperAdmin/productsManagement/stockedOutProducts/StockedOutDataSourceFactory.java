package com.owoSuperAdmin.productsManagement.stockedOutProducts;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;
import com.owoSuperAdmin.productsManagement.entity.OwoProduct;

public class StockedOutDataSourceFactory extends DataSource.Factory {

    private final MutableLiveData<PageKeyedDataSource<Integer, OwoProduct>> itemLiveDataSource = new MutableLiveData<>();

    @Override
    public DataSource create() {
        StockedOutDataSource stockedOutDataSource = new StockedOutDataSource();
        itemLiveDataSource.postValue(stockedOutDataSource);
        return stockedOutDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, OwoProduct>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
