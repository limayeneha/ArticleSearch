package com.limayeneha.articlesearch.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Article {
    @SerializedName("_id")
    public String id;

    @SerializedName("headline")
    public ArticleHeadline headline;

    @SerializedName("multimedia")
    public List<ArticleMultimedia> multimedia;

}
