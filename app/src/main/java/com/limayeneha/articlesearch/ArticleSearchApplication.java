package com.limayeneha.articlesearch;

import android.app.Application;
import android.content.Context;

import com.limayeneha.articlesearch.network.NYTApi;
import com.limayeneha.articlesearch.network.NYTApiInterface;
import com.limayeneha.articlesearch.viewmodel.ViewModelFactory;

public class ArticleSearchApplication extends Application {

    private NYTApi nytApi;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static NYTApiInterface provideUserDataSource(Context context) {
        return NYTApi.getClient(context).create(NYTApiInterface.class);
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        NYTApiInterface apiInterface = provideUserDataSource(context);
        return new ViewModelFactory(apiInterface);
    }

}
