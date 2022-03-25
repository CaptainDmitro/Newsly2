package org.captaindmitro.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.captaindmitro.data.database.ArticleDao
import org.captaindmitro.data.database.NewslyDatabase
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