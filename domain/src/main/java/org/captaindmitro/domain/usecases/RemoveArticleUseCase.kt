package org.captaindmitro.domain.usecases

import org.captaindmitro.domain.repositories.Repository
import org.captaindmitro.domain.entities.Article
import javax.inject.Inject

class RemoveArticleUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(article: Article) = repository.removeArticle(article)
}