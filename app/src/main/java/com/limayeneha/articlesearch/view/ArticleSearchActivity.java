package com.limayeneha.articlesearch.view;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.limayeneha.articlesearch.ArticleSearchApplication;
import com.limayeneha.articlesearch.R;
import com.limayeneha.articlesearch.databinding.ActivityArticleSearchBinding;
import com.limayeneha.articlesearch.scrolllistener.EndlessRecyclerViewScrollListener;
import com.limayeneha.articlesearch.viewmodel.ArticleViewModel;
import com.limayeneha.articlesearch.viewmodel.ViewModelFactory;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.limayeneha.articlesearch.helper.CommonHelper.getApiKey;
import static com.limayeneha.articlesearch.helper.NetworkHelper.isInternetConnected;
import static com.limayeneha.articlesearch.helper.NetworkHelper.isOnline;

public class ArticleSearchActivity extends AppCompatActivity {

    private static final String TAG = ArticleSearchActivity.class.getSimpleName();

    private ActivityArticleSearchBinding binding;
    ProgressBar loadingProgress;
    RecyclerView articleRecyclerView;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private int pageCount = 0;

    private ViewModelFactory mViewModelFactory;
    private ArticleViewModel articleViewModel;
    private ArticleAdapter articleAdapter;


    private final int NUM_COLS = 2;
    private String searchString = "";

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_article_search);

        loadingProgress = binding.loadingProgress;
        loadingProgress.setVisibility(ProgressBar.VISIBLE);

        mViewModelFactory = ArticleSearchApplication.provideViewModelFactory(this);
        articleViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ArticleViewModel.class);

        articleAdapter = new ArticleAdapter(this, new ArrayList<>());

        articleRecyclerView = binding.articleRecyclerView;

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLS, StaggeredGridLayoutManager.VERTICAL);
        articleRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        articleRecyclerView.setAdapter(articleAdapter);

        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                pageCount = page;
                loadArticles(false);
            }
        };
        articleRecyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);

    }

    private void loadArticles(boolean showProgress) {
        loadingProgress.setVisibility(showProgress ? ProgressBar.VISIBLE : ProgressBar.INVISIBLE);
        fetchArticles();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                clearSearch();
                searchString = query;
                fetchArticles();
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchString = newText;
                return false;
            }
        });
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                clearSearch();
                searchString = "";
                fetchArticles();
                return true;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    private void clearSearch() {
        pageCount = 0;
        articleAdapter.clearSearch();
        endlessRecyclerViewScrollListener.resetState();
    }

    private void fetchArticles() {
        if (isInternetConnected(this) && isOnline()) {
            mDisposable.add(articleViewModel.articlesList(searchString, pageCount, getApiKey())
                    .onBackpressureDrop()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(articleList -> {
                                loadingProgress.setVisibility(View.GONE);
                                articleAdapter.setArticles(articleList.articleSearchDocs.articles);
                            },
                            throwable -> Log.e(TAG, "Unable to get articles", throwable)));
        } else {
            Toast.makeText(this, R.string.no_connection, Toast.LENGTH_LONG).show();
            loadingProgress.setVisibility(ProgressBar.INVISIBLE);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        loadArticles(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDisposable.clear();
    }
}
