package com.example.newsly2.utils

const val BASE_URL = "https://newsapi.org"
const val API_KEY = "82a7631624154ead8c07b2b70979b436"

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