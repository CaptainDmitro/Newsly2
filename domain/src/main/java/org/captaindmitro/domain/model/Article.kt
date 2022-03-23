package org.captaindmitro.domain.model

import java.util.*

data class Article(
    val id: UUID,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    val content: String,
)