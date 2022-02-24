package com.example.newsly2.utils

import com.example.newsly2.model.Article
import java.util.*

const val BASE_URL = "https://newsapi.org"
const val API_KEY = "e43b347e2bca4c3fa8e584c371f1472b"

const val DEFAULT_CATEGORY = "General"
const val DEFAULT_COUNTRY = "US"

val fromCountry = mapOf(
    "US" to "us",
    "Russia" to "ru"
)

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
    "English" to "en",
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