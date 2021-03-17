package com.example.mapapplication.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.mapapplication.R
import com.example.mapapplication.data.model.GeoSearch
import com.example.mapapplication.data.model.Query
import com.example.mapapplication.data.repository.MapRepository
import com.example.mapapplication.ui.map.MapViewModel
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
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MapViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var networkHelper: NetworkHelper

    @Mock
    private lateinit var mapRepository: MapRepository

    @Mock
    private lateinit var listGeoSearchObserver: Observer<List<GeoSearch>>

    @Mock
    private lateinit var messageStringIdObserver: Observer<Resource<Int>>

    private lateinit var testScheduler: TestScheduler

    private lateinit var mapViewModel: MapViewModel

    @Before
    fun setUp() {
        val compositeDisposable = CompositeDisposable()
        testScheduler = TestScheduler()
        val testSchedulerProvider = TestSchedulerProvider(testScheduler)
        mapViewModel = MapViewModel(
                testSchedulerProvider,
                compositeDisposable,
                networkHelper,
                mapRepository
        )
        mapViewModel.listGeoSearch.observeForever(listGeoSearchObserver)
        mapViewModel.messageStringId.observeForever(messageStringIdObserver)
    }

    @Test
    fun givenSuccessResponse_whenFetchCurrentLocation_shouldMarkersShow() {
        doReturn(true)
                .`when`(networkHelper)
                .isNetworkConnected()
        doReturn(Single.just(Query(mutableListOf())))
                .`when`(mapRepository)
                .fetchArticleList("60.1831906|7C24.9285439")
        mapViewModel.loadArticleLocations("60.1831906|7C24.9285439")
        testScheduler.triggerActions()
        verify(mapRepository).fetchArticleList("60.1831906|7C24.9285439")
        verify(listGeoSearchObserver).onChanged(listOf())
    }

    @Test
    fun givenNoInternet_whenFetchCurrentLocation_shouldShowNetworkError() {
        doReturn(false)
                .`when`(networkHelper)
                .isNetworkConnected()
        mapViewModel.loadArticleLocations("60.1831906|7C24.9285439")
        assert(mapViewModel.messageStringId.value == Resource.error(R.string.network_connection_error))
        verify(messageStringIdObserver).onChanged(Resource.error(R.string.network_connection_error))
    }

    @After
    fun tearDown() {
        mapViewModel.listGeoSearch.removeObserver(listGeoSearchObserver)
        mapViewModel.messageStringId.removeObserver(messageStringIdObserver)
    }

}