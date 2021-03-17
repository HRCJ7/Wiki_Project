package com.example.mapapplication.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.mapapplication.R
import com.example.mapapplication.data.model.ArticleQuery
import com.example.mapapplication.data.model.Page
import com.example.mapapplication.data.repository.MapRepository
import com.example.mapapplication.ui.details.DetailViewModel
import com.example.mapapplication.utils.common.Resource
import com.example.mapapplication.utils.network.NetworkHelper
import com.example.mapapplication.utils.rx.TestSchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.TestScheduler
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var networkHelper: NetworkHelper

    @Mock
    private lateinit var mapRepository: MapRepository

    @Mock
    private lateinit var pageObserver: Observer<Page>

    @Mock
    private lateinit var imageListObserver: Observer<List<String>>

    @Mock
    private lateinit var pageUrlObserver: Observer<String>

    @Mock
    private lateinit var messageStringIdObserver: Observer<Resource<Int>>

    private lateinit var testScheduler: TestScheduler

    private lateinit var detailViewModel: DetailViewModel

    @Before
    fun setUp() {
        val compositeDisposable = CompositeDisposable()
        testScheduler = TestScheduler()
        val testSchedulerProvider = TestSchedulerProvider(testScheduler)
        detailViewModel = DetailViewModel(
                testSchedulerProvider,
                compositeDisposable,
                networkHelper,
                mapRepository
        )

        detailViewModel.page.observeForever(pageObserver)
        detailViewModel.imageList.observeForever(imageListObserver)
        detailViewModel.pageUrl.observeForever(pageUrlObserver)
        detailViewModel.messageStringId.observeForever(messageStringIdObserver)
    }

    @Test
    fun givenSuccessResponse_whenClickMarker_shouldShowArticleDetail() {
        Mockito.doReturn(true)
                .`when`(networkHelper)
                .isNetworkConnected()
        Mockito.doReturn(Single.just(ArticleQuery(mutableMapOf())))
                .`when`(mapRepository)
                .fetchArticleDetail(18806750)
        detailViewModel.fetchArticleDetail(18806750)
        testScheduler.triggerActions()
        Mockito.verify(mapRepository).fetchArticleDetail(18806750)
    }

    @Test
    fun givenNoInternet_whenFetchCurrentLocation_shouldShowNetworkError() {
        Mockito.doReturn(false)
                .`when`(networkHelper)
                .isNetworkConnected()
        detailViewModel.fetchArticleDetail(18806750)
        assert(detailViewModel.messageStringId.value == Resource.error(R.string.network_connection_error))
        Mockito.verify(messageStringIdObserver).onChanged(Resource.error(R.string.network_connection_error))
    }

    @After
    fun tearDown() {
        detailViewModel.page.removeObserver(pageObserver)
        detailViewModel.pageUrl.removeObserver(pageUrlObserver)
        detailViewModel.imageList.removeObserver(imageListObserver)
        detailViewModel.messageStringId.removeObserver(messageStringIdObserver)
    }
}