package com.example.mapapplication.ui.base

import androidx.lifecycle.MutableLiveData
import com.example.mapapplication.utils.network.NetworkHelper
import com.example.mapapplication.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable


abstract class BaseItemViewModel<T : Any>(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    val data: MutableLiveData<T> = MutableLiveData()

    fun onManualCleared() = onCleared()

    fun updateData(data: T) {
        this.data.postValue(data)
    }
}