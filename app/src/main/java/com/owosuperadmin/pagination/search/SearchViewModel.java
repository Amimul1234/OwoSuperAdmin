package com.owosuperadmin.pagination.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.owosuperadmin.models.Owo_product;
import com.owosuperadmin.pagination.search.SearchDataSourceFactory;

public class SearchViewModel extends ViewModel {

    public LiveData<PagedList<Owo_product>> itemPagedList;
    LiveData<PageKeyedDataSource<Integer, Owo_product>> liveDataSource;

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
