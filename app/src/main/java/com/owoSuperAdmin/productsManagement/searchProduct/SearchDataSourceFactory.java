package com.owoSuperAdmin.productsManagement.searchProduct;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;
import com.owoSuperAdmin.productsManagement.entity.OwoProduct;

public class SearchDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, OwoProduct>> itemLiveDataSource = new MutableLiveData<>();
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

    public MutableLiveData<PageKeyedDataSource<Integer, OwoProduct>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
