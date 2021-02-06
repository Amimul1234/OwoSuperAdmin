package com.owoSuperAdmin.productsManagement.search;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.owoSuperAdmin.productsManagement.entity.Owo_product;

public class SearchDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, Owo_product>> itemLiveDataSource = new MutableLiveData<>();
    private String searchedProduct;


    public SearchDataSourceFactory(String searchedProduct) {
        this.searchedProduct = searchedProduct;
    }


    @Override
    public DataSource create() {
        SearchDataSource searchDataSource = new SearchDataSource(searchedProduct);
        itemLiveDataSource.postValue(searchDataSource);
        return searchDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Owo_product>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
