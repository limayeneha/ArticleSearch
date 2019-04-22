package com.limayeneha.articlesearch.helper;

public class CommonHelper {

    private static final String baseUrlImage = "http://www.nytimes.com/";
    private static final String API_KEY = "d31fe793adf546658bd67e2b6a7fd11a";


    public static String getApiKey() {
        return API_KEY;
    }

    public static String getImageUrl(String imageUrl) {
        StringBuilder thumbnailUrl = new StringBuilder();
        thumbnailUrl.append(baseUrlImage).append(imageUrl);
        return thumbnailUrl.toString();
    }

}
