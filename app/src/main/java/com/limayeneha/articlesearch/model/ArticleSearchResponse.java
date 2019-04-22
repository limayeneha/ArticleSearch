package com.limayeneha.articlesearch.model;

import com.google.gson.annotations.SerializedName;

public class ArticleSearchResponse {
    @SerializedName("status")
    public String id;

    @SerializedName("response")
    public ArticleSearchDocs articleSearchDocs;
}
