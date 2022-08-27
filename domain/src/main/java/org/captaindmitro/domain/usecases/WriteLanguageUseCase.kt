package org.captaindmitro.domain.usecases

import org.captaindmitro.domain.repositories.Repository
import javax.inject.Inject

class WriteLanguageUseCase @Inject constructor(
    private val repository: Repository
    ) {
    suspend operator fun invoke(country: String) = repository.writeLanguage(country)
}