package org.captaindmitro.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.captaindmitro.domain.di.IoDispatcher
import org.captaindmitro.domain.repositories.Repository
import javax.inject.Inject

class GetTopHeadlinesUseCase @Inject constructor(
    private val repository: Repository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
    ) {
    suspend operator fun invoke(country: String, category: String) = withContext(dispatcher) {
        repository.topHeadlines(country, category)
    }
}