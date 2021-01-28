package com.owosuperadmin.pagination.cancelled_orders;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;
import com.owosuperadmin.models.Shop_keeper_orders;

public class CancelledDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, Shop_keeper_orders>> itemLiveDataSource = new MutableLiveData<>();


    @Override
    public DataSource create() {
        CancelledDataSource itemDataSource = new CancelledDataSource();
        itemLiveDataSource.postValue(itemDataSource);
        return itemDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Shop_keeper_orders>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
