package com.example.newsly2.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ArticleEntity::class], version = 1)
abstract class NewslyDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}