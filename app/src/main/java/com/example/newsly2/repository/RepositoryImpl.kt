package com.example.newsly2.repository

import com.example.newsly2.database.ArticleEntity
import com.example.newsly2.database.toDomainModel
import com.example.newsly2.model.Article
import com.example.newsly2.model.Repository
import com.example.newsly2.network.toDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// TODO: add local data source and implement caching
class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : Repository {

    override fun topHeadlines(
        country: String,
        category: String
    ) = flow {
        emit(
            remoteDataSource.topHeadlines(country, category)
        )
    }.flowOn(Dispatchers.Default)

    override fun searchByKeyword(
        keyword: String
    ) = flow {
        emit(
            remoteDataSource.searchByKeyword(keyword).toDomainModel()
        )
    }.flowOn(Dispatchers.Default)

    override fun getAllArticles() = localDataSource.getAllArticles().map { it.map { article -> article.toDomainModel() } }

    override suspend fun addArticle(article: Article) = localDataSource.addArticle(article)

    override suspend fun removeArticle(articleEntity: ArticleEntity) = localDataSource.deleteArticle(articleEntity)

}