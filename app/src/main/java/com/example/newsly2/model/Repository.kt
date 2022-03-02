package com.example.newsly2.model

import com.example.newsly2.database.ArticleEntity
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun topHeadlines(country: String, category: String): Flow<List<Article>>

    fun searchByKeyword(keyword: String): Flow<List<Article>>

    fun getAllArticles(): Flow<List<Article>>

    suspend fun addArticle(article: Article): Unit

    suspend fun removeArticle(articleEntity: ArticleEntity): Unit

}