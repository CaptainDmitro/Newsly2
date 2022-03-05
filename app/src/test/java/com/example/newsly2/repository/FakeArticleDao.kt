package com.example.newsly2.repository

import com.example.newsly2.database.ArticleDao
import com.example.newsly2.database.ArticleEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeArticleDao : ArticleDao {

    private val articleList = mutableListOf<ArticleEntity>()

    override fun getAll(): Flow<List<ArticleEntity>> = flow {
        emit(articleList)
    }

    override suspend fun insertAll(vararg articles: ArticleEntity) {
        articleList += articles
    }

    override suspend fun delete(articleEntity: ArticleEntity) {
        articleList.remove(articleEntity)
    }
}