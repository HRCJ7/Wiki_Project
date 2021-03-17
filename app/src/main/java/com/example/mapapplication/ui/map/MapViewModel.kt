package com.example.mapapplication.ui.map

import androidx.lifecycle.MutableLiveData
import com.example.mapapplication.data.model.GeoSearch
import com.example.mapapplication.data.repository.MapRepository
import com.example.mapapplication.ui.base.BaseViewModel
import com.example.mapapplication.utils.network.NetworkHelper
import com.example.mapapplication.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable


class MapViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        private val mapRepository: MapRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    val listGeoSearch: MutableLiveData<List<GeoSearch>> = MutableLiveData()

    override fun onCreate() {

    }

    fun loadArticleLocations(gsCoord: String) {
        if (checkInternetConnectionWithMessage()) {
            compositeDisposable.addAll(
                    mapRepository.fetchArticleList(gsCoord)
                            .subscribeOn(schedulerProvider.io())
                            .subscribe(
                                    {
                                        listGeoSearch.postValue(it.geoSearch)
                                    },
                                    {
                                        handleNetworkError(it)
                                    }
                            ))
        }
    }
}