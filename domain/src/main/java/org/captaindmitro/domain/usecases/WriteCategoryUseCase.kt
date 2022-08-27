package org.captaindmitro.domain.usecases

import org.captaindmitro.domain.repositories.Repository
import javax.inject.Inject

class WriteCategoryUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(category: String) = repository.writeCategory(category)
}