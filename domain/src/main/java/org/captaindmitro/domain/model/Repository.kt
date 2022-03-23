package org.captaindmitro.domain.model

import kotlinx.coroutines.flow.Flow

interface Repository {

    fun topHeadlines(country: String, category: String): Flow<List<Article>>

    fun searchByKeyword(keyword: String): Flow<List<Article>>

    fun getAllArticles(): Flow<List<Article>>

    suspend fun addArticle(article: Article)

    suspend fun removeArticle(article: Article)

}