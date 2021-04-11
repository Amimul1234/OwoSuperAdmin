package com.owoSuperAdmin.productsManagement.searchProduct;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;
import com.owoSuperAdmin.productsManagement.entity.OwoProduct;

public class SearchViewModel extends ViewModel {

    public LiveData<PagedList<OwoProduct>> itemPagedList;
    LiveData<PageKeyedDataSource<Integer, OwoProduct>> liveDataSource;

    public SearchViewModel(String searched_product) {

        SearchDataSourceFactory searchDataSourceFactory = new SearchDataSourceFactory(searched_product);
        liveDataSource = searchDataSourceFactory.getItemLiveDataSource();

        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setPageSize(30)                          //setPageSize() is a mandatory for paging list.
                        .setEnablePlaceholders(false)
                        .build();

        itemPagedList = (new LivePagedListBuilder(searchDataSourceFactory, config)).build();
    }
}
