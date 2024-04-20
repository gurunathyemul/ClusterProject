package com.example.domain.model.news

import com.google.gson.annotations.SerializedName

data class NewsHeadLineResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)

data class Article(
    val source: Source,
    val author: String?,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    val content: String
)

data class Source(
    @SerializedName("id") val sourceId: String?,  //use @SerializedName if key of json and variable is different
    val name: String
)

