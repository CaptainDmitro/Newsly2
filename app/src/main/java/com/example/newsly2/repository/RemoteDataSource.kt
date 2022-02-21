package com.example.newsly2.repository

import com.example.newsly2.network.NewsApi
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val api: NewsApi
) {

    suspend fun topHeadlines(
        country: String,
        category: String
    ) = api.topHeadlines(country = country, category = category)

}