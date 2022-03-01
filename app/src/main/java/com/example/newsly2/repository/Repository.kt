package com.example.newsly2.repository

import com.example.newsly2.database.ArticleEntity
import com.example.newsly2.database.toDomainModel
import com.example.newsly2.model.Article
import com.example.newsly2.network.toDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// TODO: add local data source and implement caching
class Repository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) {

    fun topHeadlines(
        country: String,
        category: String
    ) = flow {
        emit(
            remoteDataSource.topHeadlines(country, category)
        )
    }.flowOn(Dispatchers.Default)

    fun searchByKeyword(
        keyword: String
    ) = flow {
        emit(
            remoteDataSource.searchByKeyword(keyword).toDomainModel()
        )
    }.flowOn(Dispatchers.Default)

    suspend fun addArticle(article: Article) = localDataSource.addArticle(article)

    fun getFavoriteArticlesFlow(): Flow<List<Article>> = localDataSource.getAllArticlesFlow().map { it.map { it.toDomainModel() } }

    suspend fun removeArticle(articleEntity: ArticleEntity) = localDataSource.deleteArticle(articleEntity)

}