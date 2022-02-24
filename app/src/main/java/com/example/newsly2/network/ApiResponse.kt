package com.example.newsly2.network

import com.example.newsly2.model.Article
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class ApiResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<ArticleResponse>
)

@JsonClass(generateAdapter = true)
data class ArticleResponse(
    val source: SourceResponse,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?,
)

@JsonClass(generateAdapter = true)
data class SourceResponse(
    val id: String?,
    val name: String?
)

fun ApiResponse.toDomainModel(): List<Article> = articles.map {
    Article(
        id = UUID.nameUUIDFromBytes((it.title + it.publishedAt + it.url).toByteArray()),
        author = it.author ?: "Unknown",
        title = it.title ?: "No title",
        description = it.description ?: "No description",
        url = it.url ?: "Now url",
        urlToImage = it.urlToImage ?: "No url",
        publishedAt = it.publishedAt ?: "No published date",
        content = it.content ?: "No content"
    )
}