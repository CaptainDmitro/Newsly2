package org.captaindmitro.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.captaindmitro.data.network.ApiResponse
import org.captaindmitro.data.network.ArticleResponse
import org.captaindmitro.data.network.SourceResponse
import org.captaindmitro.domain.entities.Article
import org.captaindmitro.domain.repositories.Repository
import org.junit.Assert.assertEquals

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.util.*


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class RepositoryImplTest {

    @Mock
    private lateinit var remoteDataSource: RemoteDataSource
    @Mock
    private lateinit var localDataSource: LocalDataSource
    @Mock
    private lateinit var sharedPrefsDataSource: SharedPrefsDataSource
    private val dispatcher = StandardTestDispatcher()
    private lateinit var repository: Repository

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository = RepositoryImpl(remoteDataSource, localDataSource, sharedPrefsDataSource, dispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Test top headlines response is mapped to domain`() = runTest {
        `when`(remoteDataSource.topHeadlines("us", "general")).thenReturn(
            ApiResponse(
                "ok",
                10,
                List(10) { ArticleResponse(SourceResponse("$it", "$it"), "$it", "$it", "$it", "$it", "$it", "$it", "$it") })
        )

        val actual = repository.topHeadlines("us", "general")
        val expected = List(10) { Article(UUID.nameUUIDFromBytes(("$it" + "$it" + "$it").toByteArray()), "$it", "$it", "$it", "$it", "$it", "$it", "$it") }

        assertEquals(expected, actual)
    }
}