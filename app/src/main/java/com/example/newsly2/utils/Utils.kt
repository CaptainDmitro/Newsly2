package com.example.newsly2.utils

import org.captaindmitro.domain.model.Article
import java.util.*

const val DEFAULT_CATEGORY = "General"
const val DEFAULT_COUNTRY = "US"

const val LAST_VISITED_CATEGORY = "LAST_VISITED_CATEGORY"
const val SELECTED_COUNTRY = "SELECTED_COUNTRY"

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


// TODO: temporary solution to pass links to navControl (slashes are taken as it was a deeplink)
fun navMaskUrl(url: String) = url.replace("/", "*")
fun navUnmaskUrl(url: String) = url.replace("*", "/")