package com.example.mapapplication.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.mapapplication.ui.base.BaseItemViewModel
import com.example.mapapplication.utils.log.Logger
import com.example.mapapplication.utils.network.NetworkHelper
import com.example.mapapplication.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ImageItemViewModel @Inject constructor(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper
) : BaseItemViewModel<String>(schedulerProvider, compositeDisposable, networkHelper) {

    companion object {
        const val TAG = "ImageItemViewModel"
    }

    val url: LiveData<String?> = Transformations.map(data) { it }

    fun onItemClick(position: Int) {
        // messageString.postValue(Resource.success("onItemClick at $position of ${data.value?.name}"))
        Logger.d(TAG, "onItemClick at $position")
    }

    override fun onCreate() {
        Logger.d(TAG, "onCreate called")
    }
}