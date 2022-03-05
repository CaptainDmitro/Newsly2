package com.example.newsly2.repository

import com.example.newsly2.database.ArticleEntity
import com.example.newsly2.database.toDomainModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.*
import org.hamcrest.MatcherAssert.*
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertEquals

class LocalDataSourceTest {
    private lateinit var localDataSource: LocalDataSource
    private lateinit var fakeArticleDao: FakeArticleDao

    @Before
    fun setUp() {
        fakeArticleDao = FakeArticleDao()
        localDataSource = LocalDataSource(fakeArticleDao)

        val articleToInsert = mutableListOf<ArticleEntity>()
        ('a'..'z').forEachIndexed { index, c -> articleToInsert.add(ArticleEntity(UUID.randomUUID(), "$c", "$c", "$c", "$c", "$c", "$c", "$c")) }
        //articleToInsert.shuffle()
        runBlocking {
            articleToInsert.forEach { localDataSource.addArticle(it.toDomainModel()) }
        }
    }

    @Test
    fun `Articles are added by descending order`() = runBlocking {
        val articles = localDataSource.getAllArticles().first()
        for (i in 0..articles.size - 2) {
            assertEquals(articles[i].title, ('a' + i).toString())
        }
    }

    @Test
    fun `Removing of first article is correct`() = runBlocking {
        val firstArticle = localDataSource.getAllArticles().first().first()
        val articles = localDataSource.deleteArticle(firstArticle)

        assertThat(localDataSource.getAllArticles().first().contains(firstArticle), `is`(false))
    }

    @Test
    fun `Insert a single new article at the end of list`() = runBlocking {
        val newArticleEntity = ArticleEntity(UUID.randomUUID(), "TEST", "TEST", "TEST","TEST", "TEST", "TEST", "TEST")
        localDataSource.addArticle(newArticleEntity.toDomainModel())
        val articles = localDataSource.getAllArticles().first()

        assertThat(articles.last(), `is`(newArticleEntity))
    }

}