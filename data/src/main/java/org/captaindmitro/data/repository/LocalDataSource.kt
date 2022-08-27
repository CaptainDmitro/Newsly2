package org.captaindmitro.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.captaindmitro.data.database.ArticleDao
import org.captaindmitro.data.database.ArticleEntity
import org.captaindmitro.data.database.toDaoModel
import org.captaindmitro.domain.entities.Article
import javax.inject.Inject

interface LocalDataSource {

    fun getAllArticles(): Flow<List<ArticleEntity>>
    suspend fun addArticle(article: Article)
    suspend fun deleteArticle(articleEntity: ArticleEntity)

    class Base @Inject constructor(
        private val articleDao: ArticleDao,
        private val dispatcher: CoroutineDispatcher
    ) : LocalDataSource {
        override fun getAllArticles(): Flow<List<ArticleEntity>> = articleDao.getAll().flowOn(dispatcher)

        override suspend fun addArticle(article: Article) = withContext(dispatcher) {
            articleDao.insertAll(article.toDaoModel())
        }

        override suspend fun deleteArticle(articleEntity: ArticleEntity) = withContext(dispatcher) {
            articleDao.delete(articleEntity)
        }
    }
}