package com.limayeneha.articlesearch.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArticleSearchDocs {

    @SerializedName("docs")
    public List<Article> articles;

    @SerializedName("meta")
    public ArticleSearchResponseMeta meta;
}
