package com.limayeneha.articlesearch.network;

import com.limayeneha.articlesearch.model.ArticleSearchResponse;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface NYTApiInterface {
    @Headers("Cache-Control: public, max-age=640000, s-maxage=640000 , max-stale=2419200")
    @GET("articlesearch.json")
    Flowable<ArticleSearchResponse> getSearchResults(@Query("q") String userSearchText, @Query("page") int page, @Query("api-key") String apiKey);
}
