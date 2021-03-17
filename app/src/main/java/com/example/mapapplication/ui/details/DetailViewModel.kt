package com.example.mapapplication.ui.details

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.mapapplication.data.model.Page
import com.example.mapapplication.data.model.PageImage
import com.example.mapapplication.data.repository.MapRepository
import com.example.mapapplication.ui.base.BaseViewModel
import com.example.mapapplication.utils.network.NetworkHelper
import com.example.mapapplication.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class DetailViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        private val mapRepository: MapRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    val page: MutableLiveData<Page> = MutableLiveData()
    val imageList: MutableLiveData<List<String>> = MutableLiveData()
    val urlList: MutableList<String> = mutableListOf()
    val pageUrl: MutableLiveData<String> = MutableLiveData()

    override fun onCreate() {

    }

    fun fetchArticleDetail(pageId: Int) {
        if (checkInternetConnectionWithMessage()) {
            compositeDisposable.addAll(
                    mapRepository.fetchArticleDetail(pageId)
                            .subscribeOn(schedulerProvider.io())
                            .subscribe(
                                    {
                                        page.postValue(it.pages[pageId.toString()])
                                    },
                                    {
                                        handleNetworkError(it)
                                    }
                            ))
        }
    }

    fun fetchPageUrl(pageId: Int) {
        if (checkInternetConnectionWithMessage()) {
            compositeDisposable.addAll(
                    mapRepository.fetchPageUrl(pageId)
                            .subscribeOn(schedulerProvider.io())
                            .subscribe(
                                    {
                                        pageUrl.postValue(it.pages[pageId.toString()]?.fullurl)
                                    },
                                    {
                                        handleNetworkError(it)
                                    }
                            ))
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun fetchImageUrl(titleList: List<PageImage>?) {
        if (checkInternetConnectionWithMessage()) {
            titleList?.forEach {
                compositeDisposable.addAll(
                        mapRepository.fetchImageUrl(it.title)
                                .subscribeOn(schedulerProvider.io())
                                .subscribe(
                                        {
                                            it.pages.forEach { t, u -> urlList.add(u.thumbnail.source) }
                                            if (urlList.size == titleList.size) imageList.postValue(urlList)
                                        },
                                        {
                                            handleNetworkError(it)
                                        }
                                ))
            }
        }
    }
}