package org.captaindmitro.domain.usecases

import org.captaindmitro.domain.repositories.Repository
import javax.inject.Inject

class SearchByKeywordUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(keyword: String) = repository.searchByKeyword(keyword)
}