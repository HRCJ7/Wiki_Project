package com.example.mapapplication.data.repository

import com.example.mapapplication.data.model.Query
import com.example.mapapplication.data.remote.NetworkService
import com.example.mapapplication.data.remote.response.ArticleListResponse
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MapRepositoryTest {

    @Mock
    private lateinit var networkService: NetworkService

    private lateinit var mapRepository: MapRepository

    @Before
    fun setUp() {
        mapRepository = MapRepository(networkService)
    }

    @Test
    fun fetchArticleList_requestDoArticleListCall() {
        doReturn(Single.just(ArticleListResponse("statusCode", Query(mutableListOf()))))
                .`when`(networkService)
                .doArticleListCall(
                        "60.1831906|7C24.9285439"
                )
        mapRepository.fetchArticleList("60.1831906|7C24.9285439")

        verify(networkService).doArticleListCall(
                "60.1831906|7C24.9285439"
        )
    }

}