package org.captaindmitro.data.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.captaindmitro.data.repository.SharedPrefsDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPrefs {

    @Singleton
    @Provides
    fun provideSharedPrefs(@ApplicationContext context: Context): SharedPreferences = context.getSharedPreferences("LAST_CATEGORY", Context.MODE_PRIVATE)

}