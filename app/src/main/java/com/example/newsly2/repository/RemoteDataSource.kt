package com.example.newsly2.repository

import com.example.newsly2.model.Article
import com.example.newsly2.network.NewsApi
import com.example.newsly2.network.toDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val api: NewsApi
) {

    suspend fun topHeadlines(
        country: String,
        category: String
    ): List<Article> = api.topHeadlines(country, category).toDomainModel()

    suspend fun searchByKeyword(
        keyword: String
    ) = api.searchByKeyword(keyword = keyword)

}