package org.captaindmitro.domain.usecases

import org.captaindmitro.domain.repositories.Repository
import javax.inject.Inject

class GetLanguageUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(): String = repository.readLanguage()
}