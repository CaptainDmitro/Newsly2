package com.example.newsly2.repository

import com.example.newsly2.network.toDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class Repository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {

    fun topHeadlines(
        country: String,
        category: String
    ) = flow {
        emit(
            remoteDataSource.topHeadlines(country, category).toDomainModel()
        )
    }.flowOn(Dispatchers.Default)

}