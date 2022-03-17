package com.example.newsly2.di

import com.example.newsly2.model.Repository
import com.example.newsly2.repository.RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class Repo {

    @Binds
    abstract fun bindRepository(repImpl: RepositoryImpl): Repository

}