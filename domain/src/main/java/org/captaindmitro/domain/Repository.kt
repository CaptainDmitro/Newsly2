package org.captaindmitro.domain

import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun topHeadlines(country: String, category: String): List<Article>

    suspend fun searchByKeyword(keyword: String): List<Article>

    fun getAllArticles(): Flow<List<Article>>

    suspend fun addArticle(article: Article)

    suspend fun removeArticle(article: Article)

}