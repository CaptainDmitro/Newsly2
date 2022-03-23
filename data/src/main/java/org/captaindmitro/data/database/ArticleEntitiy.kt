package org.captaindmitro.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.captaindmitro.domain.model.Article
import java.util.*

@Entity(tableName = "article_table")
data class ArticleEntity(
    @PrimaryKey val id: UUID,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "urlToImage") val urlToImage: String,
    @ColumnInfo(name = "publishedAt") val publishedAt: String,
    @ColumnInfo(name = "content") val content: String,
)

fun ArticleEntity.toDomainModel() = Article(
    id = this.id,
    author = this.author,
    title = this.title,
    content = this.content,
    publishedAt = this.publishedAt,
    urlToImage = this.urlToImage,
    description = this.description,
    url =  this.url
)

fun Article.toDaoModel() = ArticleEntity(
    id = this.id,
    author = this.author,
    title = this.title,
    content = this.content,
    publishedAt = this.publishedAt,
    urlToImage = this.urlToImage,
    description = this.description,
    url =  this.url
)