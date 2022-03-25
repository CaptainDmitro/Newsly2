package org.captaindmitro.data.repository

import kotlinx.coroutines.flow.Flow
import org.captaindmitro.data.database.ArticleDao
import org.captaindmitro.data.database.ArticleEntity
import org.captaindmitro.domain.entities.Article
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val articleDao: ArticleDao
) {

    fun getAllArticles(): Flow<List<ArticleEntity>> = articleDao.getAll()

    suspend fun addArticle(article: Article) {
        val daoArticle = ArticleEntity(
            id = article.id,
            url = article.url,
            description = article.description,
            urlToImage = article.urlToImage,
            publishedAt = article.publishedAt,
            content = article.content,
            title = article.title,
            author = article.author
        )

        articleDao.insertAll(daoArticle)
    }

    suspend fun deleteArticle(articleEntity: ArticleEntity) = articleDao.delete(articleEntity)

}