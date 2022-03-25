package org.captaindmitro.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.captaindmitro.data.database.toDaoModel
import org.captaindmitro.data.database.toDomainModel
import org.captaindmitro.data.network.toDomainModel
import org.captaindmitro.domain.entities.Article
import org.captaindmitro.domain.repositories.Repository
import javax.inject.Inject

// TODO: add local data source and implement caching
class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : Repository {

    override suspend fun topHeadlines(
        country: String,
        category: String
    ): List<Article> = withContext(Dispatchers.IO) { remoteDataSource.topHeadlines(country, category) }

    override suspend fun searchByKeyword(
        keyword: String
    ): List<Article> = withContext(Dispatchers.IO) { remoteDataSource.searchByKeyword(keyword).toDomainModel() }

    override fun getAllArticles() = localDataSource.getAllArticles().map { it.map { article -> article.toDomainModel() } }

    override suspend fun addArticle(article: Article) = localDataSource.addArticle(article)

    override suspend fun removeArticle(article: Article) = localDataSource.deleteArticle(article.toDaoModel())

}