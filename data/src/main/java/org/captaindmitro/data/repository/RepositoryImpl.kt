package org.captaindmitro.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.captaindmitro.data.DEFAULT_CATEGORY
import org.captaindmitro.data.DEFAULT_COUNTRY
import org.captaindmitro.data.database.toDaoModel
import org.captaindmitro.data.database.toDomainModel
import org.captaindmitro.data.network.toDomainModel
import org.captaindmitro.domain.di.IoDispatcher
import org.captaindmitro.domain.entities.Article
import org.captaindmitro.domain.repositories.Repository
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val sharedPrefsDataSource: SharedPrefsDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : Repository {

    override suspend fun topHeadlines(
        country: String,
        category: String
    ): List<Article> = withContext(dispatcher) {
        remoteDataSource.topHeadlines(country, category).toDomainModel()
    }

    override suspend fun searchByKeyword(
        keyword: String
    ): List<Article> = withContext(dispatcher) {
        remoteDataSource.searchByKeyword(keyword).toDomainModel()
    }

    override fun getAllArticles() = localDataSource.getAllArticles().map { list ->
        list.map { article ->
            article.toDomainModel()
        }
    }

    override suspend fun addArticle(article: Article) = localDataSource.addArticle(article)

    override suspend fun removeArticle(article: Article) = localDataSource.deleteArticle(article.toDaoModel())

    override suspend fun writeCategory(category: String) = sharedPrefsDataSource.writeCategory(category)

    override fun readCategory(): String = sharedPrefsDataSource.readCategory() ?: DEFAULT_CATEGORY

    override suspend fun writeLanguage(language: String) = sharedPrefsDataSource.writeLanguage(language)

    override fun readLanguage(): String = sharedPrefsDataSource.readLanguage() ?: DEFAULT_COUNTRY

}