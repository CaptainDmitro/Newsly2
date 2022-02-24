package com.example.newsly2.di

import android.content.Context
import androidx.room.Room
import com.example.newsly2.database.ArticleDao
import com.example.newsly2.database.NewslyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Database {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): NewslyDatabase = Room.databaseBuilder(context, NewslyDatabase::class.java, "newsly2").build()

    @Singleton
    @Provides
    fun provideDao(newslyDatabase: NewslyDatabase): ArticleDao = newslyDatabase.articleDao()

}