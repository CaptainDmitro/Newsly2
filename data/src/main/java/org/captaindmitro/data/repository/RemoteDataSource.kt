package org.captaindmitro.data.repository

import org.captaindmitro.domain.model.Article
import org.captaindmitro.data.network.NewsApi
import org.captaindmitro.data.network.toDomainModel
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