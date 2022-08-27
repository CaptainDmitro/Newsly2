package org.captaindmitro.newsly2.utils

import org.captaindmitro.domain.entities.Article
import java.util.*

val fromCategory = mapOf(
    "General" to "general",
    "Entertainment" to "entertainment",
    "Business" to "business",
    "Health" to "health",
    "Science" to "science",
    "Sports" to "sports",
    "Technology" to "technology",
)

val fromLanguage = mapOf(
    "English" to "us",
    "Russian" to "ru"
)

val fakeArticle = Article(
    UUID.randomUUID(),
    "Author",
    "Title",
    "description",
    "url",
    "urlToImage",
    "publishedAt",
    "content"
)

fun navMaskUrl(url: String) = url.replace("/", "*")
fun navUnmaskUrl(url: String) = url.replace("*", "/")