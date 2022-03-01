package com.example.newsly2.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Query("SELECT * FROM article_table")
    fun getAllFlow(): Flow<List<ArticleEntity>>

    @Insert
    suspend fun insertAll(vararg articles: ArticleEntity)

    @Delete
    suspend fun delete(articleEntity: ArticleEntity)

}