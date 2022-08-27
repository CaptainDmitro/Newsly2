package org.captaindmitro.data.di

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import org.captaindmitro.data.database.ArticleDao
import org.captaindmitro.data.network.NewsApi
import org.captaindmitro.data.repository.LocalDataSource
import org.captaindmitro.data.repository.RemoteDataSource
import org.captaindmitro.data.repository.SharedPrefsDataSource
import org.captaindmitro.domain.di.IoDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideLocalDataSource(
        articleDao: ArticleDao,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): LocalDataSource = LocalDataSource.Base(articleDao, dispatcher)

    @Singleton
    @Provides
    fun provideRemoteDataSource(
        api: NewsApi,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): RemoteDataSource = RemoteDataSource.Base(api, dispatcher)

    @Singleton
    @Provides
    fun provideSharedPrefsDataSource(sharedPreferences: SharedPreferences): SharedPrefsDataSource = SharedPrefsDataSource.Base(sharedPreferences)

}