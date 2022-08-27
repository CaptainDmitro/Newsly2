package org.captaindmitro.domain.repositories

import kotlinx.coroutines.flow.Flow
import org.captaindmitro.domain.entities.Article

interface Repository {

    suspend fun topHeadlines(country: String, category: String): List<Article>
    suspend fun searchByKeyword(keyword: String): List<Article>
    fun getAllArticles(): Flow<List<Article>>
    suspend fun addArticle(article: Article)
    suspend fun removeArticle(article: Article)

    suspend fun writeCategory(category: String)
    fun readCategory(): String
    suspend fun writeLanguage(language: String)
    fun readLanguage(): String

}