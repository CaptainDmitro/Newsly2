package org.captaindmitro.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.captaindmitro.data.network.ApiResponse
import org.captaindmitro.data.network.NewsApi
import javax.inject.Inject

interface RemoteDataSource {

    suspend fun topHeadlines(country: String, category: String): ApiResponse
    suspend fun searchByKeyword(keyword: String): ApiResponse

    class Base @Inject constructor(
        private val api: NewsApi,
        private val dispatcher: CoroutineDispatcher
    ) : RemoteDataSource {
        override suspend fun topHeadlines(
            country: String,
            category: String
        ): ApiResponse = withContext(dispatcher) {
            api.topHeadlines(country, category)
        }

        override suspend fun searchByKeyword(keyword: String) = withContext(dispatcher) {
            api.searchByKeyword(keyword = keyword)
        }
    }
}