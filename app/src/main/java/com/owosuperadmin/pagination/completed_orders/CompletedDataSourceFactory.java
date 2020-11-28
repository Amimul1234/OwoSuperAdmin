package com.owosuperadmin.pagination.completed_orders;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;
import com.owosuperadmin.model.Shop_keeper_orders;

public class CompletedDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, Shop_keeper_orders>> itemLiveDataSource = new MutableLiveData<>();


    @Override
    public DataSource create() {
        CompletedDataSource itemDataSource = new CompletedDataSource();
        itemLiveDataSource.postValue(itemDataSource);
        return itemDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Shop_keeper_orders>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
