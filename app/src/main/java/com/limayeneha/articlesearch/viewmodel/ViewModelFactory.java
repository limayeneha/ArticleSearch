package com.limayeneha.articlesearch.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.limayeneha.articlesearch.network.NYTApiInterface;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final NYTApiInterface nytApiInterface;

    public ViewModelFactory(NYTApiInterface nytApiInterface) {
        this.nytApiInterface = nytApiInterface;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ArticleViewModel.class)) {
            return (T) new ArticleViewModel(nytApiInterface);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
