package com.example.newsly2.network

import com.example.newsly2.utils.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    // TODO: Wrap network response in Result object

    @GET("/v2/top-headlines")
    suspend fun topHeadlines(
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("apiKey") apiKey: String = API_KEY
    ): ApiResponse

    @GET("/v2/everything")
    suspend fun searchByKeyword(
        @Query("q") keyword: String,
        @Query("apiKey") apiKey: String = API_KEY
    ): ApiResponse

}