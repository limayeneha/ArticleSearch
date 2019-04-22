package com.limayeneha.articlesearch.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.limayeneha.articlesearch.model.ArticleSearchResponse;
import com.limayeneha.articlesearch.network.NYTApiInterface;

import io.reactivex.Flowable;

public class ArticleViewModel extends ViewModel {
    private NYTApiInterface nytApiInterface;

    public ArticleViewModel(NYTApiInterface nytApiInterface) {
        this.nytApiInterface = nytApiInterface;
    }

    public Flowable<ArticleSearchResponse> articlesList(String q, int page, String api_key) {
        return nytApiInterface.getSearchResults(q, page, api_key);
    }
}
